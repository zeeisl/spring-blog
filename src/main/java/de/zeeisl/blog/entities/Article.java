package de.zeeisl.blog.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String title;

    private String banner;

    @NotNull
    private String teaser;

    @NotBlank
    @Lob
    private String text;

    @NotNull
    private Date publishDate;

    @NotNull
    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    public Article() {

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
        this.title = Jsoup.parse(title).text();
    }

    public String getBanner() {
        return this.banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getTeaser() {
        return this.teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = Jsoup.parse(teaser).text();
    }

    public String getText() {
        return this.text;
    }

    public String getTextAsPlainText() {
        return Jsoup.parse(this.text).text();
    }

    public void setText(String text) {
        Safelist list = new Safelist();
        list.addTags("p", "b", "i", "a", "h2", "h3", "br");
        list.addAttributes("a", "href");

        Cleaner c = new Cleaner(list);

        this.text = c.clean(Jsoup.parse(text)).body().html();
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
