package com.k4m.eXperdb.webconsole.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	DashboardDAO dashboardDAO;
	
	@Override
	public int dashboardTargetDBMS() {
		return dashboardDAO.dashboardTargetDBMS();
	}

	@Override
	public int dashboardCheckReservation() {
		return dashboardDAO.dashboardCheckReservation();
	}

	@Override
	public int dashboardRunning() {
		return dashboardDAO.dashboardRunning();
	}

	@Override
	public List<Map<String, Object>> dashboardTargetList() {
		return dashboardDAO.dashboardTargetList();
	}

	@Override
	public List<Map<String, Object>> dashboardTargetListTwo() {
		return dashboardDAO.dashboardTargetListTwo();
	}

}
