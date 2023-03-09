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
    public static final long DEFAULT_SEARCH_RANGE = 83000 * 6;

    @Query(
            value =
                    "SELECT * FROM location\n"
                            + "WHERE ST_DistanceSphere(\n"
                            + "    geo,\n"
                            + "    ST_MakePoint(:longitude, :latitude)\n"
                            + ") < COALESCE(:searchRange, :defaultSearchRange)\n"
                            + "LIMIT 10",
            nativeQuery = true)
    List<Location> getNearByLocations(
            @Param("searchRange") Long searchRange,
            @Param("defaultSearchRange") Long defaultSearchRange,
            @Param("longitude") Double longitude,
            @Param("latitude") Double latitude);
}
