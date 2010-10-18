package com.payparade.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class ServletListener implements HttpSessionListener,
		ServletContextListener {
	protected Logger logger_ = null;
	private static Timer ops_timer_ = new Timer();
	private static Timer rep_timer_ = new Timer();
	private static Integer numUsers_ = 0;
	private static Integer maxUsers_ = 0;
	private static ServletContext context_ = null;
	private static ArrayList<HttpSession> sessions_ = new ArrayList<HttpSession>();

	public synchronized int getUsers() {
		return numUsers_;
	}

	public synchronized int getMaxUsers() {
		return maxUsers_;
	}

	public synchronized void setUsers(int newValue) {
		numUsers_ = newValue;
	}

	public synchronized void setMaxUsers(int newValue) {
		maxUsers_ = newValue;
	}

	public synchronized void incrementUsers() {
		if (++numUsers_ > maxUsers_)
			maxUsers_ = numUsers_;
	}

	public synchronized void decrementUsers() {
		if (--numUsers_ > maxUsers_)
			maxUsers_ = numUsers_;
		if (numUsers_ < 0) {
			logger_.warn("zero boundry error");
			numUsers_ = 0;
		}
	}

	public synchronized void resetMaxUsers() {
		maxUsers_ = 0;
	}

	public void sessionCreated(HttpSessionEvent event) {
		incrementUsers();
		synchronized (sessions_) {
			sessions_.add(event.getSession());
		}
		logger_.info("sessionCreated('" + event.getSession().getId() + "')  "
				+ numUsers_ + "(" + sessions_.size()
				+ ") Concurrent Users.   Max was " + maxUsers_);
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		String session = event.getSession().getId();
		OAuth2.clearSession(session);
		decrementUsers();
		synchronized (sessions_) {
			sessions_.remove(event.getSession());
		}
		logger_.info("sessionDestroyed('" + session + "')  " + numUsers_ + "("
				+ sessions_.size() + ") Concurrent Users.   Max was "
				+ maxUsers_);
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger_ = Logger.getLogger(ServletListener.class.getSimpleName());
		logger_.info("contextInitialized()");
		context_ = servletContextEvent.getServletContext();
		try {
			// create the timer and timer task objects

			OpsTimerTask task = new OpsTimerTask();
			task.setServletContext(context_);
			task.setListener(this);
			// get a calendar to initialize the start time
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 2);
			Date startTime = calendar.getTime();
			
			//Read data from property file
			/*int minute = 1;
			try{
				 Properties props = new Properties();
			      InputStream inputStream = new FileInputStream(context_.getRealPath("WEB-INF/configuration.properties"));
			      props.load(inputStream);
			      minute=Integer.parseInt(props.getProperty("minute"));
				}catch(Exception e){
					e.printStackTrace();
				}
				*/
			ops_timer_.scheduleAtFixedRate(task, startTime, 1000 * 60* 100);
			ReportTimerTask reportTask = new ReportTimerTask();
			reportTask.setServletContext(context_);
			reportTask.setListener(this);

			Calendar reportCalendar = Calendar.getInstance();
			reportCalendar.add(Calendar.MINUTE, 1);
			reportCalendar.add(Calendar.SECOND, 30);
			Date reportTime = reportCalendar.getTime();

			// schedule the task to run hourly
			rep_timer_.scheduleAtFixedRate(reportTask, reportTime,
					24 * 1000 * 60 * 60);

			// save our timer for later use
			context_.setAttribute("opstask", task);
			context_.setAttribute("opstimer", ops_timer_);
			context_.setAttribute("reptask", reportTask);
			context_.setAttribute("reptimer", rep_timer_);
			context_.setAttribute("users", numUsers_);
			context_.setAttribute("sessions", sessions_);
		} catch (Exception e) {
			context_.log("Problem initializing the ops monitoring task "
					+ e.getMessage());
		}
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger_.info("contextDestroyed()");

		ServletContext servletContext = servletContextEvent.getServletContext();

		if (ops_timer_ != null) {
			logger_.info("ops timer canceled");
			ops_timer_.cancel();
		}

		servletContext.removeAttribute("opstimer");

		if (rep_timer_ != null) {
			logger_.info("report timer canceled");
			rep_timer_.cancel();
		}

		servletContext.removeAttribute("reptimer");
	}

}
