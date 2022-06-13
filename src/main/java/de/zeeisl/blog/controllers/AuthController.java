package de.zeeisl.blog.controllers;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;
import de.zeeisl.blog.transitonObjects.user.RegisterForm;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/login")
    String login(Model model) {
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)){
            User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "redirect:/users/%d".formatted(authUser.getId());
        }
        return "auth/login";
    }

    @GetMapping("/register")
    String register(RegisterForm registerForm) {
        return "auth/register";
    }

    @PostMapping("/register")
    String registerStore(@Valid RegisterForm registerForm, BindingResult bindingResult, Model model) {
        if (userRepository.findByEmail(registerForm.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.registerForm",
                    "Es existiert bereits ein Konto mit dieser E-Mail-Addresse.");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setCreatedAt(new Date());
        userRepository.save(user);

        model.addAttribute("success", "Registrierung erfolgreich.");

        return "auth/register";
    }

    @GetMapping("/auth/resetpassword")
    String forgotPasswordFormPage(@RequestParam(required = false, name = "title", defaultValue = "Passwort Vergessen") String title, Model model) {
        model.addAttribute("title", title);
        return "auth/forgotpassword";
    }

    @PostMapping("/auth/resetpassword")
    String forgotPasswordPage(@RequestParam(name = "email", required = true) String email, Model model, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String passwordResetToken = RandomStringUtils.randomAlphanumeric(32);
            user.setPasswordResetHash(passwordResetToken);
            user.setEnabled(false);
            userRepository.save(user);

            try {
                request.logout();
            } catch (ServletException e) {

            }

            sendPasswordResetEmail(user);
        }

        model.addAttribute("success", "Eine E-Mail mit dem weiteren Vorgehen wurde an dich gesendet.");

        return "auth/forgotpassword";
    }

    public void sendPasswordResetEmail(User user) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("noreply@zeeshan-islam.de");
        mail.setTo(user.getEmail());
        mail.setSubject("blog: Passwort neu setzen");

        String message = """
                Hallo %s,

                besuche den folgenden Link um dein Passwort neu zu setzen:
                http://127.0.0.1:8080/auth/resetpassword/%s

                Schöne Grüße

                blog-team
                """.formatted(user.getUsername(), user.getPasswordResetHash());
        mail.setText(message);

        javaMailSender.send(mail);
    }

    @GetMapping("/auth/resetpassword/{token}")
    String resetPasswordPage(@PathVariable(required = true, name = "token") String token, Model model) {
        model.addAttribute("token", token);
        return "auth/resetpassword";
    }

    @PostMapping("/auth/resetpassword/{token}")
    String resetPasswordPage(@PathVariable(name = "token", required = true) String token,
            @RequestParam(required = true, name = "password1") String password1,
            @RequestParam(required = true, name = "password2") String password2, Model model) {
        
        if (!password1.equals(password2)) {
            model.addAttribute("error", "Passwörter stimmen nicht überein.");
            return "auth/resetpassword";
        }

        User user = userRepository.findByPasswordResetHash(token);
        if(user == null){
            model.addAttribute("error", "Ein Fehler ist aufgetreten.");
            return "auth/resetpassword";
        }

        user.setPasswordResetHash(null);
        user.setPassword(passwordEncoder.encode(password1));
        user.setEnabled(true);
        userRepository.save(user);

        model.addAttribute("success", "Neues Passwort erfolgreich gespeichert. Du kannst Dich jetzt einloggen.");

        return "auth/resetpassword";
    }
}
