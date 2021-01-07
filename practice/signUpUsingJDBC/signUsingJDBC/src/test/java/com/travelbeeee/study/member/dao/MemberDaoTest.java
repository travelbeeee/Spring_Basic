package com.travelbeeee.study.member.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.travelbeeee.study.member.Member;

@RunWith(SpringJUnit4ClassRunner.class) // 현재 Test코드가 스프링을 실행하는 역할을 한다 를 의미.
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")  //@ContextConfiguration 은 지정된 클래스나 문자열을 이용해서 필요한 객체들을 스프링 내에 객체로 등록하게 된다.
public class MemberDaoTest {
	@Test
	public void insertMemberTest() {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml");
		
		MemberDao memberDao = ctx.getBean(MemberDao.class);
		Member member = ctx.getBean(Member.class);
		
		member.setId(5);
		member.setUsername("testUser");
		member.setEmail("testUser@test.com");
		member.setPwd("test");
		
		memberDao.insertMember(member);
		
		ctx.close();
		
	}
}
