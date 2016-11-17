package com.k4m.eXperdb.webconsole.dbmsman;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.k4m.eXperdb.webconsole.common.CommonService;
import com.k4m.eXperdb.webconsole.common.DataHistoryService;
import com.k4m.eXperdb.webconsole.settings.SettingsService;

public class PgmonController {
	@Autowired 
	private PooledDataSource dataSource;
	@Autowired
	private CommonService commonService;
	
}
