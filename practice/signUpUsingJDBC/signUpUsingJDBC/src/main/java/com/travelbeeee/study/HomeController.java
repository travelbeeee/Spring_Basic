package com.travelbeeee.study;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@ModelAttribute("cp")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		return "home";
	}
	
	@RequestMapping(value="/main")
	public String main(HttpSession session) {
		if(session.getAttribute("member") == null)
			return "/";
		return "/main";
	}
}
