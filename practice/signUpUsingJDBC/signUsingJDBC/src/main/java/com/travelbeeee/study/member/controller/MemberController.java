package com.travelbeeee.study.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.travelbeeee.study.member.Member;
import com.travelbeeee.study.member.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	MemberService memberService;
	
	@ModelAttribute("cp")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "/member/loginForm";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Member member) {
		// 로그인 가능한지 체크 --> 불가능하면 다시 home 으로 돌리기
		if(memberService.findMember(member) == null)
			return "home";
		// 가능하다면 세션에 회원정보 추가해주기.
		HttpSession session = request.getSession();
		session.setAttribute("member", memberService.findMember(member));
		session.setMaxInactiveInterval(300);
		
		System.out.println("로그인결과 : " + memberService.findMember(member));
		return "/main";
	}
	
	@RequestMapping(value="/signUp", method = RequestMethod.GET)
	public String signUpForm() {
		return "/member/signUpForm";
	}
	
	@RequestMapping(value="/signUp", method = RequestMethod.POST)
	public String signUp(Member member) {
		// 회원가입 양식 맞는지 체크 --> 불가능하면 다시 home 으로 돌리기
		
		// 가능하다면 DB에 회원정보 추가해주기.
		memberService.registerMember(member);
		
		return "home";
	}
	
	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public String modifyForm(HttpSession session, Model model) {
		if(session.getAttribute("member") == null)
			return "home";
			
		model.addAttribute("member", session.getAttribute("member"));
		
		return "member/modifyForm";
	}
	
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(HttpSession session, Model model, Member member) {
		// form 에서 입력된 거를 토대로 수정해주자!
		Member modifiedMember = (Member)session.getAttribute("member");
		modifiedMember.setUsername(member.getUsername());
		modifiedMember.setEmail(member.getEmail());
		modifiedMember.setPwd(member.getPwd());
		
		memberService.modifyMember(modifiedMember);
		
		return "/main";
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	public String delete(HttpSession session) {
		Member member = (Member)session.getAttribute("member");
		int result = memberService.removeMember(member);
		System.out.println("삭제결과! : " + result);
		session.invalidate();
		
		return "home";
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "home";
	}
	
	@RequestMapping(value="/main")
	public String back() {
		return "/main";
	}
}
