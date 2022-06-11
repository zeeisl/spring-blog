package de.zeeisl.blog.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.UserRepository;

@Controller
// @RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin/users")
    String index(Model model) {
        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        return "admin/users/index";
    }
    
}