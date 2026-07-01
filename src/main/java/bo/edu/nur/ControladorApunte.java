// Declaramos la pertenencia del archivo a la arquitectura base del proyecto universitario.
package bo.edu.nur;

// Importamos la clase abstracta File para manipular archivos y directorios del sistema operativo.
import java.io.File;
// Importamos la captura de errores de entrada y salida para evitar colapsos por falta de espacio.
import java.io.IOException;
// Importamos la utilidad Files para gestionar el copiado binario de disco a disco.
import java.nio.file.Files;
// Importamos la interfaz Path para definir direcciones lógicas dentro del disco duro.
import java.nio.file.Path;
// Importamos la fábrica Paths para construir direcciones legibles.
import java.nio.file.Paths;
// Importamos la bandera de copia que forzará el reemplazo de archivos si detecta colisiones de disco.
import java.nio.file.StandardCopyOption;
// Importamos el motor matemático para generar cadenas UUID únicas en el universo.
import java.util.UUID;

// Importamos la anotación que convierte a esta clase en un punto final (Endpoint) REST.
import org.springframework.web.bind.annotation.RestController;
// Importamos el receptor de peticiones de subida (POST).
import org.springframework.web.bind.annotation.PostMapping;
// Importamos el extractor de variables inyectadas desde los formularios HTML.
import org.springframework.web.bind.annotation.RequestParam;
// Importamos la interfaz MultipartFile para recibir secuencias de bytes pesadas.
import org.springframework.web.multipart.MultipartFile;
// Importamos el gestor de memoria temporal para autenticar a los estudiantes conectados.
import jakarta.servlet.http.HttpSession;

// Registramos este componente en el escáner de Spring Boot.
@RestController
// Declaramos la clase pública que gestionará el tráfico de la propiedad intelectual.
public class ControladorApunte {

    // Asignamos la ruta estricta '/subir-apunte' a este método interceptor.
    @PostMapping("/subir-apunte")
    // Declaramos la firma del método exigiendo el título, la materia, el archivo binario y la sesión HTTP.
    public String procesarSubida(@RequestParam("tituloApunte") String titulo,
                                 @RequestParam("categoriaMateria") String categoria,
                                 @RequestParam("archivoFisico") MultipartFile archivo,
                                 HttpSession sesion) {

        // ====================================================================================
        // FASE A: BARRERA PERIMETRAL DE SEGURIDAD (CON DELEGACIÓN)
        // ====================================================================================
        String aliasUsuario = (String) sesion.getAttribute("usuarioLogueado");
        if (aliasUsuario == null) {
            return "<h2>Acceso Denegado</h2><p>Debes iniciar sesión para subir apuntes en la plataforma de la NUR.</p>";
        }

        // Delegamos la validación física estricta a nuestra clase especializada.
        if (!ValidadorApunte.esArchivoValido(archivo)) {
            return "<h2>Rechazo del Sistema</h2><p>El archivo viola las políticas de tamaño, está corrupto o no es un PDF válido.</p>";
        }

        String nombreOriginal = archivo.getOriginalFilename();

        // ====================================================================================
        // FASE B: INGENIERÍA DE ANTI-COLISIONES LÓGICAS
        // ====================================================================================

        // Exigimos a la CPU que calcule un identificador UUID único.
        String codigoUnico = UUID.randomUUID().toString();
        // Fusionamos ese código con el nombre del documento para que jamás ocurra una sobrescritura accidental en el disco.
        String nombreSeguro = codigoUnico + "_" + nombreOriginal;

        // Abrimos el bloque crítico de transacciones de I/O (Input/Output).
        try {

            // ====================================================================================
            // FASE C: PERSISTENCIA FÍSICA BINARIA
            // ====================================================================================

            // Definimos el nombre del directorio raíz donde residirán todos los aportes de la universidad.
            String nombreCarpeta = "repositorio_nur/";
            // Instanciamos el objeto File apuntando a esa carpeta.
            File directorioDestino = new File(nombreCarpeta);

            // Si el directorio no existe físicamente, ordenamos su construcción automática.
            if (!directorioDestino.exists()) {
                // Ejecutamos la creación del árbol de carpetas.
                directorioDestino.mkdirs();
                // Cerramos chequeo del directorio padre.
            }

            // Instanciamos un objeto File apuntando a la subcarpeta que alojará las fotos de portada (Thumbnails).
            File directorioPreviews = new File(nombreCarpeta + "previews/");

            // Si la subcarpeta de imágenes no existe, ordenamos que sea construida.
            if (!directorioPreviews.exists()) {
                // Ejecutamos la creación de la carpeta de miniaturas.
                directorioPreviews.mkdirs();
                // Cerramos chequeo de la bóveda fotográfica.
            }

            // Calculamos la ruta matemática exacta donde el PDF del alumno se guardará de forma definitiva.
            Path rutaFinal = Paths.get(nombreCarpeta + nombreSeguro);
            // Ordenamos al sistema operativo que copie el flujo de datos desde la RAM hacia el archivo final en el disco SSD.
            Files.copy(archivo.getInputStream(), rutaFinal, StandardCopyOption.REPLACE_EXISTING);

            // ====================================================================================
            // FASE D: EXTRACCIÓN GRÁFICA (GENERACIÓN DE LA MINIATURA)
            // ====================================================================================

            // Calculamos la ruta exacta donde queremos que la fotografía JPG resultante se almacene.
            // Le concatenamos ".jpg" al nombre del PDF (Ejemplo final: 123_calculo.pdf.jpg).
            String rutaPreview = nombreCarpeta + "previews/" + nombreSeguro + ".jpg";

            // Invocamos a nuestro motor de renderizado pasando la ruta del PDF de origen y la ruta de destino fotográfica.
            GeneradorMiniaturas.generar(rutaFinal.toString(), rutaPreview);

            // ====================================================================================
            // FASE E: PERSISTENCIA RELACIONAL (SQLite) Y GAMIFICACIÓN
            // ====================================================================================

            // Interrogamos a la base de datos para traducir el nombre de usuario (alias) a su Llave Primaria numérica (ID).
            int idAutor = UsuarioDAO.obtenerIdPorAlias(aliasUsuario);

            // Comprobamos que la cuenta del estudiante siga existiendo.
            if (idAutor != -1) {
                // Construimos el objeto relacional del Apunte con el título, categoría, creador y la ruta segura.
                Apunte documento = new Apunte(titulo, categoria, idAutor, nombreSeguro);
                // Instruimos al DAO para inyectar este nuevo registro en el archivo de SQLite.
                ApunteDAO.registrarApunte(documento);

                // Llamamos a la banca virtual para depositarle al creador su incentivo económico por subir material.
                UsuarioDAO.sumarCreditos(idAutor, MotorEconomia.RECOMPENSA_SUBIDA);
                // Cerramos el bloque de transacciones de base de datos.
            }

            // Renderizamos un mensaje de éxito HTML crudo notificando la validación del apunte y la inyección financiera.
            return "<h2>¡Aporte Validado y Procesado!</h2><p>El documento y su previsualización fueron asegurados. ¡Ganaste " + MotorEconomia.RECOMPENSA_SUBIDA + " créditos!</p><br><a href='/dashboard'>Volver a la Tienda</a>";

            // Capturamos violaciones de escritura físicas.
        } catch (IOException e) {
            // Imprimimos la traza de colapso en la terminal roja de IntelliJ.
            System.out.println("ERROR I/O -> Fallo físico al escribir binarios en disco: " + e.getMessage());
            // Mostramos una alerta crítica al usuario.
            return "<h2>Fallo del Servidor</h2><p>Imposible almacenar el documento en los discos de la universidad.</p>";
            // Cerramos el paracaídas de excepciones.
        }
        // Cerramos el algoritmo maestro del punto final (Endpoint) de subidas.
    }
// Cerramos la estructura absoluta de la clase controladora.
}