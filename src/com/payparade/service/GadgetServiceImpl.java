package com.payparade.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.payparade.dataobject.PPMessagesDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPShareLinkDataObject;
import com.payparade.util.ConstantQuery;
import com.payparade.util.Logger;

public class GadgetServiceImpl implements GadgetService {
	static Logger logger = Logger.getLogger("GadgetServiceImpl");
	private Map<String, String> connectSize_ = new HashMap<String, String>();
	private Map<String, String> connectText_ = new HashMap<String, String>();
	private Map<String, String> partnerCSS_ = new HashMap<String, String>();
	private Map<String, String> mySpaceApps_ = new HashMap<String, String>();

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public Map<String, String> getMySpaceApps_() {
		return mySpaceApps_;
	}

	public void setMySpaceApps_(Map<String, String> mySpaceApps) {
		mySpaceApps_ = mySpaceApps;
	}

	public Map<String, String> getConnectSize_() {
		return connectSize_;
	}

	public void setConnectSize_(HashMap<String, String> connectSize) {
		connectSize_ = connectSize;
	}

	public Map<String, String> getConnectText_() {
		return connectText_;
	}

	public void setConnectText_(HashMap<String, String> connectText) {
		connectText_ = connectText;
	}

	public Map<String, String> getPartnerCSS_() {
		return partnerCSS_;
	}

	public void setPartnerCSS_(HashMap<String, String> partnerCSS) {
		partnerCSS_ = partnerCSS;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void initializeGadgetList() {
		logger
				.info(" Inside initializeGadgetList() of GadgetServiceImpl class ");
		try {

			connectSize_ = (Map) jdbcTemplate.query(ConstantQuery
					.getSelect_ppgadget_connect_size().toString(),
					new ResultSetExtractor() {
						Map<String, String> map = new HashMap<String, String>();

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							while (arg0.next()) {
								map.put(arg0.getString("partner_domain_name"),
										arg0.getString("connect_style"));
							}
							return map;
						}
					});

			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppgadget_connect_size);
			connectText_ = (Map) jdbcTemplate.query(ConstantQuery
					.getSelect_ppgadget_connect_size().toString(),
					new ResultSetExtractor() {
						Map<String, String> map = new HashMap<String, String>();

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							while (arg0.next()) {
								map.put(arg0.getString("partner_domain_name"),
										arg0.getString("connect_text"));
							}
							return map;
						}
					});
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppgadget_connect_size);
			partnerCSS_ = (Map) jdbcTemplate.query(ConstantQuery
					.getSelect_ppgadget_connect_size().toString(),
					new ResultSetExtractor() {
						Map<String, String> map = new HashMap<String, String>();

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							while (arg0.next()) {
								map.put(arg0.getString("partner_domain_name"),
										arg0.getString("css_file"));
							}
							return map;
						}
					});

			mySpaceApps_ = (Map) jdbcTemplate.query(ConstantQuery
					.getSelect_ppgadget_myspace_app().toString(),
					new ResultSetExtractor() {
						Map<String, String> map = new HashMap<String, String>();

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							while (arg0.next()) {
								map.put(arg0.getString("partner_domain_name"),
										arg0.getString("partner_css"));
							}
							return map;
						}
					});

		} catch (DataAccessException dae) {
			logger.error("PartnerNetworkServiceImpl::init()::Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error("PartnerNetworkServiceImpl::init()::Exception is::"
					, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppgadget_connect_size);
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_ppgadget_myspace_app);
		}
		logger
				.info(" Outside initializeGadgetList() of GadgetServiceImpl class ");

	}

	public int select_id_from_pp_partner_domain(String partnerdomain) {
		logger
				.info(" Inside int=select_id_from_pp_partner_domain(String) from GadgetSeriveImpl class ");
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
				.info(" Outside int=select_id_from_pp_partner_domain(String) from GadgetSeriveImpl class ");
		return partner_domain_id;
	}

	public int insert_pp_shared_link(
			final PPShareLinkDataObject ppShareLinkDataObject) {
		logger
				.info("Inside insert_pp_shared_link(PPShareLinkDataObject) of GadgetServiceImpl class  ");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) {
					PreparedStatement ps = null;
					try {
						ps = connection.prepareStatement(ConstantQuery
								.setInsert_pp_shared_link().toString(),
								new String[] { "id" });
						ps.setInt(1, ppShareLinkDataObject.getPp_id());
						ps
								.setString(2, ppShareLinkDataObject
										.getShare_social_id());
						ps
								.setInt(3, ppShareLinkDataObject
										.getNetwork_code_id());
						ps.setInt(4, ppShareLinkDataObject
								.getPartner_domain_id());
						ps.setString(5, ppShareLinkDataObject.getShared_url());
						ps
								.setString(6, ppShareLinkDataObject
										.getShared_title());
						ps.setString(7, ppShareLinkDataObject
								.getShared_content());
					} catch (DataAccessException dae) {
						logger
								.error(" BatchServiceImpl::insert_pp_shared_link:: Exception is::"
										, dae);
					} catch (SQLException sql) {
						logger
								.error(" BatchServiceImpl::insert_pp_shared_link:: Exception is::"
										, sql);
					} catch (Exception e) {
						logger
								.error(" BatchServiceImpl::insert_pp_shared_link:: Exception is::"
										, e);
					}
					return ps;
				}
			}, keyHolder);
		} catch (DataAccessException dae) {
			logger.error("BatchSeriveImpl:insert_pp_shared_link::Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error("BatchSeriveImpl:insert_pp_shared_link::Exception is::"
					, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_pp_shared_link);
		}
		logger
				.info("Outside insert_pp_shared_link(PPShareLinkDataObject) of GadgetServiceImpl class  ");
		return keyHolder.getKey().intValue();
	}

	public int getNetworkId(String networkcode) {
		System.out
				.println("Inside int getNetworkId(String networkcode) of GadgetServiceImpl class");
		int networkcodeid = 0;
		networkcodeid = jdbcTemplate.queryForInt(
				ConstantQuery.select_id_ppnetwork.toString(),
				new Object[] { networkcode });
		System.out
				.println("Outside int getNetworkId(String networkcode) of GadgetServiceImpl class");
		return networkcodeid;
	}

	public List<PPMessagesDataObject> select_pp_messages() {
		logger
				.info(" Inside List<PPMessagesDataObject> = select_pp_messages() of GadgetServiceImpl class");
		List<PPMessagesDataObject> ppMessageDataObjectList = null;
		try {
			ppMessageDataObjectList = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_pp_messages().toString(),
					new ResultSetExtractor() {
						List<PPMessagesDataObject> list = new ArrayList<PPMessagesDataObject>();
						PPMessagesDataObject ppMessagesDataObject = null;

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							if(arg0.next()){
							ppMessagesDataObject = new PPMessagesDataObject();
							ppMessagesDataObject.setId(arg0.getInt("id"));
							ppMessagesDataObject.setMessage(arg0
									.getString("message"));
							ppMessagesDataObject.setTitle(arg0
									.getString("title"));
							list.add(ppMessagesDataObject);
							}
							return list;

						}
					});
		} catch (DataAccessException dae) {
			logger.error(" GadgetServiceImpl:select_pp_messages::Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error(" GadgetServiceImpl:select_pp_messages::Exception is::"
					, e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.select_pp_messages);
		}
		logger
				.info(" Outside List<PPMessagesDataObject> = select_pp_messages() of GadgetServiceImpl class");
		return ppMessageDataObjectList;
	}

	public List<PPOpsSnapShotDataObject> select_numuser_from_pp_ops_snapshot() {
		logger
				.info(" Inside List<PPOpsSnapShotDataObject>=select_numuser_from_pp_ops_snapshot() of GadgetServiceImpl class ");
		List<PPOpsSnapShotDataObject> pp_ops_snap_short_list = null;
		try {
			pp_ops_snap_short_list =(List)jdbcTemplate.query(ConstantQuery
					.setSelect_numuser_from_pp_ops_snapshot().toString(),
					new ResultSetExtractor() {
						
						public Object extractData(ResultSet arg0) throws SQLException,
								DataAccessException {
							List<PPOpsSnapShotDataObject> list = new ArrayList<PPOpsSnapShotDataObject>();
							PPOpsSnapShotDataObject ppOpsSnapShotDataObject = null;
							while(arg0.next()){
								ppOpsSnapShotDataObject = new PPOpsSnapShotDataObject();
								ppOpsSnapShotDataObject.setNum_of_users(arg0.getInt("num_of_users"));
								list.add(ppOpsSnapShotDataObject);
								
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(" GadgetServiceImpl:select_numuser_from_pp_ops_snapshot::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error(" GadgetServiceImpl:select_numuser_from_pp_ops_snapshot::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_numuser_from_pp_ops_snapshot);
		}
		logger
				.info(" Outside List<PPOpsSnapShotDataObject>=select_numuser_from_pp_ops_snapshot() of GadgetServiceImpl class");					
		return pp_ops_snap_short_list;
	}

	public List<PPOpsSnapShotDataObject> select_numofrequest_from_pp_ops_snapshot() {
		logger
				.info(" Inside List=select_numofrequest_from_pp_ops_snapshot() of GadgetServiceImpl class ");
		List<PPOpsSnapShotDataObject> pp_ops_snap_short_list = null;
		try {
			pp_ops_snap_short_list =(List)jdbcTemplate.query(ConstantQuery
					.setselect_numofrequest_from_pp_ops_snapshot().toString(),
					new ResultSetExtractor() {
						
						public Object extractData(ResultSet arg0) throws SQLException,
								DataAccessException {
							List<PPOpsSnapShotDataObject> list = new ArrayList<PPOpsSnapShotDataObject>();
							PPOpsSnapShotDataObject ppOpsSnapShotDataObject = null;
							while(arg0.next()){
								logger.info("Record is get successfully...");
								ppOpsSnapShotDataObject = new PPOpsSnapShotDataObject();
								ppOpsSnapShotDataObject.setNum_of_request(arg0.getInt("num_of_request"));
								list.add(ppOpsSnapShotDataObject);
							}
							logger.info("Size of list is::"+list.size());
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(" GadgetServiceImpl:select_numofrequest_from_pp_ops_snapshot::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error(" GadgetServiceImpl:select_numofrequest_from_pp_ops_snapshot::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_numofrequest_from_pp_ops_snapshot);
		}
		logger
				.info(" Outside List=select_numofrequest_from_pp_ops_snapshot() of GadgetServiceImpl class value of number_of_user::");
		logger.info("Size of list is::"+pp_ops_snap_short_list.size());		
		return pp_ops_snap_short_list;
	}

	public List<PPOpsSnapShotDataObject> select_totalmills_from_pp_ops_snapshot() {
		logger
				.info(" Inside List<PPOpsSnapShotDataObject>=select_totalmills_from_pp_ops_snapshot() of GadgetServiceImpl class ");
		List<PPOpsSnapShotDataObject> pp_ops_snap_short_list = null;
		try {
			pp_ops_snap_short_list =(List)jdbcTemplate.query(ConstantQuery
					.setSelect_totalmills_from_pp_ops_snapshot().toString(),
					new ResultSetExtractor() {
						
						public Object extractData(ResultSet arg0) throws SQLException,
								DataAccessException {
							List<PPOpsSnapShotDataObject> list = new ArrayList<PPOpsSnapShotDataObject>();
							PPOpsSnapShotDataObject ppOpsSnapShotDataObject = null;
							while(arg0.next()){
								ppOpsSnapShotDataObject = new PPOpsSnapShotDataObject();
								ppOpsSnapShotDataObject.setTotal_milis(arg0.getInt("total_milis"));
								list.add(ppOpsSnapShotDataObject);
								
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(" GadgetServiceImpl:select_totalmills_from_pp_ops_snapshot::Exception is::"
							+ dae);
		} catch (Exception e) {
			logger
					.error(" GadgetServiceImpl:select_totalmills_from_pp_ops_snapshot::Exception is::"
							+ e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_totalmills_from_pp_ops_snapshot);
		}
		logger
				.info(" Outside List<PPOpsSnapShotDataObject>=select_totalmills_from_pp_ops_snapshot() of GadgetServiceImpl class value of number_of_user::");
						
		return pp_ops_snap_short_list;
	}

}
