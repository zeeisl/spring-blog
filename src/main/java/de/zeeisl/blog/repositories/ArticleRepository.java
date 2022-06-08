
package de.zeeisl.blog.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.zeeisl.blog.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.id = ?1 and a.publishDate <= ?2")
    Article findPublishedArticleById(Long id, Date date);
}
