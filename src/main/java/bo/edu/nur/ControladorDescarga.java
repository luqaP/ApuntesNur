// Definimos el paquete arquitectónico de la universidad.
package bo.edu.nur;

// Importamos GetMapping porque las descargas en los navegadores se ejecutan mediante peticiones GET.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
// Importamos ResponseEntity para poder devolver un archivo físico en lugar de texto o HTML.
import org.springframework.http.ResponseEntity;
// Importamos HttpHeaders para decirle a Google Chrome "¡Oye, esto es un archivo, descárgalo!".
import org.springframework.http.HttpHeaders;
// Importamos MediaType para definir que enviaremos un flujo de bytes genéricos.
import org.springframework.http.MediaType;
// Importamos Resource y UrlResource para mapear el archivo del disco duro hacia la red.
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
// Importamos Paths y Path para navegar por las carpetas de Windows de forma segura.
import java.nio.file.Path;
import java.nio.file.Paths;
// Importamos la sesión para saber a quién cobrarle.
import jakarta.servlet.http.HttpSession;

// Inyectamos el anotador para encender el nodo de red.
@RestController
// Declaramos la clase que administra la distribución del conocimiento.
public class ControladorDescarga {

    // Escuchamos peticiones GET en la ruta específica de descarga.
    @GetMapping("/descargar-apunte")
    // Declaramos el método utilizando el comodín <?> en la respuesta, ya que a veces devolveremos un archivo (Resource) y a veces un texto de error (String).
    public ResponseEntity<?> procesarDescarga(@RequestParam("archivoDestino") String nombreArchivo, HttpSession sesion) {

        // 1. VALIDACIÓN DE IDENTIDAD
        // Preguntamos a la memoria RAM quién está intentando descargar.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");
        // Si es un fantasma o intruso, denegamos el acceso.
        if (aliasUsuario == null) {
            // Retornamos un estado HTTP 401 (No Autorizado) con un mensaje textual.
            return ResponseEntity.status(401).body("Error: Debes iniciar sesión para descargar.");
            // Cerramos el control de acceso.
        }

        // 2. VALIDACIÓN ECONÓMICA
        // Traducimos el alias del usuario a su ID numérico interno en la base de datos.
        int idAutor = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);
        // Consultamos la bóveda de SQLite para saber exactamente cuántos créditos tiene.
        int saldoActual = UsuarioDAO.consultarSaldo(idAutor);

        // Invocamos al MotorEconomia para que ejecute su regla de negocio matemática (¿Tiene al menos 5 créditos?).
        if (!MotorEconomia.puedeDescargar(saldoActual)) {
            // Si la matemática falla, bloqueamos la transacción y avisamos de la pobreza virtual.
            return ResponseEntity.status(403).body("Error: Fondos insuficientes. Tienes " + saldoActual + " créditos y necesitas " + MotorEconomia.COSTO_DESCARGA + ".");
            // Cerramos la barrera de peaje económico.
        }

        // 3. EXTRACCIÓN Y ENTREGA FÍSICA
        // Abrimos el bloque try-catch porque leer el disco duro es propenso a fallas (ej. archivo borrado accidentalmente).
        try {
            // Construimos la ruta exacta hacia la bóveda donde se guardan los archivos físicos.
            Path rutaArchivo = Paths.get("repositorio_nur/" + nombreArchivo).normalize();
            // Empaquetamos la ruta en un objeto Resource que Spring Boot puede enviar por internet.
            Resource recurso = new UrlResource(rutaArchivo.toUri());

            // Verificamos si el archivo realmente existe en tu computadora y si se puede leer.
            if (recurso.exists() && recurso.isReadable()) {

                // Antes de entregar el archivo, procesamos el cobro invocando al DAO.
                UsuarioDAO.cobrarCreditos(idAutor, MotorEconomia.COSTO_DESCARGA);

                // Construimos la respuesta HTTP definitiva instruyendo al navegador web a lanzar la ventana de "Guardar como...".
                return ResponseEntity.ok()
                        // Añadimos el encabezado 'Content-Disposition' forzando la descarga como adjunto (attachment).
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                        // Definimos el tipo de contenido como un octeto binario universal.
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        // Inyectamos el archivo crudo en el cuerpo de la respuesta.
                        .body(recurso);

                // Si el archivo fue borrado de tu HP Celeron o está corrupto, abrimos la condición alterna.
            } else {
                // Informamos del error 404 de no existencia.
                return ResponseEntity.status(404).body("Error: El documento físico no existe en el servidor.");
                // Cerramos la validación de integridad del archivo.
            }

            // Capturamos cualquier desvío crítico de entrada/salida (I/O).
        } catch (Exception e) {
            // Retornamos un error interno del servidor 500 para ocultar detalles sensibles de la excepción al usuario final.
            return ResponseEntity.status(500).body("Error interno del servidor al procesar la descarga.");
            // Cerramos el bloque de contingencia final.
        }

        // Cerramos el método controlador de transacciones y entregas.
    }

// Cerramos la arquitectura general de la clase.
}