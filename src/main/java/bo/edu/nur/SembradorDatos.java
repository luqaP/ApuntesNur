// Declaramos el paquete estructural que organiza nuestras clases de persistencia.
package bo.edu.nur;

// Importamos la interfaz de Spring Boot para ejecutar lógica inmediatamente después de iniciar Tomcat.
import org.springframework.boot.CommandLineRunner;
// Importamos la anotación Component para registrar esta clase en el contenedor de Spring.
import org.springframework.stereotype.Component;
// Importamos el gestor de conexiones SQL nativo de Java.
import java.sql.Connection;
// Importamos la interfaz Statement para enviar instrucciones DDL crudas a SQLite.
import java.sql.Statement;

// Inyectamos el decorador para que Spring Boot instancie este componente de forma automática.
@Component
// Declaramos la clase que sembrará la infraestructura inicial de datos en el disco.
public class SembradorDatos implements CommandLineRunner {

    // Sobrescribimos el método obligatorio run que se ejecuta al encender la aplicación.
    @Override
    public void run(String... args) throws Exception {

        // Imprimimos un encabezado decorativo de auditoría en la terminal de IntelliJ.
        System.out.println("====== INICIANDO PROTOCOLO DE SIEMBRA DE DATOS ======");
        // Notificamos el inicio de la verificación física del archivo de base de datos.
        System.out.println("1. Verificando la infraestructura física de SQLite...");

        // Abrimos un bloque de recursos seguros para conectar y dialogar con SQLite de forma transitoria.
        try (Connection conexion = ConexionDB.conectar();
             // Instanciamos el objeto de transporte para nuestras sentencias estructuradas SQL.
             Statement declaracion = conexion.createStatement()) {

            // Ejecutamos el DDL maestro para asegurar la existencia de la tabla Usuario con el esquema correcto.
            declaracion.execute("CREATE TABLE IF NOT EXISTS Usuario (" +
                    // Definimos la llave primaria entera autoincremental delegada al motor.
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    // Definimos el alias único que identificará las credenciales del estudiante.
                    "nombre_usuario TEXT UNIQUE, " +
                    // Almacenamos el nombre civil completo del alumno registrado.
                    "nombre_completo TEXT, " +
                    // Guardamos la matrícula de seis dígitos previamente auditada en el validador.
                    "numero_credencial TEXT, " +
                    // Almacenamos la cadena cifrada con el algoritmo hash de jBCrypt.
                    "contrasena TEXT, " +
                    // Añadimos soporte para la carrera del estudiante pensando en futuras expansiones.
                    "carrera_estudiante TEXT, " +
                    // Añadimos el promedio flotante de reputación para el sistema de gamificación.
                    "calificacion_autor REAL DEFAULT 0.0, " +
                    // Establecemos la columna de créditos financieros virtuales con un valor base cero.
                    "creditos_virtuales INTEGER DEFAULT 0, " +
                    // Registramos la racha consecutiva de ingresos del estudiante al portal.
                    "racha_diaria INTEGER DEFAULT 0, " +
                    // Almacenamos la estampa de tiempo textual de su última actividad en el sistema.
                    "fecha_ultimo_acceso TEXT)");

            // Ejecutamos el DDL para dar vida a la tabla Apunte encargada del catálogo de documentos.
            declaracion.execute("CREATE TABLE IF NOT EXISTS Apunte (" +
                    // Llave primaria auto-incrementable propia de cada archivo subido.
                    "id_apunte INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    // Nombre comercial asignado al documento por el creador en el modal.
                    "titulo TEXT, " +
                    // Taxonomía o materia académica asociada al apunte para los filtros SPA.
                    "categoria_materia TEXT, " +
                    // Llave foránea entera que vincula directamente el archivo con su creador.
                    "id_autor INTEGER, " +
                    // Ubicación física segura combinada con el identificador UUID único.
                    "ruta_archivo_fisico TEXT, " +
                    // Contador de lecturas visuales acumuladas por el apunte en el servidor.
                    "conteo_vistas INTEGER DEFAULT 0, " +
                    // Contador de descargas comerciales exitosas procesadas en la red.
                    "conteo_descargas INTEGER DEFAULT 0, " +
                    // Contador de interacciones positivas otorgadas por los alumnos de la NUR.
                    "conteo_likes INTEGER DEFAULT 0, " +
                    // Contador de interacciones negativas registradas en las auditorías de calidad.
                    "conteo_dislikes INTEGER DEFAULT 0, " +
                    // Forzamos el vínculo relacional destruyendo los apuntes en cascada si el autor es eliminado.
                    "FOREIGN KEY(id_autor) REFERENCES Usuario(id_usuario) ON DELETE CASCADE)");

            // BUG FIX: Sincronizamos el nombre de la tabla puente a 'Adquisicion' para encajar con ApunteDAO.
            declaracion.execute("CREATE TABLE IF NOT EXISTS Adquisicion (" +
                    // Llave foránea del estudiante que desembolsa sus créditos virtuales.
                    "id_usuario INTEGER, " +
                    // Llave foránea del documento digital cuyo derecho perpetuo ha comprado.
                    "id_apunte INTEGER, " +
                    // Registramos de forma nativa el instante cronológico exacto del intercambio comercial.
                    "fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    // Establecemos una clave primaria compuesta para impedir duplicidad de recibos de compra en disco.
                    "PRIMARY KEY (id_usuario, id_apunte), " +
                    // Enlazamos jerárquicamente al comprador aplicando borrado masivo en cascada.
                    "FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE, " +
                    // Enlazamos jerárquicamente al apunte aplicando borrado masivo en cascada.
                    "FOREIGN KEY(id_apunte) REFERENCES Apunte(id_apunte) ON DELETE CASCADE)");

            // Imprimimos la validación en consola confirmando la alineación de la infraestructura.
            System.out.println("-> Infraestructura de Base de Datos V2 validada correctamente.");

            // Atrapamos cualquier anomalía de I/O o bloqueo relacional del archivo de SQLite.
        } catch (Exception e) {
            // Imprimimos el diagnóstico exacto del colapso en la terminal de IntelliJ.
            System.out.println("Error crítico al reconstruir las tablas: " + e.getMessage());
            // Cerramos el bloque protector.
        }

        // Notificamos el inicio de la evaluación y siembra de perfiles de prueba de alto rendimiento.
        System.out.println("2. Buscando perfiles de prueba...");

        // Interrogamos al DAO si el perfil del administrador principal ya reside en la base de datos.
        if (UsuarioDAO.obtenerIdPorAlias("lucca_dev") == -1) {

            // Encriptamos la clave base usando la matemática adaptativa de jBCrypt con factor de costo predeterminado.
            String claveLucca = org.mindrot.jbcrypt.BCrypt.hashpw("password123", org.mindrot.jbcrypt.BCrypt.gensalt());
            // Instanciamos el modelo del estudiante inyectando el hash criptográfico resultante.
            Usuario estudiante1 = new Usuario("lucca_dev", "Lucca Ortiz", "REG-2026", claveLucca);
            // Ordenamos al DAO que guarde físicamente la fila en la tabla de SQLite.
            UsuarioDAO.registrarUsuario(estudiante1);
            // Rescatamos el ID numérico real autogenerado por el motor relacional.
            int idLucca = UsuarioDAO.obtenerIdPorAlias("lucca_dev");
            // Le inyectamos un capital de 50 créditos virtuales para facilitar los testeos del mercado.
            UsuarioDAO.sumarCreditos(idLucca, 50);
            // Imprimimos el éxito del procedimiento de siembra autoral.
            System.out.println("-> Estudiante 'lucca_dev' inyectado automáticamente con 50 créditos (Hash asegurado).");
            // Cerramos la condición defensiva.
        }

        // Interrogamos si el segundo perfil de pruebas ya se encuentra registrado en el disco duro.
        if (UsuarioDAO.obtenerIdPorAlias("estudiante_pro") == -1) {

            // Ciframos de forma independiente la contraseña de este perfil simulado.
            String clavePro = org.mindrot.jbcrypt.BCrypt.hashpw("qwerty", org.mindrot.jbcrypt.BCrypt.gensalt());
            // Empaquetamos al segundo alumno con sus datos ficticios estructurados.
            Usuario estudiante2 = new Usuario("estudiante_pro", "Carlos Tester", "REG-9999", clavePro);
            // Registramos la información de forma permanente en la persistencia local.
            UsuarioDAO.registrarUsuario(estudiante2);
            // Notificamos la inyección exitosa de la contraparte relacional.
            System.out.println("-> Estudiante 'estudiante_pro' inyectado automáticamente (Hash asegurado).");
            // Cerramos la condición del segundo usuario.
        }

        // Marcamos formalmente la conclusión de la inicialización de datos en consola.
        System.out.println("====== SIEMBRA FINALIZADA: SERVIDOR LISTO ======");
        // Cerramos el método maestro de CommandLineRunner.
    }
// Cerramos la clase encargada de la siembra estructural.
}