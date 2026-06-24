package bo.edu.nur;// Importamos la clase Connection nativa de java.sql para recibir la sesión activa con nuestro archivo SQLite.

import java.sql.Connection;
// Importamos la clase Statement de java.sql que nos permitirá enviar sentencias de texto SQL puro hacia el motor de base de datos.
import java.sql.Statement;
// Importamos la clase SQLException de java.sql para capturar posibles errores de sintaxis en nuestras instrucciones SQL.
import java.sql.SQLException;

// Declaramos la clase pública bo.edu.nur.InstaladorDB, la cual tendrá la responsabilidad exclusiva de fabricar todas las tablas de la plataforma.
public class InstaladorDB {

    // Definimos un método público y estático llamado crearTablas para invocar la construcción de la base de datos directamente.
    public static void crearTablas() {
        // Declaramos una variable de texto y redactamos la sentencia SQL exacta para crear la tabla de usuarios.
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuario ("
                // Definimos la llave primaria autoincrementable para identificar de forma única a cada estudiante de la NUR o externo.
                + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                // Definimos el nombre de usuario como texto obligatorio y único en todo el sistema para los inicios de sesión.
                + "nombre_usuario TEXT NOT NULL UNIQUE, "
                // Definimos el nombre completo del usuario como un campo de texto obligatorio.
                + "nombre_completo TEXT NOT NULL, "
                // Definimos el número de credencial permitiendo que sea nulo para aquellos que no pertenecen a la universidad.
                + "numero_credencial TEXT, "
                // Definimos la columna de la contraseña como texto obligatorio para guardar el hash de seguridad.
                + "contrasena TEXT NOT NULL, "
                // Definimos la cartera de créditos virtuales con un valor predeterminado de cero inicial.
                + "creditos_virtuales INTEGER DEFAULT 0, "
                // Definimos la racha diaria de inicios de sesión con un valor predeterminado de cero.
                + "racha_diaria INTEGER DEFAULT 0, "
                // Definimos la fecha del último acceso como texto, ya que SQLite almacena las fechas inmutables como cadenas (String).
                + "fecha_ultimo_acceso TEXT"
                // Cerramos el bloque de la instrucción SQL estructurada para la tabla bo.edu.nur.Usuario.
                + ");";

        // Declaramos una segunda variable de texto para almacenar la instrucción SQL que construirá la tabla de los apuntes.
        String sqlApunte = "CREATE TABLE IF NOT EXISTS Apunte ("
                // Definimos la llave primaria autoincrementable para identificar de forma única cada archivo Markdown subido.
                + "id_apunte INTEGER PRIMARY KEY AUTOINCREMENT, "
                // Definimos el título del apunte como un campo de texto estrictamente obligatorio.
                + "titulo TEXT NOT NULL, "
                // Definimos la categoría o materia a la que pertenece el documento como texto obligatorio para los foros.
                + "categoria_materia TEXT NOT NULL, "
                // Definimos una columna entera para guardar el ID del estudiante que subió el archivo (preparando la llave foránea).
                + "id_autor INTEGER NOT NULL, "
                // Definimos la ruta física del archivo en el disco duro de tu servidor local como texto obligatorio.
                + "ruta_archivo_fisico TEXT NOT NULL, "
                // Definimos la fecha en la que se subió el documento Markdown como texto.
                + "fecha_subida TEXT NOT NULL, "
                // Definimos el contador histórico de descargas del apunte, iniciando por defecto en cero.
                + "contador_descargas INTEGER DEFAULT 0, "
                // Definimos el promedio de calificación usando el tipo REAL (decimal en SQLite), iniciando en un promedio neutro de 0.0.
                + "promedio_estrellas REAL DEFAULT 0.0, "
                // Declaramos formalmente la relación de llave foránea vinculando el id_autor con el id_usuario de la tabla bo.edu.nur.Usuario.
                + "FOREIGN KEY (id_autor) REFERENCES Usuario(id_usuario)"
                // Cerramos el bloque de la instrucción SQL estructurada para la tabla bo.edu.nur.Apunte.
                + ");";

        // Declaramos una tercera variable de texto para almacenar la instrucción SQL de las transacciones de reseñas.
        String sqlResena = "CREATE TABLE IF NOT EXISTS Resena ("
                // Definimos la llave primaria autoincrementable para cada voto individual emitido en la plataforma.
                + "id_resena INTEGER PRIMARY KEY AUTOINCREMENT, "
                // Definimos la columna entera para guardar el ID del apunte que está siendo calificado.
                + "id_apunte INTEGER NOT NULL, "
                // Definimos la columna entera para guardar el ID del estudiante que está emitiendo la calificación.
                + "id_usuario INTEGER NOT NULL, "
                // Definimos la cantidad de estrellas otorgadas como un número entero obligatorio (del 1 al 5).
                + "estrellas INTEGER NOT NULL, "
                // Definimos el comentario de retroalimentación de los apuntes como un campo de texto opcional.
                + "comentario TEXT, "
                // Definimos la fecha exacta en la que se registró el voto en formato de texto.
                + "fecha_registro TEXT NOT NULL, "
                // Declaramos la llave foránea que conecta esta reseña con su documento correspondiente en la tabla central bo.edu.nur.Apunte.
                + "FOREIGN KEY (id_apunte) REFERENCES Apunte(id_apunte), "
                // Declaramos la llave foránea que conecta esta reseña con el estudiante específico en la tabla maestra bo.edu.nur.Usuario.
                + "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)"
                // Cerramos el bloque de la instrucción SQL estructurada para la tabla transaccional bo.edu.nur.Resena.
                + ");";

        // Abrimos un bloque try-with-resources llamando a la clase bo.edu.nur.ConexionDB para garantizar que la sesión se cierre sola al terminar.
        // Instanciamos también el objeto Statement que ejecutará físicamente los comandos en el motor SQLite.
        try (Connection conexion = ConexionDB.conectar();
             Statement declaracion = conexion.createStatement()) {

            // Invocamos el método execute() enviando la primera instrucción SQL para fabricar la tabla de Usuarios.
            declaracion.execute(sqlUsuario);
            // Invocamos el método execute() enviando la segunda instrucción SQL para fabricar la tabla de Apuntes.
            declaracion.execute(sqlApunte);
            // Invocamos el método execute() enviando la tercera instrucción SQL para fabricar la tabla transaccional de Reseñas.
            declaracion.execute(sqlResena);

            // Imprimimos un mensaje en consola confirmando que la estructura relacional completa fue inyectada en el archivo SQLite.
            System.out.println("Estructura de la base de datos construida: Tablas bo.edu.nur.Usuario, bo.edu.nur.Apunte y bo.edu.nur.Resena listas.");

            // Abrimos el bloque catch para capturar cualquier posible error de sintaxis SQL que rompa la ejecución.
        } catch (SQLException e) {
            // Imprimimos el mensaje de error crítico concatenado con la descripción exacta devuelta por el driver JDBC.
            System.out.println("Error crítico al intentar crear las tablas: " + e.getMessage());
            // Cerramos la estructura de control de excepciones try-catch.
        }
        // Cerramos el bloque de ejecución del método estático y principal crearTablas().
    }

// Cerramos la declaración general de la clase pública bo.edu.nur.InstaladorDB.
}