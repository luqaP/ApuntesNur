// Declaramos que esta clase pertenece al paquete principal de tu proyecto universitario para que Spring Boot la reconozca.
package bo.edu.nur;

// Importamos la clase LocalDate nativa de Java para manejar de forma profesional y matemática las fechas de acceso del estudiante.
import java.time.LocalDate;

// Declaramos la clase pública Usuario que servirá como la plantilla o molde exacto para crear a los estudiantes en la memoria RAM.
public class Usuario {

    // Declaramos el atributo privado de tipo texto para almacenar el alias único del estudiante en la plataforma.
    private String nombreUsuario;
    // Declaramos el atributo privado de tipo texto para guardar el nombre real y completo del usuario.
    private String nombreCompleto;
    // Declaramos el atributo privado de tipo texto para el número de registro o matrícula oficial de la universidad.
    private String numeroCredencial;
    // Declaramos el atributo privado de tipo texto para resguardar la contraseña de acceso (idealmente encriptada a futuro).
    private String contrasena;
    // Declaramos el atributo privado y numérico entero para manejar el saldo de la economía y descargas virtuales.
    private int creditosVirtuales;
    // Declaramos el atributo privado y numérico entero para contar los días consecutivos que el estudiante inicia sesión.
    private int rachaDiaria;
    // Declaramos el atributo privado de tipo LocalDate para registrar con precisión de calendario la última vez que el estudiante entró.
    private LocalDate fechaUltimoAcceso;

    // Definimos el constructor principal que exige los cuatro datos vitales cada vez que instanciemos un nuevo estudiante con la palabra reservada 'new'.
    public Usuario(String alias, String nombre, String credencial, String clave) {
        // Asignamos el valor del parámetro 'alias' al atributo interno de la clase utilizando 'this' para evitar ambigüedades.
        this.nombreUsuario = alias;
        // Asignamos el valor del parámetro 'nombre' al atributo persistente de nombre completo.
        this.nombreCompleto = nombre;
        // Asignamos el número de matrícula provisto al atributo de la credencial universitaria.
        this.numeroCredencial = credencial;
        // Asignamos la contraseña recibida al atributo privado de seguridad de la instancia.
        this.contrasena = clave;
        // Inicializamos los créditos virtuales estrictamente en 0 por defecto al momento de un nuevo registro.
        this.creditosVirtuales = 0;
        // Inicializamos la racha diaria en 0 por defecto, ya que es el primer día de vida de este usuario.
        this.rachaDiaria = 0;
        // Invocamos el método estático now() para capturar la fecha exacta del reloj del sistema operativo y la guardamos.
        this.fechaUltimoAcceso = LocalDate.now();
        // Cerramos el bloque de ejecución estricto del método constructor.
    }

    // Definimos un método público que retorna texto (String) para que otras clases externas consulten el nombre de usuario.
    public String obtenerNombreUsuario() {
        // Retornamos el valor almacenado en el atributo privado nombreUsuario de esta instancia específica.
        return this.nombreUsuario;
        // Cerramos el bloque de ejecución del método obtenerNombreUsuario().
    }

    // Definimos un método público que retorna texto para permitir el acceso de lectura al nombre completo del estudiante.
    public String obtenerNombreCompleto() {
        // Retornamos la cadena de texto almacenada de forma segura en el atributo privado nombreCompleto.
        return this.nombreCompleto;
        // Cerramos el bloque de ejecución del método obtenerNombreCompleto().
    }

    // Definimos un método público que retorna texto para consultar la matrícula universitaria de la NUR.
    public String obtenerNumeroCredencial() {
        // Retornamos el valor del atributo privado numeroCredencial, el cual está diseñado para soportar valores nulos si el usuario es externo.
        return this.numeroCredencial;
        // Cerramos el bloque de ejecución del método obtenerNumeroCredencial().
    }

    // Definimos un método público que retorna texto para recuperar la clave de acceso del usuario.
    public String obtenerContrasena() {
        // Retornamos el contenido del atributo privado contrasena para que el UsuarioDAO pueda validarlo contra la base de datos.
        return this.contrasena;
        // Cerramos el bloque de ejecución del método obtenerContrasena().
    }

    // Definimos un método público que retorna un entero primitivo (int) para consultar la cartera virtual del estudiante.
    public int obtenerCreditos() {
        // Retornamos el valor numérico exacto almacenado en el atributo privado creditosVirtuales.
        return this.creditosVirtuales;
        // Cerramos el bloque de ejecución del método obtenerCreditos().
    }

    // Definimos un método público que retorna un entero primitivo para auditar la racha de días consecutivos.
    public int obtenerRachaDiaria() {
        // Retornamos el valor numérico persistido en el atributo privado rachaDiaria.
        return this.rachaDiaria;
        // Cerramos el bloque de ejecución del método obtenerRachaDiaria().
    }

    // Definimos un método público que retorna la fecha convertida explícitamente a texto para garantizar compatibilidad con el motor de SQLite.
    public String obtenerFechaUltimoAccesoComoTexto() {
        // Invocamos el método nativo toString() del objeto LocalDate para transformar la estructura de fecha en un formato de texto estándar (YYYY-MM-DD).
        return this.fechaUltimoAcceso.toString();
        // Cerramos el bloque de ejecución del método obtenerFechaUltimoAccesoComoTexto().
    }

// Cerramos la declaración estructural y encapsulada de tu clase Usuario completa.
}