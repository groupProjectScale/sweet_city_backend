package com.example.repository;

import com.example.model.Location;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query(
            value =
                    "SELECT * FROM location\n"
                            + "WHERE ST_DistanceSphere((geo,\n"
                            + "    (SELECT geo FROM location WHERE name = ?1)\n"
                            + ")) < 83000;",
            nativeQuery = true)
    List<Location> getNearByLocations(String location);
}
