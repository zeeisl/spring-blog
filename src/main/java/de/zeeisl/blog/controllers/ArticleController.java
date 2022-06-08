package de.zeeisl.blog.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.services.StorageService;
import de.zeeisl.blog.transitonObjects.article.CreateArticleForm;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    StorageService storageService;

    @GetMapping("/{id}")
    String show() {
        return "articles/show";
    }

    @GetMapping("/create")
    String create(CreateArticleForm createArticleForm) {
        return "articles/create";
    }

    @PostMapping("/")
    String store(@Valid CreateArticleForm createArticleForm, BindingResult bindingResult, Model model){
        
        if(createArticleForm.getReleaseType() == 2 && createArticleForm.getPublishDate() == null){
            bindingResult.rejectValue("publishDate", "error.createArticleForm", "Bei einer späteren Veröffentlichung muss ein Veröffentlichungszeitpunkt in der Zukunft gewählt werden.");
        }

        // move banner image
        if(createArticleForm.getBanner() != null && !createArticleForm.getBanner().isEmpty()){
            String path = storageService.store(createArticleForm.getBanner());
            createArticleForm.setBannerLink(path);
        }

        if(bindingResult.hasErrors()){
            return "articles/create";
        }

        Article article = createArticleForm.toEntity();
        articleRepository.save(article);

        model.addAttribute("success", "Artikel erfolgreich gespeichert.");
        return "articles/create";
    }
}
