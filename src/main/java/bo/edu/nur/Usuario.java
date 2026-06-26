// Declaramos el paquete estructural del proyecto.
package bo.edu.nur;

// Importamos la librería nativa para generar estampas de tiempo.
import java.time.LocalDate;

// Declaramos la clase pública que servirá de molde para la memoria RAM.
public class Usuario {

    // ========================================================================================
    // ATRIBUTOS DE LA ENTIDAD
    // ========================================================================================

    // Llave primaria numérica auto-generada por SQLite.
    private int idUsuario;
    // Identificador textual único del estudiante.
    private String nombreUsuario;
    // Nombre legal y completo del estudiante.
    private String nombreCompleto;
    // Número de matrícula universitaria.
    private String numeroCredencial;
    // Hash criptográfico de seguridad.
    private String contrasena;
    // Saldo de la economía virtual.
    private int creditosVirtuales;
    // Contador de ingresos diarios.
    private int rachaDiaria;
    // Estampa de tiempo del último acceso.
    private String fechaUltimoAcceso;

    // ========================================================================================
    // CONSTRUCTORES
    // ========================================================================================

    // Constructor de Extracción: Utilizado por el DAO cuando lee una fila completa de SQLite.
    public Usuario(int idUsuario, String nombreUsuario, String nombreCompleto, String numeroCredencial, String contrasena, int creditosVirtuales, int rachaDiaria, String fechaUltimoAcceso) {
        // Mapeamos el ID.
        this.idUsuario = idUsuario;
        // Mapeamos el alias.
        this.nombreUsuario = nombreUsuario;
        // Mapeamos el nombre real.
        this.nombreCompleto = nombreCompleto;
        // Mapeamos la credencial.
        this.numeroCredencial = numeroCredencial;
        // Mapeamos la clave encriptada.
        this.contrasena = contrasena;
        // Mapeamos los créditos.
        this.creditosVirtuales = creditosVirtuales;
        // Mapeamos la racha.
        this.rachaDiaria = rachaDiaria;
        // Mapeamos la fecha.
        this.fechaUltimoAcceso = fechaUltimoAcceso;
        // Cerramos el constructor de extracción.
    }

    // Constructor de Inserción: Utilizado por el ControladorRegistro al crear un nuevo estudiante.
    public Usuario(String nombreUsuario, String nombreCompleto, String numeroCredencial, String contrasena) {
        // Asignamos un ID temporal en 0.
        this.idUsuario = 0;
        // Asignamos alias digitado.
        this.nombreUsuario = nombreUsuario;
        // Asignamos nombre real digitado.
        this.nombreCompleto = nombreCompleto;
        // Asignamos credencial validada.
        this.numeroCredencial = numeroCredencial;
        // Asignamos clave cifrada.
        this.contrasena = contrasena;
        // Inicializamos saldo en 0.
        this.creditosVirtuales = 0;
        // Inicializamos racha en 0.
        this.rachaDiaria = 0;
        // Inyectamos la fecha actual como texto.
        this.fechaUltimoAcceso = LocalDate.now().toString();
        // Cerramos el constructor de inserción.
    }

    // ========================================================================================
    // MÉTODOS PUENTE PARA THYMELEAF Y CONTROLADORES MODERNOS
    // ========================================================================================

    // Extraemos la llave primaria (Requerido por ControladorPerfil).
    public int getIdUsuario() {
        // Retornamos el ID.
        return this.idUsuario;
        // Cerramos el getter.
    }

    // Extraemos el alias (Requerido por Thymeleaf y ControladorDashboard).
    public String getAlias() {
        // Retornamos el nombre de usuario.
        return this.nombreUsuario;
        // Cerramos el getter.
    }

    // Extraemos el saldo (Requerido por Thymeleaf y ControladorDashboard).
    public int getCreditos() {
        // Retornamos los créditos virtuales.
        return this.creditosVirtuales;
        // Cerramos el getter.
    }

    // ========================================================================================
    // MÉTODOS HEREDADOS (Compatibilidad estricta con UsuarioDAO antiguo)
    // ========================================================================================

    // Retornamos el alias con la nomenclatura antigua para no romper inserciones previas.
    public String obtenerNombreUsuario() {
        // Devolvemos el dato.
        return this.nombreUsuario;
        // Cerramos el getter antiguo.
    }

    // Retornamos el nombre completo.
    public String obtenerNombreCompleto() {
        // Devolvemos el dato.
        return this.nombreCompleto;
        // Cerramos el getter antiguo.
    }

    // Retornamos la credencial institucional.
    public String obtenerNumeroCredencial() {
        // Devolvemos el dato.
        return this.numeroCredencial;
        // Cerramos el getter antiguo.
    }

    // Retornamos el hash criptográfico.
    public String obtenerContrasena() {
        // Devolvemos el dato.
        return this.contrasena;
        // Cerramos el getter antiguo.
    }

    // Retornamos el saldo con la nomenclatura antigua.
    public int obtenerCreditos() {
        // Devolvemos el dato.
        return this.creditosVirtuales;
        // Cerramos el getter antiguo.
    }

    // Retornamos la racha diaria.
    public int obtenerRachaDiaria() {
        // Devolvemos el dato.
        return this.rachaDiaria;
        // Cerramos el getter antiguo.
    }

    // Retornamos la estampa de tiempo como texto.
    public String obtenerFechaUltimoAccesoComoTexto() {
        // Devolvemos el dato.
        return this.fechaUltimoAcceso;
        // Cerramos el getter antiguo.
    }

// Cerramos la estructura absoluta de la clase Usuario.
}