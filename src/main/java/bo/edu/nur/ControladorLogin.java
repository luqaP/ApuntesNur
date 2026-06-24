// Declaramos la pertenencia estricta de la clase al paquete de nuestro proyecto.
package bo.edu.nur;

// Importamos RestController para habilitar la recepción de peticiones web.
import org.springframework.web.bind.annotation.RestController;
// Importamos PostMapping para interceptar los datos enviados por el formulario HTML.
import org.springframework.web.bind.annotation.PostMapping;
// Importamos RequestParam para extraer los campos de texto del usuario y la contraseña.
import org.springframework.web.bind.annotation.RequestParam;
// Importamos HttpSession nativo de Jakarta para crear una "memoria temporal" que recuerde al usuario.
import jakarta.servlet.http.HttpSession;
// Importamos HttpServletResponse para poder darle órdenes directas al navegador web (como redirigir).
import jakarta.servlet.http.HttpServletResponse;
// Importamos IOException para manejar errores si la redirección de red falla.
import java.io.IOException;

// Inyectamos la anotación para que Spring despierte este controlador.
@RestController
// Declaramos la clase pública que gestionará el inicio de sesión.
public class ControladorLogin {

    // Escuchamos la ruta "/login" proveniente del archivo index.html.
    @PostMapping("/login")
    // Declaramos el método, ahora inyectando 'HttpSession' y 'HttpServletResponse' para manipular la navegación.
    public void procesarAcceso(@RequestParam("aliasUsuario") String alias,
                               @RequestParam("claveUsuario") String clave,
                               HttpSession sesion,
                               HttpServletResponse respuesta) throws IOException {

        // Invocamos a tu DAO para verificar matemáticamente si las credenciales coinciden en SQLite.
        if (UsuarioDAO.validarCredenciales(alias, clave)) {
            // Si es exitoso, guardamos el alias en la memoria RAM del servidor (Sesión) bajo la llave "usuarioLogueado".
            sesion.setAttribute("usuarioLogueado", alias);
            // Ejecutamos una redirección automática forzando al navegador a cargar el Dashboard.
            respuesta.sendRedirect("/dashboard");
            // Abrimos la condición por si el usuario se equivoca o intenta entrar por fuerza bruta.
        } else {
            // Si falla, lo expulsamos redirigiéndolo de vuelta a la pantalla de inicio (raíz).
            respuesta.sendRedirect("/");
            // Cerramos el bloque condicional de validación.
        }

        // Cerramos la ejecución del método de acceso.
    }

// Cerramos la arquitectura general de la clase ControladorLogin.
}