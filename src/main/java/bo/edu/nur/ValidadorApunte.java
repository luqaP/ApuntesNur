// Declaramos el paquete estructural del proyecto.
package bo.edu.nur;

// Importamos la interfaz MultipartFile para extraer metadatos del binario antes de escribirlo.
import org.springframework.web.multipart.MultipartFile;

// Declaramos la clase que actúa como portero (Gatekeeper) de la integridad del repositorio.
public class ValidadorApunte {

    // Definimos el peso mínimo (ej: 50 KB). Un archivo menor es, casi con certeza, basura o un error de subida.
    private static final long TAMANO_MINIMO = 50 * 1024;
    // Definimos el peso máximo (ej: 15 MB). Evitamos que saturen tu HP Celeron con archivos gigantes.
    private static final long TAMANO_MAXIMO = 15 * 1024 * 1024;

    // Método maestro que evalúa si el archivo tiene calidad académica mínima para entrar al sistema.
    public static boolean esArchivoValido(MultipartFile archivo) {

        // 1. VALIDACIÓN DE PRESENCIA: Si el archivo es nulo o está vacío, abortamos inmediatamente.
        if (archivo == null || archivo.isEmpty()) {
            return false;
        }

        // 2. VALIDACIÓN DE TAMAÑO: Comprobamos si el peso está en el rango académico aceptable.
        long tamano = archivo.getSize();
        if (tamano < TAMANO_MINIMO || tamano > TAMANO_MAXIMO) {
            // Registramos el intento fallido en la terminal de diagnóstico.
            System.out.println("GATEKEEPER -> Archivo rechazado por tamaño: " + tamano + " bytes.");
            return false;
        }

        // 3. VALIDACIÓN DE EXTENSIÓN: Comprobamos si el formato es compatible con la academia.
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null ||
                (!nombreOriginal.toLowerCase().endsWith(".pdf") && !nombreOriginal.toLowerCase().endsWith(".md"))) {
            System.out.println("GATEKEEPER -> Archivo rechazado por formato inválido: " + nombreOriginal);
            return false;
        }

        // 4. VALIDACIÓN DE TIPO MIME: Verificamos el tipo de contenido real que detecta Spring Boot.
        String contentType = archivo.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") && !contentType.contains("text/markdown"))) {
            return false;
        }

        // Si sobrevive a todos los filtros, el archivo es apto para la base de datos.
        return true;
    }

// Cerramos la arquitectura inquebrantable de la clase ValidadorApunte.
}