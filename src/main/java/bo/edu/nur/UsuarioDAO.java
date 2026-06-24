// Declaramos el paquete para que Spring Boot lo encuentre sin problemas.
package bo.edu.nur;

// Importamos la conexión nativa a la base de datos.
import java.sql.Connection;
// Importamos la clase para enviar consultas seguras.
import java.sql.PreparedStatement;
// Importamos ResultSet para poder leer las respuestas del SELECT.
import java.sql.ResultSet;
// Importamos la clase para manejar errores físicos del archivo.
import java.sql.SQLException;

// Declaramos la clase que administra la tabla Usuario en SQLite.
public class UsuarioDAO {

    // -------------------------------------------------------------------------
    // MÉTODO 1: Para guardar un usuario nuevo (El que ya teníamos)
    // -------------------------------------------------------------------------
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        String sql = "INSERT INTO Usuario (nombre_usuario, nombre_completo, numero_credencial, contrasena, creditos_virtuales, racha_diaria, fecha_ultimo_acceso) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            declaracion.setString(1, nuevoUsuario.obtenerNombreUsuario());
            declaracion.setString(2, nuevoUsuario.obtenerNombreCompleto());
            declaracion.setString(3, nuevoUsuario.obtenerNumeroCredencial());
            declaracion.setString(4, nuevoUsuario.obtenerContrasena());
            declaracion.setInt(5, nuevoUsuario.obtenerCreditos());
            declaracion.setInt(6, nuevoUsuario.obtenerRachaDiaria());
            declaracion.setString(7, nuevoUsuario.obtenerFechaUltimoAccesoComoTexto());

            declaracion.executeUpdate();
            System.out.println("Estudiante insertado exitosamente en la base de datos.");
            return true;

        } catch (SQLException e) {
            System.out.println("Error crítico al registrar: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // MÉTODO 2: Para validar el inicio de sesión (El que te faltaba)
    // -------------------------------------------------------------------------
    public static boolean validarCredenciales(String alias, String claveCruda) { // Recibimos las credenciales de entrada desde la pantalla web.
        String sql = "SELECT contrasena FROM Usuario WHERE nombre_usuario = ?"; // Preparamos la consulta orientada exclusivamente a la columna de seguridad.
        try (Connection conexion = ConexionDB.conectar(); // Abrimos el túnel de comunicación con la base de datos SQLite.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) { // Preparamos la estructura de la sentencia para mitigar inyecciones SQL.
            declaracion.setString(1, alias); // Vinculamos el alias recibido al primer comodín de la consulta de lectura.
            try (java.sql.ResultSet resultados = declaracion.executeQuery()) { // Ejecutamos la lectura en el disco duro y abrimos el cursor de datos.
                if (resultados.next()) { // Evaluamos si el cursor encontró una fila que coincida con el nombre de usuario.
                    String hashGuardado = resultados.getString("contrasena"); // Extraemos la cadena de 60 caracteres criptográficos de la columna.
                    // COMPROBACIÓN: jBCrypt decodifica el salt y compara matemáticamente ambos valores en milisegundos.
                    return org.mindrot.jbcrypt.BCrypt.checkpw(claveCruda, hashGuardado); // Retorna verdadero si coinciden, falso de lo contrario.
                } // Cerramos el bloque condicional del registro encontrado.
            } // Cerramos automáticamente el ResultSet para liberar memoria RAM.
        } catch (SQLException e) { // Capturamos cualquier colapso o excepción de conectividad con SQLite.
            System.out.println("Error crítico en la validación criptográfica: " + e.getMessage()); // Registramos la falla detallada en la consola de IntelliJ.
        } // Cerramos el bloque de contingencia de errores.
        return false; // Retornamos falso por defecto si las contraseñas no coinciden o si el proceso falló.
    } // Cerramos el método de validación de credenciales.
    // -------------------------------------------------------------------------
    // MÉTODO 3: Extraer el ID numérico a partir del nombre de usuario.
    // -------------------------------------------------------------------------
    public static int obtenerIdPorAlias(String alias) {
        // Redactamos la consulta SQL para traer exclusivamente la columna id_usuario.
        String sql = "SELECT id_usuario FROM Usuario WHERE nombre_usuario = ?";
        // Abrimos la conexión física con la base de datos de manera segura.
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {
            // Reemplazamos el comodín con el alias extraído de la sesión web.
            declaracion.setString(1, alias);
            // Ejecutamos la lectura y guardamos el resultado en la memoria virtual (ResultSet).
            try (java.sql.ResultSet resultados = declaracion.executeQuery()) {
                // Si encontramos una coincidencia, extraemos y retornamos el número entero.
                if (resultados.next()) {
                    // Retornamos el valor numérico exacto de la llave primaria.
                    return resultados.getInt("id_usuario");
                    // Cerramos el bloque condicional.
                }
                // Cerramos el bloque del ResultSet.
            }
            // Capturamos cualquier falla en la lectura del archivo SQLite.
        } catch (SQLException e) {
            // Imprimimos el error crítico para el desarrollador.
            System.out.println("Error al buscar ID: " + e.getMessage());
            // Cerramos el bloque de excepciones.
        }
        // Retornamos -1 como señal de error si el usuario no existe en la base de datos.
        return -1;
        // Cerramos el método de extracción de ID.
    }


    // -------------------------------------------------------------------------
    // MÉTODO 4: Inyectar créditos virtuales como recompensa.
    // -------------------------------------------------------------------------
    public static void sumarCreditos(int idUsuario, int cantidad) {
        // Redactamos una consulta de actualización (UPDATE) matemática para sumar el valor directamente en el motor de la base de datos.
        String sql = "UPDATE Usuario SET creditos_virtuales = creditos_virtuales + ? WHERE id_usuario = ?";
        // Abrimos el túnel de comunicación y preparamos la sentencia.
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {
            // Inyectamos la cantidad de créditos a sumar (ej. 10).
            declaracion.setInt(1, cantidad);
            // Inyectamos el identificador del estudiante que recibirá el premio.
            declaracion.setInt(2, idUsuario);
            // Ejecutamos la orden de escritura y actualización en el disco duro.
            declaracion.executeUpdate();
            // Capturamos cualquier colapso de escritura.
        } catch (SQLException e) {
            // Registramos la falla si la transacción económica fracasa.
            System.out.println("Error al sumar créditos: " + e.getMessage());
            // Cerramos el manejador de errores.
        }
        // Cerramos el método de recompensa económica.
    }
    // -------------------------------------------------------------------------
    // MÉTODO 5: Consultar la billetera virtual del estudiante.
    // -------------------------------------------------------------------------
    public static int consultarSaldo(int idUsuario) {
        // Declaramos la consulta de lectura dirigida específicamente a la columna de los créditos.
        String sql = "SELECT creditos_virtuales FROM Usuario WHERE id_usuario = ?";

        // Abrimos el túnel de conexión seguro hacia el archivo de SQLite.
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el ID numérico del estudiante en el comodín de la consulta.
            declaracion.setInt(1, idUsuario);

            // Ejecutamos la lectura y almacenamos la fila resultante en la memoria RAM.
            try (java.sql.ResultSet resultados = declaracion.executeQuery()) {
                // Evaluamos si el cursor encontró al estudiante en la base de datos.
                if (resultados.next()) {
                    // Extraemos y retornamos el valor matemático exacto de sus créditos actuales.
                    return resultados.getInt("creditos_virtuales");
                    // Cerramos el condicional de existencia.
                }
                // Cerramos el lector de resultados para no dejar fugas de memoria.
            }

            // Capturamos cualquier colisión o error de lectura en el disco.
        } catch (SQLException e) {
            // Imprimimos el rastro de la falla en la consola para depuración.
            System.out.println("Error al consultar saldo: " + e.getMessage());
            // Cerramos el bloque de protección contra errores.
        }
        // Retornamos 0 por seguridad si ocurre un error catastrófico; ante la duda, nadie tiene saldo infinito.
        return 0;
        // Cerramos el método de consulta económica.
    }

    // -------------------------------------------------------------------------
    // MÉTODO 6: Cobrar por la descarga de un apunte.
    // -------------------------------------------------------------------------
    public static void cobrarCreditos(int idUsuario, int cantidadACobrar) {
        // Redactamos la consulta de actualización restando matemáticamente la cantidad del saldo actual.
        String sql = "UPDATE Usuario SET creditos_virtuales = creditos_virtuales - ? WHERE id_usuario = ?";

        // Iniciamos la conexión transaccional con la base de datos.
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el costo de la descarga (que vendrá del MotorEconomia) en el primer comodín.
            declaracion.setInt(1, cantidadACobrar);
            // Inyectamos el ID del estudiante al que se le aplicará el cobro.
            declaracion.setInt(2, idUsuario);

            // Ejecutamos la orden de escritura y modificación irreversible en el disco duro.
            declaracion.executeUpdate();

            // Atrapamos las excepciones originadas por bloqueos en el archivo .db.
        } catch (SQLException e) {
            // Anotamos en la consola el fallo del cobro.
            System.out.println("Error crítico al cobrar créditos: " + e.getMessage());
            // Cerramos el bloque de gestión de excepciones.
        }
        // Cerramos el método de cobro virtual.
    }

// Cerramos la clase UsuarioDAO.
}