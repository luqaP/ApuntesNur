// Declaramos la pertenencia innegociable al paquete raíz de tu proyecto universitario.
package bo.edu.nur;

// Importamos la interfaz nativa de Spring Boot que nos permite ejecutar código justo después de encender el servidor Tomcat.
import org.springframework.boot.CommandLineRunner;
// Importamos la anotación Component para que el radar de Spring detecte e instancie esta clase automáticamente.
import org.springframework.stereotype.Component;
// Importamos las herramientas de conexión JDBC de Java para manipular SQLite directamente.
import java.sql.Connection;
import java.sql.Statement;

// Inyectamos la directiva Component. Sin esto, la clase sería un bloque de texto inútil e ignorado por el compilador.
@Component
// Declaramos la clase pública implementando la interfaz estructurada de ejecución en línea de comandos.
public class SembradorDatos implements CommandLineRunner {

    // Sobrescribimos el método obligatorio 'run', el cual encapsula toda la lógica de inicialización autónoma.
    @Override
    public void run(String... args) throws Exception {

        // Imprimimos un aviso en la consola de IntelliJ para auditar el inicio del proceso de siembra.
        System.out.println("====== INICIANDO PROTOCOLO DE SIEMBRA DE DATOS ======");
        System.out.println("1. Verificando la infraestructura física de SQLite...");

        // FASE 1: RECONSTRUCCIÓN DE LA BASE DE DATOS (Solución al error SQLITE_ERROR)
        // Abrimos un bloque try-with-resources para conectar con la base de datos y asegurar el cierre automático del túnel.
        // FASE 1: RECONSTRUCCIÓN DE LA BASE DE DATOS
        // FASE 1: RECONSTRUCCIÓN MASIVA DE LA BASE DE DATOS (V2)
        try (Connection conexion = ConexionDB.conectar();
             Statement declaracion = conexion.createStatement()) {

            // 1. Tabla Usuario Expandida (Añadimos carrera y calificación)
            declaracion.execute("CREATE TABLE IF NOT EXISTS Usuario (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre_usuario TEXT UNIQUE, " +
                    "nombre_completo TEXT, " +
                    "numero_credencial TEXT, " +
                    "contrasena TEXT, " +
                    "carrera_estudiante TEXT, " + // NUEVA COLUMNA: Para el algoritmo de recomendación
                    "calificacion_autor REAL DEFAULT 0.0, " + // NUEVA COLUMNA: Promedio de reputación
                    "creditos_virtuales INTEGER DEFAULT 0, " +
                    "racha_diaria INTEGER DEFAULT 0, " +
                    "fecha_ultimo_acceso TEXT)");

            // 2. Tabla Apunte Expandida (Añadimos las métricas sociales)
            declaracion.execute("CREATE TABLE IF NOT EXISTS Apunte (" +
                    "id_apunte INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "titulo TEXT, " +
                    "categoria_materia TEXT, " +
                    "id_autor INTEGER, " +
                    "ruta_archivo_fisico TEXT, " +
                    "conteo_vistas INTEGER DEFAULT 0, " + // NUEVA COLUMNA
                    "conteo_descargas INTEGER DEFAULT 0, " + // NUEVA COLUMNA
                    "conteo_likes INTEGER DEFAULT 0, " + // NUEVA COLUMNA
                    "conteo_dislikes INTEGER DEFAULT 0, " + // NUEVA COLUMNA
                    "FOREIGN KEY(id_autor) REFERENCES Usuario(id_usuario))");

            // 3. NUEVA TABLA: Historial de Visitas (Para las recomendaciones y recientes)
            declaracion.execute("CREATE TABLE IF NOT EXISTS Historial_Visitas (" +
                    "id_visita INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_usuario INTEGER, " +
                    "id_apunte INTEGER, " +
                    "fecha_visita DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario), " +
                    "FOREIGN KEY(id_apunte) REFERENCES Apunte(id_apunte))");

            // 4. NUEVA TABLA: Biblioteca de Adquiridos (Para que no paguen dos veces)
            declaracion.execute("CREATE TABLE IF NOT EXISTS Biblioteca_Adquiridos (" +
                    "id_adquisicion INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "id_usuario INTEGER, " +
                    "id_apunte INTEGER, " +
                    "fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(id_usuario) REFERENCES Usuario(id_usuario), " +
                    "FOREIGN KEY(id_apunte) REFERENCES Apunte(id_apunte))");

            System.out.println("-> Infraestructura de Base de Datos V2 validada correctamente.");

        } catch (Exception e) {
            System.out.println("Error crítico al reconstruir las tablas: " + e.getMessage());
        }
        // FASE 2: INYECCIÓN DE USUARIOS Y CRIPTOGRAFÍA
        System.out.println("2. Buscando perfiles de prueba...");

        // Utilizamos el DAO para preguntar si tu perfil de desarrollador ya existe en la tabla recién creada.
        if (UsuarioDAO.obtenerIdPorAlias("lucca_dev") == -1) {

            // CRIPTOGRAFÍA: Encriptamos la contraseña de prueba usando el algoritmo adaptativo jBCrypt con su respectivo 'salt'.
            String claveLucca = org.mindrot.jbcrypt.BCrypt.hashpw("password123", org.mindrot.jbcrypt.BCrypt.gensalt());

            // Instanciamos el modelo matemático del estudiante inyectando el hash seguro en lugar de texto plano.
            Usuario estudiante1 = new Usuario("lucca_dev", "Lucca Ortiz", "REG-2026", claveLucca);

            // Ordenamos al DAO que escriba esta fila en el archivo .db.
            UsuarioDAO.registrarUsuario(estudiante1);

            // Recuperamos el ID numérico autogenerado por SQLite para poder manipular la economía.
            int idLucca = UsuarioDAO.obtenerIdPorAlias("lucca_dev");
            // Le otorgamos un capital semilla de 50 créditos para facilitar las pruebas de descargas.
            UsuarioDAO.sumarCreditos(idLucca, 50);

            // Avisamos en la consola que el primer perfil fue creado con éxito.
            System.out.println("-> Estudiante 'lucca_dev' inyectado automáticamente con 50 créditos (Hash asegurado).");
            // Cerramos la condición del primer estudiante.
        }

        // Repetimos la lógica de validación para un segundo usuario de prueba.
        if (UsuarioDAO.obtenerIdPorAlias("estudiante_pro") == -1) {

            // Hasheamos una contraseña distinta para este segundo perfil.
            String clavePro = org.mindrot.jbcrypt.BCrypt.hashpw("qwerty", org.mindrot.jbcrypt.BCrypt.gensalt());

            // Empaquetamos al segundo estudiante.
            Usuario estudiante2 = new Usuario("estudiante_pro", "Carlos Tester", "REG-9999", clavePro);

            // Guardamos el registro en la persistencia local.
            UsuarioDAO.registrarUsuario(estudiante2);

            // Avisamos de la creación exitosa.
            System.out.println("-> Estudiante 'estudiante_pro' inyectado automáticamente (Hash asegurado).");
            // Cerramos la condición del segundo estudiante.
        }

        // Finalizamos el reporte de la consola indicando que el servidor web puede comenzar a recibir tráfico humano.
        System.out.println("====== SIEMBRA FINALIZADA: SERVIDOR LISTO ======");

        // Cerramos el método run() de ejecución autónoma.
    }

// Cerramos la arquitectura general de la clase SembradorDatos.
}