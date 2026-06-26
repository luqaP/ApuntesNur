// Declaramos el paquete estructural que contiene la configuración del servidor.
package bo.edu.nur;

// Importamos la interfaz nativa para manejar el conducto hacia el archivo SQLite.
import java.sql.Connection;
// Importamos la interfaz para enviar sentencias SQL crudas al motor de base de datos.
import java.sql.Statement;
// Importamos la clase de excepción para capturar colapsos físicos del disco.
import java.sql.SQLException;

// Declaramos la clase constructora que actuará como el albañil de nuestra base de datos.
public class InstaladorDB {

    // Declaramos el método público y estático que debe ser invocado al arrancar el servidor (en el Main).
    public static void inicializarBaseDeDatos() {

        // Iniciamos un bloque try-with-resources para garantizar el cierre hermético de la conexión y evitar fugas de RAM en tu HP Celeron.
        try (Connection conexion = ConexionDB.conectar();
             // Creamos el objeto instanciado que transportará nuestras órdenes SQL. AQUÍ NACE LA VARIABLE 'declaracion'.
             Statement declaracion = conexion.createStatement()) {

            // ========================================================================================
            // FASE 1: CREACIÓN DE LA TABLA PADRE 'USUARIO'
            // ========================================================================================

            // Redactamos el esquema DDL para la tabla que almacenará a los estudiantes de la universidad.
            String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuario ("
                    // Llave primaria auto-incremental delegada al motor SQLite.
                    + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                    // Alias único para el acceso a la plataforma.
                    + "alias TEXT UNIQUE NOT NULL, "
                    // Nombre real y legal del estudiante.
                    + "nombre TEXT NOT NULL, "
                    // Credencial de 6 dígitos validada previamente en el controlador.
                    + "credencial TEXT NOT NULL, "
                    // Hash criptográfico Bcrypt de la contraseña.
                    + "clave TEXT NOT NULL, "
                    // Billetera virtual inicializada por defecto.
                    + "creditos INTEGER DEFAULT 0, "
                    // Racha de inicio de sesión diario.
                    + "racha_dias INTEGER DEFAULT 1, "
                    // Estampa temporal de la creación de la cuenta.
                    + "ultima_fecha_login TEXT NOT NULL"
                    // Cerramos la definición de la tabla Usuario.
                    + ");";

            // Ejecutamos la orden de construcción en el disco duro.
            declaracion.execute(sqlUsuario);

            // ========================================================================================
            // FASE 2: CREACIÓN DE LA TABLA 'APUNTE'
            // ========================================================================================

            // Redactamos el esquema DDL para el catálogo de documentos.
            String sqlApunte = "CREATE TABLE IF NOT EXISTS Apunte ("
                    // Llave primaria auto-incremental para el documento.
                    + "id_apunte INTEGER PRIMARY KEY AUTOINCREMENT, "
                    // Nombre comercial del resumen o documento.
                    + "titulo TEXT NOT NULL, "
                    // Clasificación taxonómica (Álgebra, Programación, etc.).
                    + "categoria_materia TEXT NOT NULL, "
                    // Llave foránea que enlaza el documento con su creador.
                    + "id_autor INTEGER NOT NULL, "
                    // Ruta local encriptada hacia el binario físico (.pdf o .md).
                    + "ruta_archivo_fisico TEXT NOT NULL, "
                    // Imponemos la restricción relacional: el autor debe existir en la tabla Usuario.
                    + "FOREIGN KEY (id_autor) REFERENCES Usuario(id_usuario) ON DELETE CASCADE"
                    // Cerramos la definición de la tabla Apunte.
                    + ");";

            // Ejecutamos la orden de construcción en el disco duro.
            declaracion.execute(sqlApunte);

            // ========================================================================================
            // FASE 3: CREACIÓN DE LA TABLA PUENTE 'ADQUISICION' (LIBRO MAYOR N:M)
            // ========================================================================================

            // Redactamos el esquema DDL para registrar los derechos de propiedad intelectual pagados.
            String sqlAdquisicion = "CREATE TABLE IF NOT EXISTS Adquisicion ("
                    // Columna para la llave foránea del estudiante que compra.
                    + "id_usuario INTEGER NOT NULL, "
                    // Columna para la llave foránea del documento comprado.
                    + "id_apunte INTEGER NOT NULL, "
                    // Auditoría temporal de la transacción automática.
                    + "fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP, "
                    // Llave primaria compuesta: un estudiante no puede comprar el mismo apunte dos veces.
                    + "PRIMARY KEY (id_usuario, id_apunte), "
                    // Restricción relacional estricta hacia el comprador.
                    + "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE, "
                    // Restricción relacional estricta hacia el producto intelectual.
                    + "FOREIGN KEY (id_apunte) REFERENCES Apunte(id_apunte) ON DELETE CASCADE"
                    // Cerramos la definición de la tabla puente.
                    + ");";

            // Ejecutamos la orden definitiva de construcción estructural.
            declaracion.execute(sqlAdquisicion);

            // Imprimimos un registro de éxito rotundo en la terminal de diagnóstico de IntelliJ.
            System.out.println("INSTALADOR -> Estructura relacional completa (Usuario, Apunte, Adquisicion) validada con éxito en SQLite.");

            // Atrapamos cualquier anomalía de I/O o bloqueos del archivo .db a nivel de sistema operativo.
        } catch (SQLException e) {
            // Documentamos la falla crítica para evitar que pase desapercibida.
            System.out.println("ERROR CRÍTICO AL INICIALIZAR LA BASE DE DATOS -> " + e.getMessage());
            // Cerramos el bloque de contingencia de fallos.
        }
        // Cerramos el método arquitectónico maestro.
    }
// Cerramos la clase InstaladorDB.
}