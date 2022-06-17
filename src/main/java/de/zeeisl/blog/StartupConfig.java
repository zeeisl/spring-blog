package de.zeeisl.blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.github.javafaker.Faker;

import de.zeeisl.blog.entities.Advertisement;
import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.Tag;
import de.zeeisl.blog.entities.User;
import de.zeeisl.blog.repositories.AdvertisementRepository;
import de.zeeisl.blog.repositories.ArticleRepository;
import de.zeeisl.blog.repositories.TagRepository;
import de.zeeisl.blog.repositories.UserRepository;
import de.zeeisl.blog.services.ArticleSearchService;
import de.zeeisl.blog.services.StorageService;

@Configuration
public class StartupConfig {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    AdvertisementRepository advertisementRepository;

    @Autowired
    ArticleSearchService articleSearchService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StorageService storageService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    String hibernate_ddl_auto_mode;

    @Bean
    void seedData() {
        if(hibernate_ddl_auto_mode.equals("update")){
            System.out.println("SKIPPING SEEDDATA");
            return;
        }

        if(hibernate_ddl_auto_mode.equals("create") && articleRepository.count() > 0){
            System.out.println("SKIPPING SEEDDATA");
            return;
        }

        System.out.println("START SEEDDATA");

        Faker faker = new Faker(new Locale("de"));

        // clean up es-index
        String url = articleSearchService.getUrl() + "/articles";
        // restTemplate.delete(url);
        restTemplate.put(url, null);

        // tags
        List<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < 20; i++) {
            Tag tag = new Tag();
            tag.setName(faker.lorem().word());
            tagRepository.save(tag);
            tags.add(tag);
        }

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername(faker.name().fullName());
            user.setEmail(faker.name().firstName() + "@example.de");
            user.setPassword(passwordEncoder.encode("password"));
            user.setProfilePicture("https://picsum.photos/id/" + faker.number().numberBetween(1, 100) + "/200");
            user.setUserRole(i == 0 ? "ADMIN" : "USER");
            user.setEnabled(true);
            user.setCreatedAt(new Date());
            userRepository.save(user);

            for (int j = 0; j < 10; j++) {
                Article article = new Article();
                article.setTitle(faker.lorem().sentence(5));
                article.setBanner("https://picsum.photos/id/" + faker.number().numberBetween(100, 500) + "/800/600");
                article.setTeaser(faker.lorem().sentence(20));
                article.setText(faker.lorem().paragraph(10));
                article.setCreateAt(new Date());
                article.setPublishDate(article.getCreateAt());

                // add random subset of tags
                if (faker.bool().bool()) {
                    int tagCount = faker.number().numberBetween(1, 5);
                    Collections.shuffle(tags);

                    List<Tag> articleTags = new ArrayList<Tag>(tags.subList(0, tagCount));
                    article.setTags(articleTags);

                    String tagStr = String.join(" ", articleTags.stream().map(t -> "#" + t.getName()).toList());
                    article.setText(article.getText() + "<br />" + tagStr);
                }

                article.setAuthor(user);
                articleRepository.save(article);
            }
        }

        articleSearchService.putBulk(articleRepository.findAll());

        // ads
        for (int i = 0; i < 10; i++) {

            Advertisement ad = new Advertisement();
            ad.setSize("320x50");
            ad.setImage("https://picsum.photos/id/%d/320/50".formatted(faker.number().numberBetween(500, 600)));
            ad.setLink("https://www.google.de/");
            ad.setStatus("active");

            // add random subset of tags
            if (faker.bool().bool()) {
                int tagCount = faker.number().numberBetween(1, 5);
                Collections.shuffle(tags);

                List<Tag> articleTags = new ArrayList<Tag>(tags.subList(0, tagCount));
                ad.setTags(articleTags);
            }
            advertisementRepository.save(ad);

        }
        System.out.println("END SEEDDATA");
    }
}
