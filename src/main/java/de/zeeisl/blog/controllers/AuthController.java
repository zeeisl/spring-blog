package de.zeeisl.blog.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;
import de.zeeisl.blog.transitonObjects.user.RegisterForm;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    String login() {
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

}
