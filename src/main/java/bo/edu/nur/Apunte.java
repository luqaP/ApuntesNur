// Declaramos el paquete estructural al que pertenece este modelo dentro de tu proyecto universitario.
package bo.edu.nur;

// Declaramos la clase pública que servirá como el molde matemático para empaquetar los datos de la base de datos a la memoria RAM.
public class Apunte {

    // Declaramos el atributo privado que almacenará el nombre del documento (ej. "Resumen de Matrices").
    private String titulo;
    // Declaramos el atributo privado para clasificar académicamente el documento (ej. "Algebra").
    private String categoria;
    // Declaramos el atributo numérico privado que guarda la clave foránea del estudiante que subió el archivo.
    private int idAutor;
    // Declaramos el atributo privado que contiene la ruta exacta en tu disco duro o el nombre único (ej. "550e_archivo.md").
    private String rutaArchivoFisico;

    // Declaramos el constructor público estricto que exige los cuatro parámetros para instanciar el objeto correctamente.
    public Apunte(String titulo, String categoria, int idAutor, String rutaArchivoFisico) {

        // Asignamos el valor del parámetro 'titulo' recibido al atributo encapsulado de esta clase (this).
        this.titulo = titulo;
        // Asignamos el valor del parámetro 'categoria' al atributo interno de la clase.
        this.categoria = categoria;
        // Vinculamos el ID numérico del autor al objeto en la memoria RAM.
        this.idAutor = idAutor;
        // Guardamos la ruta del archivo para que Thymeleaf y el Controlador de Descargas sepan qué entregar.
        this.rutaArchivoFisico = rutaArchivoFisico;

        // Cerramos el bloque del constructor estructurado.
    }

    // Declaramos el método público getter para extraer el título del objeto desde otras clases.
    public String getTitulo() {

        // Retornamos el valor almacenado en el atributo 'titulo'.
        return titulo;

        // Cerramos el método getter.
    }

    // Declaramos el método público setter por si necesitamos modificar el título de forma dinámica en el futuro.
    public void setTitulo(String titulo) {

        // Reemplazamos el título actual en la memoria por el nuevo valor recibido.
        this.titulo = titulo;

        // Cerramos el método setter.
    }

    // Declaramos el método público getter para extraer la materia académica.
    public String getCategoria() {

        // Retornamos la categoría almacenada.
        return categoria;

        // Cerramos el getter de categoría.
    }

    // Declaramos el método setter para sobreescribir la categoría académica.
    public void setCategoria(String categoria) {

        // Actualizamos la categoría con el parámetro inyectado.
        this.categoria = categoria;

        // Cerramos el setter de categoría.
    }

    // Declaramos el método getter para obtener el ID numérico de quien subió el archivo a la plataforma.
    public int getIdAutor() {

        // Retornamos la clave foránea del usuario creador.
        return idAutor;

        // Cerramos el getter del ID de autor.
    }

    // Declaramos el método setter para modificar la autoría del apunte en caso de correcciones en la base de datos.
    public void setIdAutor(int idAutor) {

        // Actualizamos el atributo numérico del ID en el objeto.
        this.idAutor = idAutor;

        // Cerramos el setter de autoría.
    }

    // Declaramos el método getter vital para obtener la ruta del archivo físico, usado intensivamente por Thymeleaf.
    public String getRutaArchivoFisico() {

        // Retornamos la ruta o nombre del archivo necesario para el proceso de enlace de descarga.
        return rutaArchivoFisico;

        // Cerramos el getter de la ruta.
    }

    // Declaramos el método setter para actualizar la ruta física en caso de renombrar o mover el archivo en Windows.
    public void setRutaArchivoFisico(String rutaArchivoFisico) {

        // Reasignamos el atributo de la ruta del archivo en el servidor.
        this.rutaArchivoFisico = rutaArchivoFisico;

        // Cerramos el setter de la ruta física.
    }

// Cerramos la estructura general y arquitectónica de la clase Apunte.
}