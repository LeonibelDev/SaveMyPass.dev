package com.dac.passwordmanager.controller;

import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.dac.passwordmanager.dto.PasswordCredentialRequestDTO;
import com.dac.passwordmanager.service.PasswordCredentialService;
import com.dac.passwordmanager.service.UserService;
import com.dac.passwordmanager.service.email.EmailService;
import com.dac.passwordmanager.config.security.AesCipherConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.dac.passwordmanager.entity.MessageEntity;
import com.dac.passwordmanager.entity.PasswordCredential;

@Controller
public class DashboardController {
    private final PasswordCredentialService passwordCredentialService;
    private final UserService userService;
    private final AesCipherConfig aesCipherConfig;
    private final EmailService emailService;

    @Autowired
    public DashboardController(PasswordCredentialService passwordCredentialService, UserService userService,
            AesCipherConfig aesCipherConfig, EmailService emailService) {
        this.passwordCredentialService = passwordCredentialService;
        this.userService = userService;
        this.aesCipherConfig = aesCipherConfig;
        this.emailService = emailService;
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws Exception {
        String userId = session.getAttribute("userId").toString();
        List<PasswordCredential> passwords = passwordCredentialService.findByUser(userId);

        SecretKey secretKey = (SecretKey) session.getAttribute("aesKey");

        List<PasswordCredentialRequestDTO> passwordsDTO = passwords.stream().map(password -> {
            PasswordCredentialRequestDTO passwordDTO = new PasswordCredentialRequestDTO();
            passwordDTO.setId(password.getId());
            passwordDTO.setSite(password.getSite());
            passwordDTO.setUsername(password.getUsername());
            try {
                passwordDTO.setPassword(aesCipherConfig.DecipherPassword(password.getPassword(), secretKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return passwordDTO;
        }).collect(Collectors.toList());

        model.addAttribute("passwords", passwordsDTO);

        /*
         * User data
         */

        Map<String, String> user = new HashMap<>();
        user.put("name", userService.getUserByTarjetaEmpleado(userId).get().getNombreCompleto());
        user.put("card", userService.getUserByTarjetaEmpleado(userId).get().getTarjetaEmpleado());

        model.addAttribute("user", user);
        return "home";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute PasswordCredentialRequestDTO passwordCredential, HttpSession session,
            HttpServletRequest request)
            throws Exception {
        PasswordCredential newPassword = new PasswordCredential();

        SecretKey secretKey = (SecretKey) session.getAttribute("aesKey");

        newPassword.setSite(passwordCredential.getSite());
        newPassword.setUsername(passwordCredential.getUsername());
        newPassword.setPassword(aesCipherConfig.CipherPassword(passwordCredential.getPassword(), secretKey));
        newPassword.setCreatedAt(LocalDateTime.now().toString());
        newPassword.setUpdatedAt(LocalDateTime.now().toString());
        newPassword.setUser(userService.getUserByTarjetaEmpleado(session.getAttribute("userId").toString()).get());

        passwordCredentialService.save(newPassword);

        /*
         * Send email confirmation
         */
        MessageEntity message = new MessageEntity();
        message.setRecipient(session.getAttribute("email").toString());
        message.setSubject("Nueva contraseña agregada");
        message.setBody("Se ha agregado una nueva contraseña a tu cuenta<br>" + "<strong>Sitio:</strong> "
                + passwordCredential.getSite() + "<br><strong>Usuario:</strong> "
                + passwordCredential.getUsername());
        message.setUrl("http://localhost:8080/");
        message.setButtonText("Ir al inicio");
        message.setDate(LocalDateTime.now().toString());
        message.setDevice(request.getHeader("User-Agent"));
        message.setIp(request.getRemoteAddr());
        message.setLocation(request.getRemoteHost());

        emailService.sendConfirmationEmail(session.getAttribute("email").toString(), message);

        return "redirect:/home";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") String passwordId, HttpSession session) {
        /*
         * Get password from db
         */

        Optional<PasswordCredential> password = passwordCredentialService
                .getCredentialById(UUID.fromString(passwordId));

        if (password.isEmpty()) {
            return "redirect:/home";
        }

        /*
         * Compare owner Id
         */
        String ownerId = password.get().getUser().getTarjetaEmpleado().toString();
        String sessionUserId = session.getAttribute("userId").toString();

        if (ownerId.equals(sessionUserId)) {
            passwordCredentialService.deleteCredentialById(password.get().getId());
        }

        return "redirect:/home";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable(name = "id") String passwordId, HttpSession session, HttpServletRequest request,
            @RequestParam String site, @RequestParam String username, @RequestParam String newpassword)
            throws Exception {
        /*
         * Get password from db
         */

        Optional<PasswordCredential> password = passwordCredentialService
                .getCredentialById(UUID.fromString(passwordId));

        if (password.isEmpty()) {
            return "redirect:/home";
        }

        /*
         * Compare owner Id
         */
        String ownerId = password.get().getUser().getTarjetaEmpleado().toString();
        String sessionUserId = session.getAttribute("userId").toString();

        if (ownerId.equals(sessionUserId)) {
            SecretKey secretKey = (SecretKey) session.getAttribute("aesKey");

            password.get().setSite(site);
            password.get().setPassword(aesCipherConfig.CipherPassword(newpassword, secretKey));
            password.get().setUpdatedAt(LocalDateTime.now().toString());

            passwordCredentialService.save(password.get());

            /*
             * Send email confirmation
             */
            MessageEntity message = new MessageEntity();
            message.setRecipient(session.getAttribute("email").toString());
            message.setSubject("Contraseña actualizada");
            message.setBody("Se ha actualizado una contraseña a tu cuenta<br>" + "<strong>Sitio:</strong> "
                    + password.get().getSite() + "<br><strong>Usuario:</strong> "
                    + password.get().getUsername());
            message.setUrl("http://localhost:8080/");
            message.setButtonText("Ver contraseña actualizada");
            message.setDate(LocalDateTime.now().toString());
            message.setDevice(request.getHeader("User-Agent"));
            message.setIp(request.getRemoteAddr());
            message.setLocation(request.getRemoteHost());

            emailService.sendConfirmationEmail(session.getAttribute("email").toString(), message);

        }

        return "redirect:/home";
    }
}