package com.payparade.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.util.CookieGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPDashBoardDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.service.DataService;
import com.payparade.service.SigningSecretServiceImpl;
import com.payparade.util.Activity;
import com.payparade.util.Database;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DataController extends PassController {
	private DataService dataService;
	protected boolean open_ = true;
	private static HashMap<String, String> timeString_ = null;
	private static HashMap<String, String> dateFormats_ = null;
	private static HashMap<String, String> dateLimits_ = null;
	private static HashMap<String, String> allowed_ = null;

	public boolean isOpen_() {
		return open_;
	}

	public void setOpen_(boolean open) {
		open_ = open;
	}

	private PPConnectionDataObject ppConnectionDataObject;

	public PPConnectionDataObject getPpConnectionDataObject() {
		return ppConnectionDataObject;
	}

	public void setPpConnectionDataObject(
			PPConnectionDataObject ppConnectionDataObject) {
		this.ppConnectionDataObject = ppConnectionDataObject;
	}

	public DataService getDataService() {
		return dataService;
	}

	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	private void doCheckin(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger
				.info(" Inside doCheckin(HttpServletRequest,HttpServletResponse) of DataController class ");
		try {
			recordActivity(request, (short) 3);
			String passedList = request.getParameter("a");
			logger.info(" PassedList is::: " + passedList);
			logger.info(" Value of getPpConnectionDataObject()::"
					+ getPpConnectionDataObject());
			String friends[] = passedList.split(",");
			logger.info(" Value of friends::" + friends);
			getPpConnectionDataObject().setFriend_id(friends);
			String referer = request.getHeader("referer");
			logger.info(" Value of referer::" + referer);
			short networkid = 1;

			networkid = getPassService().getNetworkIdUsingNetworkcodeid(
					getSocnet(request));

			if (networkid == 0)
				networkid = 1;

			getPpConnectionDataObject().setNetwork_code_id(networkid);
			if (getSocid(request) != null) {
				getPpConnectionDataObject().setSocial_id(getSocid(request));
			} else {
				getPpConnectionDataObject().setSocial_id("");
			}
			getDataService().insertPPConnection(ppConnectionDataObject);
			CookieGenerator cookieGenerator = new CookieGenerator();
			cookieGenerator.setCookieName("pp_xv2");
			cookieGenerator.setCookiePath("/");
			cookieGenerator.addCookie(response, String.format("%d",
					friends.length));
		} catch (Exception e) {
			logger.error("doCheckin::DataController::Exception is::", e);
		}
		logger
				.info(" Outside doCheckin(HttpServletRequest,HttpServletResponse) of DataController class ");
	}

	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		logger
				.info(" Inside myHandleRequest(HttpServletRequest,HttpServletResponse,String) of DataController class ");
		logger.info(" Resource String is:::" + resourceString);
		boolean done = true;
		if (isSigned || open_) {
			if (resourceString == null) {
				logger.error("null resource string received");
				done = false;
			} else if ("pp".equals(resourceString))
				getLoginAuth(request, response, out);
			else if (resourceString.startsWith("dashboard/activity"))
				getDashboardActivity(request, response, out, resourceString);
			else if (resourceString.startsWith("dashboard/metrics"))
				getDashboardMetrics(request, response, out, resourceString);
			else if (resourceString.startsWith("dashboard/ops/metrics"))
				getOpsMetrics(request, response, out, resourceString);
			else if (resourceString.startsWith("activity"))
				getActivity(request, response, out, resourceString);
			else if (resourceString.startsWith("connections"))
				getConnections(request, response, out, resourceString);
			else if (resourceString.startsWith("logo"))
				getLogo(request, response, out, resourceString);
			else if (resourceString.startsWith("socnet"))
				getSocnet(request, response, out, resourceString);
			else if (resourceString.startsWith("partner/id"))
				getPartnerId(request, response, out, resourceString);
			else if (resourceString.startsWith("partner"))
				getPartnerData(request, response, out, resourceString);
			else if (resourceString.startsWith("access"))
				getAccess(request, response, out, resourceString);
			else if (resourceString.startsWith("domain"))
				getDomain(request, response, out, resourceString);
			else if (resourceString.startsWith("memberlist"))
				getMemberList(request, response, out, resourceString);
			else if (resourceString.startsWith("sessions"))
				getSessions(request, response, out, resourceString);
			else {
				done = false;
			}
		}
		/*
		 * HttpSession session = request.getSession(false) ;
		 * getPpConnectionDataObject().setNetwork_code_id((short)1);
		 * if(getSocid(request)!=null && getSocid(request).equals("")==false)
		 * getPpConnectionDataObject().setSocial_id((getSocid(request))); else
		 * getPpConnectionDataObject().setSocial_id((""));
		 */
		logger.info(" value of done is::" + done);
		logger.info(" Outside get() of DataController class ");
		return done;

	}

	private void getDashboardMetrics(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		// recordActivity( request, "Metrics") ;
		recordActivity(request, Activity.METRICS);
		String baseString = "/pass/data/" + resourceString;
		boolean isAllowed = SigningSecretServiceImpl.checkSignature(baseString, request
				.getParameter("pp_signature"));
		logger.info("getDashboardData(" + baseString + ") = " + isAllowed);
		Map<Integer, Integer> vars = new HashMap<Integer, Integer>();

		String parts[] = resourceString.split("/");
		for (int i = 0; i < parts.length; i++) {
			logger.info("Parts[" + i + "]" + parts[i]);
		}
		logger.info("getActivity(" + parts[1] + ")");
		logger.logStringArray(parts);
		/*
		 * String sql =
		 * "SELECT activity_code code, count(distinct member_session_id) val FROM pp_member_activity p "
		 * + " WHERE partner_domain = '"+parts[2]+"' "+
		 * "   AND time_stamp > SUBTIME(NOW(), '"
		 * +timeString_.get(parts[3].toLowerCase())+"') "+
		 * "   AND activity_code IN ('Connect Option', 'Click Through', 'Share' ) "
		 * + " GROUP BY activity_code" ;
		 */
		int partnerdomainid = getDataService()
				.select_id_from_pp_partner_domain(parts[2]);
		String sql = "SELECT activity_code_id code, count(distinct member_session_id) val FROM pp_member_activity p "
				+ " WHERE partner_domain_id ="
				+ partnerdomainid
				+ " "
				+ "   AND last_time_stamp > SUBTIME(NOW(), '"
				+ timeString_.get(parts[3].toLowerCase())
				+ "') "
				+ "   AND activity_code_id IN (11,8,7) "
				+ " GROUP BY activity_code_id";
		logger.info("Query 1::::--->" + sql);
		List<PPMemberActivityDataObject> ppMemberDataObjectList = getDataService()
				.select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity(
						sql);
		/*
		 * DatabaseResult results = db_.executeQuery(sql ) ; DataSet dataset =
		 * results.getDataSet() ; HashMap<String, Integer> vars = new
		 * HashMap<String,Integer>() ; for ( DatabaseRow row : dataset ) {
		 * logger_.logDatabaseRow(row) ; vars.put((String) row.get("code"),
		 * row.getInteger("val") ) ; }
		 */
		for (PPMemberActivityDataObject ppMemberActivityDataObject : ppMemberDataObjectList) {
			vars.put((Integer) ppMemberActivityDataObject.getActivityCodeId(),
					(Integer) ppMemberActivityDataObject.getGeneral_variable());
		}

		/*
		 * sql = "SELECT count(*) val from pp_membership m "+
		 * " WHERE partner_domain = '"+parts[2]+"' "+
		 * "   AND connected_at > SUBTIME(NOW(), '"
		 * +timeString_.get(parts[3].toLowerCase())+"') ; " ;
		 */

		sql = "SELECT count(*) val from pp_membership m "
				+ " WHERE partner_domain_id= " + partnerdomainid + " "
				+ "   AND connected_time_stamp > SUBTIME(NOW(), '"
				+ timeString_.get(parts[3].toLowerCase()) + "') ; ";
		logger.info("Query 2::::--->" + sql);
		int total_connect = getDataService()
				.SELECT_count_val_from_pp_membership(sql);
		vars.put(10, total_connect);
		/*
		 * results = db_.executeQuery(sql ) ; dataset = results.getDataSet() ;
		 * for ( DatabaseRow row : dataset ) { logger_.logDatabaseRow(row) ;
		 * vars.put("Connect", row.getInteger("val") ) ; }
		 */

		sql = "SELECT count(distinct member_session_id) val FROM pp_member_activity p "
				+ " WHERE partner_domain_id = "
				+ partnerdomainid
				+ " "
				+ "   AND last_time_stamp > SUBTIME(NOW(), '"
				+ timeString_.get(parts[3].toLowerCase())
				+ "') "
				+ "   AND activity_code_id IN (6) "
				+ " GROUP BY activity_code_id";
		logger.info("Query 3::::--->" + sql);
		/*
		 * results = db_.executeQuery(sql ) ; dataset = results.getDataSet() ;
		 * for ( DatabaseRow row : dataset ) { logger_.logDatabaseRow(row) ;
		 * vars.put("Share Option", row.getInteger("val") ) ; }
		 */
		ppMemberDataObjectList = getDataService()
				.select_distinctmembersession_id_val_FROM_pp_member_activity(
						sql);

		for (PPMemberActivityDataObject ppMemberActivityDataObject : ppMemberDataObjectList) {
			vars.put(13, (Integer) ppMemberActivityDataObject
					.getGeneral_variable());
		}

		/*
		 * int conops =
		 * (vars.get("Connect Option")==null)?0:vars.get("Connect Option") ; int
		 * shrops = (vars.get("Share Option")==null)?0:vars.get("Share Option")
		 * ; int cons = (vars.get("Connect")==null)?0:vars.get("Connect") ; int
		 * shares = (vars.get("Share")==null)?0:vars.get("Share") ; int clicks =
		 * (vars.get("Click Through")==null)?0:vars.get("Click Through") ;
		 */

		int conops = (vars.get(11) == null) ? 0 : vars.get(11);// Connect
																// Options
		int shrops = (vars.get(13) == null) ? 0 : vars.get(13);// Share Options
		int cons = (vars.get(10) == null) ? 0 : vars.get(10);// Connect
		int shares = (vars.get(7) == null) ? 0 : vars.get(7); // Share
		int clicks = (vars.get(8) == null) ? 0 : vars.get(8); // Click Through

		int connect_rate = 0;
		int share_rate = 0;
		int response_rate = 0;

		if (conops > 0)
			connect_rate = 100 * cons / conops;
		if (shrops > 0)
			share_rate = 100 * shares / shrops;
		if (shares > 0)
			response_rate = 100 * clicks / shares;

		out.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.printf("<data>\n");
		out.printf(" <connect_rate>" + connect_rate + "</connect_rate>\n");
		out.printf(" <share_rate>" + share_rate + "</share_rate>\n");
		out.printf(" <response_rate>" + response_rate + "</response_rate>\n");
		out.printf("</data>\n");

	}

	public void getLoginAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		out.print("<html><body>OK</body></html>");
	}

	public void init() {
		controllerName = "data";
		if (dateFormats_ == null) {
			dateFormats_ = new HashMap<String, String>();
			dateFormats_.put("hour", "%Y-%m-%d %H:00");
			dateFormats_.put("day", "%Y-%m-%d");
			dateFormats_.put("week", "%Y-%u");
			dateFormats_.put("month", "%Y-%m");
		}
		if (dateLimits_ == null) {
			dateLimits_ = new HashMap<String, String>();
			dateLimits_.put("hour", "SUBTIME(NOW(), '0 25:0:0')");
			dateLimits_.put("day", "SUBDATE(NOW(), 35 )");
			dateLimits_.put("week", "SUBDATE(NOW(), 372 )");
			dateLimits_.put("month", "SUBDATE(NOW(), 395 )");
		}

		if (timeString_ == null) {
			timeString_ = new HashMap<String, String>();
			timeString_.put("hour", "0 1:0:0");
			timeString_.put("day", "1 0:0:0");
			timeString_.put("week", "7 0:0:0");
			timeString_.put("month", "30 0:0:0");
		}
		super.init();
	}

	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		logger.info(" Inside post() of  DataController class ");
		logger.info(" value of resourceString is::" + resourceString);

		boolean done = true;
		if (resourceString.startsWith("checkin"))
			doCheckin(request, response, out, resourceString);
		else if (resourceString.startsWith("partner/options"))
			doUpdateOptions(request, response, out, resourceString);
		else if (resourceString.startsWith("file"))
			doFileUpload(request, response, out, resourceString);
		else
			done = false;

		logger.info(" value of done is::" + done);
		logger.info(" Outside post() of  DataController class ");
		return done;
	}

	private void getDashboardData(String table, String granularity,
			String activityValue, String timeField, String subType,
			String partner, PrintWriter out) {
		try {
			String data = "<list>\n";
			String nullClause = "";
			int partnerdomainid = getDataService()
					.select_id_from_pp_partner_domain(partner);
			int activitycodeid = getDataService().select_id_from_pp_activity(
					activityValue);
			if (!activityValue.equals("Connect Option"))
				nullClause = " network_code_id!= 'null' AND ";
			// DatabaseResult results =
			// db_.executeQuery("SELECT period, "+subType+", total, average FROM ( SELECT DATE_FORMAT("+timeField+",'"+dateFormats_.get(granularity)+"') period, "+subType+", count(*) total, 0 average FROM (select "+timeField+","+subType+",1 cnt FROM "+table+" WHERE partner_domain= '"+partner+"' AND activity_code = '"+activityValue+"' AND "+nullClause+timeField+" > "+dateLimits_.get(granularity)+" ) a GROUP BY DATE_FORMAT("+timeField+",'"+dateFormats_.get(granularity)+"'), "+subType+" ORDER BY DATE_FORMAT("+timeField+",'"+dateFormats_.get(granularity)+"')  ) b ORDER BY period ASC "
			// ) ;
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT period,");
			sql.append(subType);
			sql.append(", total, average FROM ( SELECT DATE_FORMAT(");
			sql.append(timeField);
			sql.append(",'");
			sql.append(dateFormats_.get(granularity));
			sql.append("') period, ");
			sql.append(subType);
			sql.append(", count(*) total, 0 average FROM (select ");
			sql.append(timeField);
			sql.append(",");
			sql.append(subType);
			sql.append(",1 cnt FROM ");
			sql.append(table);
			sql.append(" WHERE partner_domain_id=");
			sql.append(partnerdomainid);
			sql.append(" AND activity_code_id =");
			sql.append(activitycodeid);
			sql.append(" AND ");
			sql.append(nullClause);
			sql.append(timeField);
			sql.append(" > ");
			sql.append(dateLimits_.get(granularity));
			sql.append(" ) a GROUP BY DATE_FORMAT(");
			sql.append(timeField);
			sql.append(",'");
			sql.append(dateFormats_.get(granularity));
			sql.append("'), ");
			sql.append(subType);
			sql.append(" ORDER BY DATE_FORMAT(");
			sql.append(timeField);
			sql.append(",'");
			sql.append(dateFormats_.get(granularity));
			sql.append("')  ) b ORDER BY period ASC ");
			logger.info("Query is::" + sql.toString());
			List<PPDashBoardDataObject> listdashboarddataobjectlist = getDataService()
					.dashBoardDataObjectList(sql.toString(), subType);

			String currentPeriod = "";
			String currentNetworks = "";
			int activity = 0;
			int num = 0;
			if (listdashboarddataobjectlist != null
					&& listdashboarddataobjectlist.size() > 0)
				for (PPDashBoardDataObject ppDashBoardDataObject : listdashboarddataobjectlist) {
					if (currentPeriod.equals("")) {
						currentNetworks += "\t\t<network name=\""
								+ ppDashBoardDataObject.getSubType()
								+ "\" activity=\""
								+ ppDashBoardDataObject.getActivity()
								+ "\"/>\n";
						currentPeriod = ppDashBoardDataObject
								.getCurrentPeriod();
						num++;
						activity += ppDashBoardDataObject.getActivity();
					} else {
						if (currentPeriod.equals(ppDashBoardDataObject
								.getCurrentPeriod())) {
							logger.info("data row:");
							// logger.logDatabaseRow(row) ;
							currentNetworks += "\t\t<network name=\""
									+ ppDashBoardDataObject
											.getPublic_network_name()
									+ "\" activity=\""
									+ ppDashBoardDataObject.getActivity()
									+ "\"/>\n";
							num++;
							activity += ppDashBoardDataObject.getActivity();
						} else {
							int average = num > 0 ? activity / num : 0;
							data += "\t<period name=\"" + currentPeriod
									+ "\" activity=\"" + activity
									+ "\" average=\"" + average + "\">\n";
							data += currentNetworks;
							data += "\t</period>\n";

							currentPeriod = ppDashBoardDataObject
									.getCurrentPeriod();
							activity = ppDashBoardDataObject.getActivity();
							num = 1;

							currentNetworks = "\t\t<network name=\""
									+ ppDashBoardDataObject
											.getPublic_network_name()
									+ "\" activity=\""
									+ ppDashBoardDataObject.getActivity()
									+ "\"/>\n";
						}
					}
				}

			if (listdashboarddataobjectlist != null
					&& listdashboarddataobjectlist.size() > 0) {
				int average = num > 0 ? activity / num : 0;
				data += "\t<period name=\"" + currentPeriod + "\" activity=\""
						+ activity + "\" average=\"" + average + "\">\n";
				data += currentNetworks;
				data += "\t</period>\n";
			}

			data += "</list>\n";
			out.print(data);
		} catch (Exception e) {
			logger.error("getDashBoardData::DataController::Exception is::", e);
		}
	}

	private void getDashboardActivity(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		// recordActivity( request, "Activity") ;
		recordActivity(request, Activity.ACTIVITY);
		String baseString = "/pass/data/" + resourceString;
		boolean isAllowed = SigningSecretServiceImpl.checkSignature(baseString, request
				.getParameter("pp_signature"));
		logger.info("getDashboardData(" + baseString + ") = " + isAllowed);

		String parts[] = resourceString.split("/");
		logger.info("getActivity(" + parts[1] + ")");
		logger.logStringArray(parts);
		getDashboardData("pp_member_activity", parts[4].toLowerCase(),
				parts[3], "last_time_stamp", "network_code_id", parts[2], out);
	}

	private void getOpsMetrics(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		// recordActivity( request, "OpsMetrics") ;
		recordActivity(request, Activity.OPSMETRICS);
		String baseString = "/pass/data/" + resourceString;
		boolean isAllowed = SigningSecretServiceImpl.checkSignature(baseString, request
				.getParameter("pp_signature"));
		logger.info("getDashboardData(" + baseString + ") = " + isAllowed);

		String parts[] = resourceString.split("/");
		logger.info("getActivity(" + parts[1] + ")");
		logger.logStringArray(parts);
		getDataService().select_FROM_pp_ops_snapshot(out);
	}

	private void getActivity(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		recordActivity(request, Activity.ACTIVITY);
		String baseString = "/pass/data/" + resourceString;
		// boolean isAllowed = Signing.checkSignature(baseString,
		// request.getParameter("pp_signature")) ;
		String partner = null;
		String select = null;
		String filter = null;
		String limit = " LIMIT 1 ";

		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);

		if (parts.length > 1) {
			String start = parts[1];
			if (parts.length > 2) {
				partner = parts[2];
				int partnerdomainid = getDataService()
						.select_id_from_pp_partner_domain(partner);
				select = "SELECT * FROM pp_member_activity ";
				filter = " WHERE partner_domain_id= " + partnerdomainid
						+ " AND network_code_id != 0 ";
			} else {
				select = "SELECT * FROM pp_member_activity ";
				filter = " WHERE network_code_id!= 0 ";
			}
			if ("0".equals(start)) {
				filter += " AND idx >= (SELECT max(idx) from pp_member_activity "
						+ filter + " ) ";
			} else {
				filter += " AND idx > " + start;
			}
		}

		logger.info("getActivity(" + select + filter + limit + ")");
		// db_.executeQuery(select+filter+limit, out ) ;
		getDataService().select_FROM_pp_member_activiy(select + filter + limit,
				out);
	}

	private void getConnections(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString)
			throws IOException {
		// recordActivity( request, "Connections Data") ;
		recordActivity(request, Activity.CONNECTION_DATA);
		String baseString = "/pass/data/" + resourceString;
		boolean isAllowed = SigningSecretServiceImpl.checkSignature(baseString, request
				.getParameter("pp_signature"));
		String network = null;
		String id = "";
		// String sql = null ;
		int networkcodeid = 0;
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);

		if (parts.length > 2) {
			network = parts[1];
			if (parts[2] != null && parts[2].equals("") == false)
				id = (parts[2]);
			networkcodeid = getDataService().select_id_from_pp_network(
					network.toUpperCase());
			// db_.executeQuery(sql, out ) ;
			getDataService()
					.setSelect_distinct_social_id_friend_id_from_pp_connection(
							networkcodeid, id, out);
		} else {
			response.sendError(400);
		}
	}

	private void getLogo(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		String parts[] = resourceString.split("/");
		if (parts.length > 1) {
			logger.info("getLogo(" + parts[1] + ")");
			int partnerdomainid = getDataService()
					.select_id_from_pp_partner_domain(parts[1]);
			// db_.executeQuery("SELECT logo_file from pp_partner_options WHERE partner_domain='"+parts[1]+"' ; ",
			// out) ;
			getDataService().setSelect_logo_from_pp_partneroption(
					partnerdomainid, out);
		} else {
			response.setStatus(400);
			logger.warn("Missing logo domain");
		}
	}

	private void getSocnet(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		String parts[] = resourceString.split("/");
		if (parts.length > 1) {
			logger.info("getSocnet(" + parts[1] + ")");
			int partnerdomainid = getDataService()
					.select_id_from_pp_partner_domain(parts[1]);
			// db_.executeQuery("SELECT * from pp_partner_network WHERE partner_domain='"+parts[1]+"' ; ",
			// out) ;
			getDataService().setSelect_from_pp_partnernetwork(partnerdomainid,
					out);
		} else {
			response.setStatus(400);
			logger.warn("Missing logo domain");
		}
	}

	private void getPartnerId(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger.info("getPartnerId()");
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		int partnerdomainid = getDataService()
				.select_id_from_pp_partner_domain(parts[2]);
		int networkcodeid = getDataService()
				.select_id_from_pp_network(parts[3]);
		getDataService()
				.select_from_pp_partner_network_where_partner_domain_idand_network_code_id(
						partnerdomainid, networkcodeid, out);
		// db_.executeQuery("SELECT * from pp_partner_network WHERE partner_domain = '"+parts[2]+"' AND network_code = '"+parts[3]+"'",
		// out) ;
	}

	private void getPartnerData(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		try {
			logger.info("getPartnerData()");
			String parts[] = resourceString.split("/");
			logger.logStringArray(parts);
			int partnerdomainid = getDataService()
					.select_id_from_pp_partner_domain(parts[2]);
			// db_.executeQuery("SELECT * from pp_partner_options WHERE partner_domain = '"+parts[2]+"'",
			// out) ;
			getDataService()
					.select_from_pp_partner_options_WHERE_partner_domain(
							partnerdomainid, out);
		} catch (Exception e) {
			logger.error("getPartnerData::DataController::Exception is::", e);
		}
	}

	// 01.Sep.2010
	private void getAccess(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		// outputXmlHeader(out) ;
		// db_.executeQuery("SELECT a.partner_domain, o.display_name, a.ops_access, a.metrics_access, a.config_access from pp_access a, pp_partner_options o WHERE a.partner_domain = o.partner_domain AND user_email='"+request.getParameter("user_email")+"' ; ",
		// out) ;
		logger.info("User Email is:::" + request.getParameter("user_email"));
		getDataService().select_from_pp_partner_options_where_usermail(
				request.getParameter("user_email"), out);
	}

	private void getDomain(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		// db_.executeQuery("SELECT * from pp_partner_options WHERE partner_domain='"+request.getParameter("partner_domain")+"' ; ",
		// out) ;
		if (request.getParameter("partner_domain") != null
				&& request.getParameter("partner_domain").equals("") == false) {
			int partnerdomainid = getDataService()
					.select_id_from_pp_partner_domain(
							request.getParameter("partner_domain"));
			getDataService()
					.select_from_pp_partner_options_WHERE_partner_domain(
							partnerdomainid, out);
		}
	}

	private void getMemberList(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger.info("Inside getMemberList() of DataController class");
		String parts[] = resourceString.split("/");
		Double lat = Double.valueOf(parts[2]);
		Double lng = Double.valueOf(parts[3]);
		Double radius = Double.valueOf(parts[4]);
		int partnerdomainid = 0;
		if (radius == 0.0) {
			if (parts[1] != null && parts[1].equals("") == false)
				partnerdomainid = getDataService()
						.select_id_from_pp_partner_domain(parts[1]);
			/*
			 * db_.executeQuery("SELECT * FROM pp_customer_rank WHERE partner_domain ='"
			 * +parts[1]+"' "+ " LIMIT 100 ; ", out) ;
			 */

			getDataService().select_from_ppcustomer_rank_wherepartnerdomain_id(
					partnerdomainid, out);

		} else {
			double lat1 = lat - (radius / 69L);
			double lat2 = lat + (radius / 69L);
			double lng1 = lng
					- (radius / Math.abs(Math.cos(Math.toRadians(lat
							.doubleValue())) * 69L));
			double lng2 = lng
					+ (radius / Math.abs(Math.cos(Math.toRadians(lat
							.doubleValue())) * 69L));

			logger.info("Pulling members from between (" + lat1 + "," + lng1
					+ ") and (" + lat2 + "," + lng2 + ") for center (" + lat
					+ "," + lng + ") and radius " + radius.doubleValue());
			getDataService()
					.select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude(
							partnerdomainid, out, lat1, lat2, lng1, lng2);
			/*
			 * db_.executeQuery("SELECT * FROM pp_customer_rank WHERE partner_domain ='"
			 * +parts[1]+"' "+ " AND lattitude > "+lat1+
			 * " AND lattitude < "+lat2+ " AND longitude > "+lng1+
			 * " AND longitude < "+lng2+ " LIMIT 100 ; ", out) ;
			 */
		}
		logger.info("Outside getMemberList() of DataController class");
	}

	private void getSessions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		HttpSession currentSession = request.getSession();
		ServletContext context = currentSession.getServletContext();
		ArrayList<HttpSession> sessions = (ArrayList<HttpSession>) context
				.getAttribute("sessions");

		DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM);

		out.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.println("<data>");
		for (HttpSession session : sessions) {
			Date creationTime = new Date(session.getCreationTime());
			Date lastAccessed = new Date(session.getLastAccessedTime());
			out.println("\t<session>");
			out.println("\t<sessionid>" + session.getId() + "</sessionid>");
			out.println("\t<created>" + formatter.format(creationTime)
					+ "</created>");
			out.println("\t<lastaccessed>" + formatter.format(lastAccessed)
					+ "</lastaccessed>");
			Enumeration names = session.getAttributeNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				out.println("\t\t<" + name + ">" + session.getAttribute(name)
						+ "</" + name + ">");
			}
			out.println("\t</session>");
		}
		out.println("</data>");
	}

	public void doFileUpload(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		// Setup the various objects used during this upload operation
		// Commons file upload classes are specifically instantiated
		File disk = null;
		FileItem item = null;
		FileItemFactory factory = new DiskFileItemFactory();
		Iterator iter = null;
		List items = null;
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// Parse the incoming HTTP request
			// Commons takes over incoming request at this point
			// Get an iterator for all the data that was sent
			// TODO: Leverage generics
			items = upload.parseRequest(request);
			iter = items.iterator();
			logger.info("found " + items.size() + " file items");

			// Set a response content type
			response.setContentType("text");

			// Setup the output stream for the return XML data
			// out.println( "<response>" );
			logger.info("has next " + iter.hasNext());

			// Iterate through the incoming request data
			while (iter.hasNext()) {

				// Get the current item in the iteration
				item = (FileItem) iter.next();
				logger.info("file chunk " + item.getContentType());
				// If the current item is an HTML form field
				if (item.isFormField()) {
					// Return an XML node with the field name and value
					// out.println( "<field name=" + item.getFieldName() +
					// " value=" + item.getString() + " />" );

					// If the current item is file data
				} else {
					String fileName = "/home/jill/web/public/" + parts[1] + "/"
							+ item.getName();
					logger.info("File Upload(" + fileName + ")");
					disk = new File(fileName);
					item.write(disk);

					// Return an XML node with the file name and size (in bytes)
					out.println("File " + item.getName()
							+ " successfully uploaded.  Size was"
							+ item.getSize() + " bytes.");
				}
			}

			// Close off the response XML data and stream
			out
					.println("Press OK to continue.  Don't forget to press the Save button.");
			out.close();
			// Rudimentary handling of any exceptions
			// TODO: Something useful if an error occurs
		} catch (FileUploadException fue) {
			logger.error("fue - " + fue.getMessage());
			fue.printStackTrace();
		} catch (IOException ioe) {
			logger.error("ioex - " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (Exception e) {
			logger.error("ex - " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void doUpdateOptions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger.info("updateOptions()");
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		String partner = parts[2];

		String message = "";

		try {
			BufferedReader reader = request.getReader();
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {
				message += inputLine + "\n";
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("post-data=" + message + "<");

		Document document = null;
		DocumentBuilder builder;
		DocumentBuilderFactory factory;

		// set content type and other response header fields first
		response.setContentType("text/xml; charset=utf-8");
		response.addHeader("Cache-Control", "no-cache");
		response.setBufferSize(10000000);

		InputSource is = new InputSource(new StringReader(message));
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);

		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(is);
		} catch (ParserConfigurationException e) {
			logger.error("Parser Ex - ",e);
		} catch (SAXException e) {
			logger.error("SAX Ex - " , e);
		} catch (IOException e) {
			logger.error("IO Ex - " , e);
		}

		logger.info("Document:" + document.getTextContent());
		Element table = document.getDocumentElement();
		logger.info("Table Tag:" + table.getTagName());

		NodeList fieldList = table.getChildNodes();
		int numfield = fieldList.getLength();
		String updateClause = "UPDATE " + table.getTagName() + " set ";
		String whereClause = " WHERE partner_domain_id = '";
		int fieldnum = 0;
		for (int c = 0; c < numfield; c++) {
			Node field = fieldList.item(c);
			logger.info("field " + field.getNodeName() + "="
					+ field.getTextContent() + "  isKey="
					+ Database.isKey(table.getTagName(), field.getNodeName()));
			if ("partner_domain_id".equals(field.getNodeName())) {
				whereClause += field.getTextContent() + "' ";
			} else {
				if (fieldnum++ > 0)
					updateClause += ", ";
				updateClause += field.getNodeName() + "='"
						+ field.getTextContent() + "'";
			}
		}
		getDataService().updatedynamictable(updateClause + whereClause);
		// db_.executeUpdate(updateClause+whereClause, true) ;

		/*
		 * String opString = requestElement.getAttribute("op") ; String
		 * modeString = requestElement.getAttribute("mode") ; String scopeString
		 * = requestElement.getAttribute("scopeid") ; String session =
		 * requestElement.getAttribute("sess") ;
		 */

	}

}
