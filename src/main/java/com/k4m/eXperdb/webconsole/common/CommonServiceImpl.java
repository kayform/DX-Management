package com.k4m.eXperdb.webconsole.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	private CommonDAO commonDAO;
	
	@Override
	public List<Map<String, Object>> selectSystemCode(String SystemGroupCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("systemCode", SystemGroupCode);
		return commonDAO.selectSystemCode(param);
	}

	@Override
	public List<Map<String, Object>> selectAllMenuList() {
		Map<String, Object> param = new HashMap<String, Object>();
		return commonDAO.selectAllMenuList(param);
	}
}
