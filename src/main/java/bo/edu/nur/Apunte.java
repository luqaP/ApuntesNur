// Declaramos el paquete estructural que contiene las entidades de la base de datos.
package bo.edu.nur;

// Declaramos la clase pública que actúa como el molde para los documentos del sistema.
public class Apunte {

    // Identificador único y llave primaria del apunte en SQLite.
    private int idApunte;
    // Cadena de texto que almacena el nombre comercial del documento.
    private String titulo;
    // Rama académica a la que pertenece (Ej: Programacion, Algebra).
    private String categoria;
    // Llave foránea numérica que enlaza con la tabla Usuario.
    private int idAutor;
    // Ruta encriptada y absoluta hacia el binario en el disco duro.
    private String rutaArchivoFisico;
    // NUEVO: Atributo transitorio para almacenar en RAM el nombre del autor extraído vía SQL.
    private String aliasAutor;

    // Constructor maestro para cuando extraemos un apunte completo desde SQLite (con ID y Alias).
    public Apunte(int idApunte, String titulo, String categoria, int idAutor, String rutaArchivoFisico, String aliasAutor) {
        // Asignamos el ID primario.
        this.idApunte = idApunte;
        // Asignamos el título.
        this.titulo = titulo;
        // Asignamos la taxonomía académica.
        this.categoria = categoria;
        // Asignamos el ID numérico del creador.
        this.idAutor = idAutor;
        // Asignamos la ruta de descarga.
        this.rutaArchivoFisico = rutaArchivoFisico;
        // Asignamos el nombre de usuario textual.
        this.aliasAutor = aliasAutor;
        // Cerramos el constructor de extracción.
    }

    // Constructor transaccional para cuando el estudiante recién sube el apunte (aún no tiene ID de BD).
    public Apunte(String titulo, String categoria, int idAutor, String rutaArchivoFisico) {
        // Asignamos un 0 temporal mientras SQLite le asigna un número real.
        this.idApunte = 0;
        // Asignamos el título digitado en el formulario.
        this.titulo = titulo;
        // Asignamos la materia seleccionada.
        this.categoria = categoria;
        // Asignamos el ID numérico extraído de la sesión.
        this.idAutor = idAutor;
        // Asignamos la ruta física recién generada con el UUID.
        this.rutaArchivoFisico = rutaArchivoFisico;
        // Inicializamos el alias como desconocido hasta que se haga una consulta relacional.
        this.aliasAutor = "Desconocido";
        // Cerramos el constructor de inserción.
    }

    // Método de acceso para extraer el ID primario.
    public int getIdApunte() {
        // Devolvemos el valor numérico.
        return idApunte;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar el ID primario.
    public void setIdApunte(int idApunte) {
        // Sobrescribimos el valor de la memoria.
        this.idApunte = idApunte;
        // Cerramos el setter.
    }

    // Método de acceso para extraer el título.
    public String getTitulo() {
        // Devolvemos el texto.
        return titulo;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar el título.
    public void setTitulo(String titulo) {
        // Sobrescribimos el título.
        this.titulo = titulo;
        // Cerramos el setter.
    }

    // Método de acceso para extraer la materia.
    public String getCategoria() {
        // Devolvemos la rama académica.
        return categoria;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar la materia.
    public void setCategoria(String categoria) {
        // Sobrescribimos la categoría.
        this.categoria = categoria;
        // Cerramos el setter.
    }

    // Método de acceso para extraer la llave foránea del autor.
    public int getIdAutor() {
        // Devolvemos el ID relacional.
        return idAutor;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar la llave foránea.
    public void setIdAutor(int idAutor) {
        // Sobrescribimos el enlace relacional.
        this.idAutor = idAutor;
        // Cerramos el setter.
    }

    // Método de acceso para extraer la ubicación física del archivo.
    public String getRutaArchivoFisico() {
        // Devolvemos la ruta local.
        return rutaArchivoFisico;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar la ruta física.
    public void setRutaArchivoFisico(String rutaArchivoFisico) {
        // Sobrescribimos la ruta.
        this.rutaArchivoFisico = rutaArchivoFisico;
        // Cerramos el setter.
    }

    // Método de acceso para extraer el nombre en texto del autor (usado por Thymeleaf).
    public String getAliasAutor() {
        // Devolvemos el alias.
        return aliasAutor;
        // Cerramos el getter.
    }

    // Método de acceso para inyectar el alias desde una consulta SQL con JOIN.
    public void setAliasAutor(String aliasAutor) {
        // Sobrescribimos el texto del autor.
        this.aliasAutor = aliasAutor;
        // Cerramos el setter.
    }
// Cerramos la clase modelo.
}