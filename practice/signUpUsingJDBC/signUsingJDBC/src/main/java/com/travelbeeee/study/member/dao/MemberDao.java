package com.travelbeeee.study.member.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.travelbeeee.study.member.Member;

@Repository
public class MemberDao implements IMemberDao {
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "travelbeeee";
	private String userpw = "1234";
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	@Override	
	public int insertMember(Member member) {		
		int result = 0;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			String sql = "INSERT INTO member (id, username, email, pwd) values (member_seq.nextval, ?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPwd());
			result = pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public int updateMember(Member member) {
		int result = 0;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			System.out.println("Member 상태 : " + member);
			String sql = "UPDATE member SET username = ?, email = ?, pwd = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPwd());
			pstmt.setInt(4, member.getId());
			result = pstmt.executeUpdate();
			
			System.out.println("DAO결과 : " + result);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public int deleteMember(int id) {
		int result = 0;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			String sql = "DELETE member WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public Member selectMember(Member member) {
		Member resultMember = new Member();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			String sql = "SELECT * from member WHERE username = ? and email = ? and pwd = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3,  member.getPwd());
			
			rs = pstmt.executeQuery();
			
			if(!rs.next()) {
				resultMember = null;
			}
			else {
				resultMember.setId(rs.getInt("id"));
				resultMember.setUsername(rs.getNString("username"));
				resultMember.setEmail(rs.getNString("email"));
				resultMember.setPwd(rs.getString("pwd"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resultMember;
	}
}