package com.travelbeeee.study.persistence;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import lombok.extern.log4j.Log4j;

@Log4j
public class JDBCTests {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConnection() {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String userid = "travelbeeee";
		String userpw = "1234";
		
		try (Connection con = DriverManager.getConnection(url, userid, userpw)){
			log.info(con);
		}catch(Exception e) {
			fail(e.getMessage());
		}
	}
}
