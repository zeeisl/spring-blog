package de.zeeisl.blog.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

@Entity
@Table(name = "pages")
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String link;

    @NotBlank
    private String title;

    @Lob
    private String text;

    public Page() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = Jsoup.parse(link).text();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = Jsoup.parse(title).text();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        Safelist list = new Safelist();
        list.addTags("p", "b", "i", "a", "h2", "h3", "br");
        list.addAttributes("a", "href");

        Cleaner c = new Cleaner(list);

        this.text = c.clean(Jsoup.parse(text)).body().html();
    }

}
