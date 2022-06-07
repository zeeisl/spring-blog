package de.zeeisl.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeeisl.blog.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
