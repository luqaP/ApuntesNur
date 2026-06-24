package bo.edu.nur;// Importamos la clase Connection del paquete java.sql para mantener activa la sesión con la base de datos.
import java.sql.Connection;
// Importamos la clase DriverManager del paquete java.sql para localizar y cargar el controlador específico.
import java.sql.DriverManager;
// Importamos la clase SQLException del paquete java.sql para capturar errores de ejecución en el motor relacional.
import java.sql.SQLException;

// Declaramos la clase pública bo.edu.nur.ConexionDB que funcionará como el puente único entre tu código Java y el archivo de SQLite.
public class ConexionDB {

    // Declaramos un método público y estático que retorna un objeto Connection para ser invocado globalmente en la plataforma.
    public static Connection conectar() {
        // Declaramos la variable de conexión y la inicializamos en null para manejar un estado por defecto si ocurre un fallo.
        Connection conexion = null;

        // Abrimos un bloque try para intentar ejecutar el código de carga del driver y la comunicación física.
        try {
            // Obligamos a la Máquina Virtual de Java a buscar y cargar en memoria la clase exacta del driver de SQLite.
            // Si el archivo .jar no está correctamente vinculado en IntelliJ, esta línea lanzará un error ClassNotFoundException.
            Class.forName("org.sqlite.JDBC");

            // Definimos la ruta de conexión (URL) utilizando el protocolo JDBC específico para leer o generar tu archivo local.
            String url = "jdbc:sqlite:apuntes_nur.db";

            // Invocamos el método getConnection del DriverManager para establecer el enlace físico utilizando la URL.
            conexion = DriverManager.getConnection(url);

            // Imprimimos un mensaje en la consola de tu IDE para confirmar visualmente que el enlace fue un éxito rotundo.
            System.out.println("Conexión a SQLite establecida con éxito.");

            // Abrimos un bloque catch múltiple para capturar tanto si falta el archivo .jar como si falla la estructura del archivo.
        } catch (ClassNotFoundException | SQLException e) {
            // Imprimimos un mensaje de error crítico y concatenamos el mensaje exacto que devuelve el sistema operativo.
            // La instrucción e.getMessage() imprimirá la pista absoluta de por qué la base de datos te está rechazando.
            System.out.println("Error crítico al conectar con la base de datos: " + e.getMessage());
            // Cerramos la estructura de control estricta de manejo de excepciones.
        }

        // Retornamos el objeto conexion (ya sea válido con la sesión activa, o null si el proceso falló y fue capturado por el catch).
        return conexion;
        // Cerramos el bloque de ejecución correspondiente al método estático conectar().
    }

// Cerramos el bloque general correspondiente a la declaración estructural de la clase bo.edu.nur.ConexionDB.
}