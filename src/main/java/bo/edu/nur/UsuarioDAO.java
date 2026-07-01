// Declaramos la pertenencia estricta de la clase al paquete de nuestro proyecto universitario.
package bo.edu.nur;

// Importamos la interfaz nativa para manejar la conexión abierta con SQLite.
import java.sql.Connection;
// Importamos PreparedStatement para precompilar las consultas y blindarnos contra inyecciones SQL.
import java.sql.PreparedStatement;
// Importamos ResultSet para navegar por las tablas virtuales que devuelve la base de datos.
import java.sql.ResultSet;
// Importamos SQLException para atrapar los fallos de lectura y escritura en el disco duro.
import java.sql.SQLException;

// Declaramos la clase pública que actuará como el único intermediario entre Java y la tabla Usuario de SQLite.
public class UsuarioDAO {

    // ========================================================================================
    // MÉTODO 1: REGISTRO DE NUEVOS ESTUDIANTES
    // ========================================================================================

    // Método estático para insertar un estudiante nuevo en la base de datos.
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        // Redactamos la instrucción SQL inyectando datos en todas las columnas definidas.
        String sql = "INSERT INTO Usuario (nombre_usuario, nombre_completo, numero_credencial, contrasena, creditos_virtuales, racha_diaria, fecha_ultimo_acceso) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Abrimos el túnel blindado hacia el archivo de SQLite.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos la sentencia en el motor.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el alias.
            declaracion.setString(1, nuevoUsuario.getAlias());
            // Inyectamos el nombre real.
            declaracion.setString(2, nuevoUsuario.obtenerNombreCompleto());
            // Inyectamos la matrícula institucional.
            declaracion.setString(3, nuevoUsuario.obtenerNumeroCredencial());
            // Inyectamos el hash criptográfico Bcrypt.
            declaracion.setString(4, nuevoUsuario.obtenerContrasena());
            // Inyectamos el capital semilla.
            declaracion.setInt(5, nuevoUsuario.getCreditos());
            // Inyectamos el contador de ingresos.
            declaracion.setInt(6, nuevoUsuario.obtenerRachaDiaria());
            // Inyectamos la estampa temporal inicial.
            declaracion.setString(7, nuevoUsuario.obtenerFechaUltimoAccesoComoTexto());

            // Ejecutamos la orden de inserción física.
            declaracion.executeUpdate();
            // Avisamos al programador del éxito de la transacción.
            System.out.println("TELEMETRÍA DAO -> Estudiante registrado exitosamente.");
            // Devolvemos verdadero a la capa de servicio.
            return true;

            // Atrapamos colisiones de I/O (ejemplo: alias ya registrado por otro usuario).
        } catch (SQLException e) {
            // Imprimimos la traza del error crítico.
            System.out.println("ERROR DAO -> Fallo crítico al registrar: " + e.getMessage());
            // Devolvemos falso para notificar el fracaso.
            return false;
            // Cerramos el bloque de contingencia.
        }
        // Cerramos el método de inserción.
    }

    // ========================================================================================
    // MÉTODO 2: VALIDACIÓN DE ACCESO (LOGIN CRIPTOGRÁFICO)
    // ========================================================================================

    // Método estático que evalúa si un usuario tiene permiso de entrar al sistema.
    public static boolean validarCredenciales(String alias, String claveCruda) {
        // Buscamos exclusivamente el hash protegido basándonos en el alias ingresado.
        String sql = "SELECT contrasena FROM Usuario WHERE nombre_usuario = ?";

        // Abrimos la sesión con la base de datos.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos la búsqueda.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el alias tecleado en el navegador.
            declaracion.setString(1, alias);

            // Ejecutamos la lectura del disco.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Si la fila del estudiante existe en SQLite.
                if (resultados.next()) {
                    // Extraemos la cadena de 60 caracteres que conforma el hash guardado.
                    String hashGuardado = resultados.getString("contrasena");
                    // Ejecutamos la matemática inversa de jBCrypt para verificar la coincidencia.
                    return org.mindrot.jbcrypt.BCrypt.checkpw(claveCruda, hashGuardado);
                    // Cerramos el bloque de evaluación de fila.
                }
                // Anulamos la memoria del lector de resultados.
            }
            // Atrapamos fallos estructurales de SQL.
        } catch (SQLException e) {
            // Informamos de la vulnerabilidad en consola.
            System.out.println("ERROR DAO -> Falla en la validación criptográfica: " + e.getMessage());
            // Cerramos el bloque protector.
        }
        // Retornamos falso automáticamente si el proceso falla o si las claves no coinciden.
        return false;
        // Cerramos el validador de seguridad.
    }

    // ========================================================================================
    // MÉTODO 3: TRADUCTOR DE IDENTIDAD (ALIAS -> ID)
    // ========================================================================================

    // Método estático para descubrir la llave primaria de un usuario conociendo solo su nombre.
    public static int obtenerIdPorAlias(String alias) {
        // Instruimos a SQLite a retornar únicamente el número de la columna id_usuario.
        String sql = "SELECT id_usuario FROM Usuario WHERE nombre_usuario = ?";

        // Conectamos a la base de forma efímera.
        try (Connection conexion = ConexionDB.conectar();
             // Aseguramos la instrucción.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el alias a traducir.
            declaracion.setString(1, alias);

            // Llevamos a cabo la extracción.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Si detectamos al usuario.
                if (resultados.next()) {
                    // Entregamos la llave primaria en formato entero primitivo.
                    return resultados.getInt("id_usuario");
                    // Cerramos el condicional de impacto.
                }
                // Liberamos recursos de lectura.
            }
            // Capturamos violaciones de lectura.
        } catch (SQLException e) {
            // Documentamos el fallo.
            System.out.println("ERROR DAO -> Falla al traducir ID por Alias: " + e.getMessage());
            // Cerramos la captura.
        }
        // Si el usuario no existe, retornamos un número negativo y absurdo para alertar al sistema.
        return -1;
        // Cerramos el método traductor.
    }

    // ========================================================================================
    // MÉTODO 4: OBTENER PERFIL COMPLETO (EL MÉTODO CORREGIDO)
    // ========================================================================================

    // Método estático que mapea absolutamente todos los datos de un estudiante de SQLite a Java.
    public static Usuario obtenerPorAlias(String aliasBusqueda) {
        // Solicitamos toda la fila que pertenezca al nombre de usuario indicado.
        String sql = "SELECT * FROM Usuario WHERE nombre_usuario = ?";

        // Iniciamos la transacción de lectura.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos para neutralizar ataques.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Asignamos el alias provisto por el controlador.
            declaracion.setString(1, aliasBusqueda);

            // Ejecutamos el barrido del disco.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Si la consulta arroja una fila válida.
                if (resultados.next()) {
                    // Instanciamos el objeto orquestador con el constructor de 8 parámetros.
                    return new Usuario(
                            // Mapeo 1: Identificador absoluto.
                            resultados.getInt("id_usuario"),
                            // Mapeo 2: Alias de la cuenta.
                            resultados.getString("nombre_usuario"),
                            // Mapeo 3: Nombre legal.
                            resultados.getString("nombre_completo"),
                            // Mapeo 4: Matrícula universitaria.
                            resultados.getString("numero_credencial"),
                            // Mapeo 5: Clave cifrada.
                            resultados.getString("contrasena"),
                            // Mapeo 6: Saldo de créditos.
                            resultados.getInt("creditos_virtuales"),
                            // Mapeo 7: Racha consecutiva.
                            resultados.getInt("racha_diaria"),
                            // Mapeo 8: Fecha de última visita.
                            resultados.getString("fecha_ultimo_acceso")
                    );
                    // Cerramos el proceso de mapeo objeto-relacional.
                }
                // Cerramos el cursor temporal.
            }
            // Atrapamos caídas de hardware o bloqueos lógicos.
        } catch (SQLException e) {
            // Imprimimos el diagnóstico de la lectura completa.
            System.out.println("ERROR DAO -> Fallo al extraer el perfil absoluto: " + e.getMessage());
            // Cerramos el control de excepciones.
        }
        // Retornamos ausencia de memoria si el estudiante no reside en la base de datos.
        return null;
        // Cerramos el buscador exhaustivo de perfiles.
    }

    // ========================================================================================
    // MÉTODO 5: INYECCIÓN DE RECOMPENSAS (ECONOMÍA)
    // ========================================================================================

    // Método estático que aplica un incremento matemático a la billetera.
    public static void sumarCreditos(int idUsuario, int cantidad) {
        // Redactamos una instrucción UPDATE para que el motor de SQL haga la matemática nativamente.
        String sql = "UPDATE Usuario SET creditos_virtuales = creditos_virtuales + ? WHERE id_usuario = ?";

        // Abrimos la sesión financiera.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos el inyector.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el bono a acreditar.
            declaracion.setInt(1, cantidad);
            // Inyectamos al beneficiario.
            declaracion.setInt(2, idUsuario);
            // Ejecutamos la orden de alteración física.
            declaracion.executeUpdate();

            // Atrapamos violaciones de concurrencia.
        } catch (SQLException e) {
            // Registramos el error de inyección de capital.
            System.out.println("ERROR DAO -> Imposible inyectar créditos: " + e.getMessage());
            // Cerramos la captura.
        }
        // Cerramos el dispensador de recompensas.
    }

    // ========================================================================================
    // MÉTODO 6: AUDITORÍA DE FONDOS (ECONOMÍA)
    // ========================================================================================

    // Método estático que lee el capital del estudiante.
    public static int consultarSaldo(int idUsuario) {
        // Seleccionamos estrictamente el número que representa el dinero virtual.
        String sql = "SELECT creditos_virtuales FROM Usuario WHERE id_usuario = ?";

        // Iniciamos la conexión con SQLite.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la petición.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Asignamos la llave primaria del estudiante.
            declaracion.setInt(1, idUsuario);

            // Efectuamos la lectura.
            try (ResultSet resultados = declaracion.executeQuery()) {
                // Evaluamos si el estudiante existe.
                if (resultados.next()) {
                    // Devolvemos el saldo actual.
                    return resultados.getInt("creditos_virtuales");
                    // Cerramos condición.
                }
                // Vaciamos lector de memoria.
            }
            // Atrapamos bloqueos físicos.
        } catch (SQLException e) {
            // Notificamos problema de acceso al saldo.
            System.out.println("ERROR DAO -> Fallo al auditar los fondos: " + e.getMessage());
            // Cerramos el control.
        }
        // Retornamos saldo cero como mecanismo de seguridad ante la duda.
        return 0;
        // Cerramos el auditor financiero.
    }

    // ========================================================================================
    // MÉTODO 7: COBRO POR DESCARGAS (ECONOMÍA)
    // ========================================================================================

    // Método estático para debitar fondos tras una transacción exitosa en la tienda.
    public static void cobrarCreditos(int idUsuario, int cantidadACobrar) {
        // Alteramos la columna restando el costo estipulado.
        String sql = "UPDATE Usuario SET creditos_virtuales = creditos_virtuales - ? WHERE id_usuario = ?";

        // Abrimos la sesión transaccional.
        try (Connection conexion = ConexionDB.conectar();
             // Configuramos el inyector.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Asignamos el descuento numérico.
            declaracion.setInt(1, cantidadACobrar);
            // Asignamos la víctima del débito.
            declaracion.setInt(2, idUsuario);
            // Ejecutamos la orden de extracción financiera.
            declaracion.executeUpdate();

            // Atrapamos fallas de bloqueo I/O.
        } catch (SQLException e) {
            // Señalamos el fallo de deducción en la terminal.
            System.out.println("ERROR DAO -> Falla al debitar los créditos: " + e.getMessage());
            // Cerramos captura de errores.
        }
        // Cerramos la función de cobro.
    }
    // ========================================================================================
    // MÉTODOS DE DESTRUCCIÓN Y LIMPIEZA (ZONA DE PELIGRO)
    // ========================================================================================

    // Declaramos el método público y estático para aniquilar a un usuario específico mediante su llave primaria.
    public static boolean eliminarUsuarioPorId(int idUsuario) {

        // Redactamos la instrucción SQL DML ordenando el borrado de la fila que coincida estrictamente con el ID.
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        // Abrimos el túnel de conexión transaccional con la base de datos local SQLite.
        try (Connection conexion = ConexionDB.conectar();
             // Precompilamos la sentencia destructiva para bloquear cualquier intento de inyección de código.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Inyectamos el número de identificación del estudiante en el comodín de la sentencia.
            declaracion.setInt(1, idUsuario);

            // Ejecutamos la orden de aniquilación en el disco duro y capturamos la cantidad de filas afectadas.
            int filasBorradas = declaracion.executeUpdate();

            // Imprimimos en la consola de diagnóstico la confirmación de la limpieza por motivos de auditoría.
            System.out.println("AUDITORÍA DAO -> Se ordenó la eliminación del usuario ID: " + idUsuario);

            // Retornamos verdadero si el motor relacional confirmó la destrucción de al menos un registro (filasBorradas > 0).
            return filasBorradas > 0;

            // Capturamos cualquier bloqueo físico o error estructural emitido por el archivo .db.
        } catch (SQLException e) {

            // Imprimimos la traza del error crítico en la terminal para evaluar por qué falló la eliminación.
            System.out.println("ERROR DAO -> Fallo masivo al intentar eliminar el usuario: " + e.getMessage());

            // Retornamos falso indicando al controlador que la operación de limpieza fracasó rotundamente.
            return false;

            // Cerramos el bloque de contingencia y protección.
        }

        // Cerramos la arquitectura del método destructor individual.
    }

    // Declaramos un método extremo para limpiar absolutamente todos los usuarios (Útil solo en fase de pruebas).
    public static boolean limpiarTodosLosUsuarios() {

        // Redactamos la instrucción de borrado masivo sin condicionales (Peligro: Borrará la tabla entera).
        String sql = "DELETE FROM Usuario";

        // Establecemos el conducto temporal hacia el archivo de SQLite.
        try (Connection conexion = ConexionDB.conectar();
             // Preparamos la orden de ejecución masiva.
             PreparedStatement declaracion = conexion.prepareStatement(sql)) {

            // Ejecutamos la orden sin inyectar parámetros, barriendo con todos los registros.
            declaracion.executeUpdate();

            // Emitimos una alerta roja en la consola notificando la purga total del sistema.
            System.out.println("ALERTA DAO -> La tabla Usuario ha sido purgada por completo.");

            // Retornamos éxito absoluto en la transacción.
            return true;

            // Atrapamos colisiones de disco.
        } catch (SQLException e) {

            // Documentamos el colapso del intento de purga.
            System.out.println("ERROR DAO -> Falla al intentar vaciar la tabla de usuarios: " + e.getMessage());

            // Retornamos fracaso transaccional.
            return false;

            // Cerramos la captura de errores.
        }

        // Cerramos el método de purga global.
    }

// Cerramos de manera concluyente la arquitectura de la clase UsuarioDAO.
}