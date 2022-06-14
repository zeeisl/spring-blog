package de.zeeisl.blog.transitonObjects.article;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.jsoup.Jsoup;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import de.zeeisl.blog.entities.Article;

public class EditArticleForm {

    private Long id;

    @NotNull
    @Length(min = 5, max = 250, message = "Der Titel muss zwischen 5 und 250 Zeichen lang sein")
    private String title;

    private String bannerLink;
    private MultipartFile banner;

    @NotNull
    @Length(min = 20, max = 250, message = "Der Teaser muss zwischen 20 und 250 Zeichen lang sein")
    private String teaser;

    @NotBlank(message = "Der Text darf nicht leer sein")
    private String text;

    @Future(message = "Veröffentlichungszeitpunkt muss in der Zukunft liegen")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date publishDate;

    public EditArticleForm() {

    }

    public void fromEntity(Article article) {
        this.setId(article.getId());
        this.setTitle(article.getTitle());
        this.setBannerLink(article.getBanner());
        this.setTeaser(article.getTeaser());
        this.setText(article.getText());
        this.setPublishDate(article.getPublishDate());
    }

    public Article updateEntity(Article article) {
        article.setTitle(this.getTitle());
        article.setBanner(this.getBannerLink());
        article.setTeaser(this.getTeaser());
        article.setText(this.getText());

        return article;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeaser() {
        return this.teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public MultipartFile getBanner() {
        return this.banner;
    }

    public void setBanner(MultipartFile banner) {
        this.banner = banner;
    }

    public String getBannerLink() {
        return this.bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    @Override
    public String toString() {
        return "{" +
                " title='" + getTitle() + "'" +
                ", bannerLink='" + getBannerLink() + "'" +
                ", banner='" + getBanner() + "'" +
                ", teaser='" + getTeaser() + "'" +
                ", text='" + getText() + "'" +
                ", publishDate='" + getPublishDate() + "'" +
                "}";
    }

}
