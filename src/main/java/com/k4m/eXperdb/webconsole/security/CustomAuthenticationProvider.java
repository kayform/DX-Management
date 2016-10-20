package com.k4m.eXperdb.webconsole.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.k4m.eXperdb.webconsole.common.Globals;
import com.k4m.eXperdb.webconsole.common.SHA256;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {  
	
	
	/**
	 * Logger 설정 
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
	 
	private SqlSession sqlSession;
	
    public void setSqlSession(SqlSession sqlsession) {
		this.sqlSession = sqlsession;
	}

	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Password 체크 로직이 필요함 
		String userId = (String)authentication.getPrincipal();		
		String userPasswd = (String)authentication.getCredentials();
		String requestIp = "0.0.0.0";
		// Request 방식으로 가져오기 Web.xml 에 Listner org.springframework.web.context.request.RequestContextListener 가 있어야 사용가능함 
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); 
//        logger.info("Local IP : " + request.getRemoteAddr());
		// Security 자체 방식 사용 
		
		Object details = authentication.getDetails();
		if (details instanceof WebAuthenticationDetails) {
			requestIp = ((WebAuthenticationDetails)details).getRemoteAddress();  
		}
		
		// check whether user's credentials are valid.
		// if false, throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		
		try{
			
			HashMap<String, String> paramval = new HashMap<String, String>();
			paramval.put("userId", userId);
//			paramval.put("user_pw", user_pw);
			
			SHA256 sha = new SHA256();
			String encryptedPasswd = sha.SHA256(userPasswd);
			
			// 유저 정보 조회
			List<Map<String, String>> userLoginInfo = sqlSession.selectList("user-mapper.getLoginInfo" ,  paramval);
			
			if(userLoginInfo.size() > 0) {
				String savedPasswd = userLoginInfo.get(0).get("user_pw");
				if (savedPasswd.equals(encryptedPasswd)){
					String validexp = userLoginInfo.get(0).get("validexp");
					if (validexp.equals("N")){
						throw new Exception("해당 계정의 사용기한이 만료 되었습니다. 관리자에게 문의 하여주십시오. ");
					}
					
	//				List<StructMenuinfo> menuinfo = sqlSession.selectList("cert_0001.selectAuthMenus" , paramval);
					
					// 권한 부여 
					String authDV = userLoginInfo.get(0).get("auth_dv"); 
					String deptName = userLoginInfo.get(0).get("dept");
					String userName = userLoginInfo.get(0).get("user_nm");
					
					List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
					// 기본으로 접속시 사용자 권한을 부여한다.
			        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
			        
			        if (authDV.equals("2")) {
			        	roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			        }else if(authDV.equals("3")){
			        	roles.add(new SimpleGrantedAuthority("ROLE_SUPER")); 
			        }
			        
			        // 메뉴정보 조회
			        List<MenuVO> menuInfo = getMenuList(userId, authDV);
			        
			        // 솔루션 구분??
	//		        String solutionType = "";
	//		        List<Map<String, String>> solTypeList =sqlSession.selectList("cert_0001.getSysMntCd");
	//		        solutionType  = solTypeList.get(0).get("sys_mnt_cd");
			        
			        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userId, userPasswd, roles);
	//		        result.setDetails(new CustomUserDetails(user_id,usernm ,  user_pw , authDV , deptNm ,solutionType, menuinfo ));
			        result.setDetails(new CustomUserDetails(userId, userName, userPasswd, authDV, menuInfo));
			        
			        // 이력 남기기 
			        HashMap<String , String> putval = new HashMap<String, String>();
			        putval.put("userId", userId);
			        putval.put("userName", userName );
			        putval.put("ip", requestIp);
			        putval.put("loginType", "1");
			        sqlSession.insert("common-mapper.insertLoginHistory", putval);
			        
					return result;
					
				}else{
					throw new BadCredentialsException("사용자 계정이 잘못되었거나 비밀번호가 일치하지 않습니다.");
				}
			} else {
				throw new BadCredentialsException("사용자 계정 정보가 없습니다.");
			}
		}catch(Exception ex){
			throw new BadCredentialsException(ex.getMessage());
		}
	}
	
	public List<MenuVO> getMenuList(String userId, String authDV) throws Exception {
		List<MenuVO> list = null;
		try {
			List<MenuVO> templist = getMenuInfo(userId, authDV);
			list = new ArrayList<MenuVO>();
			for (MenuVO vo : templist) {
				String parent = vo.getParentId();
				if ("0".equals(parent)) {
					for (MenuVO subVo : templist) {
						if (vo.getItemOrder().equals(subVo.getParentId()) && vo.getMenuType().equals(subVo.getMenuType())) {
							vo.getSubMenuList().add(subVo);
						}
					}
					list.add(vo);
				}
			}
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		
		return list;
	}
	
	/*
	 *  user별로 권한을 확인하여 메뉴 리스트 조회(관리자와 슈퍼관리자 권한은 모든 메뉴 접근가능)
	 */
	private List<MenuVO> getMenuInfo(String userId, String authDV) throws Exception {
		List<MenuVO> tempList = null;
		List<Map<String, Object>> menuInfoList = null;
		
		try {
			// 메뉴 정보 조회
			if(Globals.USER_ROLE_ADMIN.equals(authDV) || Globals.USER_ROLE_SUPER.equals(authDV)) {
				menuInfoList = sqlSession.selectList("common-mapper.selectMenuList");
			} else {
				Map<String, String> param = new HashMap<String, String>();
				param.put("user_id", userId);
				menuInfoList = sqlSession.selectList("common-mapper.selectMenuListByAuth", param);
			}
			
			tempList = new ArrayList<MenuVO>();
		    for(Map<String, Object> menuInfo : menuInfoList) {
		    	MenuVO vo = new MenuVO();
		    	vo.setMenuType((String)menuInfo.get("menu_type"));
		    	vo.setParentId(Integer.toString((int)menuInfo.get("parent_id")));
		    	vo.setId((String)menuInfo.get("id"));
		    	vo.setName((String)menuInfo.get("name"));
		    	vo.setItemOrder(Integer.toString((int)menuInfo.get("item_order")));
		    	vo.setSubOrder(Integer.toString((int)menuInfo.get("sub_order")));
		    	vo.setDepth(Integer.toString((int)menuInfo.get("depth")));
		    	vo.setUrl((String)menuInfo.get("url"));
		    	vo.setMenuColor((String)menuInfo.get("menu_color"));
		    	tempList.add(vo);
		    }
		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
			throw e;
		}
		
		return tempList;
	}
}
