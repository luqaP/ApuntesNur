// Mantenemos el paquete estructural del proyecto.
package bo.edu.nur;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// Importamos la herramienta nativa para manipular la respuesta del navegador.
import jakarta.servlet.http.HttpServletResponse;
// Importamos la excepción de entrada y salida requerida para redirecciones.
import java.io.IOException;

@RestController
public class ControladorRegistro {
    @PostMapping("/registrar-usuario") // Escuchamos el formulario de creación de cuentas de la tienda.
    public void procesarRegistro(@RequestParam("aliasUsuario") String alias, // Recibimos el alias único del estudiante.
                                 @RequestParam("nombreCompleto") String nombre, // Recibimos el nombre real para el perfil.
                                 @RequestParam("numeroCredencial") String credencial, // Recibimos la matrícula oficial de la NUR.
                                 @RequestParam("claveUsuario") String clave, // Recibimos la contraseña en texto plano del formulario.
                                 HttpServletResponse respuesta) throws IOException { // Inyectamos la respuesta para controlar la navegación HTTP.

        respuesta.setContentType("text/html;charset=UTF-8"); // Forzamos el formato HTML con soporte para tildes en la respuesta.

        if (UsuarioDAO.obtenerIdPorAlias(alias) != -1) { // Consultamos en SQLite si el alias ya existe para evitar duplicación.
            respuesta.getWriter().write("<h2>Error</h2><p>El nombre de usuario ya existe.</p>"); // Notificamos el conflicto de identidad.
            return; // Abortamos la ejecución del método inmediatamente.
        } // Cerramos la verificación de duplicados.

        // CRIPTOGRAFÍA: Generamos el hash inmutable aplicando un salt aleatorio con jBCrypt.
        String claveHasheada = org.mindrot.jbcrypt.BCrypt.hashpw(clave, org.mindrot.jbcrypt.BCrypt.gensalt()); // Transformamos el texto plano en 60 caracteres seguros.

        // Instanciamos el modelo Usuario pasando la contraseña encriptada en lugar de la cruda.
        Usuario nuevoEstudiante = new Usuario(alias, nombre, credencial, claveHasheada); // El objeto en RAM queda protegido.

        // Paso 3: PERSISTENCIA EN DISCO DURO
        boolean exito = UsuarioDAO.registrarUsuario(nuevoEstudiante); // Guardamos la fila en SQLite.

        // Paso 4: INYECCIÓN ECONÓMICA Y REDIRECCIÓN
        // Evaluamos si el paso previo (insertar al usuario en SQLite) fue exitoso.
        if (exito) {

            // Disparamos la primera sonda para confirmar que entramos al bloque económico.
            System.out.println("TELEMETRÍA REGISTRO -> Usuario guardado en SQLite. Buscando ID asignado...");

            // Invocamos al DAO para que nos devuelva el ID numérico que SQLite acaba de generar.
            int idNuevoUsuario = UsuarioDAO.obtenerIdPorAlias(alias);

            // Disparamos la segunda sonda para ver si el ID es válido o si el DAO nos devolvió un -1 (error).
            System.out.println("TELEMETRÍA REGISTRO -> ID recuperado: " + idNuevoUsuario);

            // Validamos que el ID exista físicamente antes de intentar regalarle dinero.
            if (idNuevoUsuario != -1) {

                // Anunciamos la transacción antes de ejecutarla.
                System.out.println("TELEMETRÍA REGISTRO -> Inyectando " + MotorEconomia.BONO_BIENVENIDA + " créditos...");

                // Ejecutamos la orden al DAO de inyectar los fondos.
                UsuarioDAO.sumarCreditos(idNuevoUsuario, MotorEconomia.BONO_BIENVENIDA);

                // Abrimos la contingencia en caso de que el ID no se encuentre.
            } else {

                // Imprimimos un error crítico: el usuario se guardó, pero no podemos encontrarlo para pagarle.
                System.out.println("ERROR REGISTRO -> No se pudo encontrar el ID del usuario recién creado.");

                // Cerramos la validación económica.
            }

            // Forzamos al navegador web a redirigirse instantáneamente a la pantalla principal de Login.
            respuesta.sendRedirect("/");

            // Abrimos la ruta de error si la base de datos colapsó desde el primer intento de registro.
        } else {

            // Escribimos un HTML crudo en la pantalla del usuario indicando un fallo interno.
            respuesta.getWriter().write("<h2>Fallo del Servidor</h2><p>No se pudo crear la cuenta.</p>");

            // Cerramos la validación principal de éxito.
        }

        // Cerramos el método. // Cerramos la bifurcación de la persistencia.
    } // Cerramos el método procesarRegistro.
}