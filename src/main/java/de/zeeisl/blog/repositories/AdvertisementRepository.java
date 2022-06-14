package de.zeeisl.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zeeisl.blog.entities.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAllByStatus(String status);
}
