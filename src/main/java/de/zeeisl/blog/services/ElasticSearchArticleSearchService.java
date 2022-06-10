package de.zeeisl.blog.services;

import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.repositories.ArticleRepository;

@Service
public class ElasticSearchArticleSearchService implements ArticleSearchService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ArticleRepository articleRepository;

    public static final String ES_URL = "http://127.0.0.1:9200";

    @Override
    public void create(Article article) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = ES_URL + "/articles/_doc/" + article.getId();
        String body = """
                {
                    "id": "%d",
                    "author_id": "%d",
                    "banner": "%s",
                    "title": "%s",
                    "teaser": "%s",
                    "text": "%s",
                    "create_at": "%s",
                    "publish_date": "%s",
                    "tags": "%s"
                }
                """.formatted(article.getId(), article.getAuthor().getId(), article.getBanner(), article.getTitle(),
                article.getTeaser(),
                article.getText(),
                article.getCreateAt(), article.getPublishDate(),
                String.join(", ", article.getTags().stream().map(t -> t.getName()).toList()));

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        restTemplate.postForObject(
                url, request, JsonNode.class);
    }

    @Override
    public void putBulk(List<Article> articles){
        for(Article a: articles){
            update(a);
        }
    }

    @Override
    public void update(Article article) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = ES_URL + "/articles/_doc/" + article.getId();
        String body = """
                {
                    "id": "%d",
                    "author_id": "%d",
                    "banner": "%s",
                    "title": "%s",
                    "teaser": "%s",
                    "text": "%s",
                    "create_at": "%s",
                    "publish_date": "%s",
                    "tags": "%s"
                }
                """.formatted(article.getId(), article.getAuthor().getId(), article.getBanner(), article.getTitle(),
                article.getTeaser(),
                article.getText(),
                article.getCreateAt(), article.getPublishDate(),
                String.join(", ", article.getTags().stream().map(t -> t.getName()).toList()));

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        restTemplate.put(url, request);
    }

    @Override
    public List<Article> find(String query) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = ES_URL + "/articles/_search";
        String body = """
                {
                    "query": {
                        "query_string": {
                            "query": "%s",
                            "fields": ["title", "teaser", "text", "tags"]
                        }
                    },
                    "fields": ["id"],
                    "_source": false
                }
                """.formatted(query);

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        JsonNode response = restTemplate.postForObject(
                url, request, JsonNode.class);

        List<Long> articleIds = new ArrayList<Long>();
        response.get("hits").get("hits").forEach((a) -> {
            articleIds.add(a.get("fields").get("id").get(0).asLong());
        });
        List<Article> articles = articleRepository.findAllById(articleIds);
        Date date = new Date();
        articles = articles.stream().filter(a -> a.getPublishDate().compareTo(date) <= 0).toList();

        return articles;
    }

}
