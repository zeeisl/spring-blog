package de.zeeisl.blog.transitonObjects.advertisement;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

import de.zeeisl.blog.entities.Advertisement;
import de.zeeisl.blog.entities.Tag;

public class CreateAdvertisementForm {

    @NotBlank(message = "Der Link dark nicht leer sein.")
    private String link;

    @NotBlank(message = "Es muss min. ein Tag/Thema gewählt werden.")
    private String tags;

    private String image;

    private MultipartFile imageFile;

    public CreateAdvertisementForm() {

    }

    public Advertisement toEntity() {
        Advertisement ad = new Advertisement();
        ad.setSize("320x50");
        ad.setStatus("active");
        ad.setImage(this.image);
        ad.setLink(this.link);

        String[] tags = this.tags.split(",");
        List<Tag> adTags = new ArrayList<Tag>();
        for (String tag : tags) {
            Tag tObject = new Tag();
            tObject.setName(tag.trim());
            adTags.add(tObject);
        }
        ad.setTags(adTags);

        return ad;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = Jsoup.parse(link).text();
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = Jsoup.parse(tags).text();
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getImageFile() {
        return this.imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
