// Declaramos el paquete estructural al que pertenece esta clase dentro de tu proyecto universitario.
package bo.edu.nur;

// Importamos la herramienta Connection para mantener el túnel abierto con SQLite.
import java.sql.Connection;
// Importamos PreparedStatement para precompilar las consultas y evitar inyecciones de código SQL malicioso.
import java.sql.PreparedStatement;
// Importamos ResultSet para poder leer y navegar por las filas que nos devuelve la base de datos.
import java.sql.ResultSet;
// Importamos SQLException para atrapar cualquier colapso físico o de lectura en el disco duro.
import java.sql.SQLException;
// Importamos la estructura ArrayList para crear listas en la memoria RAM que puedan crecer dinámicamente.
import java.util.ArrayList;
// Importamos la interfaz List para definir el tipo de colección que enviaremos al controlador.
import java.util.List;

// Declaramos la clase pública encargada exclusivamente de la lectura y escritura de la tabla Apunte.
public class ApunteDAO {

    // ========================================================================================
    // MÉTODO 1: REGISTRAR UN NUEVO APUNTE EN LA BASE DE DATOS
    // ========================================================================================

    // Declaramos un método público y estático que recibe un objeto 'Apunte' armado y retorna un boolean (éxito/fracaso).
    public static boolean registrarApunte(Apunte nuevoApunte) {

        // Redactamos la consulta de inyección SQL definiendo exactamente las cuatro columnas que vamos a llenar.
        String sql = "INSERT INTO Apunte (titulo, categoria_materia, id_autor, ruta_archivo_fisico) VALUES (?, ?, ?, ?)";

        // Abrimos el bloque try-with-resources para conectar a la base de datos de forma segura y efímera.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos la sentencia SQL en el motor para inyectar las variables de forma blindada.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el título del apunte en el primer comodín leyendo el getter del objeto.
            declaracion.setString(1, nuevoApunte.getTitulo());
            // Inyectamos la categoría académica (ej. "Algebra") en el segundo comodín.
            declaracion.setString(2, nuevoApunte.getCategoria());
            // Inyectamos el ID numérico del autor en el tercer comodín.
            declaracion.setInt(3, nuevoApunte.getIdAutor());
            // Inyectamos la ruta física y el nombre del archivo (con su UUID) en el cuarto comodín.
            declaracion.setString(4, nuevoApunte.getRutaArchivoFisico());

            // Ejecutamos la orden de escritura en el disco duro de tu computadora.
            declaracion.executeUpdate();

            // Imprimimos una sonda de telemetría en la consola confirmando la persistencia.
            System.out.println("TELEMETRÍA DAO -> Apunte físico '" + nuevoApunte.getTitulo() + "' registrado en SQLite.");

            // Retornamos verdadero indicando que la operación de guardado fue un éxito absoluto.
            return true;

            // Capturamos cualquier excepción de base de datos (como disco lleno o archivo bloqueado).
        } catch (SQLException e) {

            // Registramos el error crítico en la consola roja para depurar sin tumbar el servidor de Tomcat.
            System.out.println("ERROR DAO -> Fallo al guardar el apunte en SQLite: " + e.getMessage());

            // Retornamos falso para que el controlador sepa que la transacción fracasó y no regale los créditos.
            return false;

            // Cerramos el bloque de protección arquitectónica.
        }

        // Cerramos el método de registro de apuntes.
    }

    // ========================================================================================
    // MÉTODO 2: EXTRAER EL CATÁLOGO COMPLETO PARA LA TIENDA VISUAL
    // ========================================================================================

    // Definimos el método público y estático que retornará la Lista estructurada de objetos para Thymeleaf.
    public static List<Apunte> obtenerTodosLosApuntes() {

        // Instanciamos una lista vacía en la memoria RAM para ir apilando los documentos que encontremos.
        List<Apunte> listaCatalogo = new ArrayList<>();

        // Redactamos la consulta SQL de lectura para traer absolutamente todas las filas.
        String sql = "SELECT * FROM Apunte";

        // Abrimos el túnel seguro hacia la bóveda de SQLite usando un bloque autolimpiable.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos la sentencia SQL de lectura.
             PreparedStatement declaracion = conexion.prepareStatement(sql);
             // Ejecutamos la consulta inmediatamente y capturamos todos los registros resultantes.
             ResultSet resultados = declaracion.executeQuery()) {

            // Iniciamos un bucle repetitivo (while) que avanzará fila por fila.
            while (resultados.next()) {

                // Extraemos el título del documento leyendo la columna textual.
                String titulo = resultados.getString("titulo");
                // Extraemos la categoría académica desde la fila actual.
                String categoria = resultados.getString("categoria_materia");
                // Extraemos el ID numérico del estudiante que lo subió.
                int idAutor = resultados.getInt("id_autor");
                // Extraemos la ruta física completa guardada en disco.
                String rutaCompleta = resultados.getString("ruta_archivo_fisico");

                // Limpiamos la ruta removiendo la etiqueta de la carpeta para dejar exclusivamente el nombre del archivo.
                String nombreArchivo = rutaCompleta.replace("repositorio_nur/", "");

                // Instanciamos un nuevo objeto Apunte en la RAM usando el constructor con los datos extraídos.
                Apunte apunteRecuperado = new Apunte(titulo, categoria, idAutor, nombreArchivo);

                // Inyectamos este objeto armado al final de nuestra lista maestra.
                listaCatalogo.add(apunteRecuperado);

                // Cerramos el bucle de recolección de filas.
            }

            // Capturamos cualquier colapso de lectura en el archivo .db.
        } catch (SQLException e) {

            // Imprimimos el error crítico en la consola para alertarnos de la falla.
            System.out.println("ERROR DAO -> Fallo masivo al cargar el catálogo de apuntes: " + e.getMessage());

            // Cerramos el bloque de contingencia.
        }

        // Retornamos la lista completa al ControladorDashboard.
        return listaCatalogo;

        // Cerramos el método de extracción del catálogo.
    }

// Cerramos la arquitectura completa de la clase ApunteDAO.
}