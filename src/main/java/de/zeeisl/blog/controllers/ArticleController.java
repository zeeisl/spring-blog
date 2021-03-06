package de.zeeisl.blog.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import de.zeeisl.blog.auth.AuthContainer;
import de.zeeisl.blog.entities.Advertisement;
import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.Tag;
import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.AdvertisementRepository;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.repositories.TagRepository;
import de.zeeisl.blog.services.ArticleSearchService;
import de.zeeisl.blog.services.StorageService;
import de.zeeisl.blog.transitonObjects.article.CreateArticleForm;
import de.zeeisl.blog.transitonObjects.article.EditArticleForm;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleSearchService articleSearchService;

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    AuthContainer authContainer;

    @GetMapping("/{id}")
    String show(@PathVariable(name = "id", required = true) Long id, Model model) {
        Article article = articleRepository.findPublishedArticleById(id, new Date());

        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artikel nicht gefunden.");
        }

        // replace tags with search-links
        String textWithTagLinks = replaceTagWithLinks(article.getText());
        article.setText(textWithTagLinks);
        model.addAttribute("article", article);

        // get similar articles by tags
        String tagsAsConcatString = String.join(" ", article.getTags().stream().map(t -> t.getName()).toList());
        List<Article> similarArticles = articleSearchService.find(tagsAsConcatString).stream()
                .filter(a -> !a.getId().equals(id))
                .toList();
        model.addAttribute("similarArticles", similarArticles);

        // get random active ad
        List<Advertisement> ads = advertisementRepository.findAllByStatus("active");
        if (ads.size() > 0) {
            Advertisement ad = ads.get(new Random().nextInt(0, ads.size() - 1));
            model.addAttribute("ad", ad);
        }

        return "articles/show";
    }

    private String replaceTagWithLinks(String text) {
        Pattern tagPattern = Pattern.compile("(#)(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = tagPattern.matcher(text);
        return matcher.replaceAll("<a class='btn btn-primary py-0 px-1 btn-sm' href='/search?q=$2'>#$2</a>");
    }

    @GetMapping("/create")
    String create(CreateArticleForm createArticleForm) {
        return "articles/create";
    }

    @PostMapping("/create")
    String store(@Valid CreateArticleForm createArticleForm, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (createArticleForm.getReleaseType() == 2 && createArticleForm.getPublishDate() == null) {
            bindingResult.rejectValue("publishDate", "error.createArticleForm",
                    "Bei einer sp??teren Ver??ffentlichung muss ein Ver??ffentlichungszeitpunkt in der Zukunft gew??hlt werden.");
        }

        // move banner image
        if (createArticleForm.getBanner() != null && !createArticleForm.getBanner().isEmpty()) {
            String path = storageService.store(createArticleForm.getBanner());
            createArticleForm.setBannerLink(path);
        }

        if (bindingResult.hasErrors()) {
            return "articles/create";
        }

        User user = authContainer.getLoggedInUser();
        Article article = createArticleForm.toEntity(user);
        articleRepository.save(article);
        createTagsFromText(article);
        articleRepository.save(article);

        articleSearchService.create(article);

        redirectAttributes.addFlashAttribute("success", "Artikel erfolgreich gespeichert.");
        return String.format("redirect:/articles/%d", article.getId());
    }

    private void createTagsFromText(Article article) {
        Pattern tagPattern = Pattern.compile("#(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = tagPattern.matcher(article.getText());

        List<Tag> tags = new ArrayList<Tag>();
        while (matcher.find()) {
            Tag t = new Tag();
            t.setName(matcher.group(1));
            tagRepository.save(t);

            tags.add(t);
        }
        article.setTags(tags);
    }

    @GetMapping("/{id}/edit")
    String edit(@PathVariable(name = "id", required = true) Long id, EditArticleForm editArticleForm) {
        User user = authContainer.getLoggedInUser();

        Article article = articleRepository.findArticleByIdAndAuthor(id, user);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artikel nicht gefunden.");
        }

        editArticleForm.fromEntity(article);

        return "articles/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable(name = "id", required = true) Long id, EditArticleForm editArticleForm,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "articles/edit";
        }

        User user = authContainer.getLoggedInUser();
        Article article = articleRepository.findArticleByIdAndAuthor(id, user);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artikel nicht gefunden.");
        }

        editArticleForm.updateEntity(article);
        articleRepository.save(article);
        articleSearchService.update(article);

        model.addAttribute("success", "Artikel erfolgreich gespeichert.");

        return "articles/edit";
    }
}
