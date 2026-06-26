// Declaramos el paquete maestro del sistema universitario.
package bo.edu.nur;

// Importamos la directiva principal para que Spring MVC despierte esta clase.
import org.springframework.stereotype.Controller;
// Importamos el escuchador de peticiones HTTP GET.
import org.springframework.web.bind.annotation.GetMapping;
// Importamos la herramienta para secuestrar palabras de la URL (Path Variables).
import org.springframework.web.bind.annotation.PathVariable;
// Importamos el inyector de datos hacia el HTML.
import org.springframework.ui.Model;
// Importamos la interfaz de colecciones iterables.
import java.util.List;
// Importamos el gestor de memoria temporal.
import jakarta.servlet.http.HttpSession;

// Registramos el controlador en el kernel de la aplicación.
@Controller
// Declaramos la clase que orquesta la carga visual de la identidad pública.
public class ControladorPerfil {

    // Forzamos al motor a interceptar cualquier visita al dominio seguida de /perfil/ y un texto.
    @GetMapping("/perfil/{aliasEstudiante}")
    // Declaramos el método pasando las variables de entorno, seguridad y enlace.
    public String verPerfilUsuario(@PathVariable("aliasEstudiante") String alias, Model modelo, HttpSession sesion) {

        // ====================================================================================
        // FASE 1: BARRERA PERIMETRAL DE PRIVACIDAD
        // ====================================================================================

        // Rescatamos el nombre del estudiante que está tecleando detrás del monitor.
        String visitanteActual = (String) sesion.getAttribute("usuarioLogueado");

        // Bloqueamos el paso a usuarios anónimos o no matriculados.
        if (visitanteActual == null) {
            // Ejecutamos la orden HTTP 302 de redirección a la raíz pública.
            return "redirect:/";
            // Cerramos el control perimetral.
        }

        // ====================================================================================
        // FASE 2: INVESTIGACIÓN DE LA BASE DE DATOS
        // ====================================================================================

        // Consultamos al DAO para confirmar que el alias escrito en la URL es legítimo y extraemos su objeto completo.
        Usuario propietarioPerfil = UsuarioDAO.obtenerPorAlias(alias);

        // Evaluamos si el perfil fue eliminado o la URL fue adulterada por un atacante.
        if (propietarioPerfil == null) {
            // Mandamos al usuario de vuelta al panel con una advertencia de inexistencia.
            return "redirect:/dashboard?error=perfil_no_encontrado";
            // Cerramos la validación de integridad referencial.
        }

        // Ordenamos al DAO que recopile exclusivamente los documentos donde este usuario figura como creador.
        List<Apunte> bibliotecaPersonal = ApunteDAO.obtenerApuntesPorAutor(propietarioPerfil.getIdUsuario());

        // ====================================================================================
        // FASE 3: HEURÍSTICA Y MATEMÁTICAS MOCKEADAS (PREPARACIÓN PARA RED SOCIAL)
        // ====================================================================================

        // Inicializamos la variable que contendrá el resultado de prestigio del estudiante.
        double promedioEstrellas = 0.0;

        // Verificamos si la colección de aportes no está vacía para evitar fallos matemáticos.
        if (!bibliotecaPersonal.isEmpty()) {
            // MOCK DE ARQUITECTURA: Aquí deberás implementar la llamada a ResenaDAO.
            // Temporalmente forzamos una calificación alta para demostrar la inyección en la vista.
            promedioEstrellas = 4.5;
            // Cerramos la lógica de cálculo estadístico.
        }

        // ====================================================================================
        // FASE 4: INYECCIÓN DE VARIABLES AL DOM
        // ====================================================================================

        // Inyectamos el molde del usuario para que Thymeleaf pinte sus datos personales.
        modelo.addAttribute("perfil", propietarioPerfil);
        // Inyectamos su repositorio personal para construir la cuadrícula inferior.
        modelo.addAttribute("listaApuntes", bibliotecaPersonal);
        // Inyectamos la métrica numérica final formateada a un decimal.
        modelo.addAttribute("calificacion", promedioEstrellas);
        // Inyectamos un booleano computado en RAM para saber si el usuario está viendo su propio espejo.
        modelo.addAttribute("esMiPerfil", alias.equals(visitanteActual));

        // Ordenamos al compilador web que fusione los datos con la plantilla física 'perfil.html'.
        return "perfil";

        // Cerramos el método maestro de perfiles sociales.
    }
// Cerramos la arquitectura inquebrantable de ControladorPerfil.
}