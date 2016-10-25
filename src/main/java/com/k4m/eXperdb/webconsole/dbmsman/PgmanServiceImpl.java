package com.k4m.eXperdb.webconsole.dbmsman;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgmanServiceImpl implements PgmanService {

	@Autowired 
	private PgmanDAO pgmanDAO;
	
	@Override
	public List<Map<String,Object>> selectPrsnCdList() throws Exception {
		Map<String,Object> parameter = new HashMap<String,Object>();
		
		List<Map<String,Object>> prsnCdList =  pgmanDAO.selectPrsnCdList(parameter);
		
		return prsnCdList;
	}
	
	@Override
	public List<Map<String,Object>> selectScheduleList() throws Exception {
		Map<String,Object> parameter = new HashMap<String,Object>();
		
		List<Map<String,Object>> scheduleList =  pgmanDAO.selectScheduleList(parameter);
		
		return scheduleList;
	}
	
	@Override
	public List<Map<String,Object>> selectDBMSList() throws Exception {
		Map<String,Object> parameter = new HashMap<String,Object>();
		
		List<Map<String,Object>> scheduleList =  pgmanDAO.selectDBMSList(parameter);
		
		return scheduleList;
	}

	@Override
	public List<Map<String,Object>> selectMonthlyStatistics(String year, String sysNm) throws Exception {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("YYYY", year);
		parameter.put("SYS_NM", sysNm);
		
		List<Map<String,Object>> statisticsList =  pgmanDAO.selectMonthlyStatistics(parameter);
		
		return statisticsList;
	}

	@Override
	public List<Map<String,Object>> makeMonthlyStatisticsList(
			List<Map<String,Object>> prsnCdList,
			List<Map<String,Object>> statisticsList
			) throws Exception {
		
		List<Map<String,Object>> statisticsTableList = new ArrayList<Map<String,Object>>();
		
		Calendar cal = Calendar.getInstance();
		int mm = cal.get(Calendar.MONTH) + 1;
		
		for(int i=1;i<=12;i++) {
			Map<String,Object> row = new HashMap<String,Object>();
			String month = i+"월";
			row.put("month", month);
			row.put("number", i);
			int monthcnt = 0;
			for(Map<String,Object> code : prsnCdList) {
				int prsncnt = 0;
				for(Map<String,Object> sel : statisticsList) {
					if(month.equals(sel.get("mm")) && code.get("prsn_cd").equals(sel.get("prsn_cd"))) {
						row.put("tbl_tot_rslt_cnt", sel.get("tbl_tot_rslt_cnt"));
						row.put("tbl_tot_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("tbl_tot_rslt_cnt")));
						row.put("col_tot_rslt_cnt", sel.get("col_tot_rslt_cnt"));
						row.put("col_tot_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("col_tot_rslt_cnt")));
						row.put("tbl_prsn_rslt_cnt", sel.get("tbl_prsn_rslt_cnt"));
						row.put("tbl_prsn_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("tbl_prsn_rslt_cnt")));
						row.put("col_prsn_rslt_cnt", sel.get("col_prsn_rslt_cnt"));
						row.put("col_prsn_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("col_prsn_rslt_cnt")));
						row.put("sel_" + code.get("prsn_cd"), NumberFormat.getInstance().format(sel.get("data_sel_cnt")));
						row.put("rslt_" + code.get("prsn_cd"), NumberFormat.getInstance().format(sel.get("data_rslt_cnt")));
						row.put("per_" + code.get("prsn_cd"), sel.get("sel_rslt_per"));
						prsncnt++;
						monthcnt++;
					}
				}
				if(prsncnt==0) {
					row.put("sel_" + code.get("prsn_cd"), new BigDecimal("0"));
					row.put("rslt_" + code.get("prsn_cd"), new BigDecimal("0"));
					row.put("per_" + code.get("prsn_cd"), new BigDecimal("0"));
				}
			}
			if(monthcnt==0) {
				row.put("tbl_tot_rslt_cnt", 0);
				row.put("tbl_tot_rslt_cnt_format", "0");
				row.put("col_tot_rslt_cnt", 0);
				row.put("col_tot_rslt_cnt_format", "0");
				row.put("tbl_prsn_rslt_cnt", 0);
				row.put("tbl_prsn_rslt_cnt_format", "0");
				row.put("col_prsn_rslt_cnt", 0);
				row.put("col_prsn_rslt_cnt_format", "0");
			}

			if(i == mm) {
				row.put("currentMonth", true);
			} else {
				row.put("currentMonth", false);
			}
			
			statisticsTableList.add(row);
		}
		
		return statisticsTableList;
	}

	@Override
	public List<Map<String, Object>> selectYearlyStatistics(String sysNm) throws Exception {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("SYS_NM", sysNm);
		
		List<Map<String,Object>> statisticsList =  pgmanDAO.selectYearlyStatistics(parameter);
		
		return statisticsList;
	}

	@Override
	public List<Map<String, Object>> makeYearlyStatisticsList(List<Map<String, Object>> prsnCdList, List<Map<String, Object>> statisticsList) throws Exception {
		List<Map<String,Object>> statisticsTableList = new ArrayList<Map<String,Object>>();
		
		Calendar cal = Calendar.getInstance();
		int yyyy = cal.get(Calendar.YEAR);
		
		List<String> yearList = new ArrayList<String>();
		for(Map<String, Object> yearMap : statisticsList) {
			if(!yearList.contains(yearMap.get("yyyy_format"))) {
				yearList.add((String)yearMap.get("yyyy_format"));
			}
		}
		
		for(String year : yearList) {
			Map<String,Object> row = new HashMap<String,Object>();
			row.put("year", year);
			int yearcnt = 0;
			for(Map<String,Object> code : prsnCdList) {
				int prsncnt = 0;
				for(Map<String,Object> sel : statisticsList) {
					if(year.equals(sel.get("yyyy_format")) && code.get("prsn_cd").equals(sel.get("prsn_cd"))) {
						row.put("number", sel.get("yyyy"));
						row.put("tbl_tot_rslt_cnt", sel.get("tbl_tot_rslt_cnt"));
						row.put("tbl_tot_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("tbl_tot_rslt_cnt")));
						row.put("col_tot_rslt_cnt", sel.get("col_tot_rslt_cnt"));
						row.put("col_tot_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("col_tot_rslt_cnt")));
						row.put("tbl_prsn_rslt_cnt", sel.get("tbl_prsn_rslt_cnt"));
						row.put("tbl_prsn_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("tbl_prsn_rslt_cnt")));
						row.put("col_prsn_rslt_cnt", sel.get("col_prsn_rslt_cnt"));
						row.put("col_prsn_rslt_cnt_format", NumberFormat.getInstance().format(sel.get("col_prsn_rslt_cnt")));
						row.put("sel_" + code.get("prsn_cd"), NumberFormat.getInstance().format(sel.get("data_sel_cnt")));
						row.put("rslt_" + code.get("prsn_cd"), NumberFormat.getInstance().format(sel.get("data_rslt_cnt")));
						row.put("per_" + code.get("prsn_cd"), sel.get("sel_rslt_per"));
						prsncnt++;
						yearcnt++;
					}
				}
				if(prsncnt==0) {
					row.put("sel_" + code.get("prsn_cd"), new BigDecimal("0"));
					row.put("rslt_" + code.get("prsn_cd"), new BigDecimal("0"));
					row.put("per_" + code.get("prsn_cd"), new BigDecimal("0"));
				}
			}
			if(yearcnt==0) {
				row.put("tbl_tot_rslt_cnt", 0);
				row.put("tbl_tot_rslt_cnt_format", "0");
				row.put("col_tot_rslt_cnt", 0);
				row.put("col_tot_rslt_cnt_format", "0");
				row.put("tbl_prsn_rslt_cnt", 0);
				row.put("tbl_prsn_rslt_cnt_format", "0");
				row.put("col_prsn_rslt_cnt", 0);
				row.put("col_prsn_rslt_cnt_format", "0");
			}
			
			if(year.equals(yyyy+"년")) {
				row.put("currentYear", true);
			} else {
				row.put("currentYear", false);
			}
			statisticsTableList.add(row);
		}
		
		return statisticsTableList;
	}
	
	@Override
	public void statisticsRegist() throws Exception {
		Logger logger = Logger.getRootLogger();

		Map<String,Object> parameter = new HashMap<String,Object>();
		
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DATE) < 10) {
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DATE, 1);
			parameter.put("YYYYMM", new SimpleDateFormat("yyyyMM").format(cal.getTime()));
			
			int del = pgmanDAO.statisticsDelete(parameter);
			int reg = pgmanDAO.statisticsRegist(parameter);
			
			logger.info(parameter.get("YYYYMM") + " DELETE:" + del + " / INSERT:" + reg);
		}

		cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		parameter.put("YYYYMM", new SimpleDateFormat("yyyyMM").format(cal.getTime()));
		
		int del = pgmanDAO.statisticsDelete(parameter);
		int reg = pgmanDAO.statisticsRegist(parameter);
		
		logger.info(parameter.get("YYYYMM") + " DELETE:" + del + " / INSERT:" + reg);
	}
	
	@Override
	public Map<String, Object> selectServer(HashMap<String, String> param) throws Exception {		
		List<Map<String,Object>> list = pgmanDAO.selectServer(param);
		Map<String, Object> user = new HashMap<String, Object>();
		if (list.size() > 0) {
			user = list.get(0);
		} 		
		return user;
	}
	
	@Override
	public List<Map<String, Object>> selectServerList(HashMap<String, String> param) throws Exception {		
		List<Map<String, Object>> serverList = pgmanDAO.selectServerList(param);		
		return serverList;
	}
}
