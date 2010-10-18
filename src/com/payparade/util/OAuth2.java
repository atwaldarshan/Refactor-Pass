package com.payparade.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

public class OAuth2 {
	public static final String VERSION_1_0 = "1.0";

	/** The encoding used to represent characters as bytes. */
	public static final String ENCODING = "UTF-8";

	/** The MIME type for a sequence of OAuth parameters. */
	public static final String FORM_ENCODED = "application/x-www-form-urlencoded";

	public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
	public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
	public static final String OAUTH_SIGNATURE = "oauth_signature";
	public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
	public static final String OAUTH_NONCE = "oauth_nonce";
	public static final String OAUTH_VERSION = "oauth_version";

	public static final String HMAC_SHA1 = "HMAC-SHA1";
	public static final String RSA_SHA1 = "RSA-SHA1";

	public static class Problems {
		public static final String TOKEN_NOT_AUTHORIZED = "token_not_authorized";
		public static final String INVALID_USED_NONCE = "invalid_used_nonce";
		public static final String SIGNATURE_INVALID = "signature_invalid";
		public static final String INVALID_EXPIRED_TOKEN = "invalid_expired_token";
		public static final String INVALID_CONSUMER_KEY = "invalid_consumer_key";
		public static final String CONSUMER_KEY_REFUSED = "consumer_key_refused";
		public static final String TIMESTAMP_REFUSED = "timestamp_refused";
		public static final String PARAMETER_REJECTED = "parameter_rejected";
		public static final String PARAMETER_ABSENT = "parameter_absent";
		public static final String VERSION_REJECTED = "version_rejected";
		public static final String SIGNATURE_METHOD_REJECTED = "signature_method_rejected";

		public static final String OAUTH_PARAMETERS_ABSENT = "oauth_parameters_absent";
		public static final String OAUTH_PARAMETERS_REJECTED = "oauth_parameters_rejected";
		public static final String OAUTH_ACCEPTABLE_TIMESTAMPS = "oauth_acceptable_timestamps";
		public static final String OAUTH_ACCEPTABLE_VERSIONS = "oauth_acceptable_versions";
	}

	private static final String API_URL = "http://api.myspace.com";
	private HashMap<String, HashMap> consumerKeys_ = new HashMap<String, HashMap>();
	private HashMap<String, HashMap> consumerSecrets_ = new HashMap<String, HashMap>();
	protected static HashMap<String, String> tokens_ = new HashMap<String, String>();
	protected static HashMap<String, String> secrets_ = new HashMap<String, String>();
	protected static HashMap<String, String> sessionHosts_ = new HashMap<String, String>();
	protected static HashMap<String, String> token_secrets_ = new HashMap<String, String>();

	// private String consumerKey_ = "http://www.myspace.com/444020713";
	// private String consumerSecret_ = "06a48537421c45febc02bec90e66492f";
	// private static final String API_URL =
	// "http://term.ie/oauth/example/request_token.php";
	// private String consumerKey_ = "key";
	// private String consumerSecret_ = "secret";
	private String callbackUrl_ = null;
	protected static Logger logger_ = Logger.getLogger(OAuth2.class
			.getSimpleName());

	public OAuth2() {
		super();
		/*
		 * MySpace
		 */
		HashMap<String, String> keys = new HashMap<String, String>();
		keys.put("payparade.net", "628e235925ea4aad9ec487645057c891");
		keys.put("davidsprom.com", "43538b1985b84811981787be5dbc4305");
		keys.put("trellist.com", "43538b1985b84811981787be5dbc4305");
		HashMap<String, String> secrets = new HashMap<String, String>();
		secrets.put("payparade.net", "c11a01f731f440cea4f0f3c7e926ed66");
		secrets.put("davidsprom.com", "e7ee647382b645acbe183dafc4fd48db");
		secrets.put("trellist.com", "e7ee647382b645acbe183dafc4fd48db");
		consumerKeys_.put("myspace", keys);
		consumerSecrets_.put("myspace", secrets);

	}

	public HashMap<String, String> getRequestToken(String session,
			String partnerCode, String partnerHost) {
		/*		
     */
		logger_
				.info(" Inside getRequestToken(String,String,string) of OAuth2 class ");
		HashMap<String, String> args = new HashMap<String, String>();
		String urlString = generateUrl("/request_token", args, "", partnerCode);
		String resp = Client.doGet(urlString);
		logger_.info("MySpace request (" + urlString + ") retrieved(" + resp
				+ ")");

		if (resp == null) {
			logger_
					.error("myspace unavailable to retreive request token for session "
							+ session + " - null response returned");
		}

		HashMap<String, String> retArgs = StringUtils.parseParams(StringUtils
				.percentDecode(resp, ENCODING));
		logger_.info("getRequestToken() returned:");
		logger_.logHashMap(retArgs);

		String token = retArgs.get("oauth_token");
		String secret = retArgs.get("oauth_token_secret");

		tokens_.put(session, token);
		secrets_.put(session, secret);
		sessionHosts_.put(session, partnerHost);
		logger_
				.info(" Outside getRequestToken(String,String,string) of OAuth2 class ");
		return retArgs;
	}

	public static void clearSession(String session) {
		tokens_.remove(session);
		secrets_.remove(session);
		sessionHosts_.remove(session);
		token_secrets_.remove(session);
	}

	public String authorize(HashMap<String, String> args, String hostname,
			String partnerHost, String partnerCode, String session) {
		HashMap<String, String> authArgs = new HashMap<String, String>();
		authArgs.put("oauth_callback", "http://" + partnerHost
				+ "/pass/auth/myspace/authorized?session=" + session);
		authArgs.put("oauth_token", args.get("oauth_token"));
		String urlString = generateUrl("/authorize", authArgs, args
				.get("oauth_token_secret"), partnerCode);
		return urlString;
	}

	public static String getPartnerHost(String session) {
		return sessionHosts_.get(session);
	}

	public HashMap<String, String> getAccessToken(String session) {
		logger_.info("getAccessToken() for session:" + session);
		logger_.info("tokens:");
		logger_.logHashMap(tokens_);

		logger_.info("secrets:");
		logger_.logHashMap(secrets_);

		HashMap<String, String> retArgs = null;

		String realPartnerCode;
		try {
			realPartnerCode = StringUtils.getPartnerCode(sessionHosts_
					.get(session));
			logger_.logHashMap(sessionHosts_);
			logger_.info("Session host:" + sessionHosts_.get(session) + "("
					+ realPartnerCode + ") retrieved for session " + session);

			HashMap<String, String> args = new HashMap<String, String>();
			args.put("oauth_token", tokens_.get(session));
			// args.put("oauth_token_secret",
			// this.decodePercent(secrets_.get(session)));
			String urlString = generateUrl("/access_token", args, secrets_
					.get(session), realPartnerCode);
			String resp = Client.doGet(urlString);
			logger_.info("MySpace request (" + urlString + ") retrieved("
					+ resp + ")");
			retArgs = StringUtils.parseParams(resp);
			logger_.info("getAccessToken() returned:");
			logger_.logHashMap(retArgs);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retArgs;

	}

	public JSONObject getMyData(String token, String secret, String partnerCode) {
		HashMap<String, String> retArgs = null;
		JSONObject entry = null;
		JSONObject myData = null;
		String realPartnerCode;
		HashMap<String, String> args = new HashMap<String, String>();
		args.put("oauth_token", token);
		String urlString = generateUrl("/v2/people/@me/@self", args, secret,
				partnerCode);
		Client client = new Client();
		try {
			client.webFile(urlString);

			String resp = (String) client.getContent();
			logger_.info("MySpace request (" + urlString + ") retrieved("
					+ resp + ")");
			if (resp != null) {
				if (resp.contains("{")) {
					entry = new JSONObject(resp);
					myData = new JSONObject(entry.getString("entry"));
					logger_.info("JSON " + myData.length() + " objects stored");
					logger_.info("getMyData() returned:");
					// logger_.logJSONObject(myData) ;
				} else
					logger_.info("getMyData() returned non JSON object >"
							+ resp + "<");
			} else
				logger_.info("getMyData() returned null");
		} catch (MalformedURLException e1) {
			logger_.error("Malformed (" + urlString + ") caused "
					+ e1.getMessage());
		} catch (IOException e1) {
			logger_.error("io exception caused " + e1.getMessage());
		} catch (JSONException e) {
			logger_.error("JSON Exception " + e.getMessage());
		}

		return myData;

	}

	public String generateUrl(String path, HashMap<String, String> args,
			String secret, String partnerCode) {
		logger_
				.info(" Inside generateurl(String,HashMap,String,String) of OAuth2 class ");
		String resp = "";
		try {
			long randomNum = new Random().nextLong();
			long timestamp = (long) System.currentTimeMillis() / 1000;
			args.put("oauth_consumer_key", (String) consumerKeys_
					.get("myspace").get(partnerCode));
			args.put("oauth_nonce", Long.toString(randomNum));
			args.put("oauth_signature_method", "HMAC-SHA1");
			args.put("oauth_timestamp", Long.toString(timestamp));
			args.put("oauth_version", "1.0");
			logger_.info("generateUrl arguments:");
			logger_.logHashMap(args);
			List<String> argList = new ArrayList<String>();

			for (String key : args.keySet()) {
				String encodedArg = null;
				encodedArg = StringUtils.percentEncode(args.get(key), ENCODING);
				logger_.info(key + "  arg:" + args.get(key) + "   encoded:"
						+ encodedArg);
				argList.add(key + "=" + encodedArg);
			}

			Collections.sort(argList);
			StringBuilder part3 = new StringBuilder();
			for (int i = 0; i < argList.size(); i++) {
				part3.append(argList.get(i));
				if (i != argList.size() - 1) {
					part3.append("&");
				}
			}
			String part1 = "GET";
			String part2 = API_URL + path;
			String baseString = StringUtils.percentEncode(part1, ENCODING)
					+ "&" + StringUtils.percentEncode(part2, ENCODING) + "&"
					+ StringUtils.percentEncode(part3.toString(), ENCODING);
			String sig = getHMACSHA1(StringUtils.percentEncode(
					(String) consumerSecrets_.get("myspace").get(partnerCode),
					ENCODING)
					+ "&" + StringUtils.percentEncode(secret, ENCODING),
					baseString);
			String esig = StringUtils.percentEncode(sig, ENCODING);
			resp = part2 + "?" + part3 + "&" + "oauth_signature=" + esig;
			logger_.info("basestring=" + baseString + "<  sig=" + esig);
		} catch (Exception e) {
			logger_.error("OAuth2::generateUrl::" + e);
		}
		logger_
				.info(" Outside generateurl(String,HashMap,String,String) of OAuth2 class ");
		return resp;
	}

	private String getHMACSHA1(String key, String data) {
		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"),
					"HMAC-SHA1");

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));

			// base64-encode the hmac
			return new String(
					com.sun.org.apache.xerces.internal.impl.dv.util.Base64
							.encode(rawHmac)); // Base64Utils.base64Encode(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException("unable to generate HMAC-SHA1", e);
		}
	}

	/*
	 * 
	 * base
	 * me:GET&http%3A%2F%2Fapi.myspace.com%2Faccess_token&oauth_consumer_key
	 * %3Dhttp%253A%252F%252Fwww.myspace.com%252F444020713%26oauth_nonce%3D
	 * 8505949338463220654
	 * %26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp
	 * %3D1234535356%26oauth_token
	 * %3DiU%252BLYDW67qVZOky1UN0%252FnrBLfeU32NQlJLL0KBz6VuAwcPM0Q
	 * %252BAA9Veu2XNuPnCUZeAmT098XugMJ6LZDbdzUQ
	 * %253D%253D%26oauth_version%3D1.0<
	 * ms:GET&http%3A%2F%2Fapi.myspace.com%2Faccess_token
	 * &oauth_consumer_key%3Dhttp
	 * %253A%252F%252Fwww.myspace.com%252F444020713%26oauth_nonce
	 * %3D8505949338463220654
	 * %26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp
	 * %3D1234535641%26oauth_token
	 * %3DiU%252BLYDW67qVZOky1UN0%252FnrBLfeU32NQlJLL0KBz6VuAwcPM0Q
	 * %252BAA9Veu2XNuPnCUZeAmT098XugMJ6LZDbdzUQ%253D%253D%26oauth_version%3D1.0
	 * 
	 * sig me:2%2FBMhoq0sbeFkNwUmzDP1j6YafY%3D
	 * ms:dUh5d%2FyALhpGFr6zJdY5eEyRqGc%3D
	 */

	public synchronized String ppEncode(String s) {
		if (s == null) {
			return "";
		} else {
			String from = null;
			try {
				from = new String(s.getBytes("UTF-8"));
				return from.replace(":", "%3A").replace("/", "%2F").replace(
						"?", "%3F").replace("#", "%23").replace("[", "%5B")
						.replace("]", "%5D").replace("@", "%40").replace("!",
								"%21").replace("$", "%24").replace("&", "%26")
						.replace("'", "%27").replace("(", "%28").replace(")",
								"%29").replace("*", "%2A").replace("+", "%2B")
						.replace(",", "%2C").replace(";", "%3B").replace("=",
								"%3D");
			} catch (UnsupportedEncodingException e) {
				logger_.error(e.getMessage());
			}
			return null;
		}
	}

	/*
	 * mine:http://api.myspace.com/access_token?oauth_consumer_key=http%3A%2F%2Fwww
	 * .myspace.com%2F444020713&oauth_nonce=7070813076529439001&
	 * oauth_signature_method=HMAC-SHA1&oauth_timestamp=1234534577&oauth_token=
	 * gk3TQEy23CrCEqrGhUiO0QQY157OdxCFaZevrl5pAyIJYHLvHv
	 * %2F0UW7myzUEy0EnqBv0vEijH1NCQigBILW7qg
	 * %3D%3D&oauth_version=1.0&oauth_signature=rIishkw8rLm08rX7aVTgCzv43ds%3D
	 * 
	 * 
	 * 
	 * 
	 * *
	 */

}
