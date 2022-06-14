package de.zeeisl.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeeisl.blog.entities.Page;

public interface PageRepository extends JpaRepository<Page, Long> {
    Optional<Page> findByLink(String link);
}
