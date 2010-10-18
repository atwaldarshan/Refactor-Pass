package com.payparade.util;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.payparade.controller.AdminController;

public class OpsTimerTask extends TimerTask {
	protected static Logger logger_ = Logger.getLogger(OpsTimerTask.class
			.getSimpleName());
	// protected Database db_ = null ;
	private ServletContext servletContext_ = null;
	private ServletListener listener_ = null;

	@Override
	public void run() {
		AdminController admin = (AdminController) servletContext_
				.getAttribute("admin");
		logger_.info("running from timer ("
				+ servletContext_.getAttribute("opstimer") + ") calling "
				+ admin);

		if (admin == null) {
			logger_.error("failed to find admin");
			this.cancel();
		} else {
			admin.recordOpsStats(listener_);
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
