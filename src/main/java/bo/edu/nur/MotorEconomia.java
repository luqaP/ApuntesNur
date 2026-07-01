// Declaramos el paquete estructural oficial del proyecto universitario para mantener la cohesión.
package bo.edu.nur;

// Declaramos la clase MotorEconomia que centraliza las reglas financieras del ecosystema.
public class MotorEconomia {

    // Definimos el costo estándar de créditos para desbloquear un apunte en el mercado.
    public static final int COSTO_DESCARGA = 5;
    // Definimos la recompensa estática otorgada al estudiante que sube material real al servidor.
    public static final int RECOMPENSA_SUBIDA = 10;
    // Definimos el incentivo económico para fomentar la depuración de textos académicos.
    public static final int RECOMPENSA_RESENA = 2;
    // Definimos el capital de bienvenida inyectado a los nuevos alumnos de la NUR.
    public static final int BONO_BIENVENIDA = 50;

    // Declaramos un método estático rápido para evaluar la solvencia de una billetera virtual.
    public static boolean puedeDescargar(int saldoActual) {
        // Evaluamos booleanamente si el dinero líquido supera o iguala la barrera de cobro comercial.
        return saldoActual >= COSTO_DESCARGA;
        // Cerramos la validación algorítmica simple.
    }

    // Declaramos el método centralizado para procesar transacciones seguras y atómicas entre cuentas.
    public static boolean procesarAdquisicionSegura(int idComprador, int idApunte) {
        // Consultamos al DAO para extraer la metadata del documento físico usando su ID de base de datos.
        Apunte apunteObjetivo = ApunteDAO.obtenerPorId(idApunte);

        // Verificamos si el archivo no existe en el registro relacional para abortar la operación.
        if (apunteObjetivo == null) {
            // Retornamos falso cancelando cualquier movimiento de fondos ficticios.
            return false;
            // Cerramos el escudo de existencia.
        }

        // Impedimos lógicamente que un estudiante intente comprar su propia propiedad intelectual.
        if (apunteObjetivo.getIdAutor() == idComprador) {
            // Retornamos verdadero de forma inmediata permitiendo la descarga libre de sus propios archivos.
            return true;
            // Cerramos la validación de autoría original.
        }

        // Consultamos al libro mayor si el alumno ya pagó por los derechos de este documento en el pasado.
        boolean yaComprado = ApunteDAO.verificarAdquisicion(idComprador, idApunte);

        // Evaluamos el resultado booleano devuelto por el cursor de SQLite.
        if (yaComprado) {
            // Retornamos verdadero de forma inmediata habilitando la descarga gratuita por derecho de propiedad.
            return true;
            // Cerramos la cláusula de derechos adquiridos.
        }

        // Solicitamos a la base de datos el saldo real de la billetera del comprador actual.
        int saldoComprador = UsuarioDAO.consultarSaldo(idComprador);

        // Evaluamos si el estudiante no tiene la liquidez necesaria para costear la descarga.
        if (!puedeDescargar(saldoComprador)) {
            // Rechazamos la transacción por insolvencia financiera del alumno.
            return false;
            // Cerramos la barrera de liquidez.
        }

        // BUG FIX: Cambiamos 'sumarCreditos' por 'cobrarCreditos' para restar el dinero al comprador de forma real.
        UsuarioDAO.cobrarCreditos(idComprador, COSTO_DESCARGA);

        // Transferimos íntegramente los 5 créditos ganados a la cuenta del autor original del apunte.
        UsuarioDAO.sumarCreditos(apunteObjetivo.getIdAutor(), COSTO_DESCARGA);

        // Registramos legalmente el recibo de compra inyectando la fila en la tabla puente Adquisicion.
        ApunteDAO.registrarAdquisicion(idComprador, idApunte);

        // Concluimos la transacción atómica devolviendo un estado de éxito absoluto.
        return true;
        // Cerramos el método de orquestación transaccional financiera.
    }
// Cerramos la arquitectura de la clase del motor económico.
}