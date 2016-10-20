package com.k4m.eXperdb.webconsole.security;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

@Service 
public class CustomAuthenticationProviderLogout extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
	
	
	private SqlSession sqlSession;
	
    public void setSqlSession(SqlSession sqlsession) {
		this.sqlSession = sqlsession;
	}


	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		
		if(auth != null) {
			CustomUserDetails userDetail =  (CustomUserDetails) auth.getDetails();
			
			// 이력 남기기 
	        HashMap<String , String> putval = new HashMap<String, String>();
	        putval.put("userId", userDetail.getUserid());
	        putval.put("userName", userDetail.getUsername() );
	        putval.put("ip", request.getRemoteAddr());
	        putval.put("loginType", "2");
	        
	        sqlSession.insert("common-mapper.insertLoginHistory", putval);
			
			// 로깅 폼 출력
			//String refererUrl = arg0.getHeader("Referer");
	        //System.out.println("Logout from: " + refererUrl);
	
			super.onLogoutSuccess(request, response, auth);
		} else {
			response.sendRedirect("login");
		}
	}

}
