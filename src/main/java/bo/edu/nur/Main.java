// Declaramos que esta clase pertenece al paquete principal de la universidad para evitar el escaneo global.
package bo.edu.nur;

// Importamos la clase SpringApplication para levantar el servidor web en memoria.
import org.springframework.boot.SpringApplication;
// Importamos la anotación SpringBootApplication para orquestar los componentes del proyecto.
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Importamos la clase DataSourceAutoConfiguration para poder referenciarla y apagarla.
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// Inyectamos la anotación maestra, pero le agregamos el parámetro "exclude" para apagar la auto-conexión a la base de datos.
// Esto obliga a Spring Boot a respetar nuestra clase ConexionDB manual.
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Main {

    // Declaramos el método principal de ejecución.
    public static void main(String[] args) {
        // Ejecutamos el servidor web aislado de los conflictos de base de datos.
        SpringApplication.run(Main.class, args);
        // Cerramos el bloque de ejecución del método main.
    }

// Cerramos la declaración general de tu clase Main.
}