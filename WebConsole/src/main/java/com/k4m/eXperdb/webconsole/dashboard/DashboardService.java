package com.k4m.eXperdb.webconsole.dashboard;

import java.util.List;
import java.util.Map;

public interface DashboardService {

	int dashboardTargetDBMS();

	int dashboardCheckReservation();

	int dashboardRunning();
	
	List<Map<String, Object>> dashboardTargetList();

	List<Map<String, Object>> dashboardTargetListTwo();

}
