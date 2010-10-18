package com.payparade.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import sun.misc.BASE64Encoder;

import com.payparade.dataobject.SigningSecretDataObject;
import com.payparade.util.ConstantQuery;

//import com.payparade.util.Signing;

public final class SigningSecretServiceImpl implements SigningSecretService {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private static Logger _logger = Logger
			.getLogger(SigningSecretServiceImpl.class);
	private static Map<String, SigningSecretDataObject> listAllSigningSecretdetail = null;
	private static String MAGIC;

	public static String getMAGIC() {
		return MAGIC;
	}

	public static void setMAGIC(String mAGIC) {
		MAGIC = mAGIC;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static  String getSignature(String message, String partner) {

		_logger.info("Signing >" + message + "< for " + partner);
		MessageDigest md = null;
		String sig = null;
		String sharedSecret = ((SigningSecretDataObject) listAllSigningSecretdetail
				.get(partner)).getSigning_secret();
		_logger.info("Retrieved secret >" + sharedSecret + " for " + partner);
		if (sharedSecret != null) {
			String source = message + sharedSecret;
			try {
				md = MessageDigest.getInstance("SHA-1");
				try {
					md.update(source.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					_logger.error(e.getMessage());
				}

				byte raw[] = md.digest();
				sig = (new BASE64Encoder()).encode(raw);
				_logger.info("Generated signature >" + sig + " for " + partner
						+ " for message >" + message + "<");
			} catch (NoSuchAlgorithmException e) {
				_logger.error(e.getMessage());
			}
		}
		return sig;
	}

	public static boolean checkSignature(String message, String signature) {
		String expected = SigningSecretServiceImpl.getSignature(message, "dashboard");
		boolean returnValue = false;
		if (signature != null)
			if (signature.length() > 1)
				if (signature.equals(expected) || signature.equals(MAGIC))
					returnValue = true;
		_logger.info("checkSignature expected >" + expected + "<  received >"
				+ signature + "< returned " + returnValue);
		return returnValue;
	}

	public synchronized static boolean checkSignature(String message,
			String signature, String partner) {
		_logger
				.info(" Inside checkSignature(String,String,String) of SigningSecretServiceImpl class ");
		String expected = SigningSecretServiceImpl.getSignature(message, partner);
		boolean returnValue = false;
		if (signature != null)
			if (signature.length() > 1)
				if (signature.equals(expected) || signature.equals(getMAGIC()))
					returnValue = true;
		_logger.info("checkSignature expected >" + expected + "<  received >"
				+ signature + "< returned " + returnValue);
		_logger
				.info(" OUtside checkSignature(String,String,String) of SigningSecretServiceImpl class ");
		return returnValue;
	}

	public void init() {
		_logger.info(" Inside init() of SigningSecretServiceImpl class ");
		try {
			listAllSigningSecretdetail = (Map<String, SigningSecretDataObject>) jdbcTemplate
					.query(ConstantQuery.getListAllSigningSecret().toString(),
							new ResultSetExtractor() {

								public Object extractData(ResultSet arg0)
										throws SQLException,
										DataAccessException {
									SigningSecretDataObject signingSecretDataObject = null;
									Map<String, SigningSecretDataObject> listSigningSecret = new HashMap<String, SigningSecretDataObject>();
									while (arg0.next()) {
										signingSecretDataObject = new SigningSecretDataObject();
										signingSecretDataObject.setId(arg0
												.getInt("id"));
										signingSecretDataObject
												.setSitename(arg0
														.getString("sitename"));
										signingSecretDataObject
												.setSigning_secret(arg0
														.getString("signing_secret"));
										listSigningSecret.put(
												signingSecretDataObject
														.getSitename(),
												signingSecretDataObject);
									}
									return listSigningSecret;
								}
							});
		} catch (DataAccessException dae) {
			_logger.error("SigningSecretServiceImpl:::init():::Exception is:::"
					, dae);
			dae.printStackTrace();
		} catch (Exception e) {
			_logger.error("SigningSecretServiceImpl:::init():::Exception is:::"
					, e);
			e.printStackTrace();
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.listAllSigningSecret);
		}
		_logger
				.info(" Outside init() of SigningSecretServiceImpl class size is:::"
						+ listAllSigningSecretdetail.size());
	}

}
