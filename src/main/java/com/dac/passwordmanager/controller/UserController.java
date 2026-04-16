package com.dac.passwordmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.annotation.Validated;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.dac.passwordmanager.config.security.KeyUtil;
import com.dac.passwordmanager.dto.*;
import com.dac.passwordmanager.entity.User;
import com.dac.passwordmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKey;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

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
            @Validated @ModelAttribute("signupForm") SignupRequestDTO signupRequestDTO,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            userService.saveNewUser(signupRequestDTO);
            redirectAttributes.addFlashAttribute("message", "Account created successfully");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login_controller(
            @ModelAttribute("loginForm") LoginRequestDTO loginRequestDTO,
            RedirectAttributes redirectAttributes,
            HttpSession session) throws Exception {

        try {

            User user = userService.login(loginRequestDTO);

            /*
             * Generate SecretKey
             */
            SecretKey secretKey = KeyUtil.deriveKey(loginRequestDTO.getPassword(),
                    Base64.getDecoder().decode(user.getSalt()));

            session.setAttribute("aesKey", secretKey);
            session.setAttribute("userId", user.getId());
            session.setAttribute("email", user.getEmail());

            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }

    }

    @GetMapping("/profile")
    private String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        Optional<User> user = userService.getUserById(userId);
        model.addAttribute("user", user.get());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateUserData(@Validated @ModelAttribute ChangeUserPassword changeUserPassword,
            HttpSession session, RedirectAttributes redirectAttributes, BindingResult result) {

        try {
            Long userId = (Long) session.getAttribute("userId");
            SecretKey oldSecretKey = (SecretKey) session.getAttribute("aesKey");

            SecretKey newSecretKey = userService.changePassword(changeUserPassword, userId, oldSecretKey);

            session.setAttribute("aesKey", newSecretKey);
            redirectAttributes.addFlashAttribute("message", "Password updated successfully and vault re-encrypted.");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile";
        }
    }
}