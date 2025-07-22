package com.novelbot.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class ApiApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("스프링 컨텍스트가 정상적으로 로드된다.")
	void contextLoads() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
	}

	@Test
	@DisplayName("local 프로필의 데이터베이스에 연결할 수 있다.")
	void databaseConnectionTest() throws SQLException {
		assertThat(dataSource).isNotNull();
		try (Connection conn = dataSource.getConnection()) {
			assertThat(conn).isNotNull();
			System.out.println("Successfully connected to database: " + conn.getMetaData().getURL());
		}
	}
}
