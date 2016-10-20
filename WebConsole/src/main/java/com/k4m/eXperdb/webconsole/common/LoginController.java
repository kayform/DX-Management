package com.k4m.eXperdb.webconsole.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.k4m.eXperdb.webconsole.security.CustomUserDetails;

@Controller
public class LoginController {
	//@Autowired
	//private PimService pimService;
	
	@RequestMapping(value = "/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		
		response.setStatus(401);// session exfiration http status code.
		// HttpStatus
		return mav;
	}
	
	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public ModelAndView login_success(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login_success");
		
		CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		
		session.setAttribute("userLoginInfo", userDetails);
		session.setAttribute("userId", userDetails.getUserid());
		session.setAttribute("userAuth", userDetails.getAuthDV());
		
		return mav;
	}
	
	@RequestMapping(value="/logout")
	public String logout(Model model, HttpSession session, HttpServletRequest request) throws Exception {
		try {
			session.invalidate();
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		return "redirect:login";
	}	
}
