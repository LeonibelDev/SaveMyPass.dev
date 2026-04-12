package com.dac.passwordmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import com.dac.passwordmanager.dto.SignupRequestDTO;
import com.dac.passwordmanager.config.security.KeyUtil;
import com.dac.passwordmanager.dto.LoginRequestDTO;
import com.dac.passwordmanager.entity.User;
import com.dac.passwordmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKey;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index(
            HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        return "redirect:/home";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup_controller(
            @ModelAttribute("signupForm") SignupRequestDTO signupRequestDTO,
            Model model,
            RedirectAttributes redirectAttributes) {

        /*
         * Validar que las contrasenas intoducidas coincidan
         */
        if (!signupRequestDTO.getPassword().equals(signupRequestDTO.getPasswordConfirmacion())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "signup";
        }

        /*
         * Validar todos los inputs
         */
        if (signupRequestDTO.getTarjetaEmpleado().isEmpty() || signupRequestDTO.getNombreCompleto().isEmpty()
                || signupRequestDTO.getEmail().isEmpty() || signupRequestDTO.getPassword().isEmpty()) {
            model.addAttribute("error", "Todos los campos son obligatorios");

            return "signup";
        }

        /*
         * Validar que la tarjeta de empleado no exista
         */
        Optional<User> verifyUserByTarjetaEmpleado = userService
                .getUserByTarjetaEmpleado(signupRequestDTO.getTarjetaEmpleado());
        if (verifyUserByTarjetaEmpleado.isPresent()) {
            model.addAttribute("error", "La tarjeta de empleado ya existe");
            return "signup";
        }

        /*
         * Validar que el correo no exista
         */
        Optional<User> verifyUserByEmail = userService.getUserByEmail(signupRequestDTO.getEmail());
        if (verifyUserByEmail.isPresent()) {
            model.addAttribute("error", "El correo ya existe");
            return "signup";
        }

        /*
         * Crear usuario
         */
        User user = new User();
        user.setTarjetaEmpleado(signupRequestDTO.getTarjetaEmpleado());
        user.setNombreCompleto(signupRequestDTO.getNombreCompleto());
        user.setEmail(signupRequestDTO.getEmail());
        user.setCedula(signupRequestDTO.getCedula());
        user.setAdmin(false);

        /*
         * Encriptar password
         */
        String encodedPassword = passwordEncoder.encode(signupRequestDTO.getPassword());
        user.setPassword(encodedPassword);

        /*
         * General Salt
         */
        byte[] salt = KeyUtil.generateSalt();
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        user.setSalt(saltBase64);

        /*
         * Guardar usuario
         */
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("message", "Usuario registrado exitosamente");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login_controller(
            @ModelAttribute("loginForm") LoginRequestDTO loginRequestDTO,
            Model model,
            HttpSession session) throws Exception {

        /*
         * Obtener usuario por tarjeta
         */
        Optional<User> user = userService.getUserByTarjetaEmpleado(loginRequestDTO.getTarjetaEmpleado());
        if (user.isEmpty()) {
            model.addAttribute("error", "Datos incorrectos");

            return "login";
        }

        /*
         * Validar password
         */
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.get().getPassword())) {

            model.addAttribute("error", "Datos incorrectos");

            return "login";
        }

        /*
         * Generar SecretKey
         */
        byte[] salt = Base64.getDecoder().decode(user.get().getSalt());

        SecretKey secretKey = KeyUtil.deriveKey(loginRequestDTO.getPassword(), salt);

        session.setAttribute("aesKey", secretKey);
        session.setAttribute("userId", user.get().getTarjetaEmpleado());
        session.setAttribute("email", user.get().getEmail());

        return "redirect:/home";

    }
}