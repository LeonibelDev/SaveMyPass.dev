package com.dac.passwordmanager.controller;

import java.io.File;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.dac.passwordmanager.dto.PasswordCredentialRequestDTO;
import com.dac.passwordmanager.dto.response.CredentialsDTO;
import com.dac.passwordmanager.service.PasswordCredentialService;
import com.dac.passwordmanager.service.UserService;
import com.dac.passwordmanager.service.email.EmailService;
import com.dac.passwordmanager.service.pdf.CreatePdfFile;
import com.dac.passwordmanager.config.security.AesCipherConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.dac.passwordmanager.entity.MessageEntity;
import com.dac.passwordmanager.entity.PasswordCredential;
import com.dac.passwordmanager.entity.User;

@RequiredArgsConstructor
@Controller
public class DashboardController {

    private final PasswordCredentialService passwordCredentialService;
    private final UserService userService;
    private final AesCipherConfig aesCipherConfig;
    private final EmailService emailService;
    private final CreatePdfFile createPdfFile;

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws Exception {
        Long userId = (Long) session.getAttribute("userId");
        SecretKey secretKey = (SecretKey) session.getAttribute("aesKey");

        List<CredentialsDTO> passwords = passwordCredentialService.findByUser(userId, secretKey);

        model.addAttribute("passwords", passwords);

        Map<String, String> user = new HashMap<>();
        User currentUser = userService.getUserById(userId).orElseThrow();
        user.put("name", currentUser.getNombreCompleto());
        user.put("email", currentUser.getEmail());

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
        newPassword.setUser(userService.getUserById((Long) session.getAttribute("userId")).get());

        passwordCredentialService.save(newPassword);

        /*
         * Send email confirmation
         */
        MessageEntity message = new MessageEntity();
        message.setRecipient(session.getAttribute("email").toString());
        message.setSubject("New password added");
        message.setBody("A new password has been added to your account.<br>" + "<strong>Site:</strong> "
                + passwordCredential.getSite() + "<br><strong>Username:</strong> "
                + passwordCredential.getUsername());
        message.setUrl("http://localhost:8080/");
        message.setButtonText("Go to Dashboard");
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
        Long ownerId = password.get().getUser().getId();
        Long sessionUserId = (Long) session.getAttribute("userId");

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
        Long ownerId = password.get().getUser().getId();
        Long sessionUserId = (Long) session.getAttribute("userId");

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
            message.setSubject("Password updated");
            message.setBody("A password in your account has been updated.<br>" + "<strong>Site:</strong> "
                    + password.get().getSite() + "<br><strong>Username:</strong> "
                    + password.get().getUsername());
            message.setUrl("http://localhost:8080/");
            message.setButtonText("View updated password");
            message.setDate(LocalDateTime.now().toString());
            message.setDevice(request.getHeader("User-Agent"));
            message.setIp(request.getRemoteAddr());
            message.setLocation(request.getRemoteHost());

            emailService.sendConfirmationEmail(session.getAttribute("email").toString(), message);

        }

        return "redirect:/home";
    }

    @GetMapping("/export")
    public void export(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setHeader("Content-Type", "application/pdf");

        SecretKey secretKey = (SecretKey) session.getAttribute("aesKey");
        Long userId = (Long) session.getAttribute("userId");

        List<CredentialsDTO> passwords = passwordCredentialService
                .findByUser(userId, secretKey);

        /*
         * Generate a temp file
         */
        User user = userService.getUserById((Long) session.getAttribute("userId")).get();
        File tempFile = createPdfFile.createFile(passwords, user.getEmail(), user.getId().toString());

        /*
         * Send email confirmation
         */
        MessageEntity message = new MessageEntity();
        message.setRecipient(session.getAttribute("email").toString());
        message.setSubject("Passwords exported | SaveMyPass.dev");
        message.setBody(
                "Your passwords have been exported. <br> for decrypt the file you need to confirm your email in the file");
        message.setUrl("http://localhost:8080/");
        message.setFile(tempFile);
        message.setButtonText("Go to SaveMyPass.dev");
        message.setDate(LocalDateTime.now().toString());
        message.setDevice(request.getHeader("User-Agent"));
        message.setIp(request.getRemoteAddr());
        message.setLocation(request.getRemoteHost());

        emailService.sendConfirmationEmail(session.getAttribute("email").toString(), message);
    }
}