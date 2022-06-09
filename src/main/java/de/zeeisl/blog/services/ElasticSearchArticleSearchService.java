package de.zeeisl.blog.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import de.zeeisl.blog.entities.Article;

@Service
public class ElasticSearchArticleSearchService implements ArticleSearchService {

    @Autowired
    RestTemplate restTemplate;

    private final String ES_URL = "http://127.0.0.1:9200";

    @Override
    public void create(Article article) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = ES_URL + "/articles/_doc/";
        String body = """
                {
                    "id": "%d",
                    "author_id": "%d",
                    "banner": "%s",
                    "teaser": "%s",
                    "text": "%s",
                    "create_at": "%s",
                    "publish_date": "%s",
                    "tags": "%s"
                }
                """.formatted(article.getId(), article.getAuthor().getId(), article.getBanner(), article.getTeaser(),
                article.getText(),
                article.getCreateAt(), article.getPublishDate(),
                String.join(", ", article.getTags().stream().map(t -> t.getName()).toList()));

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        JsonNode response = restTemplate.postForObject(
                url, request, JsonNode.class);

        System.out.println(response);
    }

    @Override
    public void createBulk(List<Article> articles) {
        for (Article a : articles) {
            this.create(a);
        }
    }

    @Override
    public List<Article> find(String query) {
        // TODO Auto-generated method stub
        return null;
    }

}
