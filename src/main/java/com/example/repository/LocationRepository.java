package com.example.repository;

import com.example.model.Location;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    public static final int SEARCH_RANGE = 83000;

    @Query(
            value =
                    "SELECT * FROM location\n"
                            + "WHERE ST_DistanceSphere((geo,\n"
                            + "    (SELECT geo FROM location WHERE name = :location)\n"
                            + ")) < COALESCE(:searchRange, :SEARCH_RANGE);",
            nativeQuery = true)
    List<Location> getNearByLocations(@Param("searchRange") long searchRange, String location);
}
