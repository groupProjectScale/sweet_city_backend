package com.example.repository;

import com.example.model.Tag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    @Query(value = "SELECT * FROM tag WHERE tag_description = ?1", nativeQuery = true)
    Tag findByTagDescription(String tagDescription);

    @Modifying
    @Query("update Tag t set t.numOfCreations = t.numOfCreations + 1 where t.tagId =?1")
    void addOneCreation(UUID tagId);
}
