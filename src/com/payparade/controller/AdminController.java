package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.payparade.dataobject.PPAdminMailDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPPartnerMemberActivityDataObject;
import com.payparade.dataobject.PPPartnerNetworkDataObject;
import com.payparade.dataobject.PPPartnerServerDataObject;
import com.payparade.service.AdminService;
import com.payparade.util.Client;
import com.payparade.util.ServletListener;

public class AdminController extends PassController {

	private AdminService adminService;
	private PPOpsSnapShotDataObject ppOpsSnapShotDataObject = null;

	public PPOpsSnapShotDataObject getPpOpsSnapShotDataObject() {
		return ppOpsSnapShotDataObject;
	}

	public void setPpOpsSnapShotDataObject(
			PPOpsSnapShotDataObject ppOpsSnapShotDataObject) {
		this.ppOpsSnapShotDataObject = ppOpsSnapShotDataObject;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	@Override
	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		boolean done = true;

		try {

			if (resourceString == null) {
				logger.error("null resource string received");
				done = false;
			} else if ("version".equals(resourceString)) {
				getVersion(request, response, out);
			} else if (resourceString.startsWith("connect/")) {
				doConnect(request, response, out, resourceString, isSigned);
			} else if (resourceString.equals("mail")) {
				doMail(request, response, out);
			} else if (resourceString.equals("ip")) {
				doIp(request, response, out);
			} else if ("stats/op".equals(resourceString)) {
				getOpStats(request, response, out);
			} else if ("reload".equals(resourceString)) {
				refreshData();
			} else if ("stats/op/xml".equals(resourceString)) {
				getXMLOpStats(request, response, out);
			} else if (resourceString.startsWith("graph/op/")) {
				getOpGraph(request, response, out, resourceString);
			} else if ("js/pp_head.js".equals(resourceString)) {
				getHeadJs(request, response, out, partnerCode, partnerHost);
			} else if ("js/pp_body.js".equals(resourceString)) {
				getBodyJs(request, response, out, partnerCode, partnerHost,
						resourceString);
			} else if (resourceString.equals("debug/on")) {
				debug_ = true;
			} else if (resourceString.equals("debug/off")) {
				debug_ = false;
			} else if (resourceString.equals("alert/on")) {
				alert_ = true;
			} else if (resourceString.equals("alert/off")) {
				alert_ = false;
			} else if (resourceString.equals("mail/on")) {
				mail_ = true;
			} else if (resourceString.equals("mail/off")) {
				mail_ = false;
			} else
				done = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (debug_ && !done) {
			logger.info(controllerName + ".get did NOT process - "
					+ request.getRequestURI());
		}

		request.getSession().invalidate();

		return done;
	}

	private void getOpGraph(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger.info(" Inside getOpGraph() of AdminController class ");
		try{
			String parts[] = resourceString.split("/");
			getAdminService().getOpGraph(parts);
		}catch(Exception e){
			logger.error("AdminController::getOpGraph::Exception is::",e);
		}
		logger.info(" Outside getOpGraph() of AdminController class ");
	}

	public void doIp(HttpServletRequest request, HttpServletResponse response,
			PrintWriter out) throws IOException {
		logger.info("Inside doIp() of AdminController class");
		HttpSession session = request.getSession(false);
		if (session.getAttribute("customerip") != null) {
			out.print("<ip>" + (String) session.getAttribute("customerip")
					+ "</ip>");
			logger.info(session.getAttribute("customerip"));
		} else {
			out.print("<ip>IP does not exists.</ip>");
		}
		logger.info("Outside doIp() of AdminController class");
	}

	public void getVersion(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		messageResponse(out, version_);
	}

	public void doConnect(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned) throws IOException {
		String partnerId = resourceString.substring(8);
		if (debug_)
			logger.info("doConnect from "
					+ request.getHeader("pp_social_network") + " for id="
					+ request.getHeader("pp_social_id") + " (partner id "
					+ partnerId + ")");

		messageResponse(out, "Thank You for connecting with "
				+ request.getHeader("pp_social_network"));
	}

	public void doMail(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		if (debug_) {
			logger.info("doMail");
		}
		PPAdminMailDataObject ppAdminMailDataObject = getAdminService()
				.select_pp_admin_mail();
		// TODO: Externalize email address
		String to = ppAdminMailDataObject.getTo_user_id();
		String from = ppAdminMailDataObject.getFrom_user_id() + hostname_;
		String resultMsg = "";
		// String from = request.getParameter("from");
		// String to = request.getParameter("to");
		// String subject = request.getParameter("subject");
		// String content = request.getParameter("message");

		String subject = ppAdminMailDataObject.getSubject();
		String content = ppAdminMailDataObject.getContent();
		Properties props = new Properties();
		props.put(ppAdminMailDataObject.getPropertykey(), ppAdminMailDataObject
				.getPropertyvalue());

		Session mailSession = Session.getDefaultInstance(props, null);
		mailSession.setDebug(debug_);

		try {
			Transport transport = mailSession
					.getTransport(ppAdminMailDataObject.getProtocolname());

			// Setup message
			MimeMessage message = new MimeMessage(mailSession);
			// From address
			message.setFrom(new InternetAddress(from));
			// To address
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			// Subject
			message.setSubject(subject);
			// Content
			message.setText(content);

			// Send message
			transport.connect();
			transport.send(message);
		} catch (NoSuchProviderException e) {
			logger.error(e.getStackTrace()[0] + " - " + e.getMessage());
		} catch (AddressException e) {
			logger.error(e.getStackTrace()[0] + " - " + e.getMessage());
		} catch (MessagingException e) {
			logger.error(e.getStackTrace()[0] + " - " + e.getMessage());
		}

		// Build result message
		resultMsg = "<b>Result:</b>";
		resultMsg += "<br>Message sent: " + new Date();
		resultMsg += "<br>To: " + to;
		resultMsg += "<br>From: " + from;
		messageResponse(out, resultMsg);
	}

	public void getXMLOpStats(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		Date date = new Date();
		String mess = "Operational Stats as of "
				+ DateFormat.getDateInstance().format(date) + " at "
				+ DateFormat.getTimeInstance().format(date) + "<br/>";
		Set<String> keys = numServed_.keySet();
		for (String endpoint : keys) {
			mess = mess + "<br/><b>" + endpoint + " Endpoint</b><br/>";
			mess = mess + (numServed_.get(endpoint) - 1L)
					+ " requests served <br/>";
			mess = mess + "Longest response time was "
					+ maxMillis_.get(endpoint).toString()
					+ " milliseconds<br/>";
			mess = mess + "Last response time was "
					+ lastMillis_.get(endpoint).toString()
					+ " milliseconds<br/>";
			mess = mess + "Average response time was "
					+ totalMillis_.get(endpoint).longValue()
					/ numServed_.get(endpoint).longValue()
					+ " milliseconds<br/>";
		}
		mess = mess + "<br/><br/>";
		mess = mess + "Total Memory:" + Runtime.getRuntime().totalMemory()
				/ 1024 / 1024 + "MB<br/>";
		mess = mess + "Free Memory: " + Runtime.getRuntime().freeMemory()
				/ 1024 / 1024 + "MB<br/>";
		mess = mess
				+ "Used Memory:"
				+ (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
						.freeMemory()) / 1024 / 1024 + "MB<br/>";

		messageResponse(out, mess);
	}

	@Override
	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		boolean value = true;
		if (resourceString.equals("debug/on")) {
			debug_ = true;
		} else if (resourceString.equals("debug/off")) {
			debug_ = false;
		} else if (resourceString.equals("alert/on")) {
			alert_ = true;
		} else if (resourceString.equals("alert/off")) {
			alert_ = false;
		} else if (resourceString.equals("mail/on")) {
			mail_ = true;
		} else if (resourceString.equals("mail/off")) {
			mail_ = false;
		} else
			value = false;
		return value;
	}

	public void getOpStats(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		Date date = new Date();
		String mess = "Operational Stats as of "
				+ DateFormat.getDateInstance().format(date) + " at "
				+ DateFormat.getTimeInstance().format(date) + "<br/>"
				+ " * Not including this request<br/>";
		Set<String> keys = numServed_.keySet();
		for (String endpoint : keys) {
			mess = mess + "<br/><b>" + endpoint + " Endpoint</b><br/>";
			mess = mess + (numServed_.get(endpoint) - 1L)
					+ " requests served <br/>";
			mess = mess + "Longest response time was "
					+ maxMillis_.get(endpoint).toString()
					+ " milliseconds<br/>";
			mess = mess + "Last response time was "
					+ lastMillis_.get(endpoint).toString()
					+ " milliseconds<br/>";
			mess = mess + "Average response time was "
					+ totalMillis_.get(endpoint).longValue()
					/ numServed_.get(endpoint).longValue()
					+ " milliseconds<br/>";
		}
		mess = mess + "<br/><br/>Client Operations:<br/>" + Client.getOpStats();

		mess = mess + "<br/><br/>";
		mess = mess + "Total Memory:" + Runtime.getRuntime().totalMemory()
				/ 1024 / 1024 + "MB<br/>";
		mess = mess + "Free Memory: " + Runtime.getRuntime().freeMemory()
				/ 1024 / 1024 + "MB<br/>";
		mess = mess
				+ "Used Memory:"
				+ (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
						.freeMemory()) / 1024 / 1024 + "MB<br/>";

		messageResponse(out, mess, 10);
	}

	private void getHeadJs(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) {

		logger.info(" Inside getHeadJs() of AdminController class ");

		int partnerdomainid = getAdminService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		PPPartnerServerDataObject ppPartnerServerDataObject = null;
		String assignedServer = null;
		if (servers_ != null && servers_.size() > 0) {
			ppPartnerServerDataObject = servers_.get(partnerHost);
		}
		if (ppPartnerServerDataObject == null) {
			ppPartnerServerDataObject = servers_.get(partnerCode);
		}
		if (ppPartnerServerDataObject != null)
			assignedServer = ppPartnerServerDataObject.getPartner_server();
		/*
		 * String assignedServer = servers_.get(partnerHost) ; Devarshi Pandya
		 * if ( assignedServer == null ) assignedServer =
		 * servers_.get(partnerCode) ;
		 */
		/*
		 * Facilitate desktop debugging
		 */
		if (assignedServer == null) {
			assignedServer = "dev2.payparade.net";
			partnerCode = "payparade.net";
		}

		String result = "	var pp_server = '"
				+ assignedServer
				+ "' ; \n"
				+ "	var pp_partner_host = '"
				+ partnerHost
				+ "' ; \n"
				+ "	var pp_partner_code = '"
				+ partnerCode
				+ "' ; \n"
				+ "   var pp_app_id = '"
				+ partnerNetworkDataObject.getApi_key()
				+ "' ; \n"
				+ "  \n "
				+ "function pp_include(area, script_filename) { \n"
				+ "    var html_doc = document.getElementsByTagName(area).item(0); \n"
				+ "    var js = document.createElement('script'); \n"
				+ "    js.setAttribute('language', 'javascript'); \n"
				+ "    js.setAttribute('type', 'text/javascript'); \n"
				+ "    js.setAttribute('src', script_filename); \n"
				+ "    html_doc.appendChild(js); \n" + "    return false; \n"
				+ "    }; ";

		logger.info("Result is:::" + result);

		out.println(result);

	}

	private void getBodyJs(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost, String resourceString) {
		PPPartnerServerDataObject ppPartnerServerDataObject = null;
		String assignedServer = null;
		if (servers_ != null && servers_.size() > 0) {
			ppPartnerServerDataObject = servers_.get(partnerHost);
		}
		if (ppPartnerServerDataObject == null) {
			ppPartnerServerDataObject = servers_.get(partnerCode);
		}
		if (ppPartnerServerDataObject != null)
			assignedServer = ppPartnerServerDataObject.getPartner_server();
		/*
		 * Facilitate desktop debugging
		 */
		logger.info("server:" + assignedServer + "<");
		if (assignedServer == null) {
			assignedServer = "dev2.payparade.net";
			partnerCode = "payparade.net";
		}

		String result = " pp_include('head', 'http://"
				+ assignedServer
				+ "/js/payparade_head2.js') ;  \n"
				+ " pp_include('head', 'http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js') ;  \n"
				+ " pp_include('body', 'http://"
				+ assignedServer
				+ "/js/payparade_body2.js') ;  \n"
				+ " \n"
				+ " var params = { "
				+ " wmode: \"transparent\", "
				+ " allowNetworking: \"all\", "
				+ " allowScriptAccess: \"always\""
				+ " } ; \n"
				+ " var flashVars = { as_swf_name: \"pp_sio\","
				+ " pp_server: \""
				+ assignedServer
				+ "\","
				+ " pp_partner_host: \""
				+ partnerHost
				+ "\","
				+ " pp_partner_code: \""
				+ partnerCode
				+ "\""
				+ " } ; \n"
				+ "var flashVars2 = { as_swf_name: \"pp_twitter_search_scroll\" } ; ";

		logger.info("Result is:::" + result);

		out.println(result);

	}

	public void recordOpsStats(ServletListener listener) {
		Integer users = (Integer) this.getServletContext()
				.getAttribute("users");
		logger
				.info("recordOpsStats() with context "
						+ this.getServletContext());

		ppOpsSnapShotDataObject.setNum_of_users(listener.getMaxUsers());
		ppOpsSnapShotDataObject.setNum_of_request((int) overallNumServed_);
		ppOpsSnapShotDataObject.setTotal_milis((int) overallTotalMillis_);
		ppOpsSnapShotDataObject.setMax_milis((int) overallMaxMillis_);
		ppOpsSnapShotDataObject
				.setUsed_memeory(((Runtime.getRuntime().totalMemory() - Runtime
						.getRuntime().freeMemory()) / 1024 / 1024));
		ppOpsSnapShotDataObject.setFree_memory((Runtime.getRuntime()
				.maxMemory() / 1024 / 1024));
		/*
		 * db_.executeUpdate("INSERT INTO pp_ops_snapshot (num_users, max_connections, num_requests, total_millis, max_millis, used_memory, free_memory) "
		 * + "VALUES ("+ listener.getMaxUsers()+", "+
		 * db_.getMaxConnections()+", "+ overallNumServed_ +", "+
		 * overallTotalMillis_ +", "+ overallMaxMillis_ +", "+
		 * ((Runtime.getRuntime
		 * ().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024)+","+
		 * (Runtime.getRuntime().maxMemory()/1024/1024)+ ") ;") ;
		 */
		// db_.resetMaxConnections() ;
		getAdminService().insert_into_pp_ops_snapshot(ppOpsSnapShotDataObject);
		overallNumServed_ = 0L;
		overallTotalMillis_ = 0L;
		overallMaxMillis_ = 0L;
		listener.resetMaxUsers();

	}

	public void runReports(ServletListener listener) {
		logger.info("runReports");
		runPassActivityReport(listener, "davidsprom.com",
				"pp-reports@payparade.net");
		runPassActivityReport(listener, "payparade.net",
				"pp-reports@payparade.net");
		runPassOpsReport(listener, "davidsprom.com", "pp-reports@payparade.net");
	}

	public void runPassActivityReport(ServletListener listener, String partner,
			String recipient) {
		StringBuffer sql = new StringBuffer();
		String timeConstraint = "";
		String groupBy = "";
		int parterdomainid = getAdminService()
				.select_id_from_pp_partner_domain(partner);
		sql
				.append("SELECT c.dt, c.con, o.opp, CONCAT(c.con/o.opp*100,'%') 'Conv %', last_time_stamp FROM ");
		sql
				.append(" ( SELECT DATE_FORMAT(connected_time_stamp,'%a %b-%e') dt, count(*) con FROM pp_membership p WHERE partner_domain_id = "
						+ parterdomainid
						+ " AND connected_time_stamp > DATE('2009-03-22 17:00:00') GROUP BY DATE_FORMAT(connected_time_stamp,'%a %b-%e') 	) c, ");
		sql
				.append(" ( SELECT DATE_FORMAT(last_time_stamp,'%a %b-%e') dt, count(DISTINCT pp_id) opp, last_time_stamp FROM pp_member_activity a WHERE partner_domain_id = "
						+ parterdomainid
						+ " AND activity_code_id = 11 AND last_time_stamp > DATE('2009-03-22 17:00:00') GROUP BY DATE_FORMAT(last_time_stamp,'%a %b-%e') ) o ");
		sql.append(" WHERE c.dt = o.dt ");
		sql.append(" ORDER BY DATE(last_time_stamp) DESC ");

		/*
		 * DatabaseResult results = db_.executeQuery(sql) ; DataSet dataset =
		 * results.getDataSet() ;
		 */
		List<PPMemberActivityDataObject> ppMemberActivityDataObjectList = getAdminService()
				.select_dt_con_opp_from_pp_membership_ppmemberactivity(
						parterdomainid, sql.toString());
		String report = "\nConversion:\n";
		report += "\t\t\t\tDay\tConnects\tOppor\tConv %\n";
		report += "\t\t\t\t-----\t-----\t-----\t-----\n";
		for (PPMemberActivityDataObject pPMemberActivityDataObject : ppMemberActivityDataObjectList) {
			// report +=
			// "\t"+row.get("dt")+"\t"+row.get("con")+"\t"+row.get("opp")+"\t"+row.get("Conv %")+"\n"
			// ;
			report += "\t" + pPMemberActivityDataObject.getDt() + "\t"
					+ pPMemberActivityDataObject.getCon() + "\t"
					+ pPMemberActivityDataObject.getOpp() + "\t"
					+ pPMemberActivityDataObject.getConv() + "\n";
		}
		report += "\n";

		/*
		 * sql =
		 * "SELECT activity_code, network_code, count(*) activity_count FROM pp_partner_member_activity p WHERE member_social_id != 'null' AND activity_code IN ('Connect', 'Badge') AND partner_domain = '"
		 * +partner+"' " ; timeConstraint =
		 * " AND time_stamp > SUBTIME(NOW(),'0 1:0:0.000000') " ; groupBy =
		 * "  GROUP BY activity_code, network_code " ;
		 */
		sql.delete(0, sql.length());
		// sql =
		// "SELECT activity_code, network_code, count(*) activity_count FROM pp_partner_member_activity p WHERE member_social_id != 'null' AND activity_code IN ('Connect', 'Badge') AND partner_domain = '"+partner+"' "
		// ;
		sql
				.append(" select (select activity_code from pp_activity where id = activity_code_id) activity_code, ");
		sql
				.append(" (select network_code from pp_network where id = network_code_id) network_code,count(*) activity_count FROM pp_member_activity p ");
		sql.
				append(" where member_social_network_id != 'null' AND activity_code_id IN (10,6) AND partner_domain_id = "+parterdomainid+"");
		timeConstraint = " AND last_time_stamp > SUBTIME(NOW(),'0 1:0:0.000000') ";
		groupBy = "  GROUP BY activity_code_id, network_code_id ";
		List<PPPartnerMemberActivityDataObject> ppPartnerMemberActivityDataObjectList = getAdminService()
				.select_from_pp_partner_member_activity(
						sql.toString() + timeConstraint + groupBy);
		// results = db_.executeQuery(sql+timeConstraint+groupBy) ;
		// dataset = results.getDataSet() ;

		report += "\n\n\nActivity Detail\n\nLast Hour:\n";
		// for ( DatabaseRow row : dataset ) {
		for (PPPartnerMemberActivityDataObject partnerMemberActivityDataObject : ppPartnerMemberActivityDataObjectList) {
			// report +=
			// "\t"+row.get("activity_count")+" "+row.get("network_code")+" "+row.get("activity_code")+"s\n"
			// ;
			report += "\t"
					+ partnerMemberActivityDataObject.getActivity_count() + " "
					+ partnerMemberActivityDataObject.getNetwork_code() + " "
					+ partnerMemberActivityDataObject.getActivity_code()
					+ "s\n";
		}
		report += "\n";

		timeConstraint = " AND last_time_stamp > SUBTIME(NOW(),'1 0:0:0.000000') ";
		// results = db_.executeQuery(sql+timeConstraint+groupBy) ;
		ppPartnerMemberActivityDataObjectList = getAdminService()
				.select_from_pp_partner_member_activity(
						sql.toString() + timeConstraint + groupBy);
		// dataset = results.getDataSet() ;
		// logger_.info("rows: "+dataset.size()) ;

		report += "\n Last Day:\n";
		for (PPPartnerMemberActivityDataObject partnerMemberActivityDataObject : ppPartnerMemberActivityDataObjectList) {
			// report +=
			// "\t"+row.get("activity_count")+" "+row.get("network_code")+" "+row.get("activity_code")+"s\n"
			// ;
			report += "\t"
					+ partnerMemberActivityDataObject.getActivity_count() + " "
					+ partnerMemberActivityDataObject.getNetwork_code() + " "
					+ partnerMemberActivityDataObject.getActivity_code()
					+ "s\n";
		}
		report += "\n";

		timeConstraint = " AND time_stamp > SUBTIME(NOW(),'7 0:0:0.000000') ";
		// results = db_.executeQuery(sql+timeConstraint+groupBy) ;
		ppPartnerMemberActivityDataObjectList = getAdminService()
				.select_from_pp_partner_member_activity(
						sql.toString() + timeConstraint + groupBy);
		// dataset = results.getDataSet() ;
		// logger_.info("rows: "+dataset.size()) ;

		report += "\n Last Week:\n";
		for (PPPartnerMemberActivityDataObject partnerMemberActivityDataObject : ppPartnerMemberActivityDataObjectList) {
			// report +=
			// "\t"+row.get("activity_count")+" "+row.get("network_code")+" "+row.get("activity_code")+"s\n"
			// ;
			report += "\t"
					+ partnerMemberActivityDataObject.getActivity_count() + " "
					+ partnerMemberActivityDataObject.getNetwork_code() + " "
					+ partnerMemberActivityDataObject.getActivity_code()
					+ "s\n";
		}
		report += "\n";

		mailer_.send(recipient, "PASS Activity Report for " + partner, report);
	}

	public void runPassOpsReport(ServletListener listener, String partner,
			String recipient) {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'0 1:0:0.000000')");
		List<PPOpsSnapShotDataObject> pPOpsSnapShotDataObjectList = getAdminService()
				.select_reportfieldsfrom_pp_ops_snapshort(sql.toString());
		/*
		 * DatabaseResult results =db_.executeQuery(
		 * "SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'0 1:0:0.000000') "
		 * ) ; DataSet dataset = results.getDataSet() ;
		 * logger_.info("rows: "+dataset.size()) ;
		 */
		String report = "\nLast Hour:\n";
		for (PPOpsSnapShotDataObject ppOpsSnapShotDataObject : pPOpsSnapShotDataObjectList) {
			report += "\t\t\t\tAvg\tMax\tMin\n";
			report += "\t\t\t\t-----\t-----\t-----\n";
			report += "\t Users\t\t"
					+ ppOpsSnapShotDataObject.getAverage_users() + "\t"
					+ ppOpsSnapShotDataObject.getMax_users() + "\tna\n";
			report += "\t Response\t" + ppOpsSnapShotDataObject.getMax_milis()
					+ "\t" + ppOpsSnapShotDataObject.getAverage_milis()
					+ "\tna\n";
			report += "\t Memory\t\t" + "na\tna\t"
					+ ppOpsSnapShotDataObject.getFree_memory() + "\n";
			/*
			 * report +=
			 * "\t Users\t\t"+row.get("avg_users")+"\t"+row.get("max_users"
			 * )+"\tna\n" ; report +=
			 * "\t Response\t"+row.get("max_millis")+"\t"+
			 * row.get("average_millis")+"\tna\n" ; report +=
			 * "\t Memory\t\t"+"na\tna\t"+row.get("min_free")+"\n" ;
			 */
		}
		report += "\n";
		sql.delete(0, sql.length());
		sql
				.append("SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'1 0:0:0.000000')");
		/*
		 * results =db_.executeQuery(
		 * "SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'1 0:0:0.000000') "
		 * ) ; dataset = results.getDataSet() ;
		 * logger_.info("rows: "+dataset.size()) ;
		 */
		report += "\n Last Day:\n";
		for (PPOpsSnapShotDataObject ppOpsSnapShotDataObject : pPOpsSnapShotDataObjectList) {
			report += "\t\t\t\tAvg\tMax\tMin\n";
			report += "\t\t\t\t-----\t-----\t-----\n";
			report += "\t Users\t\t"
					+ ppOpsSnapShotDataObject.getAverage_users() + "\t"
					+ ppOpsSnapShotDataObject.getMax_users() + "\tna\n";
			report += "\t Response\t" + ppOpsSnapShotDataObject.getMax_milis()
					+ "\t" + ppOpsSnapShotDataObject.getAverage_milis()
					+ "\tna\n";
			report += "\t Memory\t\t" + "na\tna\t"
					+ ppOpsSnapShotDataObject.getFree_memory() + "\n";
		}
		/*
		 * for ( DatabaseRow row : dataset ) { report +=
		 * "\t\t\t\tAvg\tMax\tMin\n" ; report += "\t\t\t\t-----\t-----\t-----\n"
		 * ; report +=
		 * "\t Users\t\t"+row.get("avg_users")+"\t"+row.get("max_users"
		 * )+"\tna\n" ; report +=
		 * "\t Response\t"+row.get("max_millis")+"\t"+row.
		 * get("average_millis")+"\tna\n" ; report +=
		 * "\t Memory\t\t"+"na\tna\t"+row.get("min_free")+"\n" ; }
		 */

		sql.delete(0, sql.length());
		sql
				.append("SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'7 0:0:0.000000')");
		report += "\n";
		/*
		 * results =db_.executeQuery(
		 * "SELECT max(num_users) max_users, round(avg(num_users)) avg_users, max_millis, round(total_millis/num_requests) average_millis, min(free_memory) min_free FROM pp_ops_snapshot p WHERE time_stamp > SUBTIME(NOW(),'7 0:0:0.000000') "
		 * ) ; dataset = results.getDataSet() ;
		 * logger_.info("rows: "+dataset.size()) ;
		 */
		report += "\n Last Week:\n";

		for (PPOpsSnapShotDataObject ppOpsSnapShotDataObject : pPOpsSnapShotDataObjectList) {
			report += "\t\t\t\tAvg\tMax\tMin\n";
			report += "\t\t\t\t-----\t-----\t-----\n";
			report += "\t Users\t\t"
					+ ppOpsSnapShotDataObject.getAverage_users() + "\t"
					+ ppOpsSnapShotDataObject.getMax_users() + "\tna\n";
			report += "\t Response\t" + ppOpsSnapShotDataObject.getMax_milis()
					+ "\t" + ppOpsSnapShotDataObject.getAverage_milis()
					+ "\tna\n";
			report += "\t Memory\t\t" + "na\tna\t"
					+ ppOpsSnapShotDataObject.getFree_memory() + "\n";
		}

		/*
		 * for ( DatabaseRow row : dataset ) { report +=
		 * "\t\t\t\tAvg\tMax\tMin\n" ; report += "\t\t\t\t-----\t-----\t-----\n"
		 * ; report +=
		 * "\t Users\t\t"+row.get("avg_users")+"\t"+row.get("max_users"
		 * )+"\tna\n" ; report +=
		 * "\t Response\t"+row.get("max_millis")+"\t"+row.
		 * get("average_millis")+"\tna\n" ; report +=
		 * "\t Memory\t\t"+"na\tna\t"+row.get("min_free")+"\n" ; }
		 */
		report += "\n";

		mailer_.send(recipient, "PASS Ops Report", report);
	}

	public void init() {
		controllerName = "admin";
		this.getServletContext().setAttribute("admin", this);
		super.init();
	}

	/*
	 * public void destroy() { super.destroy(); }
	 */

}
