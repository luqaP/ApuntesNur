// Declaramos el paquete base de la estructura del proyecto universitario.
package bo.edu.nur;

// Importamos el estereotipo Controller para que Spring Boot asigne esta clase al motor de vistas MVC.
import org.springframework.stereotype.Controller;
// Importamos PostMapping para interceptar los envíos del formulario HTML.
import org.springframework.web.bind.annotation.PostMapping;
// Importamos RequestParam para extraer los datos digitados por el estudiante.
import org.springframework.web.bind.annotation.RequestParam;
// Importamos la librería de encriptación bcrypt para asegurar la contraseña.
import org.mindrot.jbcrypt.BCrypt;

// Inyectamos el decorador principal para habilitar la clase en el ecosistema.
@Controller
// Declaramos la clase pública que actuará como la aduana de creación de cuentas.
public class ControladorRegistro {

    // Enrutamos el método para escuchar la acción del formulario HTML.
    @PostMapping("/procesar-registro")
    // Declaramos el método maestro exigiendo todos los parámetros del formulario HTML.
    public String procesarRegistro(@RequestParam("aliasUsuario") String alias,
                                   @RequestParam("nombreCompleto") String nombre,
                                   @RequestParam("correoInstitucional") String correo,
                                   @RequestParam("numeroCredencial") String credencial,
                                   @RequestParam("contrasena") String contrasenaCruda) {

        // ====================================================================================
        // FASE 1: GATEKEEPING ESTÁTICO (FORMATOS Y DOMINIO)
        // ====================================================================================

        // Definimos la expresión regular inquebrantable para forzar exactamente 6 dígitos numéricos.
        String regexCredencial = "^\\d{6}$";

        // Comprobamos si la credencial viola el formato estandarizado.
        if (!credencial.matches(regexCredencial)) {
            // Imprimimos una alerta en la consola advirtiendo del formato inválido.
            System.out.println("REGISTRO DENEGADO -> La credencial no cumple con los 6 dígitos: " + credencial);
            // Ejecutamos el retorno anticipado enviando un código de error a la vista.
            return "redirect:/registro.html?error=credencial_invalida";
            // Cerramos el filtro de formato de credencial.
        }

        // Invocamos a la aduana de identidad para verificar el dominio institucional.
        if (!ValidadorIdentidad.esEstudianteLegitimo(correo)) {
            // Alertamos en la terminal que un impostor intentó registrarse.
            System.out.println("REGISTRO DENEGADO -> Intento de registro con dominio no oficial: " + correo);
            // Abortamos la ejecución y devolvemos al usuario al formulario.
            return "redirect:/registro.html?error=dominio_invalido_solo_nur";
            // Cerramos la muralla anti-impostores.
        }

        // ====================================================================================
        // FASE 2: SEGURIDAD Y PREPARACIÓN DE DATOS
        // ====================================================================================

        // Ciframos la contraseña plana usando el algoritmo bcrypt para proteger la base de datos.
        String contrasenaCifrada = BCrypt.hashpw(contrasenaCruda, BCrypt.gensalt(12));

        // ====================================================================================
        // FASE 3: MAPEO OBJETO-RELACIONAL EXACTO (ORM)
        // ====================================================================================

        // CORRECCIÓN: Instanciamos el objeto enviando ESTRICTAMENTE los 4 parámetros que exige tu clase Usuario.java.
        // El objeto en la memoria RAM se encargará por sí solo de poner sus propios créditos en 0.
        Usuario nuevoEstudiante = new Usuario(alias, nombre, credencial, contrasenaCifrada);

        // Delegamos al DAO la responsabilidad física de abrir el flujo hacia SQLite y hacer el INSERT.
        boolean guardadoExitoso = UsuarioDAO.registrarUsuario(nuevoEstudiante);

        // ====================================================================================
        // FASE 4: ENRUTAMIENTO FINAL
        // ====================================================================================

        // Evaluamos si el disco duro confirmó la escritura sin colapsar por alias duplicados.
        if (guardadoExitoso) {
            // Si la cuenta se materializó, enviamos al estudiante a la pantalla de login con un aviso de éxito.
            return "redirect:/?exito=cuenta_creada";
            // En caso de que SQLite levante una excepción.
        } else {
            // Informamos a la consola del conflicto de integridad referencial.
            System.out.println("REGISTRO FALLIDO -> Colisión en SQLite (Alias ya existente).");
            // Devolvemos al formulario indicando que el nombre de usuario está ocupado.
            return "redirect:/registro.html?error=alias_duplicado";
            // Cerramos el bloque de evaluación de persistencia.
        }

        // Cerramos el método orquestador principal.
    }
// Cerramos la estructura defensiva de la clase ControladorRegistro.
}