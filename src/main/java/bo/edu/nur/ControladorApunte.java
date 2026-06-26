// Declaramos el paquete oficial de tu universidad para la estructura lógica del proyecto.
package bo.edu.nur;

// Importamos librerías nativas para la manipulación física de archivos en el disco duro.
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// Importamos los componentes core de Spring Web para la gestión de peticiones HTTP.
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

// Inyectamos la directiva Controller (MVC) para que este componente gestione vistas y redirecciones.
@Controller
public class ControladorApunte {

    // Escuchamos la petición POST que envía el formulario de subida desde el Dashboard.
    @PostMapping("/subir-apunte")
    // Declaramos el método público que orquesta la validación, el guardado y el cobro de recompensas.
    public String procesarSubida(@RequestParam("tituloApunte") String titulo,
                                 @RequestParam("categoriaMateria") String categoria,
                                 @RequestParam("archivoFisico") MultipartFile archivo,
                                 HttpSession sesion) {

        // 1. SEGURIDAD DE SESIÓN: Verificamos si el estudiante está realmente logueado.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");
        if (aliasUsuario == null) {
            return "redirect:/";
        }

        // 2. GATEKEEPING: Invocamos al validador estricto para bloquear archivos basura antes de tocar el disco.
        if (!ValidadorApunte.esArchivoValido(archivo)) {
            // Si el archivo falla, redirigimos al dashboard con una bandera de error técnica.
            return "redirect:/dashboard?error=archivo_invalido";
        }

        // 3. GENERACIÓN DE IDENTIDAD ÚNICA: Creamos un UUID para evitar colisiones de nombres de archivos.
        String codigoUnico = UUID.randomUUID().toString();
        String nombreOriginal = archivo.getOriginalFilename();
        String nombreSeguro = codigoUnico + "_" + nombreOriginal;

        // 4. PERSISTENCIA FÍSICA: Bloque protegido para operaciones de escritura en disco duro.
        try {
            String nombreCarpeta = "repositorio_nur/";
            File directorio = new File(nombreCarpeta);

            // Garantizamos que la carpeta repositorio exista antes de intentar escribir en ella.
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            // Copiamos los bytes del navegador al disco físico de tu servidor.
            Path rutaFinal = Paths.get(nombreCarpeta + nombreSeguro);
            Files.copy(archivo.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            // 5. REGISTRO EN BASE DE DATOS: Persistimos la metadata y damos el premio por aporte académico.
            int idAutor = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

            if (idAutor != -1) {
                // Instanciamos el objeto con el constructor ORM que mapea la realidad al objeto Java.
                Apunte documento = new Apunte(titulo, categoria, idAutor, nombreSeguro);
                // Impactamos el registro en SQLite.
                ApunteDAO.registrarApunte(documento);
                // Sumamos la recompensa al creador por su aporte a la comunidad.
                UsuarioDAO.sumarCreditos(idAutor, MotorEconomia.RECOMPENSA_SUBIDA);
            }

            // Atrapamos errores de I/O en caso de fallo de hardware o permisos de lectura.
        } catch (IOException e) {
            System.out.println("CRÍTICO -> Fallo en escritura física: " + e.getMessage());
            return "redirect:/dashboard?error=fallo_servidor";
        }

        // 6. REDIRECCIÓN LIMPIA: Forzamos la vuelta al Dashboard, eliminando la página blanca de raíz.
        return "redirect:/dashboard";
    }
}