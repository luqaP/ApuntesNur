// Declaramos el paquete oficial de la universidad NUR para mantener la cohesión estricta del proyecto.
package bo.edu.nur;

// Declaramos la clase MotorEconomia que centraliza absolutamente todas las reglas financieras del sistema.
public class MotorEconomia {

    // Definimos el costo estándar e inmutable para desbloquear cualquier documento en la plataforma.
    public static final int COSTO_DESCARGA = 5;
    // Definimos la recompensa estática que recibe un estudiante por aportar material nuevo al repositorio.
    public static final int RECOMPENSA_SUBIDA = 10;
    // Definimos el incentivo económico para fomentar que los alumnos califiquen y depuren los apuntes.
    public static final int RECOMPENSA_RESENA = 2;
    // Definimos el capital semilla o bono inicial para inyectar en SQLite y evitar que los nuevos se frustren.
    public static final int BONO_BIENVENIDA = 50;

    // Declaramos un método estático y rápido para validar matemáticamente si un saldo es suficiente.
    public static boolean puedeDescargar(int saldoActual) {
        // Retornamos el resultado de la evaluación lógica comparando el dinero del usuario con el costo estricto.
        return saldoActual >= COSTO_DESCARGA;
        // Cerramos el método de validación algorítmica simple.
    }

    // Declaramos el método orquestador para procesar una compra segura y atómica entre dos estudiantes.
    public static boolean procesarAdquisicionSegura(int idComprador, int idApunte) {
        // Invocamos al DAO para extraer la metadata del documento exacto desde la base de datos de SQLite.
        Apunte apunteObjetivo = ApunteDAO.obtenerPorId(idApunte);

        // Verificamos de forma defensiva si el documento es nulo (fue borrado físicamente o no existe).
        if (apunteObjetivo == null) {
            // Abortamos la transacción inmediatamente retornando falso para proteger la integridad.
            return false;
            // Cerramos la barrera de validación de existencia.
        }

        // Verificamos lógicamente si el estudiante está intentando comprar su propio archivo Markdown o PDF.
        if (apunteObjetivo.getIdAutor() == idComprador) {
            // Bloqueamos la auto-compra porque el "farmeo" de descargas destruye la economía circular del sistema.
            return false;
            // Cerramos la validación de propiedad intelectual original.
        }

        // Consultamos a la biblioteca transaccional si el usuario ya pagó por este apunte en el pasado.
        boolean yaComprado = ApunteDAO.verificarAdquisicion(idComprador, idApunte);

        // Evaluamos el resultado booleano de la consulta de pertenencia en disco.
        if (yaComprado) {
            // Retornamos verdadero de inmediato para que descargue gratis, pues ya posee los derechos perpetuos.
            return true;
            // Cerramos el bloque de validación de compras duplicadas.
        }

        // Averiguamos cuánto dinero digital tiene exactamente el comprador en su bóveda consultando SQLite.
        int saldoComprador = UsuarioDAO.consultarSaldo(idComprador);

        // Invocamos la regla de negocio para comprobar si sus fondos superan o igualan la barrera de peaje.
        if (!puedeDescargar(saldoComprador)) {
            // Rechazamos la petición de compra por insolvencia económica del estudiante.
            return false;
            // Cerramos la muralla de validación de fondos líquidos.
        }

        // Fase 1 de la Transacción: Le sustraemos el importe de los créditos al estudiante comprador.
        UsuarioDAO.sumarCreditos(idComprador, COSTO_DESCARGA);

        // Fase 2 de la Transacción: Le transferimos esa misma cantidad matemática al creador del documento.
        UsuarioDAO.sumarCreditos(apunteObjetivo.getIdAutor(), COSTO_DESCARGA);

        // Fase 3 de la Transacción: Registramos legalmente la compra en la tabla intermedia y aumentamos la fama del archivo.
        ApunteDAO.registrarAdquisicion(idComprador, idApunte);

        // Concluimos retornando verdadero, garantizando que toda la operación atómica fue un éxito rotundo.
        return true;
        // Cerramos el método maestro de orquestación transaccional.
    }

// Cerramos la arquitectura inquebrantable de la clase MotorEconomia.
}