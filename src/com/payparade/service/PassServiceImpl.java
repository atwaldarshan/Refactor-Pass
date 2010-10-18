package com.payparade.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.payparade.dataobject.PPIdDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPMemberSessionDataObject;
import com.payparade.dataobject.PPPartnerOptionDataObject;
import com.payparade.dataobject.PPPartnerServerDataObject;
import com.payparade.util.ConstantQuery;

public class PassServiceImpl implements PassService {
	Logger logger = Logger.getLogger(PassServiceImpl.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insertPPMemberActivity(
			PPMemberActivityDataObject ppMemberActivityDataObject) {
		logger
				.info(" Inside insertPPMemberActivity() of PassServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery.insert_pp_member_activity,
					new Object[] {
							ppMemberActivityDataObject
									.getMemberSocialNetworkId(),
							ppMemberActivityDataObject.getMemberSessionId(),
							ppMemberActivityDataObject.getNetworkCodeId(),
							ppMemberActivityDataObject.getActivityCodeId(),
							ppMemberActivityDataObject.getPageUrl(),
							ppMemberActivityDataObject.getTargetUrl(),
							ppMemberActivityDataObject.getPartnerDomainId(),
							ppMemberActivityDataObject.getIdx(),
							ppMemberActivityDataObject.getPp_id() });
		} catch (DataAccessException sql) {
			logger.error("Exception is:: ", sql);
		}catch(Exception e){
			logger.error("Exception is:: ", e);
		}
		logger
				.info(" Outside insertPPMemberActivity() of PassServiceImpl class ");
	}

	public long insertPPId(final PPIdDataObject ppIdDataObject) {
		logger
				.info(" Inside insertPPId(PPIdDataObject) of PassServiceImpl class ");
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(
					Connection connection) {
				PreparedStatement ps = null;
				try {
					ps = connection.prepareStatement(
							ConstantQuery.insert_pp_id,
							new String[] { "pp_id" });
					logger.info("Session Id is:::"
							+ ppIdDataObject.getSessionId());
					logger.info("Browser Id is:::"
							+ ppIdDataObject.getBrowserId());
					logger.info("Reference Url is:::"
							+ ppIdDataObject.getReferenceUrl());
					logger.info("User IP is:::" + ppIdDataObject.getUserIp());
					ps.setString(1, ppIdDataObject.getSessionId());
					ps.setString(2, ppIdDataObject.getBrowserId());
					ps.setString(3, ppIdDataObject.getReferenceUrl());
					ps.setString(4, ppIdDataObject.getUserIp());
				} catch (SQLException sql) {
					logger.error(" InsertPPId exception::", sql);
					sql.printStackTrace();
				}catch(Exception e){
					logger.error("Exception is:: ", e);
				}
				return ps;
			}
		}, keyHolder);
		logger
				.info(" Outside insertPPId(PPIdDataObject) of PassServiceImpl class ");

		return keyHolder.getKey().longValue();
	}

	// Get Network Id Facebook,Twitter,myspace
	public short getNetworkIdUsingNetworkcodeid(final String networkurl) {

		logger
				.info(" Inside getNetworkIdUsingNetworkcodeid(String) of PassServiceImpl class");
		short networkid = 0;
		try {
			logger.info("Network Url is::" + networkurl);
			networkid = (short) jdbcTemplate.queryForInt(ConstantQuery
					.getSelectidfrompppnetwork_code().toString(),
					new Object[] { networkurl.trim().toUpperCase() });
		}catch(EmptyResultDataAccessException erd){
				return 0;
		} catch (DataAccessException dae) {
			logger.error(" PassServiceImpl:getNetworkIdUsingNetworkcodeid:::",
					dae);
		}catch(Exception e){
			logger.error("Exception is:: ", e);
		} 
		finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppnetwork_code);
		}
		logger
				.info(" Outside getNetworkIdUsingNetworkcodeid(String) of PassServiceImpl class networkid::"
						+ networkid);
		return networkid;

	}

	public short getNetworkIdUsingURL(final String networkurl) {

		logger
				.info(" Inside getNetworkIdUsingURL(String) of PassServiceImpl class");
		short networkid = 0;
		try {
			logger.info("Network Url is::" + networkurl);
			networkid = (short) jdbcTemplate.queryForInt(ConstantQuery
					.getSelectidfrompppnetwork_url().toString(),
					new Object[] { networkurl.trim() });
		} 
		catch(EmptyResultDataAccessException erd){
			return 0;
		}catch (DataAccessException dae) {
			logger.error(" PassServiceImpl:getNetworkIdUsingURL:::", dae);
		}
		catch(Exception e){
			logger.error("Exception is:: ", e);
		} 
		finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppnetwork_url);
		}
		logger
				.info(" Outside getNetworkIdUsingURL(String) of PassServiceImpl class networkid::"
						+ networkid);
		return networkid;

	}

	public short getPartnerDomainId(final String partnerdomainurl) {
		logger
				.info(" Inside short = getPartnerDomainId(String) of PassServiceImpl class partnerdomainurl::"
						+ partnerdomainurl);
		short partnerdomainid = 0;
		try {
			partnerdomainid = (short) jdbcTemplate.queryForInt(ConstantQuery
					.getSectidfrompppdomain().toString(),
					new Object[] { partnerdomainurl.trim() });
		}catch(EmptyResultDataAccessException erd){
			return 0;
		} 
		catch (DataAccessException dae) {
			logger.error(" PassSeriveImpl:getPartnerDomainId::"
					+ dae.getMessage());
		} catch (Exception e) {
			logger.error("",e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside short = getPartnerDomainId(String) of PassServiceImpl class partnerdomainid::"
						+ partnerdomainid);
		return partnerdomainid;
	}

	public Map<Integer, PPPartnerOptionDataObject> select_pp_partner_option() {
		logger
				.info(" Inside select_pp_partner_option() of PassServiceImpl class ");
		Map<Integer, PPPartnerOptionDataObject> listPPPartnerOptionDataObject = null;
		try {
			listPPPartnerOptionDataObject = (Map) jdbcTemplate.query(
					ConstantQuery.getSelect_pp_partner_option().toString(),
					new ResultSetExtractor() {
						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							PPPartnerOptionDataObject partnerOptionDataObject = null;
							Map<Integer, PPPartnerOptionDataObject> map = new HashMap<Integer, PPPartnerOptionDataObject>();
							while (arg0.next()) {
								partnerOptionDataObject = new PPPartnerOptionDataObject();
								partnerOptionDataObject
										.setId(arg0.getInt("id"));
								partnerOptionDataObject.setBadge_message(arg0
										.getString("badge_message"));
								partnerOptionDataObject
										.setBadge_salutation(arg0
												.getString("badge_salutation"));
								partnerOptionDataObject.setConnect_style(arg0
										.getString("connect_style"));
								partnerOptionDataObject.setConnect_text(arg0
										.getString("connect_text"));
								partnerOptionDataObject.setCss_file(arg0
										.getString("css_file"));
								partnerOptionDataObject.setDisplay_name(arg0
										.getString("display_name"));
								partnerOptionDataObject.setFirst_name_only(arg0
										.getShort("first_name_only"));
								partnerOptionDataObject.setLink_domain(arg0
										.getString("link_domain"));
								partnerOptionDataObject.setLogo_file(arg0
										.getString("logo_file"));
								partnerOptionDataObject.setMaster_admin(arg0
										.getString("master_admin"));
								partnerOptionDataObject.setMessage_style(arg0
										.getString("message_style"));
								partnerOptionDataObject
										.setPartner_domain_id(arg0
												.getInt("partner_domain_id"));
								partnerOptionDataObject
										.setSalutation_style(arg0
												.getString("salutation_style"));
								map.put(partnerOptionDataObject.getId(),
										partnerOptionDataObject);

							}
							return map;
						}
					});

		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::select_pp_partner_option::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::select_pp_partner_option::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_pp_partner_option);
		}
		logger
				.info(" Outside select_pp_partner_option() of PassServiceImpl class ");
		return listPPPartnerOptionDataObject;
	}

	public String Select_payparade_server_from_pp_partner_server(
			String partnercode) {
		logger
				.info("Inside String=Select_payparade_server_from_pp_partner_server(String) of PassServiceImpl class  ");
		String pp_partner_server = "";
		try {
			pp_partner_server = (String) jdbcTemplate.query(ConstantQuery
					.setSelect_payparade_server_from_pp_partner_server()
					.toString(), new Object[] { partnercode },
					new ResultSetExtractor() {

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							if (arg0.next())
								return arg0.getString("payparade_server");
							return null;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::Select_payparade_server_from_pp_partner_server::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::Select_payparade_server_from_pp_partner_server::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_payparade_server_from_pp_partner_server);
		}
		logger
				.info("Outside String=Select_payparade_server_from_pp_partner_server(String) of PassServiceImpl class  ");
		return pp_partner_server;
	}

	public Map<String, PPPartnerServerDataObject> select_from_pp_partner_servers() {
		logger
				.info(" Inside Map<Integer,PPPartnerServerDataObject>=select_from_pp_partner_servers() of PassServiceImpl class ");
		Map<String, PPPartnerServerDataObject> listPPPartnerServerDataObject = null;
		try {
			listPPPartnerServerDataObject = (Map) jdbcTemplate.query(
					ConstantQuery.setSelect_from_pp_partner_servers()
							.toString(), new ResultSetExtractor() {
						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							PPPartnerServerDataObject partnerServerDataObject = null;
							Map<String, PPPartnerServerDataObject> map = new HashMap<String, PPPartnerServerDataObject>();
							while (arg0.next()) {
								partnerServerDataObject = new PPPartnerServerDataObject();
								partnerServerDataObject
										.setId(arg0.getInt("id"));
								partnerServerDataObject.setPartner_host(arg0
										.getString("partner_host"));
								partnerServerDataObject.setPartner_server(arg0
										.getString("payparade_server"));
								map.put(partnerServerDataObject
										.getPartner_host(),
										partnerServerDataObject);

							}
							return map;
						}
					});

		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::select_from_pp_partner_servers::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::select_from_pp_partner_servers::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_servers);
		}
		logger
				.info(" Outside Map<Integer,PPPartnerServerDataObject>=select_from_pp_partner_servers() of PassServiceImpl class ");
		return listPPPartnerServerDataObject;
	}

	public void insert_into_pp_memeber_session(
			PPMemberSessionDataObject ppMemberSessionDataObject) {
		logger
				.info("Inside insert_into_pp_member_session(PPMemberSessionDataObject) of PassServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setInsertintopp_member_session()
					.toString(), new Object[] {
					ppMemberSessionDataObject.getMember_social_id(),
					ppMemberSessionDataObject.getMemeber_session_id(),
					ppMemberSessionDataObject.getBrowser_id(),
					ppMemberSessionDataObject.getNetwork_code_id(),
					ppMemberSessionDataObject.getReference_url(),
					ppMemberSessionDataObject.getPp_id(),
					ppMemberSessionDataObject.getUser_ip() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl:::insert_into_pp_member_session:::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl:::insert_into_pp_member_session:::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insertintopp_member_session);
		}
		logger
				.info("Outside insert_into_pp_memeber_session(PPMemberSessionDataObject) of PassServiceImpl class");
	}

	public int select_id_from_pp_network(String networkcode) {
		logger
				.info(" Inside int=select_id_from_pp_network(String) from PassServiceImpl class ");
		logger.info(" Value of partnerdomain is:::" + networkcode);
		int networkcodeid = 0;
		try {
			networkcodeid = jdbcTemplate.queryForInt(ConstantQuery
					.setSelect_id_from_pp_network().toString(),
					new Object[] { networkcode });
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_network::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_network::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_id_from_pp_network);
		}
		logger
				.info(" Outside int=select_id_from_pp_partner_domain(String) from PassServiceImpl class ");
		return networkcodeid;
	}

}
