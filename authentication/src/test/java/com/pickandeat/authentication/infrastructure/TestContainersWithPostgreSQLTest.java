package com.pickandeat.authentication.infrastructure;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestContainersWithPostgreSQLTest extends AbstractPostgresContainerTest {
    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbc;


    @BeforeAll
    void setup() {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Test
    void shouldHaveRoleTable() {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'role'", Integer.class);
        assertEquals(1, count);
    }
    
}
