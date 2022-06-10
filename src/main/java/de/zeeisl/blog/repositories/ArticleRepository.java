
package de.zeeisl.blog.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.zeeisl.blog.entities.Article;
import de.zeeisl.blog.entities.User;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a WHERE a.id = ?1 and a.publishDate <= ?2")
    Article findPublishedArticleById(Long id, Date date);

    @Query("SELECT a FROM Article a WHERE a.author IS ?1 and a.publishDate <= ?2 ORDER BY a.publishDate DESC")
    List<Article> findPublishedArticleOfUser(User user, Date date);

    @Query("SELECT a FROM Article a WHERE a.publishDate <= ?1 ORDER BY publishDate DESC")
    List<Article> findAllPublishedArticles(Date now);

    @Query("SELECT a FROM Article a WHERE a.id = ?1 and a.author IS ?2")
    Article findArticleByIdAndAuthor(Long id, User user);
}
