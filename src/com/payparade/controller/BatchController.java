package com.payparade.controller;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.FacebookSignatureUtil;
import com.payparade.dataobject.PPCustomerScoreDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPPartnerNetworkDataObject;
import com.payparade.service.BatchService;
import com.payparade.util.Client;
import com.payparade.util.XMLUtils;

public class BatchController extends PassController {
	private BatchService batchService;

	public BatchService getBatchService() {
		return batchService;
	}

	public void setBatchService(BatchService batchService) {
		this.batchService = batchService;
	}

	public boolean get(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode,
			String partnerHost) throws ServletException, IOException {
		boolean done = true;
		logger.info(" Inside get() of BatchController class ");
		logger.info(" Resource String is=== " + resourceString);
		try {
			if (resourceString == null) {
				done = false;
			} else if (resourceString.startsWith("scoring")) {
				runSocialScoringModel(request, response, resourceString);
			} else if ("pipeline".equals(resourceString)) {
				getPipeLine(request, response, out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(" Outside get() of BatchController class ");
		logger.info(" Value of done is=== " + done);
		return done;

	}

	private void getPipeLine(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out) {
		FileOutputStream fout; // declare a file output object
		PrintStream p = null; // declare a print stream object

		try {
			fout = new FileOutputStream("/tmp/deals.txt");
			p = new PrintStream(fout);
			getPipeline(p);
		} catch (Exception e) {
			logger.info("Error (" + e.getMessage() + ") writing to deals file");
		} finally {
			if (p != null)
				p.close();
		}

	}

	private void getPipeline(PrintStream out) {
		getDeals("21633", "dbiadmin", out);
	}

	public void runSocialScoringModel(HttpServletRequest request,
			HttpServletResponse response, String resourceString) {
		logger
				.info(" Inside runSocialScoringModel() of BatchController class ");
		String parts[] = resourceString.split("/");
		try {
			PrintWriter out = response.getWriter();
			out.println("Length is::" + parts.length);
			out.println(resourceString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (parts.length == 3)
			score(parts[1], parts[2]);
		else
			logger.error("Incorrect number of arguments.  Expected 3 - found "
					+ parts.length + " (" + resourceString + ")");
		logger
				.info(" Outside runSocialScoringModel() of BatchController class ");
	}

	private void getDeals(String id, String name, PrintStream out) {
		int numDeals = 0;
		int offset = 0;
		do {
			String urlString = "http://www.pipelinedeals.com/fc4ef13e82029c4d/deals?userId="
					+ id + "&offset=" + offset + "&limit=100";
			logger.info("Requesting deals " + offset + " - " + (offset + 100)
					+ " for user " + name);
			offset += 100;
			numDeals = 0;

			Client client = new Client();
			try {
				client.webFile(urlString);
				String resp = (String) client.getContent();
				logger.info("Pipeline request (" + urlString + ") retrieved("
						+ client.getResponseCode() + ")");

				if (resp != null) {
					XMLUtils parser = new XMLUtils(resp);
					String dealString = parser.getNext("deal");
					while (dealString != null) {
						++numDeals;
						String dealname = parser.getValue("name", dealString);
						String ownername = parser.getValue("ownerFullName",
								dealString);
						String amt = parser.getValue("value", dealString);
						String prob = parser
								.getValue("probability", dealString);
						out.println(ownername + "\t" + dealname + "\t" + prob
								+ "\t" + amt);
						dealString = parser.getNext("deal");
					}
				} else {
					logger.info("getPipeLine() returned null");
				}
			} catch (IOException e) {
				logger.info("io exception at caused " + e.getMessage());
			}
		} while (numDeals != 0);
	}

	public void score(String days, String hours) {
		logger
				.info(" Inside runSocialScoringModel() of BatchController class ");
		logger.info(" days is::: " + days);
		logger.info(" hours is::: " + hours);
		Date today = new java.util.Date();
		Timestamp now = new Timestamp(today.getTime());
		Timestamp from = new Timestamp(today.getTime());

		long h = Long.valueOf(hours).longValue();
		long d = Long.valueOf(days).longValue();

		long msNow = now.getTime();
		long minusHours = 60L * 60L * 1000L * h;
		long minusDays = 24L * 60L * 60L * 1000L * d;
		long msFrom = msNow - minusHours - minusDays;
		PPCustomerScoreDataObject ppCustomerScoreDataObject = null;
		Integer vtsMax = 0;
		Integer qovMax = 0;
		Integer sonMax = 0;
		Integer swfMax = 0;
		logger
				.info("calculating now(" + msNow + ") minus hours("
						+ minusHours + ") minus days(" + minusDays
						+ ") equals From(" + msFrom + ")");

		from.setTime(msFrom);

		String nowString = now.toString().substring(0, 19);
		String fromdate = from.toString().substring(0, 19);

		getBatchService().delete_customer_rank();
		getBatchService().delete_customer_score();
		getBatchService().delete_last_access();

		List<PPMemberActivityDataObject> memberActivityDataObjectList = getBatchService()
				.getsocial_id_networkcodeid_maxppidfrom_pp_member_activity();

		getBatchService().update_pp_id_of_pp_member_activity(
				memberActivityDataObjectList);

		getBatchService()
				.select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity(
						fromdate);

		getBatchService()
				.select_member_id_member_social_network_id_max_friends_from_pp_member_network();

		getBatchService().select_ppid_partnerdomainid_countswf(fromdate);

		getBatchService().select_ppid_partnerdomainid_countfct(fromdate);

		/***************************************************************************************************/
		List<PPCustomerScoreDataObject> ppCustomerScoreDataObjectList = getBatchService()
				.select_vts_qov_son_swf_from_ppcustomerscore();

		if (ppCustomerScoreDataObjectList != null
				&& ppCustomerScoreDataObjectList.size() > 0) {
			ppCustomerScoreDataObject = ppCustomerScoreDataObjectList.get(0);
			vtsMax = ppCustomerScoreDataObject.getVts();
			qovMax = ppCustomerScoreDataObject.getQov();
			sonMax = ppCustomerScoreDataObject.getSon();
			swfMax = ppCustomerScoreDataObject.getSwf();
		}
		int affinityMax = vtsMax * qovMax;
		int influenceMax = sonMax * swfMax;

		logger.info(" Maximums -- vts:" + vtsMax + "   qov:" + qovMax
				+ "   son:" + sonMax + "   swf:" + swfMax + "    aff denom:"
				+ affinityMax + "   inf denom:" + influenceMax);

		getBatchService().insert_ppcustomerrank(affinityMax, influenceMax);

		getBatchService().insert_pplastaccess();

		getBatchService().update_ppcustomerrank();

		getBatchService().update_ppcustomerrankv1();

		logger
				.info(" Outside runSocialScoringModel() of BatchController class ");
	}

	public void init() {

		controllerName = "batch";
		super.init();
	}

	public boolean post(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out,
			String resourceString, boolean isSigned,
			HashMap<String, String> myCookies, String partnerCode)
			throws ServletException, IOException {
		logger.info(" Inside post() of  BatchController class ");
		logger.info(" value of resourceString is::" + resourceString);
		boolean done = true;
		if (resourceString == null) {
			done = false;
		} else if (resourceString.startsWith("register")) {
			register(request, response, out, resourceString);
		}
		logger.info(" value of done is::" + done);
		logger.info(" Outside post() of  BatchController class ");
		return done;
	}

	private void register(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, String resourceString) {
		logger
				.info(" Inside register(HttpServletRequest,HttpServletResponse,PrintWriter,String) of BatchController class ");
		Object resp = null;
		HashMap<String, String> emails = new HashMap<String, String>();
		HashMap<String, String> reverse = new HashMap<String, String>();

		String parts[] = resourceString.split("/");
		logger.logStringArray(parts);
		String partnerCode = parts[1];
		// Long id=new Long(727601003L) ;

		String message = null;

		Enumeration params = request.getParameterNames();
		if (params.hasMoreElements()) {
			String name = (String) params.nextElement();
			message = name + "=" + request.getParameter(name);
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
			logger.error("Parser Ex - " + e.getMessage());
		} catch (SAXException e) {
			logger.error("SAX Ex - " + e.getMessage());
		} catch (IOException e) {
			logger.error("IO Ex - " + e.getMessage());
		}

		logger.info("Document:" + document.getTextContent());
		Element data = document.getDocumentElement();
		logger.info("Data Tag:" + data.getTagName());

		NodeList addressList = data.getChildNodes();
		int numaddr = addressList.getLength();
		logger.info(" " + numaddr + " addresses received");
		int addrnum = 0;
		for (int c = 0; c < numaddr; c++) {
			Node addr = addressList.item(c);
			emails.put(addr.getTextContent(), FacebookSignatureUtil
					.generateEmailHash(addr.getTextContent()));
			reverse.put(FacebookSignatureUtil.generateEmailHash(addr
					.getTextContent()), addr.getTextContent());
		}
		int partnerdomainid = getBatchService()
				.select_id_from_pp_partner_domain(getPartnerCode(request));
		logger.info(" Partner Domain Id is::" + partnerdomainid);
		PPPartnerNetworkDataObject partnerNetworkDataObject = getPartnerNetworkService()
				.getPPPartnerNetwork().get(partnerdomainid);
		FacebookJsonRestClient client = null;
		if (partnerNetworkDataObject != null) {
			logger.info(" APIKEY is::" + partnerNetworkDataObject.getApi_key());
			logger.info(" Secret Key is::"
					+ partnerNetworkDataObject.getSecret());
			client = new FacebookJsonRestClient(partnerNetworkDataObject
					.getApi_key(), partnerNetworkDataObject.getSecret());
		}
		if (client != null) {
			logger.info("client returned");
			try {
				ArrayList<Map<String, String>> accounts = new ArrayList<Map<String, String>>();
				Set<String> keys = emails.keySet();
				int counter = 0;
				for (String address : keys) {
					HashMap<String, String> account = new HashMap<String, String>();
					account.put("email_hash", emails.get(address));
					accounts.add(account);
					logger.info("(" + (++counter) + ") added - " + address
							+ "=" + emails.get(address));

					if ((counter % 1000 == 0) || (counter == numaddr)) {
						JSONArray list = (JSONArray) client
								.connect_registerUsers(accounts);
						logger.info(" " + list.length() + " hashes returned");
						for (int i = 0; i < list.length(); i++)
							logger.info("received " + list.get(i) + " for "
									+ reverse.get(list.get(i)));
						accounts.clear();
					}
				}
			} catch (FacebookException e) {
				logger.error("fb ex registering - " + e.getMessage());
				e.printStackTrace();
			} catch (JSONException e) {
				logger.error("JSON ex registering - " + e.getMessage());
				e.printStackTrace();
			}

		} else {
			logger.error("Failed to get client");
		}
		logger
				.info(" Outside register(HttpServletRequest,HttpServletResponse,PrintWriter,String) of BatchController class ");

	}

}
