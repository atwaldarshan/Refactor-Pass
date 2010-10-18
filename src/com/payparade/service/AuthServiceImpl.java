package com.payparade.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPMemberNetworkDataObject;
import com.payparade.dataobject.PPMemberShipDataObject;
import com.payparade.dataobject.PPUserDataObject;
import com.payparade.util.ConstantQuery;
import com.payparade.util.Logger;

public class AuthServiceImpl implements AuthService {
	Logger logger = Logger.getLogger("AuthServiceImpl");
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insertintopp_member_network(
			PPMemberNetworkDataObject ppMemberNetworkDataObject) {
		logger
				.info(" Inside insertintopp_member_network(PPMemberNetworkDataObject) of AuthServiceImpl ");
		try {
			jdbcTemplate.update(ConstantQuery.getInsertPpmembernetwork(),
					new Object[] {
							ppMemberNetworkDataObject.getNetwork_code_id(),
							ppMemberNetworkDataObject
									.getMember_social_network_id() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"AuthServiceImpl::insertintopp_member_network::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"AuthServiceImpl::insertintopp_member_network::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_ppmembernetwork);
		}
		logger
				.info(" Outside insertintopp_member_network(PPMemberNetworkDataObject) of AuthServiceImpl ");

	}

	public int getNetworkId(String networkcode) {
		logger
				.info("Inside int getNetworkId(String networkcode) of AuthServiceImpl class");
		int networkcodeid = 0;
		try {
			networkcodeid = jdbcTemplate.queryForInt(ConstantQuery
					.getSelectIdPpnetwork().toString(),
					new Object[] { networkcode });
		} catch (DataAccessException dae) {
			logger.error("AuthServiceImpl::getNetworkId::Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error("AuthServiceImpl::getNetworkId::Exception is::",
					 e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.select_id_ppnetwork);
		}
		logger
				.info("Outside int getNetworkId(String networkcode) of AuthServiceImpl class value of networkcodeid is::"
						+ networkcodeid);
		return networkcodeid;
	}

	public List<PPConnectionDataObject> setSelectfrom_pp_connection(
			int networkcodeid, String member_social_network_id) {
		logger
				.info(" Inside setSelect_distinct_social_id_friend_id_from_pp_connection(int,String) of AuthServiceImpl class ");
		List<PPConnectionDataObject> list = null;
		try {
			list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_pp_connectionv1().toString(), new Object[] {
					networkcodeid, member_social_network_id },
					new ResultSetExtractor() {
						List<PPConnectionDataObject> list = new ArrayList();

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							PPConnectionDataObject ppConnectionDataObject = null;
							while (rs.next()) {
								ppConnectionDataObject = new PPConnectionDataObject();
								ppConnectionDataObject.setId(rs.getLong("id"));
								list.add(ppConnectionDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							" AuthSeriveImpl:setSelectfrom_pp_connection::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" AuthSeriveImpl:setSelectfrom_pp_connection::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_connectionv1);
		}
		logger
				.info(" Outside setSelect_distinct_social_id_friend_id_from_pp_connection(int,int,PrintWriter) of AuthServiceImpl class ");
		return list;
	}

	public List<PPUserDataObject> PPUserDataObjectList(String username,
			String password) {
		logger
				.info(" Inside List= PPUserDataObjectList(String,String) of AuthServiceImpl class");
		List<PPUserDataObject> ppUserDataObjectsList = null;
		try {
			logger.info("User Name Is::" + username);
			logger.info("PassWord is::" + password);
			ppUserDataObjectsList = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_ppuser().toString(), new Object[] {
					username, password }, new ResultSetExtractor() {
				List<PPUserDataObject> list = new ArrayList();

				public Object extractData(ResultSet rs) throws SQLException,
						DataAccessException {
					PPUserDataObject ppUserDataObject = null;
					while (rs.next()) {
						ppUserDataObject = new PPUserDataObject();
						ppUserDataObject.setUseremail(rs
								.getString("user_email"));
						list.add(ppUserDataObject);
					}
					return list;
				}
			});
		} catch (DataAccessException dae) {
			logger
					.error(
							" AuthSeriveImpl:PPUserDataObjectList::Exception is::",
							dae);
		} catch (Exception e) {
			logger.error(
					" AuthSeriveImpl:PPUserDataObjectList::Exception is::", e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.select_from_ppuser);
		}
		logger
				.info(" Outside List= PPUserDataObjectList(String,String) of AuthServiceImpl class");
		return ppUserDataObjectsList;
	}

	public List<PPMemberNetworkDataObject> Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid(
			int networkcodeid, String membersocialnetworkid) {
		logger
				.info(" Inside List = Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid() of AuthServiceImpl ");
		List<PPMemberNetworkDataObject> ppMemberNetworkDataObjectList = null;
		try {
			logger.info("Network code id::" + networkcodeid);
			logger.info("Membersocialnetworkid::" + membersocialnetworkid);
			ppMemberNetworkDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery
									.setSelect_from_pp_member_network_where_networkcodeid_membersocialnetworkid()
									.toString(), new Object[] { networkcodeid,
									membersocialnetworkid },
							new ResultSetExtractor() {
								List<PPMemberNetworkDataObject> list = null;

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									PPMemberNetworkDataObject ppMemberNetworkDataObject = null;

									list = new ArrayList<PPMemberNetworkDataObject>();

									while (rs.next()) {
										ppMemberNetworkDataObject = new PPMemberNetworkDataObject();
										ppMemberNetworkDataObject
												.setMemeber_id(rs
														.getInt("member_id"));
										ppMemberNetworkDataObject
												.setNetwork_code_id(rs
														.getInt("network_code_id"));
										ppMemberNetworkDataObject
												.setIs_connected(rs
														.getShort("is_connected"));
										ppMemberNetworkDataObject
												.setMember_social_network_id(rs
														.getString("member_social_network_id"));
										list.add(ppMemberNetworkDataObject);
									}
									return list;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error(
							" AuthSeriveImpl:Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" AuthSeriveImpl:Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_member_network_where_networkcodeid_membersocialnetworkid);
		}
		logger
				.info(" Outside List = Select_from_pp_member_network_where_networkcodeid_membersocialnetworkid() of AuthServiceImpl ");
		return ppMemberNetworkDataObjectList;
	}

	public List<PPMemberShipDataObject> Select_from_ppmembership_where_partnerdomainid_memberid(
			int partnerdomainid, int memberid) {
		logger
				.info(" Inside List=Select_from_ppmembership_where_partnerdomainid_memberid() of AuthServiceImpl class ");
		List<PPMemberShipDataObject> ppMemberShipDataObjectList = null;
		try {
			ppMemberShipDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery
									.setSelect_from_ppmembership_where_partnerdomainid_memberid()
									.toString(), new Object[] {
									partnerdomainid, memberid },
							new ResultSetExtractor() {
								List<PPMemberShipDataObject> list = new ArrayList();

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									PPMemberShipDataObject ppMemberShipDataObject = null;
									while (rs.next()) {
										ppMemberShipDataObject = new PPMemberShipDataObject();
										ppMemberShipDataObject.setMemeber_id(rs
												.getInt("member_id"));
										ppMemberShipDataObject
												.setPartner_domain_id(rs
														.getInt("partner_domain_id"));
										ppMemberShipDataObject.setId(rs
												.getInt("id"));
										ppMemberShipDataObject
												.setPartners_social_key(rs
														.getString("partners_social_key"));
										list.add(ppMemberShipDataObject);
									}
									return list;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error(
							" AuthSeriveImpl:Select_from_ppmembership_where_partnerdomainid_memberid::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" AuthSeriveImpl:Select_from_ppmembership_where_partnerdomainid_memberid::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_ppmembership_where_partnerdomainid_memberid);
		}
		logger
				.info(" Outside List=Select_from_ppmembership_where_partnerdomainid_memberid() of AuthServiceImpl class ");
		return ppMemberShipDataObjectList;

	}

	public short getPartnerDomainId(String partnerdomainurl) {
		logger
				.info(" Inside short = getPartnerDomainId(String) of AuthServiceImpl class partnerdomainurl::"
						+ partnerdomainurl);
		short partnerdomainid = 0;
		try {
			partnerdomainid = (short) jdbcTemplate.queryForInt(ConstantQuery
					.getSectidfrompppdomain().toString(),
					new Object[] { partnerdomainurl.trim() });
		} catch (DataAccessException dae) {
			logger.info(" PassSeriveImpl:getPartnerDomainId::"
					+ dae.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside short = getPartnerDomainId(String) of PassServiceImpl class partnerdomainid::"
						+ partnerdomainid);
		return partnerdomainid;
	}

	public void update_ppmembership(
			PPMemberShipDataObject ppMemberShipDataObject) {
		logger
				.info("Inside update_ppmembership(PPMemberShipDataObject) of AuthServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setUpdate_pp_membership()
					.toString(), new Object[] {
					ppMemberShipDataObject.getPartners_social_key(),
					ppMemberShipDataObject.getPartner_domain_id() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::update_ppmembership::Exception is::",
							dae);
		} catch (Exception e) {
			logger.error(
					"PassServiceImpl::update_ppmembership::Exception is::", e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.update_pp_membership);
		}

		logger
				.info("Outside update_ppmembership(PPMemberShipDataObject) of AuthServiceImpl class");
	}

	public void insertinto_ppmembership(
			PPMemberShipDataObject ppMemberShipDataObject) {
		logger
				.info("Inside insertinto_ppmembership(PPMemberShipDataObject) of AuthServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setInsert_into_pp_membership()
					.toString(), new Object[] {
					ppMemberShipDataObject.getPartner_domain_id(),
					ppMemberShipDataObject.getMemeber_id(),
					ppMemberShipDataObject.getPartners_social_key() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::update_ppmembership::Exception is::",
							dae);
		} catch (Exception e) {
			logger.error(
					"PassServiceImpl::update_ppmembership::Exception is::", e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_into_pp_membership);
		}

		logger
				.info("Outside insertinto_ppmembership(PPMemberShipDataObject) of AuthServiceImpl class");
	}

	public void insertinto_pp_member(int partnerdomainid) {
		logger
				.info(" Insert into insertinto_pp_member(int) of AuthServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery.setInsertinto_ppmember()
					.toString(), new Object[] { partnerdomainid });
		} catch (DataAccessException dae) {
			logger.error(
					"PassServiceImpl::insertinto_pp_member::Exception is::",
					dae);
		} catch (Exception e) {
			logger.error(
					"PassServiceImpl::insertinto_pp_member::Exception is::", e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.insertinto_ppmember);
		}

		logger
				.info(" Outside into insertinto_pp_member(int) of AuthServiceImpl class ");

	}

	public void insertinto_ppmembershipv1(
			PPMemberShipDataObject ppMemberShipDataObject) {
		logger
				.info("Inside insertinto_ppmembershipv1(PPMemberShipDataObject) of AuthServiceImpl class");
		try {
			logger.info("Partner Domain Id::"
					+ ppMemberShipDataObject.getPartner_domain_id());
			logger.info("Partner Social Key::"
					+ ppMemberShipDataObject.getPartners_social_key());
			jdbcTemplate.update(ConstantQuery.setInsert_into_pp_membershipv1()
					.toString(), new Object[] {
					ppMemberShipDataObject.getPartner_domain_id(),
					ppMemberShipDataObject.getPartners_social_key() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::insertinto_ppmembershipv1::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::insertinto_ppmembershipv1::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_into_pp_membershipv1);
		}

		logger
				.info("Outside insertinto_ppmembershipv1(PPMemberShipDataObject) of AuthServiceImpl class");
	}

	public void insertinto_ppmembernetwork(
			PPMemberNetworkDataObject ppMemberNetworkDataObject) {
		logger
				.info("Inside insertinto_ppmembernetwork(PPMemberNetworkDataObject) of AuthServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setInsertinto_ppmembernetwork()
					.toString(), new Object[] {
					ppMemberNetworkDataObject.getNetwork_code_id(),
					ppMemberNetworkDataObject.getMember_social_network_id() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"PassServiceImpl::insertinto_ppmembershipv1::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::insertinto_ppmembershipv1::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insertinto_ppmembernetwork);
		}

		logger
				.info("Outside insertinto_ppmembernetwork(PPMemberNetworkDataObject) of AuthServiceImpl class");
	}

	public void update_ppmembershipv1(
			PPMemberShipDataObject ppMemberShipDataObject) {
		logger
				.info("Inside update_ppmembershipv1(PPMemberShipDataObject) of AuthServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setUpdate_pp_membershipv1()
					.toString(), new Object[] { ppMemberShipDataObject
					.getPartner_domain_id() });
		} catch (DataAccessException dae) {
			logger.error(
					"PassServiceImpl::update_ppmembershipv1::Exception is::",
					dae);
		} catch (Exception e) {
			logger
					.error(
							"PassServiceImpl::update_ppmembershipv1::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_membershipv1);
		}

		logger
				.info("Outside update_ppmembershipv1(PPMemberShipDataObject) of AuthServiceImpl class");
	}

}
