package de.zeeisl.blog.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    String show(@PathVariable(name = "id", required = true) Long id, Model model) {
        Article article = articleRepository.findPublishedArticleById(id, new Date());

        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artikel nicht gefunden.");
        }

        model.addAttribute("article", article);
        return "articles/show";
    }

    @GetMapping("/create")
    String create(CreateArticleForm createArticleForm) {
        return "articles/create";
    }

    @PostMapping("/")
    String store(@Valid CreateArticleForm createArticleForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (createArticleForm.getReleaseType() == 2 && createArticleForm.getPublishDate() == null) {
            bindingResult.rejectValue("publishDate", "error.createArticleForm",
                    "Bei einer späteren Veröffentlichung muss ein Veröffentlichungszeitpunkt in der Zukunft gewählt werden.");
        }

        // move banner image
        if (createArticleForm.getBanner() != null && !createArticleForm.getBanner().isEmpty()) {
            String path = storageService.store(createArticleForm.getBanner());
            createArticleForm.setBannerLink(path);
        }

        if (bindingResult.hasErrors()) {
            return "articles/create";
        }

        Article article = createArticleForm.toEntity();
        articleRepository.save(article);

        redirectAttributes.addFlashAttribute("success", "Artikel erfolgreich gespeichert.");
        return String.format("redirect:/articles/%d", article.getId());
    }
}
