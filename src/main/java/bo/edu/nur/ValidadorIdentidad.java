// Declaramos el paquete estructural que encapsula la lógica de nuestra plataforma universitaria.
package bo.edu.nur;

// Importamos la clase Pattern, que es el motor compilador de expresiones regulares en Java.
import java.util.regex.Pattern;
// Importamos la clase Matcher, que es el ejecutor que coteja el texto contra el patrón compilado.
import java.util.regex.Matcher;

// Declaramos la clase pública que actúa como la aduana de identidad del sistema.
public class ValidadorIdentidad {

    // Definimos el patrón criptográfico inmutable.
    // Explicación del Regex:
    // ^          -> Indica el inicio estricto de la cadena.
    // [a-zA-Z0-9._%+-]+ -> Permite cualquier combinación alfanumérica típica de un usuario antes del arroba.
    // @nur\\.edu\\.bo   -> Fuerza a que el dominio sea exactamente "@nur.edu.bo" (escapando los puntos).
    // $          -> Indica el final estricto de la cadena, evitando que pongan texto extra al final.
    private static final String REGEX_DOMINIO_OFICIAL = "^[a-zA-Z0-9._%+-]+@nur\\.edu\\.bo$";

    // Compilamos el patrón en memoria una sola vez como constante estática para no saturar tu HP Celeron en cada registro.
    private static final Pattern PATRON_CORREO = Pattern.compile(REGEX_DOMINIO_OFICIAL);

    // Declaramos el método público y estático que juzgará la legitimidad del estudiante.
    public static boolean esEstudianteLegitimo(String correoIngresado) {

        // Primera barrera defensiva: verificamos si el dato es nulo o una cadena vacía.
        if (correoIngresado == null || correoIngresado.trim().isEmpty()) {
            // Retornamos falso inmediatamente bloqueando intentos de inyección nula.
            return false;
            // Cerramos la validación de nulidad.
        }

        // Instanciamos el evaluador pasándole el texto que escribió el usuario en el formulario.
        Matcher evaluador = PATRON_CORREO.matcher(correoIngresado);

        // El método matches() retorna true SÓLO si la cadena completa encaja perfectamente en el dominio NUR.
        return evaluador.matches();

        // Cerramos el método validador de identidad.
    }

// Cerramos la arquitectura estricta de la clase.
}