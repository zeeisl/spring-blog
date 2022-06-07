
package de.zeeisl.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeeisl.blog.entities.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
