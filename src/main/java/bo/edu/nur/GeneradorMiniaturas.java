// Declaramos el paquete estructural de la universidad.
package bo.edu.nur;

// Importamos el lector de documentos PDF de Apache.
import org.apache.pdfbox.pdmodel.PDDocument;
// Importamos el motor de renderizado gráfico nativo.
import org.apache.pdfbox.rendering.PDFRenderer;
// Importamos la herramienta de escritura de imágenes hacia el disco duro.
import javax.imageio.ImageIO;
// Importamos la matriz de imagen para alojar los píxeles en la memoria RAM.
import java.awt.image.BufferedImage;
// Importamos el manejador de rutas del sistema operativo.
import java.io.File;

// Declaramos la clase utilitaria que procesará los binarios pesados.
public class GeneradorMiniaturas {

    // Método estático que exige la ruta del PDF de origen y la ruta del JPG de destino.
    public static void generar(String rutaPdfFisico, String rutaDestinoJpg) {

        // Creamos un puntero en memoria hacia el archivo PDF real.
        File archivoPdf = new File(rutaPdfFisico);

        // Blindaje matemático: Si el archivo no existe o no es PDF, abortamos instantáneamente para evitar un NullPointerException.
        if (!archivoPdf.exists() || !rutaPdfFisico.toLowerCase().endsWith(".pdf")) {
            return;
        }

        // Iniciamos el conducto blindado de memoria (try-with-resources) para leer el archivo pesado.
        try (PDDocument documento = PDDocument.load(archivoPdf)) {

            // Instanciamos el motor de renderizado sobre el documento recién cargado en RAM.
            PDFRenderer renderizador = new PDFRenderer(documento);

            // Renderizamos exclusivamente la primera página (índice 0) a 100 DPI para optimizar rendimiento de CPU.
            BufferedImage imagenPortada = renderizador.renderImageWithDPI(0, 100);

            // Escribimos la matriz de píxeles en el disco duro forzando el estándar de compresión JPEG.
            ImageIO.write(imagenPortada, "JPEG", new File(rutaDestinoJpg));

            // Atrapamos colapsos físicos, archivos corruptos o encriptados.
        } catch (Exception e) {
            // Imprimimos el diagnóstico en la terminal roja para auditoría.
            System.out.println("ERROR PDFBOX -> Falla al extraer miniatura: " + e.getMessage());
            // Cerramos el control de excepciones.
        }
        // Cerramos la función destructora/constructora.
    }
// Cerramos la arquitectura gráfica.
}