// Definimos el paquete estructural del proyecto web de la universidad NUR.
package bo.edu.nur;

// Importamos la utilidad para ajustar archivos locales en recursos manejables por Spring Boot.
import org.springframework.core.io.FileSystemResource;
// Importamos la interfaz genérica de recursos para transmisiones masivas de flujos de datos.
import org.springframework.core.io.Resource;
// Importamos las cabeceras estándar HTTP para configurar las directivas de descarga del navegador.
import org.springframework.http.HttpHeaders;
// Importamos la definición de medios de red para declarar la transferencia de binarios puros.
import org.springframework.http.MediaType;
// Importamos la entidad de control de respuestas para manejar códigos de estado de internet.
import org.springframework.http.ResponseEntity;
// Importamos el estereotipo Controller para añadir este archivo al radar MVC del framework.
import org.springframework.stereotype.Controller;
// Importamos el interceptor de peticiones de red mediante el protocolo HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;
// Importamos el extractor de parámetros obligatorios inyectados desde la URL web.
import org.springframework.web.bind.annotation.RequestParam;
// Importamos el gestor de sesiones de Jakarta para validar identidades en la memoria RAM.
import jakarta.servlet.http.HttpSession;
// Importamos el componente nativo de manipulación e inspección de archivos en disco duro.
import java.io.File;

// Registramos el componente en el kernel central de Spring para que asuma su rol transaccional.
@Controller
// Declaramos la clase pública que servirá de peaje para las descargas de documentos de la universidad.
public class ControladorDescarga {

    // Escuchamos de forma asíncrona cualquier petición dirigida al endpoint "/descargar-apunte".
    @GetMapping("/descargar-apunte")
    // Declaramos el método maestro exigiendo el ID del apunte en formato entero y el acceso a la sesión HTTP.
    public ResponseEntity<Resource> descargarApunte(@RequestParam("idApunte") int idApunte, HttpSession sesion) {

        // Extraemos el alias almacenado de forma temporal en la sesión del servidor tras el login.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Evaluamos perimetralmente si un usuario anónimo intenta vulnerar la ruta web de descargas.
        if (aliasUsuario == null) {
            // Bloqueamos la petición devolviendo un estado HTTP 401 que significa "No Autorizado".
            return ResponseEntity.status(401).build();
            // Cerramos el escudo defensivo de identidad.
        }

        // BUG FIX: Cambiamos el método erróneo por 'obtenerPorId' y reemplazamos la variable fantasma 'idAcorde' por 'idApunte'.
        Apunte apunte = ApunteDAO.obtenerPorId(idApunte);

        // Evaluamos de forma relacional si el ID solicitado no existe dentro de las tablas de SQLite.
        if (apunte == null) {
            // Retornamos un código HTTP 404 notificando que el documento no fue encontrado.
            return ResponseEntity.notFound().build();
            // Cerramos el control de integridad de datos.
        }

        // Consultamos el identificador numérico (ID) único perteneciente al alumno que hace clic en descargar.
        int idComprador = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

        // Si la base de datos reporta un error de traducción de alias, abortamos por precaución.
        if (idComprador == -1) {
            // Devolvemos una respuesta de error estructural 404 al navegador de internet.
            return ResponseEntity.notFound().build();
            // Cerramos el control del comprador.
        }

        // REFACTORIZACIÓN MÁXIMA: Delegamos la validación financiera y comercial enteramente al MotorEconomia.
        boolean transaccionValida = MotorEconomia.procesarAdquisicionSegura(idComprador, idApunte);

        // Evaluamos si el motor económico denegó el intercambio por falta de fondos o violaciones lógicas.
        if (!transaccionValida) {
            // Bloqueamos la descarga devolviendo un código HTTP 403 que representa "Acceso Prohibido".
            return ResponseEntity.status(403).build();
            // Cerramos el control del peaje transaccional.
        }

        // Reconstruimos la dirección exacta del binario físico uniendo la raíz y la nomenclatura blindada con UUID.
        String rutaFisica = "repositorio_nur/" + apunte.getRutaArchivoFisico();
        // Instanciamos el objeto File apuntando a los bloques físicos en el disco de almacenamiento local.
        File archivo = new File(rutaFisica);

        // Comprobamos si por algún fallo de hardware el archivo PDF desapareció físicamente del disco.
        if (!archivo.exists()) {
            // Respondemos con un estado HTTP 404 impidiendo lecturas de punteros vacíos en el sistema.
            return ResponseEntity.notFound().build();
            // Cerramos el control de persistencia en disco duro.
        }

        // Envolvemos el archivo binario dentro de la abstracción de recursos del framework Spring Boot.
        Resource recurso = new FileSystemResource(archivo);

        // Despachamos el recurso empaquetado hacia la red configurando una respuesta HTTP 200 de éxito.
        return ResponseEntity.ok()
                // Declaramos el tipo MIME como flujo binario puro para forzar la ventana de guardado en Windows/Android.
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                // Inyectamos la cabecera Content-Disposition adjuntando de forma limpia el nombre original del archivo PDF.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getName() + "\"")
                // Volcamos la corriente de bytes finales dentro del cuerpo de la respuesta de red.
                .body(recurso);
        // Cerramos el método de control de descargas seguras.
    }
// Cerramos la arquitectura de la clase controladora de descargas.
}