// Declaramos el paquete estructural de la universidad.
package bo.edu.nur;

// Importamos el estereotipo Controller para habilitar el motor MVC.
import org.springframework.stereotype.Controller;
// Importamos GetMapping para interceptar la ruta principal.
import org.springframework.web.bind.annotation.GetMapping;
// Importamos Model para inyectar variables nativas hacia Thymeleaf.
import org.springframework.ui.Model;
// Importamos ArrayList para poder crear listas simuladas temporalmente.
import java.util.ArrayList;
// Importamos List para manejar las colecciones dinámicas de datos.
import java.util.List;
// Importamos HttpSession para extraer la memoria temporal del estudiante.
import jakarta.servlet.http.HttpSession;

// Inyectamos el decorador para que el radar de Spring Boot registre esta clase.
@Controller
// Declaramos la clase que orquestará el renderizado masivo de la Single Page Application (SPA).
public class ControladorDashboard {

    // Enrutamos la petición HTTP GET hacia la raíz del panel de control.
    @GetMapping("/dashboard")
    // Declaramos el método maestro que orquesta la carga de todas las pestañas simultáneamente.
    public String mostrarDashboard(Model modelo, HttpSession sesion) {

        // ====================================================================================
        // FASE 1: BARRERA PERIMETRAL DE SEGURIDAD
        // ====================================================================================

        // Extraemos el alias almacenado en la memoria RAM tras el inicio de sesión.
        String aliasSesion = (String) sesion.getAttribute("usuarioLogueado");

        // Evaluamos si un intruso intenta acceder directamente por la barra de URL.
        if (aliasSesion == null) {
            // Expulsamos al anónimo hacia el login público.
            return "redirect:/";
            // Cerramos el control de acceso.
        }

        // ====================================================================================
        // FASE 2: EXTRACCIÓN MASIVA DE DATOS PARA TODAS LAS PESTAÑAS
        // ====================================================================================

        // PESTAÑA 1: Consultamos al estudiante conectado para la telemetría general.
        Usuario estudiante = UsuarioDAO.obtenerPorAlias(aliasSesion);

        // PESTAÑA 2 (TIENDA GLOBAL): Extraemos absolutamente todos los apuntes del sistema.
        List<Apunte> catalogoGlobal = ApunteDAO.obtenerTodosLosApuntes();

        // PESTAÑA 3 (MI BIBLIOTECA): Extraemos los apuntes que este estudiante ha subido.
        // NOTA: Más adelante crearemos un método DAO para unir esto con los que ha COMPRADO.
        List<Apunte> miBiblioteca = ApunteDAO.obtenerApuntesPorAutor(estudiante.getIdUsuario());

        // PESTAÑA 4 (SALÓN DE LA FAMA): Simulamos temporalmente a los 3 mejores estudiantes.
        // MOCK DATA: Preparación arquitectónica hasta que conectemos el algoritmo de ranking SQL.
        List<Usuario> rankingTop3 = new ArrayList<>();
        // Inyectamos un perfil simulado de alto rendimiento.
        rankingTop3.add(new Usuario(1, "lucca_dev", "Lucca Ortiz", "REG-2026", "hash", 1500, 12, "Hoy"));
        // Inyectamos el segundo lugar.
        rankingTop3.add(new Usuario(2, "estudiante_pro", "Carlos Tester", "REG-9999", "hash", 1200, 5, "Hoy"));
        // Inyectamos el tercer lugar.
        rankingTop3.add(new Usuario(3, "Mendo", "Adrian Pedriel", "REG-123", "hash", 950, 2, "Ayer"));

        // ====================================================================================
        // FASE 3: INYECCIÓN DE VARIABLES AL MOTOR DE RENDERIZADO (THYMELEAF)
        // ====================================================================================

        // Inyectamos el alias para la barra superior.
        modelo.addAttribute("alias", estudiante.getAlias());
        // Inyectamos la billetera del estudiante.
        modelo.addAttribute("creditos", estudiante.getCreditos());

        // Inyectamos la colección para la pestaña "Tienda Global".
        modelo.addAttribute("listaApuntes", catalogoGlobal);
        // Inyectamos la colección filtrada para la pestaña "Mi Biblioteca".
        modelo.addAttribute("misApuntes", miBiblioteca);
        // Inyectamos la colección de élite para el "Salón de la Fama".
        modelo.addAttribute("rankingEstudiantes", rankingTop3);

        // Ordenamos al servidor ensamblar el 'Dashboard.html' y dispararlo por la red.
        return "Dashboard";

        // Cerramos el método orquestador.
    }
// Cerramos la clase del controlador.
}