package de.zeeisl.blog.controllers;

import java.util.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.repositories.UserRepository;
import de.zeeisl.blog.services.StorageService;
import de.zeeisl.blog.transitonObjects.user.EditUserdataForm;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;
    
    @Autowired
    StorageService storageService;

    @GetMapping("/{id}")
    String timeline(@PathVariable(name = "id", required = true) Long id, Model model) {
        Optional<User> userMaybe = userRepository.findById(id);

        if (userMaybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden.");
        }

        User user = userMaybe.get();
        model.addAttribute("user", user);
        model.addAttribute("type", "timeline");

        List<Article> articles = articleRepository.findPublishedArticleOfUser(user, new Date());
        model.addAttribute("articles", articles);

        return "users/timeline";
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable(name = "id", required = true) Long id, EditUserdataForm editUserdataForm) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Optional<User> userMaybe = userRepository.findById(id);
        if (userMaybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden.");
        }

        User user = userMaybe.get();

        editUserdataForm.fromEntity(user);

        return "users/edit";
    }

    @PostMapping("/{id}/edit")
    String store(@Valid EditUserdataForm editUserdataForm, BindingResult bindingResult, Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getErrorCount());
            return "users/edit";
        }

        Optional<User> userMaybe = userRepository.findById(authUser.getId());
        if (userMaybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden.");
        }

        User user = userMaybe.get();

        // move banner image
        if (editUserdataForm.getProfilePicture() != null && !editUserdataForm.getProfilePicture().isEmpty()) {
            String path = storageService.store(editUserdataForm.getProfilePicture());
            editUserdataForm.setProfilePictureLink(path);
        }

        editUserdataForm.updateEntity(user);
        userRepository.save(user);

        model.addAttribute("success", "Benutzerdaten erfolgreich gespeichert.");

        return "users/edit";
        // return String.format("redirect:/users/%d", user.getId());
    }
}
