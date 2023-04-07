package com.example.repository;

import com.example.model.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Image repository. */
@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {}
