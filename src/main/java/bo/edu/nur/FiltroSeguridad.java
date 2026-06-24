// Declaramos el paquete oficial de nuestra universidad.
package bo.edu.nur;

// Importamos Component para que Spring Boot active este filtro automáticamente al encender el servidor.
import org.springframework.stereotype.Component;
// Importamos la interfaz Filter de Jakarta para interceptar las peticiones antes de que lleguen a los archivos.
import jakarta.servlet.Filter;
// Importamos FilterChain para permitir que la petición continúe su camino si todo está en orden.
import jakarta.servlet.FilterChain;
// Importamos ServletRequest para leer la petición entrante cruda.
import jakarta.servlet.ServletRequest;
// Importamos ServletResponse para poder emitir bloqueos o redirecciones de salida.
import jakarta.servlet.ServletResponse;
// Importamos HttpServletRequest para extraer la URL exacta a la que intenta acceder el usuario.
import jakarta.servlet.http.HttpServletRequest;
// Importamos HttpServletResponse para ejecutar la orden de expulsión si es necesario.
import jakarta.servlet.http.HttpServletResponse;
// Importamos HttpSession para revisar si el usuario tiene su tarjeta de identificación.
import jakarta.servlet.http.HttpSession;
// Importamos IOException y ServletException para manejar fallas críticas en la intercepción.
import java.io.IOException;
import jakarta.servlet.ServletException;

// Usamos @Component para registrar obligatoriamente este guardia de seguridad en el núcleo de Spring Boot.
@Component
// Declaramos la clase FiltroSeguridad implementando la interfaz nativa Filter.
public class FiltroSeguridad implements Filter {

    // Sobrescribimos el método principal doFilter que se ejecuta en cada milisegundo que alguien pide un archivo.
    @Override
    public void doFilter(ServletRequest peticion, ServletResponse respuesta, FilterChain cadena) throws IOException, ServletException {
        // Convertimos la petición cruda a HttpServletRequest para acceder a funciones web avanzadas.
        HttpServletRequest req = (HttpServletRequest) peticion;
        // Convertimos la respuesta cruda a HttpServletResponse para poder redirigir.
        HttpServletResponse res = (HttpServletResponse) respuesta;
        // Extraemos exactamente qué archivo HTML está intentando abrir el estudiante.
        String ruta = req.getRequestURI();

        // Verificamos de forma estricta si la ruta solicitada es nuestra tienda protegida.
        if (ruta.equals("/dashboard.html")) {
            // Solicitamos la sesión actual, enviando 'false' para que NO cree una nueva si no existe.
            HttpSession sesion = req.getSession(false);
            // Evaluamos si no hay sesión activa, o si la sesión existe pero no tiene la llave de "usuarioLogueado".
            if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
                // Al detectar al intruso, bloqueamos el acceso y lo redirigimos obligatoriamente al login.
                res.sendRedirect("/");
                // Usamos 'return' para abortar inmediatamente la ejecución del código restante.
                return;
                // Cerramos la validación de intrusión.
            }
            // Cerramos el análisis de la ruta objetivo.
        }

        // Si no es el dashboard, o si el usuario sí estaba logueado, dejamos que el servidor procese la petición normalmente.
        cadena.doFilter(peticion, respuesta);
        // Cerramos el bloque de ejecución del filtro principal.
    }

// Cerramos la declaración del componente de seguridad.
}