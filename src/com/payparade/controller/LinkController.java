package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.payparade.dataobject.PPShareLinkDataObject;
import com.payparade.service.LinkService;
import com.payparade.util.Activity;

public class LinkController extends PassController {

	private PPShareLinkDataObject ppShareLinkDataObject;

	public PPShareLinkDataObject getPpShareLinkDataObject() {
		return ppShareLinkDataObject;
	}

	public void setPpShareLinkDataObject(
			PPShareLinkDataObject ppShareLinkDataObject) {
		this.ppShareLinkDataObject = ppShareLinkDataObject;
	}

	public LinkService getLinkService() {
		return linkService;
	}

	public void setLinkService(LinkService linkService) {
		this.linkService = linkService;
	}

	private LinkService linkService;

	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean done = true;
		logger.info(" Inside get() of LinkController class ");
		logger.info(controllerName + ".get received >" + resourceString
				+ "< to process");

		if (resourceString == null) {
			logger.error("null resource string received");
			done = false;
		} else if (resourceString.startsWith("shorten")) {
			shorten(request, response, out, partnerCode, myCookies);
		} else if (resourceString.startsWith("expand")) {
			expand(request, response, out, partnerCode);
		} else if (resourceString.startsWith("info")) {
			info(request, response, out, partnerCode);
		} else if (resourceString.startsWith("list")) {
			list(request, response, out, partnerCode);
		} else {
			try {
				getLink(request, response, out, resourceString);
			} catch (Exception e) {
				logger.error("LinkController::get::Exception is::",e);
			}

			if (debug_ && !done) {
				logger.info(controllerName + ".get did NOT process - "
						+ request.getRequestURI());
			}
		}
		logger.info(" Outside get() of LinkController class ");
		return done;
	}

	@Override
	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		boolean done = false;
		return done;
	}

	public static String encode(int code) {
		logger.info("encoding " + code);
		String returnValue = "";
		byte[] b = new byte[1];
		int val = code;

		for (int i = 7; i >= 0; i--) {
			if (Math.pow(26, i) <= val) {
				b[0] = (byte) (64 + ((int) Math.floor(val / Math.pow(26, i))));
				returnValue = new String(b) + returnValue;
				val -= (b[0] - 64) * Math.pow(26, i);
				logger.info("position " + i + "  value:" + (b[0] - 64)
						+ "   return:" + returnValue);
			} else {
				returnValue = "0" + returnValue;
			}
		}

		logger.info("encoding resulted in >" + returnValue + "<");
		while (returnValue.charAt(returnValue.length() - 1) == '0') {
			returnValue = returnValue.substring(0, returnValue.length() - 1);
		}
		logger.info("encoding trimmed to >" + returnValue + "<");

		int d = decode(returnValue);
		if (d != code)
			logger.error("encode (" + code + ") / decode(" + d + ") mismatch");

		return returnValue;
	}

	public static int decode(String code) {
		logger.info("decoding " + code);
		int returnValue = 0;
		int l = code.length();
		int v = 0;

		for (int i = 0; i < l; i++) {
			if (code.charAt(i) != '0') {
				v = (int) code.charAt(i) - 64;
				returnValue += (Math.pow(26, i) * v);
				logger.info("position " + i + "  value:" + v + "   return:"
						+ returnValue);
			}
		}

		logger.info("decoding resulted in >" + returnValue + "<");
		return returnValue;
	}

	public void getLink(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString)
			throws IOException {
		logger.info(" Inside getLink() of LinkController class ");
		try{
		String password = request.getParameter("password");
		String userid = request.getParameter("userid");
		String idString = resourceString;
		String id = getPPId(request);

		if (resourceString.length() == 0) {
			logger.info("no link found");
			out
					.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">");
			out.print("  <body> ");
			out.print("    <script>");
			out.print("      if (top != window) ");
			out.print("        top.location.href =\"http://" + hostname_
					+ "/swf/PPlinkus.html\" ; ");
			out.print("      else ");
			out.print("        window.location.href =\"http://" + hostname_
					+ "/swf/PPlinkus.html\" ; ");
			out.print("</script> ");
			out.print("  </body></html> ");
		} else {
			String sid = getId(idString);
			logger.info("value of sid is:::"+sid);
			List<PPShareLinkDataObject> listPPSharedLinkDataObject = getLinkService()
					.setSelect_ppsharedlink_where_shared_id(sid);
			// DatabaseResult results =
			// db_.executeQuery("SELECT * FROM pp_shared_link WHERE share_id = "+sid
			// ) ;
			// DataSet dataset = results.getDataSet() ;
			if (listPPSharedLinkDataObject != null
					&& listPPSharedLinkDataObject.size()== 0) {
				response.sendError(404);
			} else {
				// DatabaseRow row = dataset.get(0) ;
				PPShareLinkDataObject ppShareLinkDataObject = listPPSharedLinkDataObject
						.get(0);
				if (request.getHeader("user-agent")!=null && request.getHeader("user-agent").startsWith(
						"facebookexternalhit")) {
					out.print("<html><head>"
							+ "<meta name=\"title\" content=\""
							+ ppShareLinkDataObject.getShared_title() + "\"/>"
							+ "<meta name=\"description\" content=\""
							+ ppShareLinkDataObject.getShared_content()
							+ "\"/>" + "<link rel=\"image_src\" href=\""
							+ ppShareLinkDataObject.getShared_image() + "\"/>"
							+ "</head>" + "<body>" + "<img src=\""
							+ ppShareLinkDataObject.getShared_image() + "\"/>"
							+ "</body>" + "</html");
				} else {
					HttpSession session = request.getSession(true);

					// TODO: sharee_social_id doesn't exist in this dataset.
					// Grab the actual member_session_id instead
					// this.recordActivity( request, "Click Through",
					// getSocnet(request), row.getString("partner_domain"),
					// session.getId() ) ;
					int partnerdomainid = getLinkService()
							.select_id_from_pp_partner_domain(
									getPartnerCode(request));
					short networkcodeid = getLinkService().getNetworkId(
							getSocnet(request));
					this.recordActivity(request, Activity.CLICK_THROUGH,
							networkcodeid, partnerdomainid, session.getId());
					/*
					 * db_.executeInsert("INSERT INTO pp_link_activity(share_id, sharee_pp_id) "
					 * + "VALUES ("+ "\""+sid+"\""+","+ "\""+id+"\""+ ")") ;
					 */
					getLinkService().Insert_into_ppshared_activity(sid, id);
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
							+ ppShareLinkDataObject.getShared_url() + "\" ; ");
					out.print("  </script></body></html> ");

				}
			}
		}
		}catch(Exception e){
			logger.error("LinkController::getLink::Exception is:::",e);
		}
		logger.info(" Outside getLink() of LinkController class ");
	}

	public String getId(String idString) {
		if (!idString.matches("^[1-9]*$")) {
			logger.info("new style link (" + idString + ") passed");
			int i = decode(idString);
			idString = Integer.toString(i);
		} else {
			logger.info("old style link (" + idString + ") passed");
			encode(Integer.valueOf(idString).intValue());
		}
		return idString;
	}

	public void shorten(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			HashMap<String, String> myCookies) throws ServletException,
			IOException {
		logger.info(" Inside shorten() of LinkController class ");
		try{
		String id = getPPId(request);
		String encodedURI = "";
		String unencodedURI = "";

		if (request.getParameter("id") != null) {
			id = request.getParameter("id");
		}
		logger.info(" Id is:::"+id);
		// recordActivity( request, "Shorten for id "+id) ;
		recordActivity(request, Activity.SHORTEN);
		if (request.getParameter("url").contains("http://")) {
			encodedURI = URLEncoder
					.encode(request.getParameter("url"), "UTF-8");
			unencodedURI = request.getParameter("url");
		} else {
			unencodedURI = URLDecoder.decode(request.getParameter("url"),
					"UTF-8");
			encodedURI = request.getParameter("url");
		}

		logger.info("Shorten recieved >" + request.getParameter("url")
				+ "<    unencoded was >" + unencodedURI + "<    encoded was >"
				+ encodedURI + "<");

		Integer generatedId = 0;
		int networkcodeid = getLinkService().getNetworkId(getSocnet(request));
		int partnerdomainid = getLinkService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		String sharesocialid = (getSocid(request));
		getPpShareLinkDataObject().setPp_id(Integer.parseInt(id));
		getPpShareLinkDataObject().setShare_social_id(sharesocialid);
		getPpShareLinkDataObject().setNetwork_code_id(networkcodeid);
		getPpShareLinkDataObject().setShared_url(unencodedURI);
		getPpShareLinkDataObject().setPartner_domain_id(partnerdomainid);
		getPpShareLinkDataObject().setShared_content("");
		getPpShareLinkDataObject().setShared_title("");
		getPpShareLinkDataObject().setShared_image("");
		generatedId = getLinkService().insert_pp_shared_link(
				ppShareLinkDataObject);

		/*
		 * Integer generatedId =db_.executeInsert(
		 * "INSERT INTO pp_shared_link(pp_id, network_code, sharer_social_id, shared_url, shared_title, shared_content, shared_image, partner_domain) "
		 * + "VALUES ("+ "\""+id+"\""+","+ "\""+getSocnet(request)+"\""+","+
		 * "\""+getSocid(request)+"\""+","+ "\""+unencodedURI+"\""+","+
		 * "\"\""+","+ "\"\""+","+ "\"\""+","+ "\""+partnerCode+"\""+ ")") ;
		 */
		String hash = LinkController.encode(generatedId);
		encodedURI = "http://" + linkurl_ + "/" + hash;
		logger.info("Using link tracking for " + partnerCode + "( "
				+ useLinkTracking_.get(partnerCode) + ") URL:" + encodedURI);
		out.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.println("<data>");
		out.println("\t<url>" + encodedURI + "</url>");
		out.println("\t<hash>" + hash + "</hash>");
		out.println("\t<id>" + id + "</id>");
		out.println("</data>");
		}catch(Exception e){
			logger.error("LinkController::shorten::Exception is::",e);
		}
		logger.info(" Outside shorten() of LinkController class ");
	}

	public void expand(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString)
			throws IOException {
		String idString = request.getParameter("hash");

		// db_.executeQuery("SELECT shared_url expanded_url FROM pp_shared_link WHERE share_id = "+getId(idString),
		// out ) ;
		getLinkService().setSelect_ppsharedlink_where_shared_id(idString, out);
	}

	public void info(HttpServletRequest request, HttpServletResponse response,
			PrintWriter out, String resourceString) throws IOException {
		String idString = request.getParameter("hash");

		if (!idString.matches("^[1-9]*$")) {
			logger.info("new style link (" + idString + ") passed");
			int i = decode(idString);
			idString = Integer.toString(i);
		} else {
			logger.info("old style link (" + idString + ") passed");
			encode(Integer.valueOf(idString).intValue());
		}
		getLinkService().setSelect_ppsharedlink_where_shared_id(idString, out);
	}

	private void list(HttpServletRequest request, HttpServletResponse response,
			PrintWriter out, String partnerCode) {
		logger.info("Inside list() of LinkController class");
		String id = getPPId(request);

		if (request.getParameter("id") != null) {
			id = request.getParameter("id");
		}

		recordActivity(request, Activity.SHORTEN);

		List<PPShareLinkDataObject> pPSharedDataObjectList = getLinkService()
				.setSelect_ppsharedlink_where_ppid(id);
		logger.info(pPSharedDataObjectList);

		out.println("<links>");
		for (PPShareLinkDataObject ppShareLinkDataObject : pPSharedDataObjectList) {
			out.println("\t<link>");
			out.println("\t\t<url>" + ppShareLinkDataObject.getShared_url()
					+ "</url>");
			// out.println(
			// "\t\t<shorturl>http://"+linkurl_+"/"+encode(row.getInteger("share_id"))+"</shorturl>"
			// ) ;
			out.println("\t\t<shorturl>http://" + linkurl_ + "/"
					+ encode(ppShareLinkDataObject.getId()) + "</shorturl>");
			// out.println( "\t\t<clicks>"+row.getInteger("clicks")+"</clicks>"
			// ) ;
			out.println("\t\t<clicks>" + ppShareLinkDataObject.getClick()
					+ "</clicks>");
			out.println("\t</link>");
		}
		out.println("</links>");
		logger.info("Outside list() of LinkController class");
	}
	
	public void init() {
		  controllerName = "link" ;
		  super.init() ;
	  	}
	
}
