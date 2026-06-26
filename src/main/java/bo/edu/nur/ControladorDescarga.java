// Declaramos el paquete estructural de la universidad NUR.
package bo.edu.nur;

// Importamos todas las herramientas necesarias para la gestión de peticiones web, binarios y redirecciones.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.servlet.http.HttpSession;

// Inyectamos la directiva Controller para que Spring Boot registre esta clase en el ecosistema.
@Controller
// Declaramos la clase pública que administra el egreso de datos y las transacciones de descarga.
public class ControladorDescarga {

    // Escuchamos peticiones GET dirigidas a la ruta de descarga.
    @GetMapping("/descargar-apunte")
    // Declaramos el método maestro que devuelve un binario o ejecuta una redirección de seguridad.
    public ResponseEntity<?> procesarDescarga(@RequestParam("idApunte") int idApunte, HttpSession sesion) {

        // 1. VALIDACIÓN DE IDENTIDAD Y SEGURIDAD PERIMETRAL
        // Extraemos el alias almacenado en la memoria temporal del servidor.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Evaluamos si el estudiante intentó acceder a una descarga sin haber iniciado sesión.
        if (aliasUsuario == null) {
            // Retornamos un estado HTTP 302 (FOUND) forzando al navegador a volver al índice principal.
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/").build();
            // Cerramos el bloque de validación de seguridad.
        }

        // Traducimos el alias a la llave primaria de SQLite.
        int idComprador = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

        // 2. ORQUESTACIÓN DE LA LÓGICA DE NEGOCIO
        // Delegamos la validación financiera (créditos y propiedad) al MotorEconomia.
        boolean transaccionAprobada = MotorEconomia.procesarAdquisicionSegura(idComprador, idApunte);

        // Evaluamos si el MotorEconomia rechazó la transacción por falta de fondos o fraude.
        if (!transaccionAprobada) {
            // Ejecutamos una redirección silenciosa al dashboard con un parámetro de error para notificar al usuario.
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard?error=transaccion_rechazada").build();
            // Cerramos el bloque de control financiero.
        }

        // 3. RECUPERACIÓN Y ENTREGA DEL BINARIO
        // Obtenemos la metadata del archivo desde el DAO.
        Apunte documento = ApunteDAO.obtenerPorId(idApunte);

        // CORRECCIÓN DE ALCANCE: Declaramos la variable 'recurso' fuera del bloque try para que sea visible en todo el método.
        Resource recurso = null;

        // Abrimos bloque de contingencia para la lectura física en el disco duro.
        try {
            // Construimos la ruta absoluta hacia la bóveda de archivos.
            Path rutaArchivo = Paths.get("repositorio_nur/" + documento.getRutaArchivoFisico()).normalize();
            // Inicializamos el recurso de red.
            recurso = new UrlResource(rutaArchivo.toUri());

            // Verificamos si el archivo existe y es legible por la JVM.
            if (recurso.exists() && recurso.isReadable()) {

                // Entregamos el binario forzando la descarga mediante la cabecera CONTENT_DISPOSITION.
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(recurso);

                // Si el archivo falta en el disco físico.
            } else {
                // Redirigimos al dashboard informando la pérdida del documento.
                return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard?error=archivo_perdido").build();
                // Cerramos la validación de integridad física.
            }

            // Capturamos cualquier excepción de sistema operativo o de red.
        } catch (Exception e) {
            // Redirigimos en caso de catástrofe interna del servidor.
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/dashboard?error=fallo_servidor").build();
            // Cerramos el bloque de manejo de excepciones.
        }

        // Cerramos el método controlador.
    }
// Cerramos la arquitectura definitiva de la clase.
}