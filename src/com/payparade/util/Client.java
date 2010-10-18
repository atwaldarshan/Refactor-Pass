package com.payparade.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

public class Client {
	protected static Logger logger_ = Logger.getLogger(Client.class
			.getSimpleName());
	private java.util.Map<String, java.util.List<String>> responseHeader = null;
	private java.net.URL responseURL = null;
	private int responseCode = -1;
	private String MIMEtype = null;
	private String charset = null;
	private Object content = null;

	protected static HashMap<String, Long> numServed_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> maxMillis_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> totalMillis_ = new HashMap<String, Long>();
	protected static HashMap<String, Long> lastMillis_ = new HashMap<String, Long>();
	protected static long overallNumServed_ = 0L;
	protected static long overallMaxMillis_ = 0L;
	protected static long overallTotalMillis_ = 0L;

	public static String doGet(String urlString) {
		logger_.info("Inside doGet(String) of Client class");
		String host = null;
		Long startMillis = System.currentTimeMillis();
		Long millis = null;

		String returnString = "";
		URL url;
		try {
			url = new URL(urlString);
			host = url.getHost();
			InputStream in = null;

			try {
				URLConnection uc = url.openConnection();
				uc.setDefaultUseCaches(false);
				uc.setUseCaches(false);
				uc.setRequestProperty("Cache-Control", "max-age=0,no-cache");
				uc.setRequestProperty("Pragma", "no-cache");

				in = uc.getInputStream();

				byte[] buffer = new byte[4096];
				int bytes_read;
				String s = null;
				while ((bytes_read = in.read(buffer, 0, 4096)) != -1)
					s = new String(buffer);
				returnString += s;
			} catch (Exception e) {
				logger_.error(e.getMessage());
			} finally { // Always close the streams, no matter what.
				try {
					in.close();
				} catch (Exception e) {
					logger_.warn(" closing response stream " + e.getMessage());
				}
			}
		} catch (MalformedURLException e) {
			logger_.error("malformed url" + e.getMessage());
		}
		Long endNS = System.currentTimeMillis();
		millis = new Long(endNS.longValue() - startMillis.longValue());
		// logMillis( millis, host, urlString ) ;
		logger_.info("Outside doGet(String) of Client class");
		return returnString;
	}

	private static void logMillis(Long millis, String host, String urlString) {

		try {
			logger_.info("GET - " + urlString + " took " + millis.toString()
					+ " millisecond(s)\n");
			numServed_.put(host, numServed_.get(host) + 1L);
			if (millis > maxMillis_.get(host))
				maxMillis_.put(host, millis);
			totalMillis_.put(host, totalMillis_.get(host) + millis);
			lastMillis_.put(host, millis);
			overallNumServed_ += 1L;
			overallTotalMillis_ += millis;
			if (millis > overallMaxMillis_)
				overallMaxMillis_ = millis;
		} catch (Exception e) {
			logger_.info("logMillis - " + e.getMessage());
		}

	}

	/*
	 * public static HttpResponse httpGetResponse( String urlString ) {
	 * HttpResponse response = null ; HttpParams params = new BasicHttpParams();
	 * HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	 * HttpProtocolParams.setContentCharset(params, "UTF-8");
	 * HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
	 * HttpProtocolParams.setUseExpectContinue(params, true);
	 * 
	 * BasicHttpProcessor httpproc = new BasicHttpProcessor(); // Required
	 * protocol interceptors httpproc.addInterceptor(new RequestContent());
	 * httpproc.addInterceptor(new RequestTargetHost()); // Recommended protocol
	 * interceptors httpproc.addInterceptor(new RequestConnControl());
	 * httpproc.addInterceptor(new RequestUserAgent());
	 * httpproc.addInterceptor(new RequestExpectContinue());
	 * 
	 * HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
	 * 
	 * URL url = null ; try { url = new URL( urlString ); } catch
	 * (MalformedURLException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } HttpContext context = new BasicHttpContext(null);
	 * HttpHost host = new HttpHost(url.getHost(), 80);
	 * 
	 * DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
	 * ConnectionReuseStrategy connStrategy = new
	 * DefaultConnectionReuseStrategy();
	 * 
	 * context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
	 * context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
	 * 
	 * try { if (!conn.isOpen()) { Socket socket = new
	 * Socket(host.getHostName(), host.getPort()); conn.bind(socket, params); }
	 * 
	 * BasicHttpRequest request = new BasicHttpRequest("GET", url.getQuery());
	 * logger_.info(">> Request URI: " + request.getRequestLine().getUri());
	 * 
	 * request.setParams(params); httpexecutor.preProcess(request, httpproc,
	 * context); response = httpexecutor.execute(request, conn, context);
	 * response.setParams(params); httpexecutor.postProcess(response, httpproc,
	 * context);
	 * 
	 * logger_.info("<< Response: " + response.getStatusLine());
	 * logger_.info(EntityUtils.toString(response.getEntity()));
	 * logger_.info("==============");
	 * 
	 * 
	 * if (!connStrategy.keepAlive(response, context)) { conn.close(); } else {
	 * logger_.info("Connection kept alive..."); } } catch (UnknownHostException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } catch (HttpException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { try { conn.close(); } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } } return
	 * response ; }
	 */

	public void webFile(String urlString)
			throws java.net.MalformedURLException, java.io.IOException {
		// Open a URL connection.
		logger_.info("webFile()");
		String page = null;
		Long startMillis = System.currentTimeMillis();
		Long millis = null;

		final java.net.URL url = new java.net.URL(urlString);
		final String host = url.getHost();
		logger_.info("Host is " + host);
		final java.net.URLConnection uconn = url.openConnection();
		if (!(uconn instanceof java.net.HttpURLConnection))
			throw new java.lang.IllegalArgumentException(
					"URL protocol must be HTTP.");
		final java.net.HttpURLConnection conn = (java.net.HttpURLConnection) uconn;

		// Set up a request.
		conn.setConnectTimeout(10000); // 10 sec
		conn.setReadTimeout(10000); // 10 sec
		conn.setInstanceFollowRedirects(true);
		conn.setRequestProperty("User-agent", "spider");

		// Send the request.
		conn.connect();

		// Get the response.
		responseHeader = conn.getHeaderFields();
		responseCode = conn.getResponseCode();
		responseURL = conn.getURL();
		final int length = conn.getContentLength();
		final String type = conn.getContentType();
		if (type != null) {
			final String[] parts = type.split(";");
			MIMEtype = parts[0].trim();
			for (int i = 1; i < parts.length && charset == null; i++) {
				final String t = parts[i].trim();
				final int index = t.toLowerCase().indexOf("charset=");
				if (index != -1)
					charset = t.substring(index + 8);
			}
		}

		// Get the content.
		final java.io.InputStream stream = conn.getErrorStream();
		if (stream != null)
			content = readStream(length, stream);
		else if ((content = conn.getContent()) != null
				&& content instanceof java.io.InputStream)
			content = readStream(length, (java.io.InputStream) content);
		conn.disconnect();
		Long endNS = System.currentTimeMillis();
		millis = new Long(endNS.longValue() - startMillis.longValue());

		logMillis(millis, host, urlString);
	}

	/** Read stream bytes and transcode. */
	private Object readStream(int length, java.io.InputStream stream)
			throws java.io.IOException {
		final int buflen = Math.max(1024, Math.max(length, stream.available()));
		byte[] buf = new byte[buflen];
		;
		byte[] bytes = null;

		for (int nRead = stream.read(buf); nRead != -1; nRead = stream
				.read(buf)) {
			if (bytes == null) {
				bytes = buf;
				buf = new byte[buflen];
				continue;
			}
			final byte[] newBytes = new byte[bytes.length + nRead];
			System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
			System.arraycopy(buf, 0, newBytes, bytes.length, nRead);
			bytes = newBytes;
		}

		if (charset == null)
			return bytes;
		try {
			return new String(bytes, charset);
		} catch (java.io.UnsupportedEncodingException e) {
		}
		return bytes;
	}

	/** Get the content. */
	public Object getContent() {
		return content;
	}

	/** Get the response code. */
	public int getResponseCode() {
		return responseCode;
	}

	/** Get the response header. */
	public java.util.Map<String, java.util.List<String>> getHeaderFields() {
		return responseHeader;
	}

	/** Get the URL of the received page. */
	public java.net.URL getURL() {
		return responseURL;
	}

	/** Get the MIME type. */
	public String getMIMEType() {
		return MIMEtype;
	}

	public String getMeta(String tag) {
		String value = null;
		String resp = ((String) content).toLowerCase();
		logger_.info("Retrieving meta tag " + tag + " from webpage ");
		int loc = resp.indexOf("\"" + tag + "\"");
		if (loc > 0) {
			int con = resp.indexOf("content", loc);
			if (con > 0) {
				int start = resp.indexOf("\"", con);
				if (start > 0) {
					int end = resp.indexOf("\"", start + 1);
					if (end > 0) {
						value = ((String) content).substring(start + 1, end);
						logger_.info("Found " + tag + " in web page >" + value
								+ "<  loc:" + loc + "   con:" + con
								+ "   start:" + start + "   end:" + end);
					}
				}
			}
		} else
			logger_.warn("Did NOT find " + tag + " in web page ");
		return value;
	}

	public String getHref(String tag) {
		String value = null;
		String resp = ((String) content).toLowerCase();
		logger_.info("Retrieving href tag " + tag + " from webpage ");
		int loc = resp.indexOf("<" + tag);
		logger_.info("Found " + tag + " at " + loc);
		if (loc > 0) {
			int con = resp.indexOf("href", loc);
			logger_.info("Found href for " + tag + " at " + con);
			if (con > 0) {
				int start = resp.indexOf("\"", con);
				logger_
						.info("Value of href for " + tag + " starts at "
								+ start);

				if (start > 0) {
					int end = resp.indexOf("\"", start + 1);
					logger_
							.info("Value of href for " + tag + " ends at "
									+ end);
					if (end > 0) {
						value = ((String) content).substring(start + 1, end);
						logger_.info("Found " + tag + " in web page >" + value
								+ "<  loc:" + loc + "   con:" + con
								+ "   start:" + start + "   end:" + end);
					}
				}
			}
		} else
			logger_.warn("Did NOT find " + tag + " in web page ");
		return value;
	}

	public static String getOpStats() {
		String mess = "";
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
		return mess;
	}

}
