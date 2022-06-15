package de.zeeisl.blog.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import de.zeeisl.blog.entities.Page;
import de.zeeisl.blog.repositories.PageRepository;

@Controller
@RequestMapping("/pages")
public class PageController {

    @Autowired
    PageRepository pageRepository;

    @GetMapping("/{link}")
    String show(@PathVariable(name = "link", required = true) String link, Model model) {
        Optional<Page> page = pageRepository.findByLink(link);

        if (page.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Seite nicht gefunden.");
        }

        model.addAttribute("page", page.get());
        return "pages/show";
    }

}
