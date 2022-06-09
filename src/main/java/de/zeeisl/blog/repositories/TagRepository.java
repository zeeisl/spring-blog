
package de.zeeisl.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeeisl.blog.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
