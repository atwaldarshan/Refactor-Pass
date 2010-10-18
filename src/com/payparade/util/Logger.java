package com.payparade.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Logger implements Serializable {

	org.apache.log4j.Logger parent_ = null;

	private HashMap<String, String> kvp_ = new HashMap<String, String>();
	private String ppId_ = "";
	private String sessionId_ = "";
	private String request_ = "unknown";
	private boolean debug_ = true;
	private boolean alert_ = true;
	private boolean mail_ = true;

	public synchronized static Logger getLogger(String categoryName) {
		org.apache.log4j.Logger parentLogger = org.apache.log4j.Logger
				.getLogger(categoryName);
		Logger returnLogger = new Logger(parentLogger);
		return returnLogger;
	}

	protected Logger(org.apache.log4j.Logger parent) {
		parent_ = parent;
	}

	public void logHashMap(HashMap m) {
		info("  logHashMap()");
		Set<String> keys = m.keySet();
		for (String k : keys) {
			info("  Key:" + k + "<  value:" + m.get(k) + "<");
		}
	}

	public void logMap(Map m) {
		info("  logHashMap()");
		Set<String> keys = m.keySet();
		for (String k : keys) {
			info("  Key:" + k + "<  value:" + m.get(k) + "<");
		}
	}

	public void logRequest(HttpServletRequest hrequest) {
		info("logRequest()       contextPath=" + hrequest.getContextPath());
		Cookie cookies[] = hrequest.getCookies();
		if (cookies == null)
			cookies = new Cookie[0];

		for (int i = 0; i < cookies.length; i++) {
			info("logRequest() cookie:" + cookies[i].getName() + "="
					+ cookies[i].getValue());
		}

		Enumeration names = hrequest.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = hrequest.getHeader(name);
			info("logRequest() header:" + name + "=" + value);
		}
		info("logRequest()             method=" + hrequest.getMethod());
		info("logRequest()           pathInfo=" + hrequest.getPathInfo());
		info("logRequest()        queryString=" + hrequest.getQueryString());
		info("logRequest()         remoteUser=" + hrequest.getRemoteUser());
		info("logRequest() requestedSessionId="
				+ hrequest.getRequestedSessionId());
		info("logRequest()         requestURI=" + hrequest.getRequestURI());
		info("logRequest()        servletPath=" + hrequest.getServletPath());

		info("logRequest() ");
		info("logRequest() ");
		info("logRequest() parameters");
		Enumeration params = hrequest.getParameterNames();
		while (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			String value = hrequest.getParameter(name);
			info("logRequest() parameter:" + name + "=" + value);
		}
	}

	public void debug(Object message) {
		String extraInfo = "";

		if (debug_)
			extraInfo = ppId_ + "::" + sessionId_ + "::" + request_ + "::";
		parent_.debug(extraInfo + message);
	}

	public void info(Object message) {
		String extraInfo = "";

		if (debug_)
			extraInfo = ppId_ + "::" + sessionId_ + "::" + request_ + "::";
		parent_.info(extraInfo + message);
	}

	public void warn(String message) {
		String extraInfo = "";

		if (debug_)
			extraInfo = ppId_ + "::" + sessionId_ + "::" + request_ + "::";
		parent_.warn(extraInfo + message);
	}

	public void info(Exception e) {
		e.printStackTrace();
	}

	public void logArrayList(ArrayList dataset) {
		// TODO Auto-generated method stub

	}

	public void logJSONObject(JSONObject myData) {
		Iterator<?> keys = myData.keys();
		Object value = null;
		while (keys.hasNext()) {
			String key = (String) keys.next();
			try {
				value = myData.get(key);
			} catch (JSONException e) {
				this.error(e.getMessage());
			}
			this.info(key + ":" + value);
		}

	}

	public void logContext(InitialContext context) {
		try {
			Hashtable<?, ?> env = context.getEnvironment();
			this.info("Context Environment:");
			this.logHashtable(env);
		} catch (NamingException e) {
		}
	}

	private void logHashtable(Hashtable<?, ?> table) {
		Enumeration<?> keys = table.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			info("    " + key + "=" + table.get(key));
		}
	}

	public void logStringArray(String[] elements) {
		for (int i = 0; i < elements.length; i++) {
			this.info("logStringArray():    " + elements[i]);
		}

	}

	public void setKVP(String key, String value) {
		kvp_.put(key, value);
	}

	public void setId(String id) {
		ppId_ = id;
	}

	public void setSession(String session) {
		sessionId_ = session;
	}

	public void setRequest(String string) {
		request_ = string;
	}

	public void setFlags(boolean alert, boolean debug, boolean mail) {
		alert_ = alert;
		debug_ = debug;
		mail_ = mail;
	}

	public void logStackTrace(StackTraceElement[] stackTrace) {
		for (int i = 0; i < stackTrace.length; i++) {
			parent_.error("*****" + stackTrace[i].getFileName() + ":"
					+ stackTrace[i].getLineNumber());
		}

	}

	public void error(String message, Exception e) {
		parent_.error(message, e);
	}

	public void error(String message) {
		parent_.error(message);
	}

}
