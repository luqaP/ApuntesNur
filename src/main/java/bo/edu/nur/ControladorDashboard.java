// Declaramos el paquete estructural al que pertenece esta clase dentro de tu proyecto universitario.
package bo.edu.nur;

// Importamos la anotación Controller específica para devolver vistas HTML renderizadas por el motor Thymeleaf.
import org.springframework.stereotype.Controller;
// Importamos GetMapping para interceptar las peticiones de lectura hacia una ruta web específica.
import org.springframework.web.bind.annotation.GetMapping;
// Importamos la interfaz Model que nos servirá como mensajero para enviar datos de Java hacia el interior del HTML.
import org.springframework.ui.Model;
// Importamos HttpSession para verificar la identidad del usuario guardada en la memoria temporal del servidor.
import jakarta.servlet.http.HttpSession;
// Importamos la estructura genérica List para manejar de forma dinámica el catálogo masivo de apuntes.
import java.util.List;

// Inyectamos la directiva Controller para que el radar de Spring Boot registre esta clase como un enrutador visual.
@Controller
// Declaramos la clase pública que orquestará el renderizado dinámico de la tienda.
public class ControladorDashboard {

    // Ordenamos al método escuchar exclusivamente las peticiones HTTP GET en la ruta limpia "/dashboard".
    @GetMapping("/dashboard")
    // Declaramos el método público que requiere la sesión actual y el mensajero 'Model' de Spring Boot.
    public String mostrarDashboard(HttpSession sesion, Model modelo) {

        // Extraemos el alias del usuario guardado previamente en la memoria RAM durante el proceso de login.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Verificamos de forma defensiva si la variable es nula, lo que significa que un intruso intenta acceder.
        if (aliasUsuario == null) {
            // Si no hay sesión válida, forzamos al navegador a redirigirse inmediatamente a la pantalla raíz de inicio.
            return "redirect:/";
            // Cerramos el bloque de validación de seguridad perimetral.
        }

        // Consultamos al DAO de usuarios para traducir el alias de texto al ID numérico exacto asignado por SQLite.
        int idUsuario = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);
        // Consultamos la billetera virtual en la base de datos para obtener el saldo actualizado en tiempo real.
        int saldoActual = UsuarioDAO.consultarSaldo(idUsuario);

        // Ordenamos al DAO de apuntes extraer absolutamente todos los documentos físicos registrados en el sistema.
        List<Apunte> catalogoApuntes = ApunteDAO.obtenerTodosLosApuntes();

        // Inyectamos el alias en el paquete del modelo bajo la etiqueta "alias" para que el motor visual lo lea.
        modelo.addAttribute("alias", aliasUsuario);
        // Inyectamos el saldo matemático exacto bajo la etiqueta "creditos" para reemplazar el 0 estático de la interfaz.
        modelo.addAttribute("creditos", saldoActual);
        // Empaquetamos la lista masiva de apuntes bajo la etiqueta "listaApuntes" para que el HTML dibuje la cuadrícula.
        modelo.addAttribute("listaApuntes", catalogoApuntes);

        // Retornamos estrictamente la palabra "dashboard" sin la extensión para que Thymeleaf procese la plantilla correcta.
        return "dashboard";

        // Cerramos el método encargado de construir y autorizar la interfaz visual principal.
    }

// Cerramos la arquitectura general de la clase ControladorDashboard.
}