package com.k4m.eXperdb.webconsole.security;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails {
	
	private String userId; 
	 
	private String userName;
	
	private String password; 
	
	private String authDV;
	
	private List<MenuVO> menuInfo;
	
//	private String deptNm;
	
//	private String solutionType;
	
//	private List<StructMenuinfo> menuinfo;
	
//	private List<Map<String,String>> menugroup;
	
//	@SuppressWarnings("unused")
//	private Map<String,String> getMenugroup(){
//		Map<String,String> rtnvalue = new  LinkedHashMap<String, String>();
//		for (StructMenuinfo  mnuinfo : this.menuinfo) {
//			if (!rtnvalue.containsKey(mnuinfo.getParent_nm())){
//				rtnvalue.put(mnuinfo.getParent_nm(), mnuinfo.getFrm_cd());
//			}
//		}
//		return rtnvalue;
//	}
//	
//	public String getSolutionType(){
//		return this.solutionType;
//	}
//
//	public List<Map<String,String>> getMenugroup() {
//		return menugroup;
//	}
//
	public List<MenuVO> getMenuinfo() {
		return menuInfo;
	}

	private String defmyset;
	public String getDefmyset() {
		return defmyset;
	}

	public String getDefappr() {
		return defappr;
	}

	private String defappr;
	
	
//	public CustomUserDetails(String userid , String username , String password , String authDV , String deptNm , String solutionType, List<StructMenuinfo> menuinfo) {
	public CustomUserDetails(String userId , String userName , String password, String authDV, List<MenuVO> menuInfo) {
		// TODO Auto-generated constructor stub
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.authDV = authDV;
		this.menuInfo = menuInfo; 
		
//		this.deptNm = deptNm;
//		this.solutionType = solutionType;
		
//		Map<String,String> tmpMap = new HashMap<String, String>();
//		
//		List<Map<String,String>> mnugroup =  new ArrayList<Map<String,String>>();
//		for (StructMenuinfo  mnuinfo : menuinfo) {
//			if (mnuinfo.getOrder() < 800  && !tmpMap.containsKey(mnuinfo.getParent_nm())){
//				Map<String,String> tmpmap = new HashMap<String, String>();
//				tmpMap.put(mnuinfo.getParent_nm(), mnuinfo.getParent_nm());
//				tmpmap.put("GRPNM", mnuinfo.getParent_nm());
//				tmpmap.put("DEFURL", mnuinfo.getFrm_cd());
//				
//				mnugroup.add(tmpmap);
//			}
//		}
//		this.menugroup = mnugroup;
//		
//		this.defmyset = "#";
//		for (StructMenuinfo  mnuinfo : menuinfo) {
//			if (mnuinfo.getOrder() >= 800  && mnuinfo.getOrder() < 900){
//				 this.defmyset = mnuinfo.getFrm_cd();
//				 break;
//			}
//		}
//		this.defappr = "#";
//		for (StructMenuinfo  mnuinfo : menuinfo) {
//			if (mnuinfo.getOrder() >= 900  && mnuinfo.getOrder() < 1000){
//				 this.defappr = mnuinfo.getFrm_cd();
//				 break;
//			}
//		}
		
		 
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	
	public String getAuthDV(){
		return this.authDV;
		
	}

//	public String getDeptNm(){
//		
//		return this.deptNm;
//		
//	}
	
	public String getUserid(){
		return this.userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@SuppressWarnings("unused")
	private String svrdate;

	public String getSvrdate() {
		SimpleDateFormat frm= new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		return frm.format(cal.getTime());
	}
	
}
