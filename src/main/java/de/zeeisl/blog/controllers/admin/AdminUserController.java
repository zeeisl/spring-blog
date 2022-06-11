package de.zeeisl.blog.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/admin/loginAs/{id}")
    String loginAsUserTest(@PathVariable(name = "id", required = true) Long id) {
        User user = userRepository.findById(id).get();

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList(user.getUserRole()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/users/" + user.getId();
    }

}