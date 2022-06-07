package de.zeeisl.blog.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;

@Controller
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/register")
    String register(RegisterForm registerForm) {
        return "register";
    }

    @PostMapping("/register")
    String registerStore(@Valid RegisterForm registerForm, BindingResult bindingResult, Model model) {
        if (userRepository.findByEmail(registerForm.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.registerForm",
                    "Es existiert bereits ein Konto mit dieser E-Mail-Addresse.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setUsername(registerForm.getUsername());
        user.setEmail(registerForm.getEmail());
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));
        user.setCreatedAt(new Date());
        userRepository.save(user);

        model.addAttribute("success", "Registrierung erfolgreich.");

        return "register";
    }

}
