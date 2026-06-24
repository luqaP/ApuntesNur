// Declaramos el paquete de la universidad.
package bo.edu.nur;

// Declaramos la clase MotorEconomia.
public class MotorEconomia {

    // Constantes que ya tenías...
    public static final int COSTO_DESCARGA = 5;
    public static final int RECOMPENSA_SUBIDA = 10;
    public static final int RECOMPENSA_RESENA = 2;

    // NUEVA CONSTANTE: Definimos el bono inicial para que los usuarios nuevos no empiecen frustrados.
    public static final int BONO_BIENVENIDA = 50;

    // Método que ya tenías...
    public static boolean puedeDescargar(int saldoActual) {
        return saldoActual >= COSTO_DESCARGA;
    }
}