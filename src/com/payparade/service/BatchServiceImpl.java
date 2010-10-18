package com.payparade.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.payparade.dataobject.PPCustomerScoreDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.util.ConstantQuery;

public class BatchServiceImpl implements BatchService {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	private Logger logger = Logger.getLogger(BatchServiceImpl.class);

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public boolean delete_customer_rank() {
		logger
				.info(" Inside boolean=delete_customer_rank() of BatchServiceImpl class ");
		boolean isdelete = false;
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(ConstantQuery.delete_pp_customer_rank);
			int deleterowcounter = jdbcTemplate.update(sql.toString());
			if (deleterowcounter > 0) {
				isdelete = true;
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::delete_customer_rank()::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::delete_customer_rank()::Exception is::"
							, e);
		}
		logger
				.info(" Outside boolean=delete_customer_rank() of BatchServiceImpl class::isdelete::"
						+ isdelete);
		return isdelete;
	}

	public boolean delete_customer_score() {
		logger
				.info(" Inside boolean=delete_customer_score() of BatchServiceImpl class ");
		boolean isdelete = false;
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(ConstantQuery.delete_pp_customer_score);
			int deleterowcounter = jdbcTemplate.update(sql.toString());
			if (deleterowcounter > 0) {
				isdelete = true;
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::delete_customer_score()::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::delete_customer_score()::Exception is::"
							, e);
		}
		logger
				.info(" Outside boolean=delete_customer_score() of BatchServiceImpl class::isdelete::"
						+ isdelete);
		return isdelete;
	}

	public boolean delete_last_access() {
		logger
				.info(" Inside boolean=delete_last_access() of BatchServiceImpl class ");
		boolean isdelete = false;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append(ConstantQuery.delete_pp_last_access);
			int deleterowcounter = jdbcTemplate.update(sql.toString());
			if (deleterowcounter > 0) {
				isdelete = true;
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::delete_last_access()::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::delete_last_access()::Exception is::"
							, e);
		}
		logger
				.info(" Outside boolean=delete_last_access() of BatchServiceImpl class::isdelete::"
						+ isdelete);
		return isdelete;
	}

	public List<PPMemberActivityDataObject> getsocial_id_networkcodeid_maxppidfrom_pp_member_activity() {
		logger
				.info(" Inside List=getsocial_id_networkcodeid_maxppidfrom_pp_member_activity() of BatchServiceImpl class ");
		List ppmemberActivityDataObjectList = new ArrayList<PPMemberActivityDataObject>();
		try {
			ppmemberActivityDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery.select_social_id_networkcodeid_maxppidfrom_pp_member_activity,
							new ResultSetExtractor() {

								public Object extractData(ResultSet arg0)
										throws SQLException,
										DataAccessException {
									PPMemberActivityDataObject ppMemberActivityDataObject = null;
									List templist = new ArrayList<PPMemberActivityDataObject>();
									if (arg0.next()) {
										ppMemberActivityDataObject = new PPMemberActivityDataObject();
										ppMemberActivityDataObject
												.setMemberSocialNetworkId(arg0
														.getString("member_social_network_id"));
										ppMemberActivityDataObject
												.setNetworkCodeId(arg0
														.getInt("network_code_id"));
										ppMemberActivityDataObject
												.setGeneral_variable(arg0
														.getInt("max_id"));
										templist
												.add(ppMemberActivityDataObject);
									}
									return templist;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::getsocial_id_networkcodeid_maxppidfrom_pp_member_activity()::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::getsocial_id_networkcodeid_maxppidfrom_pp_member_activity()::Exception is::"
							, e);
		}
		logger
				.info(" Outside List=getsocial_id_networkcodeid_maxppidfrom_pp_member_activity() of BatchServiceImpl class ");
		return ppmemberActivityDataObjectList;
	}

	public void update_pp_id_of_pp_member_activity(
			List<PPMemberActivityDataObject> memberActivityDataObjectList) {
		logger
				.info(" Inside update_pp_id_of_pp_member_activity(List) of BatchServiceImpl class ");
		try {
			for (PPMemberActivityDataObject ppMemberActivityDataObject : memberActivityDataObjectList) {
				logger.info(" PPID is=== "
						+ ppMemberActivityDataObject.getGeneral_variable());// set
																			// PPID
																			// max
																			// value
				logger
						.info(" MemberSocialNetwork ID is=== "
								+ ppMemberActivityDataObject
										.getMemberSocialNetworkId());
				logger.info(" Network Code ID is=== "
						+ ppMemberActivityDataObject.getNetworkCodeId());
				jdbcTemplate
						.update(
								ConstantQuery.update_pp_id_of_pp_member_activity,
								new Object[] {
										ppMemberActivityDataObject
												.getGeneral_variable(),// set
																		// PPID
																		// max
																		// value
										ppMemberActivityDataObject
												.getMemberSocialNetworkId(),
										ppMemberActivityDataObject
												.getNetworkCodeId() });
				ppMemberActivityDataObject = null;
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::update_pp_id_of_pp_member_activity()::Exception is::"
							,dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::update_pp_id_of_pp_member_activity()::Exception is::"
							, e);
		}
		logger
				.info(" Outside update_pp_id_of_pp_member_activity(List) of BatchServiceImpl class ");
	}

	public void select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity(
			String fromdate) {
		logger
				.info(" Insidddde select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity() of BatchServiceImpl class ");

		List<PPCustomerScoreDataObject> customerScoreDataObjectList = null;
		try {
			customerScoreDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery.select_ppid_membersocialid_network_code_id_partnerdomainid_vts_qov_from_pp_member_activity,
							new Object[] { fromdate },
							// new Object[]{},
							new ResultSetExtractor() {
								List<PPCustomerScoreDataObject> tempppCustomerScoreDataObjectList = null;

								public Object extractData(ResultSet arg0)
										throws SQLException,
										DataAccessException {
									PPCustomerScoreDataObject pPCustomerScoreDataObject = null;
									if (arg0.next()) {
										arg0.previous();
										tempppCustomerScoreDataObjectList = new ArrayList<PPCustomerScoreDataObject>();
									}
									while (arg0.next()) {
										pPCustomerScoreDataObject = new PPCustomerScoreDataObject();
										pPCustomerScoreDataObject.setPp_id(arg0
												.getInt("pp_id"));
										pPCustomerScoreDataObject
												.setNetwork_member_social_id(arg0
														.getString("member_social_network_id"));
										pPCustomerScoreDataObject
												.setNetwork_code_id(arg0
														.getShort("network_code_id"));
										pPCustomerScoreDataObject.setQov(arg0
												.getInt("qov"));
										pPCustomerScoreDataObject.setVts(arg0
												.getInt("vts"));
										pPCustomerScoreDataObject
												.setPartner_domain_id(arg0
														.getInt("partner_domain_id"));
										tempppCustomerScoreDataObjectList
												.add(pPCustomerScoreDataObject);
									}
									return tempppCustomerScoreDataObjectList;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity()::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity()::"
							, e);
		}
		logger.info("customerScoreDataObjectList Size:::"
				+ customerScoreDataObjectList.size());

		insert_pp_customer_score(customerScoreDataObjectList);

		logger
				.info(" Outside select_ppid_membersocialid_networkcodeid_partnerdomainid_vts_qov_from_pp_member_activity() of BatchServiceImpl class ");
	}

	public void insert_pp_customer_score(
			List<PPCustomerScoreDataObject> memberActivityDataObjectList) {
		logger
				.info(" Inside insert_pp_customer_score(List<PPCustomerScoreDataObject>) of BatchServiceImpl class ");
		try {
			for (PPCustomerScoreDataObject ppCustomerScoreDataObject : memberActivityDataObjectList) {
				logger.info("ppCustomerScoreDataObject.getPp_id()::"
						+ ppCustomerScoreDataObject.getPp_id());
				logger.info("ppCustomerScoreDataObject.getNetwork_code_id()::"
						+ ppCustomerScoreDataObject.getNetwork_code_id());
				logger
						.info("ppCustomerScoreDataObject.getNetwork_member_social_id()::"
								+ ppCustomerScoreDataObject
										.getNetwork_member_social_id());
				logger
						.info("ppCustomerScoreDataObject.getPartner_domain_id()::"
								+ ppCustomerScoreDataObject
										.getPartner_domain_id());
				jdbcTemplate.update(ConstantQuery.Insert_pp_customer_score,
						new Object[] {
								ppCustomerScoreDataObject.getPp_id(),
								ppCustomerScoreDataObject.getNetwork_code_id(),
								ppCustomerScoreDataObject
										.getNetwork_member_social_id(),
								ppCustomerScoreDataObject
										.getPartner_domain_id(),
								ppCustomerScoreDataObject.getVts(),
								ppCustomerScoreDataObject.getQov() });
				ppCustomerScoreDataObject = null;
			}
		} catch (DataAccessException dae) {
			logger.error("BatchServiceImpl class::insert_pp_customer_score()::"
					,dae);
		} catch (Exception e) {
			logger.error("BatchServiceImpl class::insert_pp_customer_score()::"
					, e);
		}
		logger
				.info(" Outside insert_pp_customer_score(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
	}

	public void select_ppid_partnerdomainid_getcount_pp_member_activity(
			String fromdate) {
		logger
				.info(" Inside select_ppid_partnerdomainid_getcount_pp_member_activity() of BatchServiceImpl class ");
		List ppmemberActivityDataObjectList = new ArrayList<PPMemberActivityDataObject>();
		try {
			ppmemberActivityDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery.select_ppid_partnerdomainid_countvaluefrom_ppmember_activity,
							new Object[] { fromdate },
							new ResultSetExtractor() {
								public Object extractData(ResultSet arg0)
										throws SQLException,
										DataAccessException {
									PPMemberActivityDataObject ppMemberActivityDataObject = null;
									List templist = new ArrayList<PPMemberActivityDataObject>();
									if (arg0.next()) {
										ppMemberActivityDataObject = new PPMemberActivityDataObject();
										ppMemberActivityDataObject
												.setPp_id(arg0.getInt("pp_id"));
										ppMemberActivityDataObject
												.setPartnerDomainId(arg0
														.getInt("partner_domain_id"));
										ppMemberActivityDataObject
												.setGeneral_variable(arg0
														.getInt("sio"));
										templist
												.add(ppMemberActivityDataObject);
									}
									return templist;
								}
							});
			update_pp_customer_score(ppmemberActivityDataObjectList);
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl class::select_ppid_partnerdomainid_getcount_pp_member_activity()::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl class::select_ppid_partnerdomainid_getcount_pp_member_activity()::"
							, e);
		}
		logger
				.info(" Outside select_ppid_partnerdomainid_getcount_pp_member_activity() of BatchServiceImpl class ");

	}

	public void update_pp_customer_score(
			List<PPMemberActivityDataObject> ppmemberActivityDataObjectList) {
		logger
				.info("Inside update_pp_customer_score(List<PPMemberActivityDataObject>) of BatchServiceImpl class");
		try {
			for (PPMemberActivityDataObject ppMemberActivityDataObject : ppmemberActivityDataObjectList) {
				jdbcTemplate
						.update(ConstantQuery.pp_customer_score_SET_sio,
								new Object[] {
										ppMemberActivityDataObject
												.getGeneral_variable(),
										ppMemberActivityDataObject
												.getPartnerDomainId(),
										ppMemberActivityDataObject.getPp_id() });
			}
		} catch (DataAccessException dae) {
			logger.error("BatchServiceImpl class::update_pp_customer_score()::"
					, dae);
		} catch (Exception e) {
			logger.error("BatchServiceImpl class::update_pp_customer_score()::"
					,e);
		}
		logger
				.info("Outside update_pp_customer_score(List<PPMemberActivityDataObject>) of BatchServiceImpl class");
	}

	public void select_member_id_member_social_network_id_max_friends_from_pp_member_network() {
		logger
				.info(" Inside select_member_id_member_social_network_id_max_friends_from_pp_member_network() of BatchServiceImpl class ");
		List<PPMemberActivityDataObject> ppmemberActivityDataObjectList = null;
		try {
			ppmemberActivityDataObjectList = (List) jdbcTemplate
					.query(
							ConstantQuery
									.getSelect_member_id_member_social_network_id_max_friends_from_pp_member_network(),
							new Object[] {}, new ResultSetExtractor() {

								public Object extractData(ResultSet arg0)
										throws SQLException,
										DataAccessException {
									List<PPMemberActivityDataObject> ppmemberActivityDataObjectList = new ArrayList<PPMemberActivityDataObject>();
									PPMemberActivityDataObject ppMemberActivityDataObject = null;
									while (arg0.next()) {
										ppMemberActivityDataObject = new PPMemberActivityDataObject();
										ppMemberActivityDataObject
												.setGeneral_variable(arg0
														.getInt("son"));
										ppMemberActivityDataObject
												.setMemberSocialNetworkId(arg0
														.getString("member_social_network_id"));
										ppMemberActivityDataObject
												.setNetworkCodeId(arg0
														.getInt("network_code_id"));
										ppmemberActivityDataObjectList
												.add(ppMemberActivityDataObject);
									}
									return ppmemberActivityDataObjectList;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error("select_member_id_member_social_network_id_max_friends_from_pp_member_network:Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("select_member_id_member_social_network_id_max_friends_from_pp_member_network:Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_member_id_member_social_network_id_max_friends_from_pp_member_network);
		}
		update_pp_customer_score_v1(ppmemberActivityDataObjectList);
		logger
				.info(" Outside select_member_id_member_social_network_id_max_friends_from_pp_member_network() of BatchServiceImpl class ");
	}

	public void update_pp_customer_score_v1(
			List<PPMemberActivityDataObject> memberActivityDataObjectList) {
		logger
				.info(" Inside update_pp_customer_score_v1(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
		try {
			for (PPMemberActivityDataObject ppMemberActivityDataObject : memberActivityDataObjectList) {
				jdbcTemplate.update(ConstantQuery.getUpdatePpCustomerScorev1()
						.toString(), new Object[] {
						ppMemberActivityDataObject.getGeneral_variable(),
						ppMemberActivityDataObject.getMemberSocialNetworkId(),
						ppMemberActivityDataObject.getNetworkCodeId() });
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:update_pp_customer_score_v1:Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:update_pp_customer_score_v1:Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_customer_scorev1);
		}
		logger
				.info(" Outside update_pp_customer_score_v1(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
	}

	public void select_ppid_partnerdomainid_countswf(String fromdate) {
		logger
				.info(" Inside select_ppid_partnerdomainid_countswf() of BatchServiceImpl class ");
		List<PPMemberActivityDataObject> ppMemberActivitydataObjectList = null;
		try {
			ppMemberActivitydataObjectList = (List) jdbcTemplate.query(
					ConstantQuery.getSelectPpidPartnerdomainidCountswf()
							.toString(), new Object[] { fromdate },
					new ResultSetExtractor() {
						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							List<PPMemberActivityDataObject> ppMemberActivitydataObjectListTemp = new ArrayList<PPMemberActivityDataObject>();
							PPMemberActivityDataObject ppMemberActivityDataObject = null;
							while (arg0.next()) {
								ppMemberActivityDataObject = new PPMemberActivityDataObject();
								ppMemberActivityDataObject.setPp_id(arg0
										.getInt("pp_id"));
								ppMemberActivityDataObject
										.setGeneral_variable(arg0.getInt("swf"));
								ppMemberActivitydataObjectListTemp
										.add(ppMemberActivityDataObject);
							}
							return ppMemberActivitydataObjectListTemp;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:select_ppid_partnerdomainid_countswf:Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:select_ppid_partnerdomainid_countswf:Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppid_partnerdomainid_countswf);
		}
		update_pp_customer_score_v2(ppMemberActivitydataObjectList);
		logger
				.info(" Outside select_ppid_partnerdomainid_countswf() of BatchServiceImpl class ");

	}

	public void update_pp_customer_score_v2(
			List<PPMemberActivityDataObject> memberActivityDataObjectList) {
		logger
				.info(" Inside update_pp_customer_score_v2(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
		try {
			for (PPMemberActivityDataObject ppMemberActivityDataObject : memberActivityDataObjectList) {
				jdbcTemplate.update(ConstantQuery.getUpdatePpCustomerScorev2()
						.toString(), new Object[] {
						ppMemberActivityDataObject.getGeneral_variable(),
						ppMemberActivityDataObject.getPp_id() });
			}
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:update_pp_customer_score_v2::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:update_pp_customer_score_v2::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_customer_scorev2);
		}
		logger
				.info(" Outside update_pp_customer_score_v2(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
	}

	public void select_ppid_partnerdomainid_countfct(String fromdate) {
		logger
				.info(" Inside select_ppid_partnerdomainid_countfct() of BatchServiceImpl class ");
		List<PPMemberActivityDataObject> ppMemberActivitydataObjectList = null;
		try {
			ppMemberActivitydataObjectList = (List) jdbcTemplate.query(
					ConstantQuery.getSelectPpidPartnerdomainidCountfct()
							.toString(), new Object[] { fromdate },
					new ResultSetExtractor() {
						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							List<PPMemberActivityDataObject> ppMemberActivitydataObjectListTemp = new ArrayList<PPMemberActivityDataObject>();
							PPMemberActivityDataObject ppMemberActivityDataObject = null;
							while (arg0.next()) {
								ppMemberActivityDataObject = new PPMemberActivityDataObject();
								ppMemberActivityDataObject.setPp_id(arg0
										.getInt("pp_id"));
								ppMemberActivityDataObject
										.setGeneral_variable(arg0.getInt("fct"));
								ppMemberActivitydataObjectListTemp
										.add(ppMemberActivityDataObject);
							}
							return ppMemberActivitydataObjectListTemp;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:select_ppid_partnerdomainid_countfct is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:select_ppid_partnerdomainid_countfct is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppid_partnerdomainid_countfct);
		}
		update_pp_customer_score_v3(ppMemberActivitydataObjectList);
		logger
				.info(" Outside select_ppid_partnerdomainid_countfct() of BatchServiceImpl class ");

	}

	public void update_pp_customer_score_v3(
			List<PPMemberActivityDataObject> memberActivityDataObjectList) {
		logger
				.info(" Inside update_pp_customer_score_v3(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
		try {
			for (PPMemberActivityDataObject ppMemberActivityDataObject : memberActivityDataObjectList) {
				jdbcTemplate.update(ConstantQuery.getUpdatePpCustomerScorev3()
						.toString(), new Object[] {
						ppMemberActivityDataObject.getGeneral_variable(),
						ppMemberActivityDataObject.getPp_id() });
			}
		} catch (DataAccessException dae) {
			logger.error("BatchServiceImpl:update_pp_customer_score_v3 is::"
					, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:update_pp_customer_score_v3 is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_customer_scorev3);
		}
		logger
				.info(" Outside update_pp_customer_score_v3(List<PPMemberActivityDataObject>) of BatchServiceImpl class ");
	}

	public List<PPCustomerScoreDataObject> select_vts_qov_son_swf_from_ppcustomerscore() {
		logger
				.info(" Inside select_vts_qov_son_swf_from_ppcustomerscore() of BatchSeriveImpl class ");
		List<PPCustomerScoreDataObject> ppCustomerScoreDataObjectList = null;
		try {
			ppCustomerScoreDataObjectList = (List) jdbcTemplate.query(
					ConstantQuery.getSelectVtsQovSonSwfFromPpcustomerscore(),
					new Object[] {}, new ResultSetExtractor() {

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							PPCustomerScoreDataObject ppCustomerScoreDataObject = null;
							List<PPCustomerScoreDataObject> ppCustomerScoreDataObjectList = new ArrayList<PPCustomerScoreDataObject>();
							while (arg0.next()) {
								ppCustomerScoreDataObject = new PPCustomerScoreDataObject();
								ppCustomerScoreDataObject.setVts(arg0
										.getInt("max_vts"));
								ppCustomerScoreDataObject.setQov(arg0
										.getInt("max_qov"));
								ppCustomerScoreDataObject.setSon(arg0
										.getInt("max_son"));
								ppCustomerScoreDataObject.setSwf(arg0
										.getInt("max_swf"));
								ppCustomerScoreDataObjectList
										.add(ppCustomerScoreDataObject);
							}
							return ppCustomerScoreDataObjectList;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:select_vts_qov_son_swf_from_ppcustomerscore::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:select_vts_qov_son_swf_from_ppcustomerscore::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_vts_qov_son_swf_from_ppcustomerscore);
		}
		logger
				.info(" Outside select_vts_qov_son_swf_from_ppcustomerscore() of BatchSeriveImpl class ");
		return ppCustomerScoreDataObjectList;
	}

	public void insert_ppcustomerrank(int affinityMax, int influenceMax) {
		logger
				.info(" Inside insert_ppcustomerrank(int,int) of BatchServiceImpl class ");
		try {
			logger.info(" affinityMax value is::: " + affinityMax);
			logger.info(" influenceMax value is::: " + influenceMax);
			jdbcTemplate.update(ConstantQuery.getInsertPpCustomerRank()
					.toString(), new Object[] { affinityMax, influenceMax });
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:insert_ppcustomerrank::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:insert_ppcustomerrank::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_pp_customer_rank);
		}
		logger
				.info(" Outside insert_ppcustomerrank(int,int) of BatchServiceImpl class ");
	}

	public void insert_pplastaccess() {
		logger.info(" Inside insert_pplastaccess() of BatchServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery.getInsertIntoPplastaccess()
					.toString(), new Object[] {});
		} catch (DataAccessException dae) {
			logger.error("BatchServiceImpl:insert_pplastaccess Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error("BatchServiceImpl:insert_pplastaccess Exception is::"
					, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_into_pplastaccess);
		}
		logger
				.info(" Outside insert_pplastaccess() of BatchServiceImpl class ");
	}

	public void update_ppcustomerrank() {
		logger
				.info(" Inside update_ppcustomerrank() of BatchServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery.getUpdate_pp_customer_rank()
					.toString(), new Object[] {});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:update_ppcustomerrank::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:update_ppcustomerrank::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_customer_rank);
		}
		logger
				.info(" Outside update_ppcustomerrank() of BatchServiceImpl class ");
	}

	public void update_ppcustomerrankv1() {
		logger
				.info(" Inside update_ppcustomerrankv1() of BatchServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery.getUpdatePpCustomerRankv1()
					.toString(), new Object[] {});
		} catch (DataAccessException dae) {
			logger
					.error("BatchServiceImpl:update_ppcustomerrankv1::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("BatchServiceImpl:update_ppcustomerrankv1::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.update_pp_customer_rankv1);
		}
		logger
				.info(" Outside update_ppcustomerrankv1() of BatchServiceImpl class ");
	}

	public int select_id_from_pp_partner_domain(String partnerdomain) {
		logger
				.info(" Inside int=select_id_from_pp_partner_domain(String) from BatchSeriveImpl class ");
		logger.info(" Value of partnerdomain is:::" + partnerdomain);
		int partner_domain_id = 0;
		try {
			partner_domain_id = jdbcTemplate.queryForInt(ConstantQuery
					.getSectidfrompppdomain().toString(),
					new Object[] { partnerdomain });
		} catch (DataAccessException dae) {
			logger
					.error(" BatchSeriveImpl:select_id_from_pp_partner_domain::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error(" BatchSeriveImpl:select_id_from_pp_partner_domain::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside int=select_id_from_pp_partner_domain(String) from BatchSeriveImpl class ");
		return partner_domain_id;
	}

}
