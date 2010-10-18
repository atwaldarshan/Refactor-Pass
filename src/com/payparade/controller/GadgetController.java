package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.payparade.dataobject.PPMessagesDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPPartnerNetworkDataObject;
import com.payparade.dataobject.PPPartnerOptionDataObject;
import com.payparade.dataobject.PPShareLinkDataObject;
import com.payparade.service.GadgetService;
import com.payparade.util.Activity;
import com.payparade.util.Client;
import com.payparade.util.OAuth2;

public class GadgetController extends PassController {

	private HashMap<String, String> sharedUrls_ = new HashMap<String, String>();
	private HashMap<String, String> sharedContent_ = new HashMap<String, String>();
	private HashMap<String, String> sharedTitle_ = new HashMap<String, String>();
	private HashMap<String, String> sharedImageLink_ = new HashMap<String, String>();
	private HashMap<String, String> mySpaceApps_ = new HashMap<String, String>();
	private PPShareLinkDataObject ppShareLinkDataObject;
	private GadgetService gadgetService;

	public PPShareLinkDataObject getPpShareLinkDataObject() {
		return ppShareLinkDataObject;
	}

	public void setPpShareLinkDataObject(
			PPShareLinkDataObject ppShareLinkDataObject) {
		this.ppShareLinkDataObject = ppShareLinkDataObject;
	}

	public GadgetService getGadgetService() {
		return gadgetService;
	}

	public void setGadgetService(GadgetService gadgetService) {
		this.gadgetService = gadgetService;
	}

	@Override
	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		logger.info(" Inside get() of GadgetController class ");
		boolean done = true;

		try {
			if (resourceString == null) {
				logger.error("null resource string received");
				done = false;
			} else if ("disconnect".equals(resourceString)) {
				getSocialDisconnect(request, response, out, partnerCode,
						myCookies);
			} else if ("multispot".equals(resourceString)) {
				getMultispot(request, response, out, partnerCode, partnerHost,
						myCookies);
			} else if ("socialoptions/connect".equals(resourceString)) {
				getSocialConnectOptions(request, response, out, partnerCode,
						partnerHost);
			} else if ("connect/myspace".equals(resourceString)) {
				connectMySpace(request, response, out, partnerCode, partnerHost);
			} else if ("socialoptions/login".equals(resourceString)) {
				getSocialLoginOptions(request, response, out, partnerCode,
						partnerHost);
			} else if ("socialoptions/share".equals(resourceString)) {
				getSocialShareOptions(request, response, out, partnerCode,
						partnerHost);
			} else if ("socialoptions/logout".equals(resourceString)) {
				getSocialLogoutOptions(request, response, out, partnerCode,
						myCookies);
			} else if ("auth/facebook".equals(resourceString)) {
				getFacebookAuth(request, response, out, partnerCode);
			} else if ("login/facebook".equals(resourceString)) {
				getFacebookLogin(request, response, out, partnerCode);
			} else if (resourceString.startsWith("badge")) {
				getSocialBadge(request, response, out, partnerCode, myCookies);
			} else if (resourceString.startsWith("logout")) {
				getSocialLogout(request, response, out, partnerCode, myCookies);
			} else if (resourceString.startsWith("init")) {
				getInit(request, response, out, partnerCode, myCookies);
			} else if (resourceString.startsWith("share")) {
				getSocialShare(request, response, out, partnerCode, myCookies);
			} else if (resourceString.startsWith("message/url")) {
				getMessageUrl(request, response, out, partnerCode, myCookies,
						resourceString);
			} else if (resourceString.startsWith("message/title")) {
				getMessageTitle(request, response, out, partnerCode, myCookies,
						resourceString);
			} else if (resourceString.startsWith("message/body")) {
				getMessageBody(request, response, out, partnerCode, myCookies,
						resourceString);
			} else if (resourceString.startsWith("sent")) {
				getSent(request, response, out, partnerCode, myCookies,
						resourceString);
			} else if (resourceString.startsWith("posted")) {
				getPosted(request, response, out, partnerCode, myCookies,
						resourceString);
			} else if (resourceString.startsWith("status")) {
				getStatus(request, response, out, partnerCode, myCookies);
			}
			// else if ( resourceString.startsWith("link") ) {
			// getStoredLink(request, response, out, partnerCode, myCookies,
			// resourceString) ; }
			else if ("meter/user".equals(resourceString)) {
				getUserMeter(request, response, out, partnerCode);
			} else if ("meter/request".equals(resourceString)) {
				getRequestMeter(request, response, out, partnerCode);
			} else if ("meter/response".equals(resourceString)) {
				getResponseMeter(request, response, out, partnerCode);
			} else if ("idashboard/ops".equals(resourceString)) {
				iOpsDashboard(request, response, out, partnerCode, partnerHost);
			} else if (resourceString.startsWith("apikey")) {
				getapikey(request, response, out, partnerCode, partnerHost);
			} else if (resourceString.startsWith("facebookconnectoption")) {
				displayedFacebookConnectedOptions(request, out, partnerCode,
						partnerHost);
			} else if (resourceString.startsWith("facebookbadge")) {
				displayedFacebookBadge(request, out, partnerCode, myCookies);
			} else {
				done = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(" Outside get() of GadgetController class ");
		return done;
	}

	public void getSocialDisconnect(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		// recordActivity( request, "Logout") ;
		recordActivity(request, Activity.LOG_OUT);// Logout

		if ("facebook".equals(myCookies.get("pp_social_network"))
				|| "myspace".equals(myCookies.get("pp_social_network"))) {
			out.print(getIframeHead(partnerCode));
			out.print("<body>" + "  <script> "
					+ "  ppDeleteCookie('pp_social_network') ;"
					+ "  ppDeleteCookie('pp_" + getSocnet(request) + "_id') ;"
					+ "  </script> " + "Disconnected" + "</body>" + "</html>");
		} else {
			logger.info("No Social Network");
		}
	}

	public void getMultispot(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost, HashMap<String, String> myCookies)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		PPPartnerNetworkDataObject partnerNetworkDataObject = null;
		if ("facebook".equals(myCookies.get("pp_social_network"))) {

			if ("yes".equals(session.getAttribute("connect"))) {
				// recordActivity( request, "Connect") ;
				recordActivity(request, Activity.CONNECT);
				session.removeAttribute("connect");
			}
			// recordActivity( request, "Badge") ;
			recordActivity(request, Activity.BADGE);
			PPPartnerOptionDataObject partnerOptionDataObject = getPartnerOptionString(partnerCode);
			// String salutation = getPartnerOptionString(partnerCode,
			// "badge_salutation") ;
			String salutation = partnerOptionDataObject.getBadge_salutation();
			logger.info("getMultiSpot retrieved badge_salutation = "
					+ salutation);

			String friendsClause = "";
			if (haveFriends(request)) {
				friendsClause = "FB.Facebook.apiClient.friends_get(null, function(result){ ppTrace(result) ; ppRegisterNetwork(result); } ) ; ";
			}

			out.print(getIframeHead(partnerCode));
			int partnerdomainid = getGadgetService()
					.select_id_from_pp_partner_domain(getPartnerCode(request));
			logger.info(" Partner Domain Id is::" + partnerdomainid);
			partnerNetworkDataObject = getPartnerNetworkService()
					.getPPPartnerNetwork().get(partnerdomainid);
			out
					.print("<body>"
							+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
							+ "<div id=\"pp_social_multispot_badge_div\" >"
							+ "<div id=\"pp_profile_picture_div\" >"
							+ " <fb:profile-pic uid=\""
							+ myCookies.get("pp_"
									+ myCookies.get("pp_social_network")
									+ "_id")
							+ "\" facebook-logo=true linked=\"false\" width=\"35\" height=\"35\"></fb:profile-pic>"
							+ "</div>"
							+ "<div id=\"pp_profile_salutation_div\" >"
							+ salutation
							+ ", <fb:name uid=\""
							+ myCookies.get("pp_"
									+ myCookies.get("pp_social_network")
									+ "_id")
							+ "\" useyou=false firstnameonly=true linked=false></fb:name>.<br />"
							+ "</div>"
							+ "</div>"
							+ "<script type=\"text/javascript\"> "
							+ "FB_RequireFeatures([\"XFBML\"], function() { "
							+
							// "  FB.Facebook.init(\""+ids_.get(partnerCode)+"\", \"xd_receiver.htm\"); "
							// +Devarshi Pandya
							"  FB.Facebook.init(\""
							+ partnerNetworkDataObject.getApi_key()
							+ "\", \"xd_receiver.htm\"); "
							+ "  FB.Facebook.get_sessionState().waitUntilReady(function() { "
							+ "    FB.XFBML.Host.parseDomTree();"
							+ "    }); "
							+ "  }); "
							+ "FB.ensureInit( pp_update_badge, pp_login_user ) ;"
							+ friendsClause
							+ "</script>"
							+ "</body>"
							+ "</html>");
		} else {
			session.setAttribute("connect", "yes");

			// recordActivity( request, "Connect Option") ;
			recordActivity(request, Activity.CONNECT_OPTION);
			out.print(getIframeHead(partnerCode));
			out.print("  <body> ");
			out
					.print("  <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>");
			out.print("  <div id=\"pp_social_multispot_connect_div\" >");
			// out.print(
			// "    <fb:login-button onlogin=\"connect_callback('facebook');\"  "+connectSize_.get(partnerCode)+"></fb:login-button>");
			out
					.print("    <fb:login-button onlogin=\"connect_callback('facebook');\"  "
							+ getGadgetService().getConnectSize_().get(
									partnerCode) + "></fb:login-button>");
			// out.print(
			// "    <script type=\"text/javascript\"> FB.init(\""+ids_.get(partnerCode)+"\", \"xd_receiver.htm\"); </script> ");
			out.print("    <script type=\"text/javascript\"> FB.init(\""
					+ partnerNetworkDataObject.getApi_key()
					+ "\", \"xd_receiver.htm\"); </script> ");
			out.print(" </div>");
			out.print(" </body>");
			out.print(" </html>");
		}
	}

	public String getIframeHead(String partnerCode) {
		// hostname_="dev2.payparade.net";
		String result="";
		try{
		logger.info("Inside getIframeHead() of GadgetController class");
		logger.info("Partner Code::"+partnerCode);
		logger.info("gadgetService.getPartnerCSS_().get(partnerCode)::"+gadgetService.getPartnerCSS_().get(partnerCode));
		 result = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
				+ "  <head> "
				+ "  <script src=\"http://"
				+ hostname_
				+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>";
		if (gadgetService.getPartnerCSS_().get(partnerCode) != null) {
			// getPartnerOptionString(partnerCode, "css_file" )
			PPPartnerOptionDataObject partnerOptionDataObject = getPartnerOptionString(partnerCode);
			result += "<link rel=\"stylesheet\" type=\"text/css\" href=\"http://"
					+ hostname_
					+ "/css/"
					+ partnerOptionDataObject.getCss_file() + "\" />";
			result += "  </head> ";
		}
		logger.info("Outside getIframeHead() of GadgetController class");
		}catch(Exception e){
			logger.error("GadgetController::getIframeHead::Exception is---->",e);
		}
		return result;
	}

	public void getSocialConnectOptions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		// recordActivity( request, "Connect Option") ;Devarshi Pandya
		recordActivity(request, Activity.CONNECT_OPTION);
		session.setAttribute("connect", "yes");
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		out.print(getIframeHead(partnerCode));
		out.print("  <body> ");
		out
				.print("  <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>");
		out.print("<table><tr>");
		if (mySpaceApps_.get(partnerCode) != null)
			out
					.print("  <td width=\"50%\"><a href=\"javascript: parent.document.getElementById('pp_social_connect_options_div').style.display='transparent' ; window.open ('http://"
							+ partnerHost
							+ "/pass/gadget/connect/myspace', 'newwindow', config='height=700, width=975, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');\" ><img src=\"http://developerwiki.myspace.com/uploads/3/36/Myspaceid.jpg\" border=\"0\" height=\"30px\" width=\"95px\" alt=\"Link with MySpace!\"/></a></td>");
		// out.print(
		// "  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  "+connectSize_.get(partnerCode)+"></fb:login-button>&nbsp;<a href=\"#\" onclick=\"FB.Connect.requireSession();\"> "+connectText_.get(partnerCode)+"</a> </td>");
		// Devarshi Pandya
		out
				.print("  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  "
						+ getGadgetService().getConnectSize_().get(partnerCode)
						+ "></fb:login-button>&nbsp;<a href=\"#\" onclick=\"FB.Connect.requireSession();\"> "
						+ getGadgetService().getConnectText_().get(partnerCode)
						+ "</a> </td>");
		out.print(" </tr></table>");
		// out.print(
		// "  <script type=\"text/javascript\"> FB.init(\""+ids_.get(partnerCode)+"\", \"xd_receiver.htm\"); </script> ");
		// Devarshi Pandya
		out.print("  <script type=\"text/javascript\"> FB.init(\""
				+ partnerNetworkDataObject.getApi_key()
				+ "\", \"xd_receiver.htm\"); </script> ");
		out.print(" </body>");
		out.print(" </html>");
	}

	public void connectMySpace(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) throws ServletException, IOException {
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
		out.print("  window.location = \""
				+ AuthController.getMySpaceAuth(partnerCode, request,
						partnerHost) + "\" ; ");
		out.print("  </script></body></html> ");
	}

	public void getSocialLoginOptions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		// recordActivity( request, "Login Option") ;
		recordActivity(request, Activity.LOGIN_OPTION);
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		out.print(getIframeHead(partnerCode));
		out.print("  <body> ");
		out
				.print("  <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>");
		out.print("<table><tr>");
		if (mySpaceApps_.get(partnerCode) != null)
			out
					.print("  <td width=\"50%\"><a href=\"javascript: parent.document.getElementById('pp_social_connect_options_div').style.display='none' ; window.open ('http://"
							+ partnerHost
							+ "/pass/gadget/connect/myspace', 'newwindow', config='height=700, width=975, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');\" ><img src=\"http://developerwiki.myspace.com/uploads/3/36/Myspaceid.jpg\" border=\"0\" height=\"30px\" width=\"95px\" alt=\"Link with MySpace!\"/></a></td>");
		out
				.print("  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  size=\"large\" background=\"light\" length=\"long\"></fb:login-button> </td>");
		out.print(" </tr></table>");
		// out.print(
		// "  <script type=\"text/javascript\"> FB.init(\""+ids_.get(partnerCode)+"\", \"xd_receiver.htm\"); </script> ");
		// Devarshi Pandya
		out.print("  <script type=\"text/javascript\"> FB.init(\""
				+ partnerNetworkDataObject.getApi_key()
				+ "\", \"xd_receiver.htm\"); </script> ");
		out.print(" </body>");
		out.print(" </html>");
	}

	public void getSocialShareOptions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		// recordActivity( request, "Share Option") ;
		recordActivity(request, Activity.SHARE_OPTION);
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		out.print(getIframeHead(partnerCode));
		out.print("  <body> ");
		out
				.print("  <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>");
		out.print("<table><tr>");
		// TODO fix when myspace blocked out.print(
		// "  <td width=\"50%\"><a href=\"javascript: parent.document.getElementById('pp_social_connect_options_div').style.display='none' ; window.open ('"+Auth.getMySpaceAuth(partnerCode,
		// request,
		// partnerHost)+"', 'newwindow', config='height=700, width=975, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');\" ><img src=\"http://dev1.payparade.net/images/myspace/myspace_icon.png\" border=\"0\" alt=\"Link with MySpace!\"/></a></td>")
		// ;
		out
				.print("  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  size=\"large\" background=\"light\" length=\"long\"></fb:login-button> </td>");
		// out.print(
		// "  <script type=\"text/javascript\"> FB.init(\""+ids_.get(partnerCode)+"\", \"xd_receiver.htm\"); </script> ");
		// Devarshi Pandya
		out.print("  <script type=\"text/javascript\"> FB.init(\""
				+ partnerNetworkDataObject.getApi_key()
				+ "\", \"xd_receiver.htm\"); </script> ");
		out.print(" </tr></table>");
		out.print(" </body>");
		out.print(" </html>");
	}

	public void getSocialLogoutOptions(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		// recordActivity( request, "Logout Option") ;
		recordActivity(request, Activity.LOGOUT_OPTION);
		if ("facebook".equals(myCookies.get("pp_social_network"))) {
			out.print(getIframeHead(partnerCode));
			out
					.print("<body>"
							+ "<a href=\"/pass/gadget/logout\"><img id=\"fb_logout_image\" src=\"http://static.ak.fbcdn.net/images/fbconnect/logout-buttons/logout_small.gif\" alt=\"Logout\"/></a>"
							+ "</body>" + "</html>");
		} else if ("myspace".equals(myCookies.get("pp_social_network"))) {
		} else {
			logger.info("No Social Network");

		}
	}

	public void getFacebookAuth(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode)
			throws ServletException, IOException {

		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);

		out
				.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"  xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
						+ "<head>"
						+ "  <script src=\"http://"
						+ hostname_
						+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
						+ "</head>"
						+ "<body>"
						+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
						+ "<script type=\"text/javascript\"> "
						+ "FB_RequireFeatures([\"XFBML\"], function() { "
						+ "FB.Facebook.init(\""
						+ partnerNetworkDataObject.getApi_key()
						+ "\", \"xd_receiver.htm\"); "
						+ "FB.Connect.requireSession(); "
						+ " }); "
						+ "</script> " + "</body>" + "</html>");
	}

	public void getFacebookLogin(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode)
			throws ServletException, IOException {
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);

		out
				.print(" <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script><script type=\"text/javascript\"> FB.init(\""
						+ partnerNetworkDataObject.getApi_key()
						+ "\", \"xd_receiver.htm\", pp_require_session); </script> ");
	}

	public void getOldBadge(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode)
			throws ServletException, IOException {
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		out
				.print(" <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  <script type=\"text/javascript\"> FB.init(\""
						+ partnerNetworkDataObject.getApi_key()
						+ "\", \"xd_receiver.htm\", {\"ifUserConnected\" : update_badge})); </script> ");
	}

	public void getSocialBadge(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		HttpSession session = request.getSession(false);
		if ("yes".equals(session.getAttribute("connect"))) {
			// recordActivity( request, "Connect") ;
			recordActivity(request, Activity.CONNECT);
			session.removeAttribute("connect");
		}
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		// recordActivity( request, "Badge") ;
		recordActivity(request, Activity.BADGE);
		PPPartnerOptionDataObject partnerOptionDataObject = getPartnerOptionString(partnerCode);
		String salutation = partnerOptionDataObject.getBadge_salutation();
		// String salutation = getPartnerOptionString(partnerCode,
		// "badge_salutation") ;Devarshi
		logger
				.info("getSocialBadge retrieved badge_salutation = "
						+ salutation);

		// String message = getPartnerOptionString(partnerCode, "badge_message")
		// ; Devarshi
		String message = partnerOptionDataObject.getBadge_message();
		logger.info("getSocialBadge retrieved badge_salutation = " + message);

		String friendsClause = "";
		if (haveFriends(request)) {
			friendsClause = "FB.Facebook.apiClient.friends_get(null, function(result){"
					+ " ppTrace(result) ; ppRegisterNetwork(result); "
					+ "} ) ; ";
		}

		if ("facebook".equals(myCookies.get("pp_social_network"))) {

			out.print(getIframeHead(partnerCode));
			out
					.print("</head>"
							+ "<body>"
							+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
							+ "<div id=\"pp_social_badge_inner_div\">"
							+ "<div id=\"pp_profile_picture_div\">"
							+ " <fb:profile-pic uid=\""
							+ myCookies.get("pp_"
									+ myCookies.get("pp_social_network")
									+ "_id")
							+ "\" facebook-logo=true linked=\"false\"></fb:profile-pic>"
							+ "</div>"
							+ "<div id=\"pp_profile_salutation_div\">"
							+ "<span class=\"pp_badge_salutation\"> Welcome, <fb:name uid=\""
							+ myCookies.get("pp_"
									+ myCookies.get("pp_social_network")
									+ "_id")
							+ "\" useyou=false linked=false></fb:name></span><br />"
							+ "<span class=\"pp_badge_message\"> "
							+ message
							+
							// " You are signed in with<br />your Facebook account.<br />"+
							// " <span style=\"float:right\"><fb:login-button size=\"small\" background=\"white\" length=\"short\" autologoutlink=\"true\"></fb:login-button></span>"+
							"</span>"
							+ "</div>"
							+ "</div>"
							+ "<script type=\"text/javascript\"> "
							+ "FB_RequireFeatures([\"XFBML\"], function() { "
							+ "  FB.Facebook.init(\""
							+ partnerNetworkDataObject.getApi_key()
							+ "\", \"xd_receiver.htm\"); "
							+ "  FB.Facebook.get_sessionState().waitUntilReady(function() { "
							+ friendsClause
							+ "    FB.XFBML.Host.parseDomTree();"
							+ "    }); "
							+ "  }); "
							+ "FB.ensureInit( pp_update_badge, pp_login_user ) ;"
							+ "</script>" + "</body>" + "</html>");
		} else if ("myspace".equals(myCookies.get("pp_social_network"))) {
			OAuth2 restClient = new OAuth2();
			logger.info(myCookies.get("pp_oauth_token"));

			JSONObject data = restClient.getMyData(myCookies
					.get("pp_oauth_token"), myCookies
					.get("pp_oauth_token_secret"), partnerCode);
			try {
				out.print(getIframeHead(partnerCode));
				out
						.print("<body>"
								+ "<div id=\"pp_social_badge_inner_div\" style=position:absolute; top:0px; left:0px; z-index:10; padding:0px; margin:0px; border:0px;\" >"
								+ "<div id=\"pp_badge_thumbnail\" style=position:absolute; top:0px; left:0px; z-index:10; padding:0px; margin:0px; border:0px;\" >"
								+ "<img id=\"pp_badge_thumbnail_image\" src=\""
								+ data.getString("thumbnailUrl")
								+ "\" height=\"60px\" width=\"60px\"/>"
								+ "</div>"
								+ "<div id=\"pp_social_badge_text\" style=\" position:absolute; top:0px; left:80px; width:170px; height:60px; padding:0px; margin:0px; border:0px;\" >"
								+ "<b>Welcome, "
								+ data.getString("nickname")
								+ "</b><br/>"
								+ "<span style=\" font-size:9px;\"> "
								+ " You are signed in with<br />your MySpace account."
								+ "</span>"
								+ "</div>"
								+ "</div>"
								+ "<div style=\"position:absolute; top:50px; left:50px; z-index:11; padding:0px; margin:0px; \" ><img src=\"http://dev1.payparade.net/images/myspace/myspace_icon.png\" border=\"0\" width=\"20px\" height=\"20px\"/></div>"
								+ "</body>" + "</html>");
			} catch (JSONException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.info("No Social Network");
			displayedFacebookConnectedOptions(request,out,partnerCode,"");

		}
	}

	public void getSocialLogout(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		// recordActivity( request, "Logout") ;
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		logger.info("API KEY IS:::"+partnerNetworkDataObject.getApi_key());
		recordActivity(request, Activity.LOG_OUT);
		if ("facebook".equalsIgnoreCase(getSocnet(request))) {
			out.print(getIframeHead(partnerCode));
			out
					.print("<body onload=\"FB.Connect.logout(function() { pp_logout_callback(); });\">"
							+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
							+ "<script type=\"text/javascript\"> "
							+ "FB_RequireFeatures([\"XFBML\"], function() { "
							+ "  FB.Facebook.init(\""
							+ partnerNetworkDataObject.getApi_key()
							+ "\", \"xd_receiver.htm\"); "
							+ "  FB.Facebook.get_sessionState().waitUntilReady(function() { "
							+ "    FB.XFBML.Host.parseDomTree();"
							+ "    }); "
							+ "  }); "
							+ "FB.ensureInit( pp_update_badge, pp_login_user ) ;"
							+ "</script>" + "</body>" + "</html>");
		} else if ("myspace".equals(myCookies.get("pp_social_network"))) {
		} else {
			logger.info("No Social Network");

		}
	}

	public void getSocialShare(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		// recordActivity( request, "Share") ;
		recordActivity(request, Activity.SHARED);

		String encodedURI = "";
		String unencodedURI = "";

		if (request.getParameter("url").contains("http://")) {
			encodedURI = URLEncoder
					.encode(request.getParameter("url"), "UTF-8");
			unencodedURI = request.getParameter("url");
		} else {
			unencodedURI = URLDecoder.decode(request.getParameter("url"),
					"UTF-8");
			encodedURI = request.getParameter("url");
		}

		logger.info("Share recieved >" + request.getParameter("url")
				+ "<    unencoded was >" + unencodedURI + "<    encoded was >"
				+ encodedURI + "<");

		Client b = new Client();
		b.webFile(unencodedURI);

		String overRideTitle = "";
		String resp = (String) b.getContent();

		resp = resp.toLowerCase();
		// logger_.info(resp) ;

		logger.info("Passed in content was >" + request.getParameter("content")
				+ "<");

		// If the user is not logged into any social network and they click on a
		// share link,
		// try to infer the requested social network from the cookies so a blank
		// page doesn't show up
		int networkcodeid = 0;
		String social = getSocnet(request);
		if (social == null) {
			logger.info("User not logged in to share link");
			Cookie cookies[] = request.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					logger.debug(cookies[i].getName());
					if (cookies[i].getName().contains("fbsetting_")) {
						social = "facebook";
						logger
								.info("Inferring social network 'facebook' by cookie 'fbsetting_'");
						break;
					}
				}
			}

			// TODO: Do something if the user is not logged into a social
			// network
			if (social == null) {
				social = "none";
				networkcodeid = 0;
			} else {
				networkcodeid = gadgetService.getNetworkId(social);
			}
		}

		// TODO: Add parameter to payparade_head.js (ppSocialShare) for partner
		// to set social network
		if (social.equals("none")) {
			logger.warn("Could not infer social network");
		}

		sharedUrls_.put(getSocid(request), unencodedURI);
		if (request.getParameter("content").equals("undefined"))
			sharedContent_.put(getSocid(request), b.getMeta("description"));
		else
			sharedContent_.put(getSocid(request), request
					.getParameter("content"));

		if (request.getParameter("title").equals("undefined"))
			sharedTitle_.put(getSocid(request), b.getMeta("title"));
		else {
			sharedTitle_.put(getSocid(request), request.getParameter("title"));
			overRideTitle = ""; // depricated
		}
		if (request.getParameter("image_link").equals("undefined"))
			sharedImageLink_.put(getSocid(request), b
					.getHref("link rel=\"image_src\""));
		else
			sharedImageLink_.put(getSocid(request), request
					.getParameter("image_link"));

		Integer generatedId = 0;
		getPpShareLinkDataObject().setNetwork_code_id(networkcodeid);
		if (getSocid(request) != null && getSocid(request).equals("") == false) {
			String sharesocialid = (getSocid(request));
			getPpShareLinkDataObject().setShare_social_id(sharesocialid);
			getPpShareLinkDataObject().setShared_url(
					sharedUrls_.get(sharesocialid));
			getPpShareLinkDataObject().setShared_content(
					sharedContent_.get(sharesocialid));
			getPpShareLinkDataObject().setShared_title(
					sharedTitle_.get(sharesocialid));
			getPpShareLinkDataObject().setShared_image(
					sharedImageLink_.get(sharesocialid));
			generatedId = gadgetService
					.insert_pp_shared_link(ppShareLinkDataObject);
		} else {
			getPpShareLinkDataObject().setShare_social_id(generatedId+"");
		}
		/*
		 * Commented By Devarshi Pandya Integer generatedId =db_.executeInsert(
		 * "INSERT INTO pp_shared_link(network_code, sharer_social_id, shared_url, shared_title, shared_content, shared_image, partner_domain) "
		 * + "VALUES ("+ "\""+social+"\""+","+ "\""+getSocid(request)+"\""+","+
		 * "\""+sharedUrls_.get(getSocid(request))+"\""+","+
		 * "\""+sharedTitle_.get(getSocid(request))+"\""+","+
		 * "\""+sharedContent_.get(getSocid(request))+"\""+","+
		 * "\""+sharedImageLink_.get(getSocid(request))+"\""+","+
		 * "\""+partnerCode+"\""+ ")") ;
		 */
		logger.info("Title");
		logger.logHashMap(sharedTitle_);
		logger.info("Content");
		logger.logHashMap(sharedContent_);

		encodedURI = "http://" + linkurl_ + "/"
				+ LinkController.encode(generatedId);
		logger.info("Using link tracking for " + partnerCode + "( "
				+ useLinkTracking_.get(partnerCode) + ") URL:" + encodedURI);

		if ("facebook".equals(social)) {
			out
					.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">");
			out.print("  <body> ");
			out.print("  <script> ");
			out
					.print("  window.location = \"http://www.facebook.com/sharer.php?u="
							+ encodedURI + overRideTitle + "\" ; ");
			out.print("  </script></body></html> ");
		} else if ("myspace".equals(social)) {
			out
					.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">");
			out.print("  <body> ");
			out.print("  <script type=\"text/javascript\"> ");
			out.print("  window.resizeTo(935,811) ; ");
			String msg = null;
			String uriUrl = null;
			String uriTitle = null;
			String uriContent = null;
			String uriLink = null;

			uriUrl = "";
			uriTitle = "";
			uriContent = "";
			uriLink = "";

			uriUrl = URLEncoder.encode(unencodedURI, "UTF-8");
			logger.info("URL >" + request.getParameter("url")
					+ "< encoded to >" + uriUrl + "<");

			uriTitle = URLEncoder.encode(sharedTitle_.get(getSocid(request)),
					"UTF-8");
			logger.info("Title >" + sharedTitle_.get(getSocid(request))
					+ "< encoded to >" + uriTitle + "<");

			String enhancedContent = "<img src=\""
					+ sharedImageLink_.get(getSocid(request)) + "\"/>"
					+ sharedContent_.get(getSocid(request));
			uriContent = URLEncoder.encode(enhancedContent, "UTF-8");
			logger.info("Content >" + sharedContent_.get(getSocid(request))
					+ "< encoded to >" + uriContent + "<");

			uriLink = URLEncoder.encode(
					sharedImageLink_.get(getSocid(request)), "UTF-8");
			logger.info("Link >" + sharedImageLink_.get(getSocid(request))
					+ "< encoded to >" + uriLink + "<");

			msg = "  window.location = \"http://www.myspace.com/index.cfm?fuseaction=postto"
					+ "&u="
					+ uriUrl
					+ "&t="
					+ uriTitle
					+ "&c="
					+ uriContent
					+ "&l=3" + "\" ; ";
			out.print(msg);
			logger.info("MySpace URL was >" + msg + "<");
			// out.print("  window.location = \"http://messaging.myspace.com/index.cfm?fuseaction=mail.message&subject="+request.getParameter("title")+"&body="+request.getParameter("url")+"\" ; "
			// ) ;
			out.print("  </script></body></html> ");
		} else {
			logger.error("Social Network " + social + " was NOT recognized");
			return;
		}

		logger.info("Shared Information:");
		logger.logHashMap(sharedUrls_);
		logger.logHashMap(sharedTitle_);
		logger.logHashMap(sharedContent_);
		logger.logHashMap(sharedImageLink_);

		sharedUrls_.remove(getSocid(request));
		sharedTitle_.remove(getSocid(request));
		sharedContent_.remove(getSocid(request));
		sharedImageLink_.remove(getSocid(request));
	}

	public void getMessageUrl(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies, String resourceString)
			throws ServletException, IOException {
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		String id = parts[parts.length - 1];
		logger.info("Message request for ID:" + id);

		recordActivity(request, Activity.MESSAGE);

		String msg = sharedUrls_.get(id);
		if (msg == null) {
			msg = "http://www.davidsprom.com/promtourage_intro.jsp";
		}
		out.print(msg);

	}

	public void getInit(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		out.print("<html><body>OK</body></html>");
	}

	public void getMessageTitle(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies, String resourceString)
			throws ServletException, IOException {
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		String id = parts[parts.length - 1];
		logger.info("Message request for ID:" + id);

		String msg = sharedTitle_.get(id);
		if (msg == null) {
			/*
			 * DatabaseResult results =
			 * db_.executeQuery("SELECT title FROM pp_messages " ) ; DataSet
			 * dataset = results.getDataSet() ; DatabaseRow row = null; for(int
			 * i = 0 ; i< dataset.size() ; i++){ row = dataset.get(i); msg =
			 * row.getString("title") ; row = null; } results = null; dataset =
			 * null;
			 */

			List<PPMessagesDataObject> messagelist = getGadgetService()
					.select_pp_messages();
			if (messagelist != null && messagelist.size() > 0) {
				PPMessagesDataObject ppMessagesDataObject = messagelist.get(0);
				msg = ppMessagesDataObject.getTitle();
			}

		}
		out.print(msg);
	}

	public void getMessageBody(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies, String resourceString)
			throws ServletException, IOException {
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		String id = parts[parts.length - 1];
		logger.info("Message request for ID:" + id);

		recordActivity(request, Activity.MESSAGEBODY);

		String msg = sharedContent_.get(id);
		if (msg == null) {
			msg = sharedUrls_.get(id);
			if (msg == null) {
				List<PPMessagesDataObject> messagelist = getGadgetService()
						.select_pp_messages();
				if (messagelist != null && messagelist.size() > 0) {
					PPMessagesDataObject ppMessagesDataObject = messagelist
							.get(0);
					msg = ppMessagesDataObject.getMessage();
				}
			}
		}
		out.print(msg);

	}

	public void getSent(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies, String resourceString)
			throws ServletException, IOException {
		String parts[] = resourceString.split("/");
		logger.info(parts);
		if (parts.length < 3) {
			out.print("<script>  window.close() ; </script> ");
		} else {
			String sender = parts[parts.length - 2];
			String recipient = parts[parts.length - 1];
			logger.info("Message request for ID:" + recipient + "  from ID:"
					+ sender);

			// recordActivity( request, "MsgSent") ;
			recordActivity(request, Activity.MESSAGESENT);

			// out.print(
			// "<script>  if (top != window) top.location.href = self.location.href; </script> "
			// ) ;
			if (sharedUrls_.get(sender) != null) {
				out
						.print("<script>  if (top != window) top.location.href =\"http://"
								+ hostname_
								+ "/pass/gadget/sent\" ; </script> ");
				sharedUrls_.remove(sender);
				sharedContent_.remove(sender);
				sharedTitle_.remove(sender);
				sharedImageLink_.remove(sender);
			} else {
				out
						.print("<script>  if (top != window) top.location.href =\"http://profile.myspace.com/Modules/Applications/Pages/Canvas.aspx?appId=128802\" ; </script> ");
			}
		}
	}

	public void getPosted(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies, String resourceString)
			throws ServletException, IOException {
		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		if (parts.length < 3) {
			out.print("<script>  window.close() ; </script> ");
		} else {
			String where = parts[parts.length - 2];
			String poster = parts[parts.length - 1];
			logger.info("ID:" + poster + " posted to " + where);

			// recordActivity( request, "Post"+where) ; Commented By Devarshi

			// out.print(
			// "<script>  if (top != window) top.location.href = self.location.href; </script> "
			// ) ;
			if (sharedUrls_.get(poster) != null) {
				out
						.print("<script>  if (top != window) top.location.href =\"http://"
								+ hostname_
								+ "/pass/gadget/posted\" ; </script> ");
			} else {
				out
						.print("<script>  if (top != window) top.location.href =\"http://profile.myspace.com/Modules/Applications/Pages/Canvas.aspx?appId=128802\" ; </script> ");
			}
		}
	}

	public void getStatus(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {

		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);

		recordActivity(request, Activity.STATUS);
		if ("facebook".equals(myCookies.get("pp_social_network"))) {
			out
					.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"  xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
							+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
							+ "<head>" + "  <script src=\"http://"
							+ hostname_
							+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
							+ "</head>"
							+ "<body>"
							+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>"
							+ " Facebook: <span id=\"fbstatus\">fetching</span>"
							+ "<script type=\"text/javascript\">"
							+ "  FB_RequireFeatures([\"XFBML\"], function() { "
							+ "    FB.Facebook.init(\""
							+ partnerNetworkDataObject.getApi_key()
							+ "\", \"xd_receiver.htm\", {\"reloadIfSessionStateChanged\":true}); "
							+ "    }); "
							+ "</script> "
							+ "<script type=\"text/javascript\"> "
							+ "FB.Connect.get_status().waitUntilReady(function(status) { "
							+ "  var x=document.getElementById(\"fbstatus\"); "
							+ "  var y = \"unknown\"; "
							+ "  if (status == 1) "
							+ "    y = \"connected\"; "
							+ "  else if (status == 2) "
							+ "    y = \"userNotLoggedIn\"; "
							+ "  else if (status == 3) "
							+ "    y = \"appNotAuthorized\"; "
							+ "  x.innerHTML=status + \": \" + y; "
							+ " }); "
							+ " </script> ");
		} else {

			OAuth2 restClient = new OAuth2();
			logger.info(myCookies.get("pp_oauth_token"));

			JSONObject data = restClient.getMyData(myCookies
					.get("pp_oauth_token"), myCookies
					.get("pp_oauth_token_secret"), partnerCode);
			if (data != null) {
				try {
					out
							.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"  xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
									+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
									+ "<head>"
									+ "  <script src=\"http://"
									+ hostname_
									+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
									+ "</head>"
									+ "<body>"
									+ "<div id=\"pp_social_badge_inner_div\" >"
									+ "<img id=\"pp_badge_thumbnail\" src=\""
									+ data.getString("thumbnailUrl")
									+ "\" height=\"60px\" width=\"60px\"/>"
									+ "<div id=\"pp_social_badge_text\" style=\" top:-60px; left:65px; position:relative\" >"
									+ "Welcome, "
									+ data.getString("nickname")
									+ "<br/>"
									+ "<span style=\" font-size:9px;\"> "
									+ " You are signed in with<br />your MySpace account."
									+ "</span>"
									+ "</div>"
									+ "</div>"
									+ "</body>" + "</html>");
				} catch (JSONException e) {
					logger.error(e.getMessage());
				}
			} else {
				out
						.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"  xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
								+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
								+ "<head>"
								+ "  <script src=\"http://"
								+ hostname_
								+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
								+ "</head>"
								+ "<body>"
								+ "<div id=\"pp_social_badge_inner_div\" >"
								+ "<img id=\"pp_badge_thumbnail\" src=\"\" height=\"60px\" width=\"60px\"/>"
								+ "<div id=\"pp_social_badge_text\" style=\" top:-60px; left:65px; position:relative\" >"
								+ "<br/>"
								+ "<span style=\" font-size:9px;\"> "
								+ " Error occurred retreiving data from MySpace."
								+ "</span>"
								+ "</div>"
								+ "</div>"
								+ "</body>"
								+ "</html>");
			}
		}
	}

	private void getUserMeter(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode) {

		List<PPOpsSnapShotDataObject> list = getGadgetService()
				.select_numuser_from_pp_ops_snapshot();

		
		/*
		 * DatabaseResult results =db_.executeQuery(
		 * "SELECT num_users FROM pp_ops_snapshot WHERE time_stamp > SUBTIME(NOW(), '0 0:02:00' ) "
		 * ) ; DataSet dataset = results.getDataSet() ;
		 * logger_.logArrayList(dataset) ; if ( dataset.size() > 0 ) {
		 * DatabaseRow row = dataset.get(0);
		 * out.print(getMeterHtml("User Load",0
		 * ,1000,row.getInteger("num_users"))) ; }
		 */
		if (list!=null && list.size()>0) {
			out.print(getMeterHtml("User Load", 0, 1000, ((PPOpsSnapShotDataObject)list.get(0)).getNum_of_users()));
		}
	}
	
	private void getRequestMeter(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode) {
		logger.info(" Inside getRequestMeter() of GadgetController class ");
		/*
		 * DatabaseResult results =db_.executeQuery(
		 * "SELECT num_requests FROM pp_ops_snapshot WHERE time_stamp > SUBTIME(NOW(), '0 0:02:00' ) "
		 * ) ; DataSet dataset = results.getDataSet() ;
		 * logger_.logArrayList(dataset) ; if ( dataset.size() > 0 ) {
		 * DatabaseRow row = dataset.get(0);
		 * out.print(getMeterHtml("Requests / Minute"
		 * ,0,1000,row.getInteger("num_requests"))) ; }
		 */

		List<PPOpsSnapShotDataObject> list = getGadgetService()
				.select_numofrequest_from_pp_ops_snapshot();
		logger.info(" Size of list is::: "+list.size());
		if (list!=null && list.size() > 0) {
			out.print(getMeterHtml("Requests / Minute", 0, 1000,
					((PPOpsSnapShotDataObject)list.get(0)).getNum_of_request()));
		}
		logger.info(" Outside getRequestMeter() of GadgetController class ");
	}
	
	private String getMeterHtml(String title, int min, int max, int val) {
		String result = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
				+ "  <body> "
				+ "<script> "
				+ "  location.href = \""
				+ getGoogleMeter(title, min, max, val)
				+ "\""
				+ "  </script></body></html> ";
		return result;
	}

	private String getGoogleMeter(String title, int min, int max, int val) {
		String result = "http://chart.apis.google.com/chart?cht=gom&" + "chtt="
				+ title + "&chs=300x150&chd=t:" + val + "&chds=" + min + ","
				+ max + "&chl=" + val
				+ "&chco=00FF00,00FF00,FFFF00,FF0000,FF0000,FF0000";
		return result;
	}

	

	private void getResponseMeter(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode) {
		logger.info(" Inside getResponseMeter() of GadgetController class ");
		/*
		 * DatabaseResult results =db_.executeQuery(
		 * "SELECT total_millis DIV num_requests tm FROM pp_ops_snapshot WHERE time_stamp > SUBTIME(NOW(), '0 0:02:00' ) "
		 * ) ; DataSet dataset = results.getDataSet() ;
		 * logger_.logArrayList(dataset) ; if ( dataset.size() > 0 ) {
		 * DatabaseRow row = dataset.get(0);
		 * out.print(getMeterHtml("Response Time (ms)"
		 * ,0,1000,row.getInteger("tm"))) ; }
		 */
		List<PPOpsSnapShotDataObject> list = getGadgetService()
				.select_totalmills_from_pp_ops_snapshot();
		logger.info(" Size of list is::: "+list.size());
		if (list!=null && list.size() > 0) {
			out.print(getMeterHtml("Response Time (ms)", 0, 1000,
					((PPOpsSnapShotDataObject)list.get(0)).getTotal_milis()));
		}
		logger.info(" Outside getResponseMeter() of GadgetController class ");
	}

	private void iOpsDashboard(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		String result = iHeader("Ops iDashboard")
				+ "<body onorientationchange=\"orient();\">"
				+ "<div id=\"wrap\">"
				+ "<div id=\"header\">"
				+ "</div>"
				+ "<div id=\"content\">"
				+ "<p>This is the main content area of the page. </p>"
				+ "<p>Using css and javascript we can manipulate any of these divs using an alternate css file. The css files in this project are for landscape and portrait views.</p>"
				+ "<p>Some more filler text here to demonstrate the page.</p>"
				+ "</div>" + "<div id=\"bottom\">" + "</div>" + "</div>"
				+ "</body>" + "</html>";
		out.print(result);
	}

	String iHeader(String title) {
		String result = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">"
				+ "<head>"
				+ "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0;\">"
				+ "<title>"
				+ title
				+ "</title>"
				+ "<link rel=\"apple-touch-icon\" href=\"/images/float_large-1.png\"/> "
				+ "<link rel=\"StyleSheet\" href=\"/css/iphone_portrait.css\" type=\"text/css\"  media=\"screen\" id=\"orient_css\">"
				+ "		<script type=\"text/javascript\">"
				+ "		function orient()"
				+ "			{"
				+ " switch(window.orientation){  "
				+ "		case 0: document.getElementById(\"orient_css\").href = \"/css/iphone_portrait.css\"; "
				+ "			break;"
				+ "		case -90: document.getElementById(\"orient_css\").href = \"/css/iphone_landscape.css\"; "
				+ "			break;"
				+ "		case 90: document.getElementById(\"orient_css\").href = \"/css/iphone_landscape.css\";"
				+ "			break;"
				+ "		} "
				+ " } "
				+ "window.onload = orient();"
				+ " </script> " + "</head>";
		return result;
	}

	private void getapikey(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost) {
		logger.info("Inside getapikey() of GadgetController Class");
		try {
			int partnerdomainid = getGadgetService()
					.select_id_from_pp_partner_domain(getPartnerCode(request));
			logger.info(" Partner Domain Id is::" + partnerdomainid);
			PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
					.getPPPartnerNetwork().get(partnerdomainid);

			// String APIKEY = ids_.get(getPartnerCode(request));
			String APIKEY = partnerNetworkDataObject.getApi_key();
			logger.info("Value of APIKEY is==" + APIKEY);
			out.print(APIKEY.trim());
			//out.print("20000");
		} catch (Exception e) {
			logger.error("GadgetController:getapikey:Exception is::", e);
		}
		logger.info("Outside getapikey() of GadgetController Class");
	}

	public void displayedFacebookConnectedOptions(HttpServletRequest request,
			PrintWriter out, String partnerCode, String partnerHost) {
		logger
				.info("Inside  displayedFacebookConnectedOptions() of GadgetController Class");
		try {
			int partnerdomainid = getGadgetService()
					.select_id_from_pp_partner_domain(getPartnerCode(request));
			logger.info(" Partner Domain Id is::" + partnerdomainid);
			PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
					.getPPPartnerNetwork().get(partnerdomainid);

			HttpSession session = request.getSession(false);
			// recordActivity( request, "Connect Option") ;
			recordActivity(request, Activity.CONNECT_OPTION);
			session.setAttribute("connect", "yes");
			logger.info("partnerNetworkDataObject.getApi_key():::"
					+ partnerNetworkDataObject.getApi_key());
			out.print(getIframeHead(partnerCode));
			out.print("  <body> ");
			out
					.print("  <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>");
			out.print("<table><tr>");
			// out.print(
			// "  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  "+connectSize_.get(partnerCode)+"></fb:login-button></td>");
			out
					.print("  <td width=\"50%\"><fb:login-button onlogin=\"connect_callback('facebook');\"  "
							+ getGadgetService().getConnectSize_().get(
									partnerCode) + "></fb:login-button></td>");
			out.print(" </tr></table>");
			out.print("  <script type=\"text/javascript\"> FB.init(\""
					+ partnerNetworkDataObject.getApi_key().trim()
					+ "\", \"xd_receiver.htm\"); </script> ");
			out.print(" </body>");
			out.print(" </html>");
		} catch (Exception e) {
			logger
					.error(
							"GadgetController::displayedFacebookConnectedOptions::Exception is:::",
							e);
		}
		logger
				.info("Outside  displayedFacebookConnectedOptions() of GadgetController Class");
	}

	public void displayedFacebookBadge(HttpServletRequest request,
			PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) {
		logger
				.info("Inside displayedFacebookBadge() of GadgetController Class");
		int partnerdomainid = getGadgetService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		getPayparadeServerdetailByPartnername(request);
		HttpSession session = request.getSession(false);
		if ("yes".equals(session.getAttribute("connect"))) {
			// recordActivity( request, "Connect") ;
			recordActivity(request, Activity.CONNECT);
			session.removeAttribute("connect");
		}

		// recordActivity( request, "Badge") ;
		recordActivity(request, Activity.BADGE);
		// String salutation = getPartnerOptionString(partnerCode,
		// "badge_salutation") ;
		PPPartnerOptionDataObject ppPartnerOptionDataObject = getPartnerOptionString(partnerCode);
		String salutation = ppPartnerOptionDataObject.getBadge_salutation();
		logger
				.info("getSocialBadge retrieved badge_salutation = "
						+ salutation);

		// String message = getPartnerOptionString(partnerCode, "badge_message")
		// ;
		String message = ppPartnerOptionDataObject.getBadge_message();
		logger.info("getSocialBadge retrieved badge_salutation = " + message);

		String friendsClause = "";
		if (haveFriends(request)) {
			friendsClause = "FB.Facebook.apiClient.friends_get(null, function(result){"
					+ " ppTrace(result) ; ppRegisterNetwork(result); "
					+ "} ) ; ";
		}

		// if ( "facebook".equals(myCookies.get("pp_social_network")) ) {

		out.print(getIframeHead(partnerCode));
		out
				.print("</head>"
						+ "<body>"
						+ "<script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
						+ "<div id=\"pp_social_badge_inner_div\">"
						+ "<div id=\"pp_profile_picture_div\">"
						+ " <fb:profile-pic uid=\""
						+ myCookies.get("pp_"
								+ myCookies.get("pp_social_network") + "_id")
						+ "\" facebook-logo=true linked=\"false\"></fb:profile-pic>"
						+ "</div>"
						+ "<div id=\"pp_profile_salutation_div\">"
						+ "<span class=\"pp_badge_salutation\"> Welcome, <fb:name uid=\""
						+ myCookies.get("pp_"
								+ myCookies.get("pp_social_network") + "_id")
						+ "\" useyou=false firstnameonly=true lastnameonly=true linked=false></fb:name></span><br />"
						+ "<span class=\"pp_badge_message\"> "
						+ message
						+
						// " You are signed in with<br />your Facebook account.<br />"+
						// " <span style=\"float:right\"><fb:login-button size=\"small\" background=\"white\" length=\"short\" autologoutlink=\"true\"></fb:login-button></span>"+
						"</span>"
						+ "</div>"
						+ "</div>"
						+ "<script language=\"JavaScript\" src=\"js/payparade_iframe.js\"></script>"
						+ "<script type=\"text/javascript\"> "
						+ "FB_RequireFeatures([\"XFBML\"], function() { "
						+ "  FB.Facebook.init(\""
						+ partnerNetworkDataObject.getApi_key()
						+ "\", \"xd_receiver.htm\"); "
						+ "  FB.Facebook.get_sessionState().waitUntilReady(function() { "
						+ friendsClause
						+ "    FB.XFBML.Host.parseDomTree();"
						+ "    }); "
						+ "  }); "
						+ "FB.ensureInit( pp_update_badge, pp_login_user ) ;"
						+ "</script>" + "</body>" + "</html>");
		// }

		logger
				.info("Outside displayedFacebookBadge() of GadgetController Class");
	}

	@Override
	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		return false;
	}

	public void init() {
		controllerName = "gadget";
		super.init();
	}

}
