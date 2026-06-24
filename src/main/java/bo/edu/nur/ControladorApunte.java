package bo.edu.nur;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
// Importamos la librería nativa para generar Identificadores Únicos Universales matemáticos.
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// Importamos la clase de Sesión para verificar la identidad del estudiante que sube el archivo.
import jakarta.servlet.http.HttpSession;

@RestController
public class ControladorApunte {

    @PostMapping("/subir-apunte")
    // Inyectamos HttpSession en el método para acceder a la identidad del usuario logueado.
    public String procesarSubida(@RequestParam("tituloApunte") String titulo,
                                 @RequestParam("categoriaMateria") String categoria,
                                 @RequestParam("archivoFisico") MultipartFile archivo,
                                 HttpSession sesion) {

        // Paso A: VERIFICACIÓN DE IDENTIDAD Y FRAUDE
        // Comprobamos si el usuario se saltó el login forzando la petición HTTP.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");
        if (aliasUsuario == null) {
            return "<h2>Acceso Denegado</h2><p>Debes iniciar sesión para subir apuntes.</p>";
        }

        // Filtramos archivos vacíos o extremadamente pequeños para evitar el farmeo de créditos.
        if (archivo.isEmpty() || archivo.getSize() < 50) {
            return "<h2>Fraude Detectado</h2><p>El archivo es inválido o está vacío.</p>";
        }

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || (!nombreOriginal.toLowerCase().endsWith(".md") && !nombreOriginal.toLowerCase().endsWith(".pdf"))) {
            return "<h2>Formato Inválido</h2><p>Solo se permiten archivos Markdown (.md) y PDF.</p>";
        }

        // Paso B: ELIMINACIÓN DE COLISIÓN DE NOMBRES (BUG 2 FIX)
        // Generamos un código UUID aleatorio (ej. "550e8400-e29b-41d4-a716-446655440000").
        String codigoUnico = UUID.randomUUID().toString();
        // Concatenamos el código con el nombre original (ej. "550e..._calculo.md"). ¡Imposible que se repita!
        String nombreSeguro = codigoUnico + "_" + nombreOriginal;

        try {
            // Paso C: PERSISTENCIA FÍSICA EN DISCO DURO
            String nombreCarpeta = "repositorio_nur/";
            File directorioDestino = new File(nombreCarpeta);
            if (!directorioDestino.exists()) {
                directorioDestino.mkdirs();
            }

            // Utilizamos el nuevo nombre seguro para guardar el archivo físico en Windows.
            Path rutaFinal = Paths.get(nombreCarpeta + nombreSeguro);
            Files.copy(archivo.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            // Paso D: PERSISTENCIA LÓGICA Y ECONOMÍA
            // Transformamos el alias de la sesión en un ID numérico usando nuestro nuevo método.
            int idAutor = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

            // Si el ID es válido, armamos el objeto de Java.
            if (idAutor != -1) {
                // Instanciamos el modelo matemático del apunte.
                Apunte documento = new Apunte(titulo, categoria, idAutor, rutaFinal.toString());
                // Invocamos al puente DAO para escribir la fila en SQLite.
                ApunteDAO.registrarApunte(documento);

                // Le otorgamos al estudiante la recompensa por su aporte solidario.
                UsuarioDAO.sumarCreditos(idAutor, MotorEconomia.RECOMPENSA_SUBIDA);
            }

            return "<h2>¡Aporte Validado!</h2><p>Tu archivo fue asegurado bajo el código " + codigoUnico.substring(0,8) + ". ¡Ganaste " + MotorEconomia.RECOMPENSA_SUBIDA + " créditos!</p><br><a href='/dashboard.html'>Volver a la Tienda</a>";

        } catch (IOException e) {
            System.out.println("Error físico al escribir: " + e.getMessage());
            return "<h2>Fallo del Servidor</h2><p>No se pudo almacenar el documento físico.</p>";
        }
    }
}