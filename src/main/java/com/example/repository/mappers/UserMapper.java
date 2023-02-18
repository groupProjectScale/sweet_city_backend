package com.example.repository.mappers;

import com.example.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return User.Builder.newBuilder()
                .withUserId(rs.getObject("user_id", java.util.UUID.class))
                .withUserName(rs.getString("username"))
                .withFirstName(rs.getString("firstname"))
                .withLastName(rs.getString("lastname"))
                .withEmail(rs.getString("email"))
                .withHashPasswordWithSalt(rs.getString("password"))
                .build();
    }
}
