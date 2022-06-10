package de.zeeisl.blog.controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.services.ArticleSearchService;

@Controller
public class SearchController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleSearchService articleSearchService; 

    @GetMapping("/search")
    String asdf(@RequestParam(name = "q", required = true) String query, Model model) {
        query = sanitizeString(query);
        
        model.addAttribute("query", query);

        List<Article> articles = articleSearchService.find(query);
        model.addAttribute("articles", articles);

        return "search/index";
    }

    private String sanitizeString(String str){
        Matcher matcher = Pattern.compile("[^a-zA-ZäöüÄÖÜß\\d\\s]").matcher(str);
        return matcher.replaceAll("");
    }
}
