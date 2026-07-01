// Declaramos el paquete oficial de nuestra universidad.
package bo.edu.nur;

// Importamos Component para que el radar de Spring Boot enganche al guardia automáticamente.
import org.springframework.stereotype.Component;
// Importamos la interfaz nativa para interceptar el tráfico de red HTTP.
import jakarta.servlet.Filter;
// Importamos la cadena de filtros para permitir el paso si el usuario es legítimo.
import jakarta.servlet.FilterChain;
// Importamos el lector de peticiones crudas.
import jakarta.servlet.ServletRequest;
// Importamos el emisor de respuestas crudas.
import jakarta.servlet.ServletResponse;
// Importamos el adaptador HTTP para extraer la URL exacta que solicita el usuario.
import jakarta.servlet.http.HttpServletRequest;
// Importamos el adaptador HTTP para ejecutar órdenes de redirección forzada.
import jakarta.servlet.http.HttpServletResponse;
// Importamos la sesión para auditar la memoria RAM en busca de credenciales.
import jakarta.servlet.http.HttpSession;
// Importamos las excepciones de manipulación de red y E/S.
import java.io.IOException;
import jakarta.servlet.ServletException;

// Registramos el filtro en el ecosistema central de la aplicación.
@Component
// Declaramos la clase blindada.
public class FiltroSeguridad implements Filter {

    // Sobrescribimos el interceptor que se dispara milisegundos antes de abrir cualquier URL.
    @Override
    public void doFilter(ServletRequest peticion, ServletResponse respuesta, FilterChain cadena) throws IOException, ServletException {

        // Transformamos (Casteo) los objetos genéricos a su versión HTTP para acceder a sus rutinas de navegación.
        HttpServletRequest req = (HttpServletRequest) peticion;
        HttpServletResponse res = (HttpServletResponse) respuesta;

        // Extraemos la ruta final solicitada (Ej: "/dashboard").
        String ruta = req.getRequestURI();

        // BUG FIX: Verificamos de forma estricta la ruta del nuevo endpoint del controlador, sin el ".html".
        if (ruta.equals("/dashboard")) {

            // Solicitamos acceso a la memoria temporal (false = no crear una sesión nueva si está vacía).
            HttpSession sesion = req.getSession(false);

            // Evaluamos algebraicamente: Si no hay memoria OR si la variable 'usuarioLogueado' no existe...
            if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
                // Denegamos el acceso al DOM central y pateamos al intruso hacia la pantalla pública (raíz).
                res.sendRedirect("/");
                // Abortamos la ejecución del método para evitar procesamientos posteriores.
                return;
                // Cerramos condición de intrusión.
            }
            // Cerramos el análisis de la ruta protegida.
        }

        // Si la ruta no está protegida o el estudiante es legítimo, cedemos el control para que el navegador cargue el HTML.
        cadena.doFilter(peticion, respuesta);

        // Cerramos el interceptor de red.
    }
// Cerramos la arquitectura del perímetro de seguridad.
}