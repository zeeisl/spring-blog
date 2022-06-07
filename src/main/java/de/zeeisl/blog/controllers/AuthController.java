package de.zeeisl.blog.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    String register() {
        return "register";
    }

    @PostMapping("/register")
    String registerStore(@RequestBody MultiValueMap<String, String> formData, Model model) {
        List<String> errors = new ArrayList<String>();

        String email = "";
        String username = "";
        String rawPassword = "";

        if (formData.containsKey("username")) {
            username = formData.getFirst("username");
            model.addAttribute("username", username);
            if (username.length() < 3 || username.length() > 60) {
                errors.add("Name muss zwischen 3 und 60 zeichen lang sein.");
            }
        } else {
            errors.add("Name muss zwischen 3 und 60 zeichen lang sein.");
        }

        if (formData.containsKey("email")) {
            email = formData.getFirst("email");
            model.addAttribute("email", email);
            if (email.length() < 5) {
                errors.add("Die E-Mail-Addresse ist ungültig.");
            }
            User user = userRepository.findByEmail(email);
            if(user != null){
                errors.add("Es existiert bereits ein Konto mit dieser E-Mail-Addresse.");
            }
        } else {
            errors.add("Die E-Mail-Addresse ist ungültig.");
        }

        if (!formData.containsKey("password") || formData.getFirst("password").length() < 8
                || formData.getFirst("password").length() > 60) {
            errors.add("Das Passwort muss zwischen 8 und 60 zeichen lang sein.");
        }
        rawPassword = formData.getFirst("password");

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCreatedAt(new Date());
        userRepository.save(user);

        model.addAttribute("success", "Registrierung erfolgreich.");

        return "register";
    }

    public class RegisterForm {
        @Length(min = 3, max = 60, message = "Name muss zwischen 3 und 60 zeichen lang sein.")
        public String name;

        @Email(message = "Die E-Mail-Addresse ist ungültig.")
        public String email;

        @Length(min = 8, max = 60, message = "Das Passwort muss zwischen 8 und 60 zeichen lang sein.")
        public String password;

        public RegisterForm() {

        }
    }
}
