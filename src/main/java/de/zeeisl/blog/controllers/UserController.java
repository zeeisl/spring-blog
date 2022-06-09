package de.zeeisl.blog.controllers;

import java.util.*;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.repositories.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/{id}")
    String timeline(@PathVariable(name = "id", required = true) Long id, Model model){
        Optional<User> userMaybe = userRepository.findById(id);
        
        if (userMaybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden.");
        }
        
        User user = userMaybe.get();
        model.addAttribute("user", user);

        List<Article> articles = articleRepository.findPublishedArticleOfUser(user, new Date());
        model.addAttribute("articles", articles);

        return "users/timeline";
    }
}
