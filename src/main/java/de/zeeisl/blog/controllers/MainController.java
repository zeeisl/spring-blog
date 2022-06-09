package de.zeeisl.blog.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.repositories.ArticleRepository;

@Controller
public class MainController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/")
    String home(Model model) {
        List<Article> articles = articleRepository.findAllPublishedArticles(new Date());
        
        model.addAttribute("articles", articles);

        return "articles/index";
    }

    // @GetMapping("/article")
    // String article() {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String currentPrincipalName = authentication.getName();

    //     System.out.println(String.format("AUTH %s", currentPrincipalName));

    //     return "article";
    // }

}
