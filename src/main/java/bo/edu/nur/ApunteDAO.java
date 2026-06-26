// Declaramos el paquete estructural para la capa de acceso a datos.
package bo.edu.nur;

// Importamos la interfaz Connection para manejar el tubo hacia SQLite.
import java.sql.Connection;
// Importamos PreparedStatement para blindar el código contra inyecciones SQL.
import java.sql.PreparedStatement;
// Importamos ResultSet para navegar por la tabla virtual que devuelve la base de datos.
import java.sql.ResultSet;
// Importamos SQLException para manejar colapsos del motor relacional.
import java.sql.SQLException;
// Importamos List y ArrayList para empaquetar las filas en colecciones de Java.
import java.util.ArrayList;
import java.util.List;

// Declaramos el Data Access Object encargado de la tabla Apunte.
public class ApunteDAO {

    // Método maestro para insertar un nuevo registro en el disco duro.
    public static boolean registrarApunte(Apunte apunte) {
        // Redactamos la orden SQL de inserción con parámetros ciegos (?).
        String sql = "INSERT INTO Apunte (titulo, categoria_materia, id_autor, ruta_archivo_fisico) VALUES (?, ?, ?, ?)";

        // Iniciamos el bloque blindado que cerrará la conexión automáticamente al terminar.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la sentencia en el motor de base de datos.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el título extraído de la RAM al primer parámetro SQL.
            declaracion.setString(1, apunte.getTitulo());
            // Inyectamos la taxonomía al segundo parámetro.
            declaracion.setString(2, apunte.getCategoria());
            // Inyectamos la llave foránea al tercer parámetro.
            declaracion.setInt(3, apunte.getIdAutor());
            // Inyectamos la ruta del binario al cuarto parámetro.
            declaracion.setString(4, apunte.getRutaArchivoFisico());

            // Ejecutamos la orden de escritura y verificamos si se afectó al menos una fila.
            return declaracion.executeUpdate() > 0;

            // Atrapamos cualquier error físico del disco o de bloqueo de la base de datos.
        } catch (SQLException e) {
            // Imprimimos la traza del error en la terminal de diagnóstico.
            System.out.println("ERROR DAO -> Fallo al insertar el apunte: " + e.getMessage());
            // Devolvemos falso para notificar el fallo a la capa de servicio.
            return false;
            // Cerramos la captura de contingencia.
        }
        // Cerramos el método de escritura.
    }

    // ========================================================================================
    // MÉTODO 2: EXTRAER EL CATÁLOGO COMPLETO MEDIANTE ÁLGEBRA RELACIONAL (CORREGIDO)
    // ========================================================================================

    // Método estático maestro para extraer la biblioteca global inyectando el alias de los creadores.
    public static List<Apunte> obtenerTodosLosApuntes() {
        // Reservamos espacio en la memoria RAM para la colección dinámica de objetos.
        List<Apunte> lista = new ArrayList<>();

        // REDACCIÓN CRÍTICA (BUG FIX):
        // Realizamos un INNER JOIN entre Apunte (a) y Usuario (u).
        // Extraemos todas las columnas de Apunte (a.*) y ÚNICAMENTE la columna 'nombre_usuario' de Usuario.
        // Anteriormente pedíamos 'u.alias', lo cual provocaba un colapso físico por inexistencia.
        String sql = "SELECT a.*, u.nombre_usuario FROM Apunte a INNER JOIN Usuario u ON a.id_autor = u.id_usuario";

        // Abrimos el conducto temporal hacia el archivo físico .db.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la consulta masiva.
             PreparedStatement declaracion = conexion.prepareStatement(sql);
             // Ejecutamos la lectura y volcamos el disco duro en el cursor de memoria.
             ResultSet resultados = declaracion.executeQuery()) {

            // Iteramos fila por fila mientras el cursor detecte registros.
            while (resultados.next()) {
                // Mapeamos la llave primaria del documento.
                int id = resultados.getInt("id_apunte");
                // Mapeamos el título comercial del documento.
                String titulo = resultados.getString("titulo");
                // Mapeamos la taxonomía académica.
                String categoria = resultados.getString("categoria_materia");
                // Mapeamos el ID foráneo del autor.
                int idAutor = resultados.getInt("id_autor");
                // Mapeamos la ruta física, purgando el nombre del directorio para facilitar el trabajo a Thymeleaf.
                String ruta = resultados.getString("ruta_archivo_fisico").replace("repositorio_nur/", "");

                // EXTRACCIÓN JOIN CORREGIDA: Rescatamos el nombre de usuario desde la columna física real 'nombre_usuario'.
                String aliasRecuperado = resultados.getString("nombre_usuario");

                // Instanciamos el objeto con el constructor ORM actualizado de 6 parámetros.
                Apunte apunte = new Apunte(id, titulo, categoria, idAutor, ruta, aliasRecuperado);

                // Sumamos la entidad armada a la lista que enviaremos al controlador.
                lista.add(apunte);
                // Cerramos el bucle repetitivo de mapeo.
            }

            // Atrapamos excepciones de lectura física en el disco.
        } catch (SQLException e) {
            // Notificamos el colapso a la consola de IntelliJ sin detener abruptamente Tomcat.
            System.out.println("ERROR DAO -> Fallo masivo al cargar apuntes JOIN: " + e.getMessage());
            // Cerramos el control de excepciones.
        }

        // Retornamos el cargamento de apuntes al ControladorDashboard.
        return lista;
        // Cerramos el método de extracción masiva.
    }

    // NUEVO MÉTODO MAESTRO: Extrae únicamente la biblioteca personal de un estudiante específico.
    public static List<Apunte> obtenerApuntesPorAutor(int idAutorBusqueda) {
        // Inicializamos la colección receptora.
        List<Apunte> lista = new ArrayList<>();
        // Redactamos la consulta condicionando la búsqueda a la llave foránea.
        String sql = "SELECT * FROM Apunte WHERE id_autor = ?";

        // Abrimos la sesión de base de datos.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la sentencia con el filtro.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el ID del autor al filtro de seguridad.
            declaracion.setInt(1, idAutorBusqueda);

            // Ejecutamos la búsqueda de alta precisión.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Recorremos los impactos confirmados.
                while (resultados.next()) {
                    // Mapeamos ID.
                    int id = resultados.getInt("id_apunte");
                    // Mapeamos título.
                    String titulo = resultados.getString("titulo");
                    // Mapeamos categoría.
                    String categoria = resultados.getString("categoria_materia");
                    // Mapeamos autor numérico.
                    int idAutor = resultados.getInt("id_autor");
                    // Mapeamos ruta.
                    String ruta = resultados.getString("ruta_archivo_fisico").replace("repositorio_nur/", "");

                    // Ensamblamos la entidad (enviamos el alias como desconocido porque en esta vista no es necesario redundar).
                    Apunte apunte = new Apunte(id, titulo, categoria, idAutor, ruta, "Mi Perfil");
                    // Añadimos a la colección.
                    lista.add(apunte);
                    // Cerramos el bucle.
                }
                // Anulamos el ResultSet de la RAM.
            }
            // Atrapamos el error.
        } catch (SQLException e) {
            // Notificamos a la consola.
            System.out.println("ERROR DAO -> Fallo al buscar apuntes por autor: " + e.getMessage());
            // Cerramos el atrapador.
        }
        // Devolvemos la biblioteca purificada.
        return lista;
        // Cerramos el método de extracción personal.
    }

    // Método inalterado para extraer la metadata de un solo archivo para procesar su descarga.
    public static Apunte obtenerPorId(int idApunteBusqueda) {
        // Redactamos la consulta con llave primaria.
        String sql = "SELECT * FROM Apunte WHERE id_apunte = ?";
        // Iniciamos el conducto.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos instrucción.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {
            // Filtramos por ID.
            declaracion.setInt(1, idApunteBusqueda);
            // Ejecutamos.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Si existe el archivo.
                if (resultados.next()) {
                    // Reconstruimos la fila en un objeto.
                    return new Apunte(
                            resultados.getInt("id_apunte"),
                            resultados.getString("titulo"),
                            resultados.getString("categoria_materia"),
                            resultados.getInt("id_autor"),
                            resultados.getString("ruta_archivo_fisico"),
                            "Sistema"
                    );
                    // Cerramos condicional.
                }
                // Liberamos recursos.
            }
            // Atrapamos excepción.
        } catch (SQLException e) {
            // Reportamos.
            System.out.println("ERROR DAO -> Fallo al buscar apunte por ID: " + e.getMessage());
            // Cerramos catch.
        }
        // Retornamos nulo si alguien inventó un ID.
        return null;
        // Cerramos método exacto.
    }
    // ========================================================================================
    // MÉTODOS FINANCIEROS Y DE DERECHOS DE PROPIEDAD (TRANSACCIONES)
    // ========================================================================================

    // Método estático que verifica en el libro mayor si un estudiante ya posee los derechos de un documento.
    public static boolean verificarAdquisicion(int idComprador, int idApunte) {
        // Redactamos la consulta condicionada buscando una fila en la tabla intermedia que cruce ambos IDs.
        // Asumimos que tu tabla de unión se llama 'Adquisicion' (si se llama distinto en InstaladorDB, cámbialo aquí).
        String sql = "SELECT 1 FROM Adquisicion WHERE id_usuario = ? AND id_apunte = ?";

        // Iniciamos el conducto blindado hacia el archivo SQLite.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la sentencia para evitar inyecciones de código.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el ID del estudiante en la primera incógnita.
            declaracion.setInt(1, idComprador);
            // Inyectamos el ID del documento en la segunda incógnita.
            declaracion.setInt(2, idApunte);

            // Disparamos la lectura hacia el disco duro.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Si el cursor avanza al menos una vez, significa que el recibo de compra existe.
                return resultados.next();
                // Cerramos la manipulación del cursor de memoria.
            }

            // Atrapamos cualquier colapso de comunicación con el archivo de base de datos.
        } catch (SQLException e) {
            // Documentamos el error técnico en la terminal de tu entorno de desarrollo.
            System.out.println("ERROR DAO -> Fallo al verificar la adquisición en el libro mayor: " + e.getMessage());
            // Cerramos el bloque de contingencia.
        }

        // Si hay un fallo crítico, devolvemos falso por defecto para proteger la propiedad intelectual.
        return false;
        // Cerramos el auditor de adquisiciones.
    }

    // Método estático que inscribe permanentemente los derechos de descarga en la base de datos tras un pago exitoso.
    public static boolean registrarAdquisicion(int idComprador, int idApunte) {
        // Redactamos la sentencia de inserción para crear un nuevo recibo en la tabla intermedia.
        String sql = "INSERT INTO Adquisicion (id_usuario, id_apunte) VALUES (?, ?)";

        // Iniciamos la sesión transaccional con tu base de datos local.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos el inyector SQL.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Asignamos el ID numérico del comprador extraído de la memoria RAM.
            declaracion.setInt(1, idComprador);
            // Asignamos el ID del apunte adquirido.
            declaracion.setInt(2, idApunte);

            // Ejecutamos la orden de escritura y validamos si se alteró exitosamente una fila en el disco.
            return declaracion.executeUpdate() > 0;

            // Capturamos violaciones de integridad estructural (ej. intentar registrar la misma compra dos veces).
        } catch (SQLException e) {
            // Registramos la colisión en la terminal para fines de auditoría.
            System.out.println("ERROR DAO -> Fallo al registrar la nueva adquisición: " + e.getMessage());
            // Devolvemos falso para que el MotorEconomia sepa que el proceso físico fracasó.
            return false;
            // Cerramos la captura de errores.
        }
        // Cerramos el notario de adquisiciones.
    }
// Cerramos la clase completa.
}