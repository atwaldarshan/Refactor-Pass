package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPMemberNetworkDataObject;
import com.payparade.dataobject.PPMemberShipDataObject;
import com.payparade.dataobject.PPUserDataObject;
import com.payparade.service.AuthService;
import com.payparade.util.Activity;
import com.payparade.util.OAuth2;
import com.payparade.util.StringUtils;

public class AuthController extends PassController {
	// static Logger logger = Logger.getLogger("AuthController");
	private AuthService authService;
	protected boolean open_ = true;
	public static final String ENCODING = "UTF-8";
	private PPMemberShipDataObject ppMemberShipDataObject;
	private PPMemberNetworkDataObject ppMemberNetworkDataObject;

	public PPMemberShipDataObject getPpMemberShipDataObject() {
		return ppMemberShipDataObject;
	}

	public void setPpMemberShipDataObject(
			PPMemberShipDataObject ppMemberShipDataObject) {
		this.ppMemberShipDataObject = ppMemberShipDataObject;
	}

	public boolean isOpen_() {
		return open_;
	}

	public void setOpen_(boolean open_) {
		this.open_ = open_;
	}

	public PPMemberNetworkDataObject getPpMemberNetworkDataObject() {
		return ppMemberNetworkDataObject;
	}

	public void setPpMemberNetworkDataObject(
			PPMemberNetworkDataObject ppMemberNetworkDataObject) {
		this.ppMemberNetworkDataObject = ppMemberNetworkDataObject;
	}

	public AuthService getAuthService() {
		return authService;
	}

	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		boolean done = true;
		logger.info(controllerName + ".get received >" + resourceString
				+ "< to process");

		try {
			if (isSigned || open_) {
				if (resourceString == null) {
					logger.error("null resource string received");
					done = false;
				} else if ("facebook".equals(resourceString))
					getFaceBookConnectAuth(request, response, out);
				else if ("login".equals(resourceString))
					getLoginAuth(request, response, out);
				else if ("dashboard".equals(resourceString))
					getDashboardAuth(request, response, out);
				else if ("myspace".equals(resourceString))
					getMySpaceAuth(partnerCode, request, partnerHost);
				else if (resourceString.startsWith("myspace/authorized"))
					getMySpaceAuthorized(request, response, out); // WARNING
																	// partnerCode
																	// is
																	// invalid
																	// in this
																	// case
				else if ("myspace/connect/close".equals(resourceString))
					getMySpaceConnectClose(request, response, out);
				else if ("myspace/unavailable".equals(resourceString))
					getMySpaceUnavailable(request, response, out);
				else {
					done = false;
				}
			} else {
				logger.warn("unsigned request NOT processed");
			}
		} catch (Exception e) {
			logger.error(e.getClass().getSimpleName() + " - " + e.getMessage());
		}

		if (debug_ && !done) {
			logger.info(controllerName + ".get did NOT process - "
					+ request.getRequestURI());
		}
		return done;
	}

	public void getFaceBookConnectAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		/*
		 * Commented By: Devarshi R Pandya
		 * out.print("<html><body><script</body></html>") ;
		 */
		/*
		 * Programmer:Devarshi R Pandya Purpose: Implemented Task 5
		 * Date:03.June.2010 11:18 AM
		 */
		logger.info(" Inside getFaceBookConnectAuth() of Auth Class ");
		String socialId = getSocid(request);
		String socialNetwork = getSocnet(request);
		int networkcodeid = getAuthService().getNetworkId(socialNetwork);
		List<PPConnectionDataObject> ppConnectionDataObjectList = getAuthService()
				.setSelectfrom_pp_connection(networkcodeid, socialId);
		/*
		 * String query =
		 * "SELECT * FROM pp_connection where network_code = \""+socialNetwork
		 * +"\" AND social_id = \""+socialId+"\""; DatabaseResult memberNetwork
		 * = db_.executeQuery(query) ; DataSet getResult =
		 * memberNetwork.getDataSet() ;
		 */
		if (ppConnectionDataObjectList != null
				&& ppConnectionDataObjectList.size() > 0) {
			response.getWriter().print(200);
		} else {
			response.getWriter().print(400);
		}

		logger.info(" Outside getFaceBookConnectAuth() of Auth Class ");
	}

	public void getConnectAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		out.print("<html><body>OK</body></html>");
	}

	public void getMySpaceAuthorized(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		logger
				.info(" Inside  getMySpaceAuthorized(HttpServletRequest,HttpServletResponse,PrintWriter) of AuthController class");
		OAuth2 restClient = new OAuth2();
		String session = request.getParameter("session");
		HashMap<String, String> accessArgs = restClient.getAccessToken(session);
		logger.info("getMySpaceAuthorized( session=" + session + ")");
		logger.logHashMap(accessArgs);
		out
				.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">");
		out.print("  <body> ");
		out.print("<head>");
		out
				.print("  <script src=\"http://"
						+ hostname_
						+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>");
		out.print("</head>");
		out.print("  <script> ");
		out.print("   ");
		out.print("  window.location = \"http://"
				+ OAuth2.getPartnerHost(session)
				+ "/pass/auth/myspace/connect/close?t="
				+ StringUtils.percentEncode(accessArgs.get("oauth_token"),
						ENCODING)
				+ "&s="
				+ StringUtils.percentEncode(accessArgs
						.get("oauth_token_secret"), ENCODING) + "\" ; ");
		out.print("  </script></body></html> ");
		logger
				.info(" Outside  getMySpaceAuthorized(HttpServletRequest,HttpServletResponse,PrintWriter) of AuthController class");
	}

	public void getLoginAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		out.print("<html><body>OK</body></html>");
	}

	private void getMySpaceUnavailable(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) {
		logger.info(" Inside getMySpaceUnavailable() of AuthController class ");
		out
				.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" >"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" >"
						+ "<head>"
						+ "<script> self.resizeTo(500,300);  </script>"
						+ "</head>"
						+ "<body><h2>Sorry - MySpace is currently unavailable</h2>"
						+ "<center><button onclick=\"javascript:window.close();\" type=\"button\">Close</button></center>"
						+ "</body></html>");
		logger
				.info(" Outside getMySpaceUnavailable() of AuthController class ");
	}

	public void getMySpaceConnectClose(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		logger
				.info(" Inside getMySpaceConnectClose(HttpServletRequest,HttpServletResponse,PrintWriter) of AuthController class ");
		logger.info("getMySpaceConnectClose(t:" + request.getParameter("t")
				+ "  s:" + request.getParameter("s") + ")");

		OAuth2 restClient = new OAuth2();
		// String session = request.getSession(true).getId() ;
		String hostName = (String) request.getSession(true).getAttribute(
				"partnerhost");
		String partnerCode = (String) request.getSession(true).getAttribute(
				"partnercode");

		String responseString = null;

		JSONObject data = restClient.getMyData(request.getParameter("t"),
				request.getParameter("s"), partnerCode);
		if (data != null) {
			logger.logJSONObject(data);

			try {
				String idString = (String) data.get("id");
				int i = idString.indexOf(":");
				if (i > -1)
					idString = idString.substring(i + 1);

				logger.info("host:" + hostName + "   partner:" + partnerCode
						+ "  id:" + idString);
				responseString = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" >"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" >"
						+ "<head>"
						+ "  <script src=\"http://"
						+ hostname_
						+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
						+ "  <script> "
						+ "  ppCreateCookie('pp_oauth_token','"
						+ request.getParameter("t")
						+ "',0) ;"
						+ "  ppCreateCookie('pp_oauth_token_secret','"
						+ request.getParameter("s")
						+ "',0) ;"
						+ "  ppCreateCookie('pp_myspace_id','"
						+ idString
						+ "',0) ;"
						+ "  </script> "
						+ "</head>"
						+ "<body><script language=\"javascript\"> connect_callback('myspace') ; top.close();</script></body></html>";
			} catch (JSONException e) {
				logger.error(e.getClass().getSimpleName() + " - "
						+ e.getMessage());
				responseString = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" >"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" >"
						+ "<head>"
						+ "  <script src=\"http://"
						+ hostname_
						+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
						+ "</head>"
						+ "<body><script language=\"javascript\"> top.close();</script></body></html>";
			}
		}
		out.print(responseString);
		request.getSession().removeAttribute("oauth_url");
		logger
				.info(" Outside getMySpaceConnectClose(HttpServletRequest,HttpServletResponse,PrintWriter) of AuthController class ");
	}

	public void doConnect(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(" Inside doConnect() of AuthController Class ");
		if (myCookies.get("pp_social_network") != null
				&& myCookies.get("pp_social_network").toString().equals("") == false)
			ppMemberNetworkDataObject
					.setNetwork_code_id(getAuthService().getNetworkId(
							myCookies.get("pp_social_network").toString()));
		else
			ppMemberNetworkDataObject.setNetwork_code_id(1);
		
		logger.info("Network Id is::"+ppMemberNetworkDataObject.getNetwork_code_id());
		logger.info("myCookies.get(pp_social_network):::"+myCookies.get("pp_social_network"));
		logger.info("myCookies.get(pp_social_network):::"+myCookies.get("pp_social_network"));
		String id = "pp_" + myCookies.get("pp_social_network") + "_id";
		logger.info("Id is::"+id);
		if (myCookies.get("pp_" + myCookies.get("pp_social_network") + "_id") != null
				&& myCookies.get(
						"pp_" + myCookies.get("pp_social_network") + "_id")
						.equals("") == false)
			ppMemberNetworkDataObject.setMember_social_network_id((myCookies
					.get("pp_" + myCookies.get("pp_social_network") + "_id")));
		getAuthService().insertintopp_member_network(ppMemberNetworkDataObject);
		logger.info(" Outside doConnect() of AuthController Class ");
	}

	public void init() {
		controllerName = "auth";
		super.init();
	}

	public static String getMySpaceAuth(String partnerCode,
			HttpServletRequest request, String partnerHost) throws IOException {
		logger.info("Inside getMySpaceAuth() of AuthController class");
		HttpSession session = request.getSession(true);
		String urlString = null;
		if (session.getLastAccessedTime() + 30000 < System.currentTimeMillis()) {
			logger.info("refreshing old oauth_token last accessed "
					+ (session.getLastAccessedTime() - System
							.currentTimeMillis()) + " seconds ago");
			urlString = (String) session.getAttribute("oauth_url");
		}
		logger.info("getMySpaceAuth");
		logger.info("Session oauthurl was " + urlString);
		if (urlString == null) {
			OAuth2 restClient = new OAuth2();
			logger.info("ref:" + request.getHeader("referer") + "  x:"
					+ request.getHeader("referer"));
			logger.info("Value of partnerCode is::" + partnerCode);
			logger.info("Value of partnerHost is::" + partnerHost);
			HashMap<String, String> req_args = restClient.getRequestToken(
					session.getId(), partnerCode, partnerHost);
			if (req_args.size() > 0) {
				urlString = restClient.authorize(req_args, hostname_,
						partnerHost, partnerCode, session.getId());
				logger.info("auth URL(" + urlString + ")");
				session.setAttribute("oauth_url", urlString);
			} else {
				urlString = "http://" + hostname_
						+ "/pass/auth/myspace/unavailable";
			}
		}
		logger.info("UrlString value is:::" + urlString);
		logger.info("Outside getMySpaceAuth() of AuthController class");
		return urlString;
	}

	public void getDashboardAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) throws IOException {
		String password = request.getParameter("password");
		String userid = request.getParameter("userid");
		logger.info("User Id is::" + userid);
		logger.info("Pass Word is::" + password);
		List<PPUserDataObject> ppUserDataObjectList = getAuthService()
				.PPUserDataObjectList(userid, password);
		/*
		 * DatabaseResult results =
		 * db_.executeQuery("SELECT user_email FROM pp_user WHERE user_email = '"
		 * +userid+"' AND password_hash ='"+password+"' ; " ) ; DataSet dataset
		 * = results.getDataSet() ;
		 */
		if (ppUserDataObjectList != null && ppUserDataObjectList.size() > 0)
			out.print("<authorized>yes</authorized>");
		else
			out.print("<authorized>no</authorized>");
	}

	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		logger.info(" Inside post() of  AuthController class ");
		logger.info(" value of resourceString is::" + resourceString);
		boolean done = true;
		try {
			if (isSigned || open_) {
				if (resourceString.startsWith("connect")) {
					doConnect(request, response, out, resourceString, isSigned,
							myCookies, partnerCode);
				} else if (resourceString.startsWith("disconnect")) {
					doDisconnect(request, response, out, resourceString,
							isSigned, myCookies, partnerCode);
				} else {
					done = false;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		if (debug_ && !done) {
			logger.info(controllerName + ".post did NOT process - >"
					+ resourceString + "<");
		}

		logger.info(" value of done is::" + done);
		logger.info(" Outside post() of  AuthController class ");
		return done;
	}

	public void doConnect(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws IOException {
		logger.info(" Inside doConnect() of AuthController class ");
		try {
			String socialNetwork = myCookies.get("pp_social_network");
			String socialId = myCookies.get("pp_" + socialNetwork + "_id");

			if (debug_) {
				logger.info("doConnect from "
						+ socialNetwork
						+ " for id="
						+ myCookies.get("pp_"
								+ myCookies.get("pp_social_network") + "_id")
						+ " signed:" + isSigned);
			}

			if (socialId == null) {
				logger.warn("ignoring connect for null social id");
			} else {
				// recordActivity( request, "Connect", socialNetwork,
				// partnerCode ) ;
				recordActivity(request, Activity.CONNECT, socialNetwork,
						partnerCode);
				int partnerdomainid = getAuthService().getPartnerDomainId(
						partnerCode);
				if (isSigned | open_) {
					logger.info("Resource String is::" + resourceString);
					int last = resourceString.lastIndexOf("/");
					logger.info("last::" + last);
					String partnersKey = resourceString.substring(last + 1);
					logger.info("partnersKey value is::" + partnersKey);
					int networkcodeid = getAuthService().getNetworkId(
							socialNetwork);
					List<PPMemberNetworkDataObject> ppMemberNetworkDataObjectList = getAuthService()
							.Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid(
									networkcodeid, socialId);
					// DatabaseResult memberNetwork = db_.executeQuery(
					// "SELECT * FROM pp_member_network where network_code = \""+socialNetwork+"\" AND network_sk = \""+socialId+"\"")
					// ;
					// logger_.logHashMap(memberNetwork) ;
					// DataSet memberNetworkDataSet = memberNetwork.getDataSet()
					// ;
					if (ppMemberNetworkDataObjectList != null
							&& ppMemberNetworkDataObjectList.size() > 1) {
						logger
								.error("Dataset of existing members contained more than one row for key:"
										+ socialNetwork + "/" + socialId);
						// logger.logArrayList(memberNetworkDataSet) ;
					} else if (ppMemberNetworkDataObjectList != null
							&& ppMemberNetworkDataObjectList.size() == 1) {
						PPMemberNetworkDataObject ppMemberNetworkDataObject = ppMemberNetworkDataObjectList
								.get(0);
						// String memberId =
						// memberNetworkDataRow.get("member_id").toString() ;
						int memberId = (ppMemberNetworkDataObject
								.getMemeber_id());
						logger
								.info("FOUND existing member(" + memberId
										+ ") for key:" + socialNetwork + "/"
										+ socialId);

						// DatabaseResult membership = db_.executeQuery(
						// "SELECT * FROM pp_membership WHERE partner_domain = \""+partnerCode+"\" AND member_id = \""+memberId+"\"")
						// ;
						// DataSet memberData = membership.getDataSet() ;
						List<PPMemberShipDataObject> ppMemberShipDataObjectList = getAuthService()
								.Select_from_ppmembership_where_partnerdomainid_memberid(
										partnerdomainid, memberId);
						if (ppMemberShipDataObjectList != null
								&& ppMemberShipDataObjectList.size() > 1) {
							logger
									.error("Dataset of existing memberships contained more than one row for key:"
											+ partnerCode + "/" + memberId);
							// logger_.logArrayList(memberData) ;
						} else if (ppMemberShipDataObjectList != null
								&& ppMemberShipDataObjectList.size() == 1) {
							getPpMemberShipDataObject().setPartners_social_key(
									partnersKey);
							getPpMemberShipDataObject().setPartner_domain_id(
									partnerdomainid);
							logger.info("UPDATING existing membership for key:"
									+ partnerCode + "/" + memberId);
							// db_.executeUpdate("Update pp_membership SET partners_sk = \""+partnersKey+"\" WHERE  partner_domain = \""+partnerCode+"\" AND member_id = \""+memberId+"\"")
							// ;
							getAuthService().update_ppmembership(
									getPpMemberShipDataObject());
						} else if (ppMemberShipDataObjectList != null
								&& ppMemberShipDataObjectList.size() > 0) {
							logger.error("INSERTING new membership for key:"
									+ partnerCode + "/" + memberId);
							/*
							 * db_.executeUpdate("INSERT INTO pp_membership (partner_domain, member_id, partners_sk, connected_at ) "
							 * +
							 * " VALUES ( \""+partnerCode+"\", \""+memberId+"\", \""
							 * +partnersKey+"\", NOW() ) ;  ") ;
							 */

							getPpMemberShipDataObject().setMemeber_id(memberId);
							getPpMemberShipDataObject().setPartner_domain_id(
									partnerdomainid);
							getPpMemberShipDataObject().setPartners_social_key(
									partnersKey);
							getAuthService().insertinto_ppmembership(
									ppMemberShipDataObject);
						}
					} else if (ppMemberNetworkDataObjectList != null
							&& ppMemberNetworkDataObjectList.size() == 0) {
						getAuthService().insertinto_pp_member(partnerdomainid);
						// db_.executeUpdate("INSERT INTO pp_member(member_id, orig_partner) VALUES (NULL, \""+partnerCode+"\");")
						// ;
						/*
						 * db_.executeUpdate("INSERT INTO pp_membership (partner_domain, member_id, partners_sk, connected_at ) "
						 * +
						 * " VALUES ( \""+partnerCode+"\", LAST_INSERT_ID(), \""
						 * +partnersKey+"\", NOW() ) ;  ") ;
						 */
						getPpMemberShipDataObject().setPartners_social_key(
								partnersKey);
						getPpMemberShipDataObject().setPartner_domain_id(
								partnerdomainid);
						getAuthService().insertinto_ppmembershipv1(
								ppMemberShipDataObject);
						networkcodeid = getAuthService().getNetworkId(
								myCookies.get("pp_social_network"));
						if (myCookies.get("pp_"
								+ myCookies.get("pp_social_network") + "_id") != null
								&& myCookies
										.get(
												"pp_"
														+ myCookies
																.get("pp_social_network")
														+ "_id").equals("") == false)
							getPpMemberNetworkDataObject()
									.setMember_social_network_id(
											(myCookies
													.get("pp_"
															+ myCookies
																	.get("pp_social_network")
															+ "_id")));
						getPpMemberNetworkDataObject().setNetwork_code_id(
								networkcodeid);
						getAuthService().insertinto_ppmembernetwork(
								getPpMemberNetworkDataObject());
						/*
						 * db_.executeUpdate("INSERT INTO pp_member_network ( member_id, network_code, network_sk, connected_at ) "
						 * +" VALUES ( LAST_INSERT_ID(), \""+myCookies.get(
						 * "pp_social_network"
						 * )+"\", \""+myCookies.get("pp_"+myCookies
						 * .get("pp_social_network")+"_id")+"\", NOW() ) ;  ") ;
						 */
					}
					messageResponse(out, "OK");
				} else {
					messageResponse(out, "Auth Failed");
					response.setStatus(403);
				}
			}
		} catch (Exception e) {
			logger.error("doConnect::AuthController::Exception is::", e);
		}
		logger.info(" Outside doConnect() of AuthController class ");
	}

	public void doDisconnect(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws IOException {
		logger.info(" Inside doDisconnect() of AuthController class ");
		if (debug_) {
			logger.info("doDisconnect from "
					+ myCookies.get("pp_social_network")
					+ " for id="
					+ myCookies.get("pp_" + myCookies.get("pp_social_network")
							+ "_id") + " signed:" + isSigned);
		}
		isSigned = true;
		if (isSigned) {
			// int last = resourceString.lastIndexOf("/") ;
			// String partnersKey = resourceString.substring(last+1) ;
			int partnerdomainid = getAuthService().getPartnerDomainId(
					partnerCode);
			getPpMemberShipDataObject().setPartner_domain_id(partnerdomainid);
			// db_.executeUpdate("UPDATE pp_membership set disconnected_at = NOW() WHERE partners_sk = \""+partnerCode+"\" ; ")
			// ;
			getAuthService().update_ppmembershipv1(getPpMemberShipDataObject());
			messageResponse(out, "OK");
		} else {
			messageResponse(out, "Auth Failed");
			response.setStatus(403);
		}
		logger.info(" Outside doDisconnect() of AuthController class ");
	}

}
