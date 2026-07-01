// Declaramos el paquete estructural de la universidad NUR.
package bo.edu.nur;

// Importamos el decorador MVC para que Tomcat asigne a esta clase el manejo de las rutas visuales.
import org.springframework.stereotype.Controller;
// Importamos el interceptor del tráfico HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;
// Importamos la sesión para auditar la memoria RAM.
import jakarta.servlet.http.HttpSession;

// Inyectamos el decorador Controller en el ecosistema.
@Controller
// Declaramos la clase que servirá de puente lógico hacia la bóveda "templates".
public class ControladorVistas {

    // ========================================================================================
    // RUTA 1: PANTALLA PRINCIPAL (LOGIN)
    // ========================================================================================

    // Interceptamos cualquier petición vacía dirigida a la raíz del dominio (Ej: ngrok-free.dev/).
    @GetMapping("/")
    // Declaramos el método puente.
    public String mostrarLogin(HttpSession sesion) {
        // Consultamos la memoria RAM para ver si el estudiante ya había iniciado sesión.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Si el usuario ya está conectado, lo desviamos.
        if (aliasUsuario != null) {
            // Lo pateamos directamente hacia el panel de control.
            return "redirect:/dashboard";
            // Cerramos la validación de conveniencia.
        }

        // Ordenamos a Thymeleaf que busque, ensamble y muestre el archivo "index.html".
        return "index";
        // Cerramos la ruta raíz.
    }

    // ========================================================================================
    // RUTA 2: PANTALLA DE REGISTRO
    // ========================================================================================

    // Interceptamos la ruta clásica de registro.
    @GetMapping("/registro.html")
    // Declaramos el método puente para la creación de cuentas.
    public String mostrarRegistro(HttpSession sesion) {
        // Consultamos nuevamente la memoria temporal.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Si ya está logueado, le negamos la vista de creación de cuentas.
        if (aliasUsuario != null) {
            // Lo enviamos de regreso a su área de trabajo.
            return "redirect:/dashboard";
            // Cerramos el control perimetral.
        }

        // Ordenamos a Thymeleaf que busque en la bóveda el archivo "registro.html" y lo entregue.
        return "registro";
        // Cerramos la ruta de creación de cuentas.
    }

// Cerramos la arquitectura del enrutador de vistas.
}