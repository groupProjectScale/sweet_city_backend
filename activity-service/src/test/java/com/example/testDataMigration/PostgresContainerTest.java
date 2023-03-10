package com.example.testDataMigration;

import com.example.BaseTest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class PostgresContainerTest extends BaseTest {

    @Autowired private Environment environment;

    @Test
    public void testDatabase() throws SQLException {
        DataSource dataSource = getDataSource(environment);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet =
                statement.executeQuery(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema ="
                                + " 'public'");
        while (resultSet.next()) {
            String tableName = resultSet.getString("table_name");
            System.out.println("Table name: " + tableName);

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    String value = resultSet.getString(i);
                    System.out.println(columnName + ": " + value);
                }
                System.out.println("----------------------");
            }
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
