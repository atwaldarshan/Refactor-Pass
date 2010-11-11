package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.util.CookieGenerator;

import com.payparade.dataobject.PPIdDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPMemberSessionDataObject;
import com.payparade.dataobject.PPPartnerOptionDataObject;
import com.payparade.dataobject.PPPartnerServerDataObject;
import com.payparade.service.PartnerNetworkService;
import com.payparade.service.PassService;
import com.payparade.service.SigningSecretService;
import com.payparade.service.SigningSecretServiceImpl;
import com.payparade.util.Geo;
import com.payparade.util.Logger;
import com.payparade.util.Mail;
import com.payparade.util.StringUtils;

public abstract class PassController extends AbstractController {
	Geo geo;

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	static public Logger logger = Logger.getLogger("PassController");
	protected boolean logInfo_ = true;
	protected boolean debug_ = true;
	protected boolean alert_ = false;
	protected boolean mail_ = true;
	protected static String linkurl_ = "pplink.us";
	protected String version_ = "";
	protected Mail mailer_ = new Mail();

	public String getVersion_() {
		return version_;
	}

	public void setVersion_(String version) {
		version_ = version;
	}

	Map<String, String> storeCookie = new HashMap<String, String>();
	protected static HashMap<String, Long> numServed_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> maxMillis_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> totalMillis_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> lastMillis_ = new HashMap<String, Long>();
	protected static long overallNumServed_ = 0L;
	protected static long overallMaxMillis_ = 0L;
	protected static long overallTotalMillis_ = 0L;
	private SigningSecretService signingSecretService;
	protected static HashMap<String, String> useLinkTracking_ = new HashMap<String, String>();
	protected static Map<Integer, PPPartnerOptionDataObject> partner_options_ = new HashMap<Integer, PPPartnerOptionDataObject>();
	protected static Map<String, PPPartnerServerDataObject> servers_ = new HashMap<String, PPPartnerServerDataObject>();
	protected static HashMap<String, String> ignore_ = new HashMap<String, String>();
	private PassService passService;
	private PPIdDataObject ppIdDataObject;
	HashMap<String, String> myCookies = null;
	private PPMemberActivityDataObject pPMemberActivityDataObject;
	private PPMemberSessionDataObject ppMemberSessionDataObject;
	private PartnerNetworkService partnerNetworkService;
	protected static Map<String, String> secrets_ = new HashMap<String, String>();
	protected static String hostname_ = "";

	String controllerName = "PassServlet";

	public SigningSecretService getSigningSecretService() {
		return signingSecretService;
	}

	public PPMemberSessionDataObject getPpMemberSessionDataObject() {
		return ppMemberSessionDataObject;
	}

	public void setPpMemberSessionDataObject(
			PPMemberSessionDataObject ppMemberSessionDataObject) {
		this.ppMemberSessionDataObject = ppMemberSessionDataObject;
	}

	public void setSigningSecretService(
			SigningSecretService signingSecretService) {
		this.signingSecretService = signingSecretService;
	}

	public PartnerNetworkService getPartnerNetworkService() {
		return partnerNetworkService;
	}

	public void setPartnerNetworkService(
			PartnerNetworkService partnerNetworkService) {
		this.partnerNetworkService = partnerNetworkService;
	}

	public PPMemberActivityDataObject getpPMemberActivityDataObject() {
		return pPMemberActivityDataObject;
	}

	public void setpPMemberActivityDataObject(
			PPMemberActivityDataObject pPMemberActivityDataObject) {
		this.pPMemberActivityDataObject = pPMemberActivityDataObject;
	}

	public PPIdDataObject getPpIdDataObject() {
		return ppIdDataObject;
	}

	public void setPpIdDataObject(PPIdDataObject ppIdDataObject) {
		this.ppIdDataObject = ppIdDataObject;
	}

	public PassService getPassService() {
		return passService;
	}

	public void setPassService(PassService passService) {
		this.passService = passService;
		// logger.info(" Inside setPassService(PassService) of PassController class ");
	}

	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {

		logger.info(" Inside handleRequestInternal() of PassController class ");
		boolean cookiesEnabled = false;
		Long millis = null;
		Long startMillis = System.currentTimeMillis();
		PrintWriter out = null;
		try {
			URI uri = null;
			try {
				uri = new URI(request.getRequestURI());
			} catch (URISyntaxException e1) {
				logger.error("doGet() - " + e1.getMessage());
			}
			if(request!=null){
			logger.info("Controller Name is---" + controllerName);
			logger.info("URL Path is:::" + uri.getPath());
			logger.info("request.getRequestURI():::"+request.getRequestURI());
			/*logger.info("Local Address is:::"+request.getLocalAddr());
			logger.info("Local Name is:::"+request.getLocalName());
			
			logger.info("Remote Host is:::"+request.getRemoteHost());
			logger.info("Remote User is:::"+request.getRemoteUser());
			*/
			}
			out = response.getWriter();
			String requestType = request.getMethod();
			String resourceString = uri.getPath().substring(
					uri.getPath().indexOf(controllerName)
							+ controllerName.length() + 1);
			logger.info("Resource String is---" + resourceString);
			HttpSession session = request.getSession(true);
			myCookies = storeCookies(request);
			setSocnet(request, myCookies.get("pp_social_network"));
			setSocid(request, myCookies.get("pp_" + getSocnet(request) + "_id"));
			setPPId(request, myCookies.get("pp_xv1"));
			setFriends(request, myCookies.get("pp_xv2"));
			logger.info("Session value is::" + session.isNew());
			//if (session.isNew()) {
			/* Commented By Devarshi Pandya 
			 * in setPartnerCode set context as a request. 
			 * */
			if (session.isNew()) {
				setPartnerHost(request, StringUtils.getPartnerHost(request));
				setPartnerCode(request, StringUtils
						.getPartnerCode(getPartnerHost(request)));
				
				/*String ip = ((ServletRequestAttributes) RequestContextHolder
						.currentRequestAttributes()).getRequest()
						.getRemoteAddr();
				logger.info("Remote Address is:::" + ip);
				*/
				String ip = null;
				  String xfor = request.getHeader("x-forwarded-for") ;
				  if ( xfor != null ) {
					  if ( xfor.contains("," ) ) {
						  String[] ips = xfor.split(",") ;
						  if ( ips.length > 0 )
							  ip = ips[0] ;
						  }
					  else
						  ip = xfor ;					
					  }
				  
				logger.info("header:x-forwarded-for::"+ip);
				setCustomerIp(request, ip);
				logger.info("value of getPPId(request)::" + getPPId(request));
				logger.info("ignore_.get(resourceString):::--->"
						+ ignore_.get(resourceString));
				
				if (getPPId(request) == null /*
											 * &&!"yes".equals(ignore_.get(
											 * resourceString).toString())
											 */) {
					logger.warn("No pp_id found");
					if (request.getHeader("user-agent") == null) {
						logger.warn("No user agent found");
					} else {
						if (request.getRequestURI().contains("xd_receiver.htm")) {
							logger.warn("ignoring xd_receiver request(");
						} else {
							if (!(request.getRequestURI().contains("gadget") || request
									.getRequestURI().contains("link"))) {
								logger.warn("ignoring misc applet request");
							} else {
								getPpIdDataObject().setSessionId(
										session.getId());
								getPpIdDataObject().setBrowserId(
										request.getHeader("user-agent"));
								getPpIdDataObject().setReferenceUrl(
										request.getHeader("referer"));
								getPpIdDataObject().setUserIp(ip);
								long ppid = 0;
								ppid = getPassService().insertPPId(
										ppIdDataObject);

								logger.info("PPId is::" + ppid);
								setPPId(request, Long.toString(ppid));
								logger.info(" getPPId(request):: "
										+ getPPId(request));

								CookieGenerator cookieGenerator = new CookieGenerator();
								cookieGenerator.setCookieName("pp_xv1");
								cookieGenerator.setCookiePath("/");
								if (getPartnerCode(request) != null)
									cookieGenerator.setCookieDomain("."
											+ getPartnerCode(request));
								cookieGenerator.addCookie(response,
										getPPId(request));
								int networkcodeid = 0;
								if (getSocnet(request) != null)
									networkcodeid = getPassService()
											.getNetworkIdUsingNetworkcodeid(
													getSocnet(request));
								logger.info("getPpMemberSessionDataObject()"
										+ getPpMemberSessionDataObject());
								logger
										.info("request.getHeader('user-agent'):::"
												+ request
														.getHeader("user-agent"));
								logger.info("getSocid(request)::"
										+ getSocid(request));
								logger.info("request.getHeader('referer')::"
										+ request.getHeader("referer"));
								logger.info("session.getId()::"
										+ session.getId());
								logger.info("getPPId(request))::"
										+ getPPId(request));
								logger.info("networkcodeid::" + networkcodeid);
								getPpMemberSessionDataObject().setBrowser_id(
										request.getHeader("user-agent"));
								getPpMemberSessionDataObject()
										.setMember_social_id(getSocid(request));
								getPpMemberSessionDataObject()
										.setReference_url(
												request.getHeader("referer"));
								
								getpPMemberActivityDataObject().setTargetUrl(
										request.getParameter("url"));
								getPpMemberSessionDataObject()
										.setMemeber_session_id(session.getId());
								getPpMemberSessionDataObject().setPp_id(
										Integer.parseInt(getPPId(request)));
								getPpMemberSessionDataObject()
										.setNetwork_code_id(networkcodeid);
								getPpMemberSessionDataObject().setUser_ip(ip);
								getPassService()
										.insert_into_pp_memeber_session(
												getPpMemberSessionDataObject());
							}
							logger.info("Geo coding ip " + ip);
						}
					}
				}
			} else {
				// Returning session
				if (request.getHeader("user-agent") != null
						&& !request.getRequestURI().contains("xd_receiver.htm")
						&& request.getRequestURI().contains("gadget")) {
					if (getPPId(request) == null) {
						logger.warn("Cookies NOT enabled for customer at IP "
								+ getCustomerIp(request));
						cookiesEnabled = false;
					} else
						cookiesEnabled = true;
				}
			}

			getGeo().geoCodeIp(getCustomerIp(request));

			logger.setId(getPPId(request));

			logInfo_ = (debug_ && !"yes".equals(ignore_.get(resourceString)));
			if (logInfo_) {
				logger.info("");
				logger.info("");
				logger
						.info("-------------------------------------------------------------------------------------------------");
				logger.info("");
				logger.info("");
				logger.info("");
				logger.info(" " + requestType + " - " + resourceString);
				logger.info("");
				logger.info("");
				logger.info("     user agent:"
						+ request.getHeader("user-agent"));
				logger.info("");
				logger.info("   session:" + session.getId());
				logger.info("         created:" + session.getCreationTime());
				logger
						.info("   last accessed:"
								+ session.getLastAccessedTime());
				logger.info("    max inactive:"
						+ session.getMaxInactiveInterval());
				logger.info("          is new:" + session.isNew());
				logger.info("        partner code:" + getPartnerCode(request));
				logger.info("        partner host:" + getPartnerHost(request));
				logger.info("");
				logger.info("           customer pp id:" + getPPId(request));
				logger.info("                  friends:"
						+ myCookies.get("pp_xv2"));
				logger.info("       customer social id:" + getSocid(request));
				logger.info("  customer social network:" + getSocnet(request));
				logger.info("              customer ip:"
						+ getCustomerIp(request));
				logger.info("                     city:" + getGeo().city_);
				logger.info("                    state:" + getGeo().region_);
				logger.info("                  country:" + getGeo().country_);
				logger.info("                      lat:" + getGeo().lattitude_);
				logger.info("                     long:" + getGeo().longitude_);
				logger.info("");
				logger.info("");
				logger.logRequest(request);
				logger.info("Resource requested: >" + resourceString + "<\n");
				logger.info("");
				logger.info("");
			}
			logger.setKVP("resource string", resourceString);
			logger.setKVP("partner code", getPartnerCode(request));
			logger.setKVP("partner host", getPartnerHost(request));
			logger.setKVP("payparade id", getPPId(request));
			logger.setKVP("customer ip", getCustomerIp(request));
			logger.setKVP("session id", session.getId());
			response.addHeader("Cache-Control", "no-cache");
			
			boolean isSigned = false;
			String signature = request.getHeader("pp_partner_signature");

			if (signature == null) {
				signature = request.getParameter("pp_partner_signature");
				logger.info("Received sig=" + signature);
			}

			if (signature != null)
				isSigned = SigningSecretServiceImpl.checkSignature(
						request.getRequestURI(), signature,
						getPartnerCode(request));
			else {
				signature = request.getParameter("pp_dashboard_signature");
				isSigned = SigningSecretServiceImpl.checkSignature(
						request.getRequestURI(), signature);
			}

			Long endMillis = System.currentTimeMillis();
			millis = new Long(endMillis.longValue() - startMillis.longValue());
			logger.info("PassServlet " + requestType + " - "
					+ request.getRequestURI() + " took " + millis.toString()
					+ " millisecond(s)\n");
			response.addHeader("ServerTime", millis.toString() + "ms");

			// internal profiling by request type
			numServed_.put(controllerName, numServed_.get(controllerName) + 1L);
			if (millis > maxMillis_.get(controllerName))
				maxMillis_.put(controllerName, millis);
			totalMillis_.put(controllerName, totalMillis_.get(controllerName)
					+ millis);
			lastMillis_.put(controllerName, millis);
			overallNumServed_ += 1L;
			overallTotalMillis_ += millis;
			if (millis > overallMaxMillis_)
				overallMaxMillis_ = millis;

			logger.info("Request Type is::" + requestType);
			if (resourceString == null) {
				response.setStatus(404);
				messageResponse(out, "Null Request");
			} else if (requestType.equalsIgnoreCase("get")) {
				if (request.getRequestURI().lastIndexOf("xd_receiver.htm") != -1) {
					getxdr(out);
				} else {
					//Update pp_member_session table
					get(request, response, out, resourceString, isSigned,
							myCookies, getPartnerCode(request),
							getPartnerHost(request));
				}
			} else if (requestType.equalsIgnoreCase("post")) {
				//Update pp_member_session table
				post(request, response, out, resourceString, isSigned,
						myCookies, getPartnerCode(request));
			}

		} catch (Exception e) {
			logger.error("---handleRequestInternal-----Exception is::" , e);
			//e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}
		logger
				.info(" Outside handleRequestInternal() of PassController class ");
		return null;
	}

	public void getxdr(PrintWriter out) throws IOException {
		out
				.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\" > <body> <script src=\"http://static.ak.connect.facebook.com/js/api_lib/v0.4/XdCommReceiver.js\" type=\"text/javascript\"></script> </body> </html> ");
	}

	abstract public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException;

	abstract public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException;

	private HashMap<String, String> storeCookies(HttpServletRequest request) {
		HashMap<String, String> result = new HashMap<String, String>();
		Cookie cookies[] = request.getCookies();
		if (cookies == null)
			cookies = new Cookie[0];
		for (int i = 0; i < cookies.length; i++) {
			result.put(cookies[i].getName(), cookies[i].getValue());
		}
		return result;
	}

	public String getSocnet(HttpServletRequest request) {
		return (String) getAttribute(request, "socnet");
	}

	public void setSocnet(HttpServletRequest request, String value) {
		setAttribute(request, "socnet", value);
	}

	public Object getAttribute(HttpServletRequest request, String attributeName) {
		HttpSession session = request.getSession(true);
		return session.getAttribute(attributeName);
	}

	public void setAttribute(HttpServletRequest request, String attributeName,
			Object value) {
		HttpSession session = request.getSession(true);
		session.setAttribute(attributeName, value);
	}

	public String getSocid(HttpServletRequest request) {
		return (String) getAttribute(request, "socid");
	}

	public void setSocid(HttpServletRequest request, String value) {
		setAttribute(request, "socid", value);
	}

	public String getPPId(HttpServletRequest request) {
		return (String) getAttribute(request, "ppid");
	}

	public void setPPId(HttpServletRequest request, String value) {
		setAttribute(request, "ppid", value);
	}

	public String getPartnerCode(HttpServletRequest request) {
		return (String) getAttribute(request, "partnercode");
	}

	public void setPartnerCode(HttpServletRequest request, String value) {
		setAttribute(request, "partnercode", value);
	}

	public String getPartnerHost(HttpServletRequest request) {
		return (String) getAttribute(request, "partnerhost");
	}

	public void setPartnerHost(HttpServletRequest request, String value) {
		setAttribute(request, "partnerhost", value);
	}

	public String getCustomerIp(HttpServletRequest request) {
		return (String) getAttribute(request, "customerip");
	}

	public void setCustomerIp(HttpServletRequest request, String value) {
		setAttribute(request, "customerip", value);
	}

	public String getFriends(HttpServletRequest request) {
		return (String) getAttribute(request, "numfriends");
	}

	public void setFriends(HttpServletRequest request, String value) {
		setAttribute(request, "numfriends", value);
	}

	public void recordActivity(HttpServletRequest request, short activity) {
		recordActivity(request, activity, getSocnet(request),
				getPartnerCode(request));
	}

	public void recordActivity(HttpServletRequest request, short activity,
			String net, String partner) {
		HttpSession session = request.getSession(true);
		short networkcodeid = 0;
		if (net != null && net.equals("") == false) {
			networkcodeid = getPassService()
					.getNetworkIdUsingNetworkcodeid(net);
		}
		recordActivity(request, activity, networkcodeid, getPassService()
				.getPartnerDomainId(partner), session.getId());
	}

	public void recordActivity(HttpServletRequest request, short activityid,
			short networkid, int partnerdomainid, String sessionid) {
		logger
				.info(" Inside recordActivity(HttpServletRequest,short,short) of PassController class ");
		pPMemberActivityDataObject = getpPMemberActivityDataObject();
		pPMemberActivityDataObject.setActivityCodeId(activityid);// activity_code_id

		if (networkid == 0) {
			String referer = request.getHeader("referer");
			if (referer != null) {
				networkid = getPassService().getNetworkIdUsingURL(referer);
			}
		}
		pPMemberActivityDataObject.setNetworkCodeId(networkid);// network_code_id
		pPMemberActivityDataObject.setPageUrl(request.getHeader("referer"));// page_url
		if (request.getParameter("url") != null) {
			pPMemberActivityDataObject
					.setTargetUrl(request.getParameter("url"));// taget_url
		} else {
			pPMemberActivityDataObject.setTargetUrl("");// taget_url
		}
		if (getSocid(request) != null) {
			pPMemberActivityDataObject
					.setMemberSocialNetworkId(getSocid(request));// member_social_id
		} else {
			pPMemberActivityDataObject.setMemberSocialNetworkId("");// member_social_id
		}
		pPMemberActivityDataObject.setMemberSessionId(sessionid);// member_session_id
		if (getPPId(request) != null && getPPId(request).equals("") == false) {
			pPMemberActivityDataObject.setPp_id(Integer
					.parseInt(getPPId(request)));// pp_id
		}
		if (getPartnerCode(request) != null) {
			pPMemberActivityDataObject.setPartnerDomainId(partnerdomainid);
		} else {
			pPMemberActivityDataObject.setPartnerDomainId(0);
		}

		recordActivity(pPMemberActivityDataObject);
		// recordActivity( request, activity, getSocnet(request),
		// getPartnerCode(request) ) ;
		logger
				.info(" Outside recordActivity(HttpServletRequest,short,short) of PassController class ");
	}

	protected void recordActivity(
			PPMemberActivityDataObject ppMemberActivityDataObject) {
		logger
				.info(" Inside recordActivity(PPMemberActivityDataObject) of PassController class ");
		logger.info("Value of PPID is::"+ppMemberActivityDataObject.getPp_id());
		getPassService().insertPPMemberActivity(ppMemberActivityDataObject);
		logger
				.info(" Outside recordActivity(PPMemberActivityDataObject) of PassController class ");
	}

	public void init() {

		logger.info(" Servlet Name is::" + controllerName);
		useLinkTracking_.put("payparade.net", "payparaded.payparade.net");
		useLinkTracking_.put("brugeshome.com", "payparade.brugeshome.com");
		useLinkTracking_
				.put("thesneakychef.com", "payparade.thesneakychef.com");
		numServed_.put(controllerName, 1L);
		maxMillis_.put(controllerName, 0L);
		totalMillis_.put(controllerName, 0L);
		lastMillis_.put(controllerName, 0L);
		ignore_.put("version", "yes");
		refreshData();
	}

	public void refreshData() {
		if (partner_options_ != null) {
			logger.info("Clearing Partner Options");
			partner_options_.clear();
		}
		partner_options_ = getPassService().select_pp_partner_option();
		if (servers_ != null) {
			logger.info("Clearing Partner Server Assignments");
			servers_.clear();
		}
		servers_ = getPassService().select_from_pp_partner_servers();
	}

	public PPPartnerOptionDataObject getPartnerOptionString(String partnerdomain) {
		logger.info(" Inside PPPartnerOptionDataObject=getPartnerOptionString(String) of PassController class");
		int partnerdomainid = getPassService()
				.getPartnerDomainId(partnerdomain);
		PPPartnerOptionDataObject partnerOptionDataObject = null;
		partnerOptionDataObject = partner_options_.get(partnerdomainid);
		logger.info(" Outside PPPartnerOptionDataObject=getPartnerOptionString(String) of PassController class");
		return partnerOptionDataObject;

	}

	public boolean haveFriends(HttpServletRequest request) {
		logger
				.info("have friends>" + getAttribute(request, "numfriends")
						+ "<");
		return "null".equals(getAttribute(request, "numfriends"));
	}

	public void getPayparadeServerdetailByPartnername(HttpServletRequest request) {
		logger
				.info("Inside getPayparadeServerdetailByPartnername() of PassServlet class");
		String partnersite = getPartnerCode(request);
		logger.info("partnersite Name is---" + partnersite);
		logger.info("Referer is===" + request.getHeader("referer"));
		/*
		 * String query =
		 * "SELECT payparade_server FROM pp_partner_servers where partner_host='"
		 * +partnersite+"'"; logger.info(query); DatabaseResult results =
		 * db_.executeQuery(query) ; DataSet dataset = results.getDataSet() ;
		 * DatabaseRow row = null; for(int i = 0 ; i< dataset.size() ; i++){ row
		 * = dataset.get(i); hostname_ = row.getString("payparade_server"); }
		 */
		hostname_ = getPassService()
				.Select_payparade_server_from_pp_partner_server(partnersite);
		logger.info("Host Name is----" + hostname_);
		logger
				.info("Outside getPayparadeServerdetailByPartnername() of PassServlet class");
	}

	public void messageResponse(PrintWriter out, String message)
			throws IOException {
		out.print(messageResponse(message));
	}

	public String messageResponse(String message) throws IOException {
		return "<html><body>PayParade Active Social Server (PASS) "
				+ controllerName + " endpoint <br/>" + message
				+ "</body></html>";
	}

	public void messageResponse(PrintWriter out, String message, int refresh)
			throws IOException {
		out.print(messageResponse(message, refresh));
	}

	public String messageResponse(String message, int refresh)
			throws IOException {
		return "<html><head><meta http-equiv=\"refresh\" content=\"" + refresh
				+ "\"></head><body>PayParade Active Social Server (PASS) "
				+ controllerName + " endpoint <br/>" + message
				+ "</body></html>";
	}
}
