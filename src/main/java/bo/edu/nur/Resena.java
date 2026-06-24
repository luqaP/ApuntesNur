// Declaramos la pertenencia estricta de esta clase al paquete principal de la aplicación universitaria.
package bo.edu.nur;

// Importamos la clase LocalDate para poder sellar temporalmente el momento exacto en el que se emite la calificación.
import java.time.LocalDate;

// Declaramos la clase pública Resena que funcionará como el modelo de datos para las opiniones y votos.
public class Resena {

    // Declaramos el atributo privado entero que funciona como la primera llave foránea apuntando al documento evaluado.
    private int idApunte;
    // Declaramos el atributo privado entero que funciona como la segunda llave foránea apuntando al estudiante que opina.
    private int idUsuario;
    // Declaramos el atributo privado entero para cuantificar la calidad del material en un rango cerrado (ej. 1 a 5).
    private int estrellas;
    // Declaramos el atributo privado de texto para que el estudiante justifique su calificación con una crítica escrita.
    private String comentario;
    // Declaramos el atributo privado de fecha para mantener una auditoría cronológica exacta de las transacciones.
    private LocalDate fechaRegistro;

    // Definimos el constructor principal para crear una reseña en el momento en que el servidor web la recibe.
    public Resena(int apunte, int usuario, int calificacion, String texto) {
        // Vinculamos la reseña con su documento, inyectando el parámetro recibido hacia el atributo interno de la instancia.
        this.idApunte = apunte;
        // Vinculamos la reseña con su creador asignando el ID del usuario directamente al estado del objeto.
        this.idUsuario = usuario;
        // Asignamos la nota numérica provista para persistirla en el atributo privado de estrellas.
        this.estrellas = calificacion;
        // Guardamos el texto descriptivo del estudiante en la variable de encapsulamiento de comentario.
        this.comentario = texto;
        // Invocamos la función estática del sistema operativo para capturar el día y mes exacto de la transacción en curso.
        this.fechaRegistro = LocalDate.now();
        // Cerramos el bloque lógico fundamental del constructor de reseñas.
    }

    // Definimos un método público que devuelve el identificador numérico de la tabla de apuntes.
    public int obtenerIdApunte() {
        // Retornamos el valor entero almacenado en la llave foránea relacional idApunte.
        return this.idApunte;
        // Cerramos la lectura pública del ID del apunte.
    }

    // Definimos un método público que devuelve el identificador numérico de la tabla de usuarios.
    public int obtenerIdUsuario() {
        // Retornamos el valor entero protegido en la llave foránea idUsuario.
        return this.idUsuario;
        // Cerramos la lectura pública del ID del usuario.
    }

    // Definimos un método público para extraer el valor matemático de la calificación para inyectarlo en operaciones SQL.
    public int obtenerEstrellas() {
        // Retornamos el atributo privado estrellas para permitir futuros cálculos de promedios ponderados.
        return this.estrellas;
        // Cerramos la lectura pública de la calificación otorgada.
    }

    // Definimos un método público que retorna la crítica textual enviada por el estudiante.
    public String obtenerComentario() {
        // Retornamos la cadena de texto encapsulada celosamente en la variable comentario.
        return this.comentario;
        // Cerramos la lectura pública del texto de la reseña.
    }

    // Definimos un método público que traduce el objeto fecha complejo a una cadena de caracteres cruda.
    public String obtenerFechaRegistroComoTexto() {
        // Transformamos el tipo LocalDate a un String puro invocando toString() para no corromper la base relacional SQLite.
        return this.fechaRegistro.toString();
        // Cerramos el bloque de conversión de la fecha hacia formato texto.
    }

// Cerramos de forma absoluta la declaración estructural de la clase Resena.
}