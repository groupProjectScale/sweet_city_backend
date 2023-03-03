package com.example.repository;

import com.example.model.Tag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @Query(value = "SELECT num_of_creations FROM tag WHERE tag_id = ?1", nativeQuery = true)
    int getNumberOfCreationsForTag(String tagId);
}
