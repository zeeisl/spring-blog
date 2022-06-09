package de.zeeisl.blog.services;

import java.util.List;

import de.zeeisl.blog.entities.Article;

public interface ArticleSearchService {
    public void create(Article article);
    public void createBulk(List<Article> articles);
    public List<Article> find(String query);
}
