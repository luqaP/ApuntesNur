// Declaramos el paquete central.
package bo.edu.nur;

// Importamos Configuration para que Spring Boot ejecute esto antes de encender la red.
import org.springframework.context.annotation.Configuration;
// Importamos ResourceHandlerRegistry para manipular las rutas de acceso público.
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// Importamos WebMvcConfigurer para alterar el comportamiento del motor Tomcat.
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Inyectamos el decorador arquitectónico maestro.
@Configuration
// Declaramos la clase que abrirá los puertos de lectura hacia el disco duro.
public class ConfiguracionRecursos implements WebMvcConfigurer {

    // Sobrescribimos el método nativo de asignación de recursos.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registro) {
        // Ordenamos al servidor que cualquier petición HTTP que comience con /previews/ sea redirigida a tu carpeta física.
        registro.addResourceHandler("/previews/**")
                // Mapeamos la URL virtual hacia la ruta física estricta en el sistema de archivos (file:).
                .addResourceLocations("file:repositorio_nur/previews/");
        // Cerramos la asignación de puertos.
    }
// Cerramos la clase de configuración perimetral.
}