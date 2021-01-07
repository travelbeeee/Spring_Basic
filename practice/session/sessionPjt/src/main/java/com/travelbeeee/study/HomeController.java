package com.travelbeeee.study;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@ModelAttribute("cp")
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	public String session(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionValue1 = request.getParameter("sessionValue1");
		String sessionValue2 = request.getParameter("sessionValue2");
		
		session.setAttribute("sessionKey1", sessionValue1);
		session.setAttribute("sessionKey2", sessionValue2);
		
		return "session";
	}

	@RequestMapping(value="/attributeDelete", method=RequestMethod.GET)
	public String attributeDelete(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("sessionKey1");
		
		return "attributeDeleted";
	}

	@RequestMapping(value="/sessionDelete", method=RequestMethod.GET)
	public String sessionDelete(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "home";
	}
}
