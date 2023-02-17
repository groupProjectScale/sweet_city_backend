package com.example.repository.mappers;


import com.example.model.Activity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ActivityMapper implements RowMapper<Activity> {
    @Override
    public Activity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Activity.Builder.newBuilder()
                .withActivityId(rs.getObject("activity_id", java.util.UUID.class))
                .withName(rs.getString("name"))
                .withUserId(rs.getObject("user_id", java.util.UUID.class))
                .withStartTime(rs.getTimestamp("start_time").getTime())
                .withEndTime(rs.getTimestamp("end_time").getTime())
                .withLocationId(rs.getObject("location_id", java.util.UUID.class))
                .withPrice(rs.getDouble("price"))
                .withCurrentParticipants(rs.getInt("current_participants"))
                .withMaximumParticipants(rs.getInt("maximum_participants"))
                .withMinimumParticipants(rs.getInt("minimum_participants"))
                .build();
    }
}
