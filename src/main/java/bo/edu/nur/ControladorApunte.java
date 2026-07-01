// Declaramos el paquete estructural al que pertenece el controlador dentro del proyecto universitario.
package bo.edu.nur;

// Importamos la clase nativa File para poder crear carpetas y manipular el disco duro.
import java.io.File;
// Importamos la excepción IOException para capturar errores de escritura física (ej. disco lleno).
import java.io.IOException;
// Importamos Files para usar utilidades avanzadas de copiado de archivos.
import java.nio.file.Files;
// Importamos Path para estructurar las rutas de carpetas al estilo de Windows.
import java.nio.file.Path;
// Importamos Paths para convertir cadenas de texto en objetos de ruta navegables.
import java.nio.file.Paths;
// Importamos StandardCopyOption para decirle al sistema operativo que sobrescriba archivos si hay duplicados.
import java.nio.file.StandardCopyOption;
// Importamos UUID para generar códigos alfanuméricos imposibles de repetir matemáticamente.
import java.util.UUID;

// Importamos RestController para que Spring Boot habilite este archivo como un receptor de datos de internet.
import org.springframework.web.bind.annotation.RestController;
// Importamos PostMapping para escuchar envíos de formularios pesados (archivos y textos largos).
import org.springframework.web.bind.annotation.PostMapping;
// Importamos RequestParam para extraer variables específicas que viajan dentro del formulario HTML.
import org.springframework.web.bind.annotation.RequestParam;
// Importamos MultipartFile para poder recibir y manipular el flujo binario del archivo subido.
import org.springframework.web.multipart.MultipartFile;
// Importamos HttpSession para extraer la identidad del estudiante desde la memoria RAM del servidor.
import jakarta.servlet.http.HttpSession;

// Inyectamos el decorador arquitectónico para que Tomcat despierte este módulo al iniciar.
@RestController
// Declaramos la clase pública que gestionará la recepción de la propiedad intelectual.
public class ControladorApunte {

    // Enrutamos el método para que intercepte cualquier orden POST dirigida a "/subir-apunte".
    @PostMapping("/subir-apunte")
    // Declaramos el método maestro exigiendo el título, la categoría, el binario del archivo y la sesión actual.
    public String procesarSubida(@RequestParam("tituloApunte") String titulo,
                                 @RequestParam("categoriaMateria") String categoria,
                                 @RequestParam("archivoFisico") MultipartFile archivo,
                                 HttpSession sesion) {

        // ====================================================================================
        // FASE A: VERIFICACIÓN DE IDENTIDAD Y SEGURIDAD ANTIFRAUDE
        // ====================================================================================

        // Extraemos el alias del estudiante desde la memoria temporal de la sesión HTTP.
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");

        // Bloqueamos la transacción si un anónimo intentó forzar la ruta mediante un cliente API.
        if (aliasUsuario == null) {
            // Retornamos un mensaje de error y detenemos el procesamiento del archivo.
            return "<h2>Acceso Denegado</h2><p>Debes iniciar sesión en la NUR para subir apuntes.</p>";
            // Cerramos el control perimetral.
        }

        // Analizamos el archivo subido para verificar que no esté vacío y pese al menos 50 bytes.
        if (archivo.isEmpty() || archivo.getSize() < 50) {
            // Abortamos para evitar que estudiantes suban archivos basura solo para farmear créditos.
            return "<h2>Fraude Detectado</h2><p>El archivo es inválido, corrupto o está vacío.</p>";
            // Cerramos la validación de integridad binaria.
        }

        // Extraemos el nombre original con el que el estudiante tenía guardado el archivo en su computadora.
        String nombreOriginal = archivo.getOriginalFilename();

        // Evaluamos si el nombre existe y forzamos estrictamente que la extensión sea PDF.
        if (nombreOriginal == null || !nombreOriginal.toLowerCase().endsWith(".pdf")) {
            // Exigimos PDF porque nuestro GeneradorMiniaturas de Apache PDFBox no sabe leer otro formato.
            return "<h2>Formato Inválido</h2><p>Para generar previsualizaciones, solo se permiten archivos PDF.</p>";
            // Cerramos la validación de extensión.
        }

        // ====================================================================================
        // FASE B: INGENIERÍA DE NOMENCLATURA (PREVENCIÓN DE COLISIONES)
        // ====================================================================================

        // Invocamos al motor aleatorio de Java para crear un hash UUID (ej. "550e8400-e29b...").
        String codigoUnico = UUID.randomUUID().toString();

        // Fusionamos el UUID con el nombre original para asegurar que jamás se sobrescriba el archivo de otro alumno.
        String nombreSeguro = codigoUnico + "_" + nombreOriginal;

        // Abrimos el conducto de I/O (Input/Output) ya que manipular el disco duro siempre es riesgoso.
        try {

            // ====================================================================================
            // FASE C: PERSISTENCIA FÍSICA Y RENDERIZADO GRÁFICO (APACHE PDFBOX)
            // ====================================================================================

            // Definimos en texto el nombre del directorio bóveda de tu disco duro.
            String nombreCarpeta = "repositorio_nur/";
            // Instanciamos el objeto File apuntando a esa ruta lógica.
            File directorioDestino = new File(nombreCarpeta);

            // Si la carpeta maestra no existe en el sistema operativo, le ordenamos que la construya.
            if (!directorioDestino.exists()) {
                // Creamos el árbol de directorios.
                directorioDestino.mkdirs();
                // Cerramos la validación de la bóveda.
            }

            // Instanciamos un objeto File apuntando a la subcarpeta de previsualizaciones fotográficas.
            File directorioPreviews = new File(nombreCarpeta + "previews/");

            // Si la subcarpeta de imágenes no existe, le ordenamos a Windows que la construya.
            if (!directorioPreviews.exists()) {
                // Creamos la subcarpeta gráfica.
                directorioPreviews.mkdirs();
                // Cerramos la validación de la subcarpeta.
            }

            // Construimos la ruta matemática exacta donde el archivo binario PDF va a aterrizar.
            Path rutaFinal = Paths.get(nombreCarpeta + nombreSeguro);

            // Ordenamos al canal de red que descargue el flujo de bytes hacia la ruta final del disco duro.
            Files.copy(archivo.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            // Redactamos la ruta exacta donde queremos que la clase GeneradorMiniaturas guarde la fotografía JPG.
            // OJO: Conservamos el nombre original con la extensión .pdf y le concatenamos .jpg (Ej: archivo.pdf.jpg).
            String rutaPreview = nombreCarpeta + "previews/" + nombreSeguro + ".jpg";

            // INVOCACIÓN CRÍTICA: Llamamos a tu clase utilitaria para que abra el PDF y dispare la foto de la portada.
            GeneradorMiniaturas.generar(rutaFinal.toString(), rutaPreview);

            // ====================================================================================
            // FASE D: PERSISTENCIA RELACIONAL (SQLITE) Y GAMIFICACIÓN (ECONOMÍA)
            // ====================================================================================

            // Le pedimos al DAO de usuarios que traduzca el nombre del estudiante a su número de identidad en base de datos.
            int idAutor = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

            // Confirmamos que el usuario realmente exista en el registro y no haya sido borrado recientemente.
            if (idAutor != -1) {
                // Armamos el objeto de Java que representa matemáticamente la fila que vamos a guardar en SQLite.
                Apunte documento = new Apunte(titulo, categoria, idAutor, nombreSeguro);

                // Disparamos la instrucción de inyección DML hacia la clase ApunteDAO.
                ApunteDAO.registrarApunte(documento);

                // Llamamos a la clase bancaria para inyectarle al estudiante su recompensa por haber subido material.
                UsuarioDAO.sumarCreditos(idAutor, MotorEconomia.RECOMPENSA_SUBIDA);
                // Cerramos la condición transaccional.
            }

            // Imprimimos la respuesta de éxito hacia el navegador informando los créditos ganados y dando un enlace de retorno.
            return "<h2>¡Aporte Validado y Procesado!</h2><p>El documento y su previsualización fueron asegurados. ¡Ganaste " + MotorEconomia.RECOMPENSA_SUBIDA + " créditos!</p><br><a href='/dashboard'>Volver a la Tienda</a>";

            // Atrapamos el colapso si IntelliJ no tiene permisos de escritura o el disco de tu computadora está lleno.
        } catch (IOException e) {
            // Imprimimos el log del fallo crítico en la terminal interna.
            System.out.println("Error físico al escribir binarios: " + e.getMessage());
            // Avisamos al usuario que el servidor web sufrió un colapso interno.
            return "<h2>Fallo del Servidor</h2><p>Imposible procesar el documento físico.</p>";
            // Cerramos el control de excepciones de flujo.
        }
        // Cerramos el método maestro de subidas de archivos.
    }
// Cerramos la arquitectura inquebrantable de ControladorApunte.
}