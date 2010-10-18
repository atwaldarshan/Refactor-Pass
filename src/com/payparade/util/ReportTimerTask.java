package com.payparade.util;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.payparade.controller.AdminController;

public class ReportTimerTask extends TimerTask {
	protected static Logger logger_ = Logger.getLogger(ReportTimerTask.class
			.getSimpleName());
	protected Database db_ = null;
	private ServletContext servletContext_ = null;
	private ServletListener listener_ = null;

	@Override
	public void run() {
		logger_.info("running");
		AdminController admin = (AdminController) servletContext_
				.getAttribute("admin");
		logger_.info("running from timer ("
				+ servletContext_.getAttribute("reptimer") + ") calling "
				+ admin);
		if (admin == null) {
			logger_.error("failed to find admin");
			this.cancel();
		} else {
			logger_.info("running reports");
			admin.runReports(listener_);
		}
		logger_.info("complete");
	}

	public void setServletContext(ServletContext servletContext) {
		servletContext_ = servletContext;
	}

	public void setListener(ServletListener servletListener) {
		listener_ = servletListener;

	}

}
