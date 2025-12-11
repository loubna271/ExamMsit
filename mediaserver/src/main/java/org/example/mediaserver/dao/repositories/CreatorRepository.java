package org.example.mediaserver.dao.repositories;

import org.example.mediaserver.dao.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, String> {
    @Query("SELECT c FROM Creator c LEFT JOIN FETCH c.videos WHERE c.id = :id")
    Optional<Creator> findByIdWithVideos(@Param("id") String id);
}
