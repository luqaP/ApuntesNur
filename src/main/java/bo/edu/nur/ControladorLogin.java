package bo.edu.nur;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ControladorLogin {

    @PostMapping("/login")
    public void procesarAcceso(@RequestParam("aliasUsuario") String alias,
                               @RequestParam("claveUsuario") String clave,
                               HttpSession sesion,
                               HttpServletResponse respuesta) throws IOException {

        // Validamos la criptografía contra SQLite.
        if (UsuarioDAO.validarCredenciales(alias, clave)) {
            // Guardamos la sesión en RAM.
            sesion.setAttribute("usuarioLogueado", alias);
            // Redirigimos al sistema.
            respuesta.sendRedirect("/dashboard");
        } else {
            // BUG FIX (Punto 4): Redirigimos a la raíz adjuntando el parámetro de error en la URL HTTP.
            respuesta.sendRedirect("/?error=credenciales_invalidas");
        }
        // Cerramos el método.
    }
}