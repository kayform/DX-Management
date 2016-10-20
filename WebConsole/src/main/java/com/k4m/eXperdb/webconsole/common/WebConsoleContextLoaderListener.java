package com.k4m.eXperdb.webconsole.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//import com.k4m.eXperdb.webconsole.util.ResourceMonitorProcess;
//import com.k4m.eXperdb.webconsole.util.StatusManagerWrap;

public class WebConsoleContextLoaderListener implements ServletContextListener {
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		try {			

		} catch (Exception e) {
			Globals.logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}
}
