package com.payparade.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Fx2Controller extends PassController {

	@Override
	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		logger.info("Inside get() of Fx2Controller class");
		boolean done = true;
		try {
			logger.info("ResourceString is:::"+resourceString);
			if (resourceString == null) {
				logger.error("null resource string received");
				done = false;
			} else if ("sio".equals(resourceString)) {
				getSIO(request, response, out, partnerCode, partnerHost,
						myCookies);
			} else {
				done = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Outside get() of Fx2Controller class");
		return done;
	}

	public void getFBConnect(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost, HashMap<String, String> myCookies)
			throws ServletException, IOException {
		out.print(getIframeHead(partnerCode));
		out
				.print("<body>"
						+ "<script>"
						+ "FB_RequireFeatures([\"Api\"], function() { FB.Facebook.init(appid, 'http://'+server+'/wh2/xd_receiver.htm'); }); "
						+ "FB.Bootstrap.requireFeatures([\"Connect\"], function() { "
						+ "	FB.Connect.requireSession(function(exception) { "
						+ "		ppFlashMovie(\"flashContent\").fxConnected() ; "
						+ "		window.close() ; " + "		}); " + "	});"
						+ "</script>" + "</body>" + "</html>");
	}

	public void getSIO(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String partnerCode,
			String partnerHost, HashMap<String, String> myCookies)
			throws ServletException, IOException {
		logger.info(" Inside getSIO() of Fx2Controller class ");
		out.print(getIframeHead(partnerCode));
		out
				.print("<body>"
						+ "<script type='text/javascript'>"
						+ "var flashVars = { as_swf_name: 'flashContent' };	"
						+ "var params = {wmode: 'transparent'};"
						+ "swfobject.embedSWF('http://"
						+ partnerHost
						+ "/wh2/sio3.swf', 'flashContent', '100%', '100%', '9.0.0', 'expressInstall.swf', flashVars, params);"
						+ "</script>" + "<div id='flashContent'></div>"
						+ "</body>" + "</html>");
		logger.info(" Outside getSIO() of Fx2Controller class ");
	}

	public String getIframeHead(String partnerCode) {
		String result = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:fb=\"http://www.facebook.com/2008/fbml\">"
				+ "  <head> "
				+ "  <script src=\"http://static.ak.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php\" type=\"text/javascript\"></script>  "
				+ "  <script src=\"http://"
				+ hostname_
				+ "/iframe/js/payparade_iframe.js\" type=\"text/javascript\"></script>"
				+ "  <script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js\"></script> "
				+ "</head> ";
		return result;
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
		controllerName = "fx2";

		super.init();
	}
}
