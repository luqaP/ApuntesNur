// Declaramos el paquete oficial de nuestra institución.
package bo.edu.nur;

// Importamos Component para que Spring Boot enganche este filtro en la fase de arranque.
import org.springframework.stereotype.Component;
// Importamos la interfaz nativa Filter de Jakarta para interceptar peticiones web de bajo nivel.
import jakarta.servlet.Filter;
// Importamos FilterChain para habilitar el pase de la petición si el usuario es válido.
import jakarta.servlet.FilterChain;
// Importamos ServletRequest para analizar la petición entrante desde el navegador.
import jakarta.servlet.ServletRequest;
// Importamos ServletResponse para poder inyectar directivas de salida.
import jakarta.servlet.ServletResponse;
// Importamos HttpServletRequest para extraer la URL exacta solicitada por el cliente.
import jakarta.servlet.http.HttpServletRequest;
// Importamos HttpServletResponse para ejecutar la orden de expulsión mediante código HTTP 302.
import jakarta.servlet.http.HttpServletResponse;
// Importamos HttpSession para revisar la existencia de la credencial en RAM.
import jakarta.servlet.http.HttpSession;
// Importamos las excepciones necesarias para el manejo de flujos rotos.
import java.io.IOException;
import jakarta.servlet.ServletException;

// Registramos el componente en el núcleo de inversión de control del framework.
@Component
// Declaramos el muro de seguridad perimetral implementando la interfaz estricta.
public class FiltroSeguridad implements Filter {

    // Sobrescribimos el método principal que audita cada milisegundo de navegación en el servidor.
    @Override
    public void doFilter(ServletRequest peticion, ServletResponse respuesta, FilterChain cadena) throws IOException, ServletException {
        // Convertimos el paquete crudo en un objeto con funciones web explícitas.
        HttpServletRequest req = (HttpServletRequest) peticion;
        // Transformamos la respuesta genérica para habilitar redirecciones.
        HttpServletResponse res = (HttpServletResponse) respuesta;
        // Extraemos la cadena de texto con la ruta solicitada por el cliente.
        String ruta = req.getRequestURI();

        // REEMPLAZO CRÍTICO: Vigilamos la ruta mapeada real "/dashboard", eliminando la extensión inútil.
        if (ruta.equals("/dashboard")) {
            // Solicitamos la sesión actual enviando 'false' para evitar crear una sesión fantasma.
            HttpSession sesion = req.getSession(false);

            // Comprobamos si el espacio en memoria es nulo o carece de la llave de autenticación.
            if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
                // Al confirmar la intrusión, disparamos una redirección forzosa hacia la pantalla de inicio de sesión.
                res.sendRedirect("/");
                // Abortamos la ejecución de la función para que el código maligno no avance a la capa de servicios.
                return;
                // Cerramos la protección perimetral del panel de control.
            }
            // Cerramos el bloque de auditoría de rutas protegidas.
        }

        // Si la validación fue exitosa o si la ruta era pública, cedemos el paso a la cadena de filtros de Tomcat.
        cadena.doFilter(peticion, respuesta);
        // Cerramos el método núcleo de filtrado.
    }

// Cerramos la arquitectura absoluta de la clase de seguridad.
}