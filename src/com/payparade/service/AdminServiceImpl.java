package com.payparade.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.payparade.dataobject.PPAdminMailDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.dataobject.PPOpsSnapShotDataObject;
import com.payparade.dataobject.PPPartnerMemberActivityDataObject;
import com.payparade.util.ConstantQuery;
import com.payparade.util.Logger;

public class AdminServiceImpl implements AdminService {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	Logger logger = Logger.getLogger("AdminServiceImpl");

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public PPAdminMailDataObject select_pp_admin_mail() {
		logger
				.info(" Inside PPAdminMailDataObject=PPAdminMailDataObject() of AdminServiceImpl class ");
		PPAdminMailDataObject ppAdminMailDataObject = null;
		try {
			ppAdminMailDataObject = (PPAdminMailDataObject) jdbcTemplate.query(
					ConstantQuery.setSelect_pp_admin_mail().toString(),
					new ResultSetExtractor() {
						PPAdminMailDataObject tempppAdminMailDataObject = null;

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							if (arg0.next()) {
								tempppAdminMailDataObject = new PPAdminMailDataObject();
								tempppAdminMailDataObject.setContent(arg0
										.getString("content"));
								tempppAdminMailDataObject.setFrom_user_id(arg0
										.getString("from_user_id"));
								tempppAdminMailDataObject.setId(arg0
										.getInt("id"));
								tempppAdminMailDataObject.setPropertykey(arg0
										.getString("propertykey"));
								tempppAdminMailDataObject.setPropertyvalue(arg0
										.getString("propertyvalue"));
								tempppAdminMailDataObject.setSubject(arg0
										.getString("subject"));
								tempppAdminMailDataObject.setTo_user_id(arg0
										.getString("to_user_id"));
							}
							return tempppAdminMailDataObject;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("AdminSeriveImpl::select_pp_admin_mail:: Exception is::"
							+ dae);
		} catch (Exception e) {
			logger
					.error("AdminSeriveImpl::select_pp_admin_mail:: Exception is::"
							+ e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.select_pp_admin_mail);
		}
		logger
				.info(" Outside PPAdminMailDataObject=PPAdminMailDataObject() of AdminServiceImpl class ");
		return ppAdminMailDataObject;
	}

	public void getOpGraph(final String[] parts) {
		logger.info("Inside getOpGraph(String[]) of AdminServiceImpl class");
		String fields = "num_users, max_connections, num_requests, total_millis, max_millis, used_memory, free_memory";
		String timeRestriction = " AND time_stamp > SUBTIME(NOW(),'0 1:40:0.000000') ";
		String sql = "SELECT ";
		try{
		if (parts.length >= 4) {
			if (fields.contains(parts[3])) {
				sql += parts[3];
				if (parts.length >= 5 && fields.contains(parts[4]))
					sql += "," + parts[4];
				logger.info("SQL::" + sql);
				String dataString = (String) jdbcTemplate.query(sql,
						new ResultSetExtractor() {
							StringBuffer append_string = new StringBuffer();

							public Object extractData(ResultSet arg0)
									throws SQLException, DataAccessException {
								append_string.append("chd:t:0.0");
								while (arg0.next()) {
									append_string.append(",");
									append_string.append(parts[3]);
								}
								return append_string.toString();
							}
						});
				logger.info(dataString);
			}
		}
		}catch(Exception e){
			logger.error("AdminServiceImpl::getOpGraph::Exception is::",e);
		}
		logger.info("Outside getOpGraph(String[]) of AdminServiceImpl class");
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
							+ dae);
		} catch (Exception e) {
			logger
					.error(" BatchSeriveImpl:select_id_from_pp_partner_domain::Exception is::"
							+ e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside int=select_id_from_pp_partner_domain(String) from GadgetSeriveImpl class ");
		return partner_domain_id;
	}

	public void insert_into_pp_ops_snapshot(
			PPOpsSnapShotDataObject ppOpsSnapShotDataObject) {
		logger
				.info(" Inside insert_into_pp_ops_snapshot(PPOpsSnapShotDataObject)of AdminServiceImpl class");
		try {
			jdbcTemplate.update(ConstantQuery.setInsertintopp_ops_snapshot()
					.toString(), new Object[] {
					ppOpsSnapShotDataObject.getNum_of_users(),
					ppOpsSnapShotDataObject.getMax_connections(),
					ppOpsSnapShotDataObject.getNum_of_request(),
					ppOpsSnapShotDataObject.getTotal_milis(),
					ppOpsSnapShotDataObject.getMax_milis(),
					ppOpsSnapShotDataObject.getUsed_memeory(),
					ppOpsSnapShotDataObject.getFree_memory() });
		} catch (DataAccessException dae) {
			logger
					.error(
							"AdminServiceImpl::insert_into_pp_ops_snapshot::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"AdminServiceImpl::insert_into_pp_ops_snapshot::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insertintopp_ops_snapshot);
		}
		logger
				.info(" Outside insert_into_pp_ops_snapshot(PPOpsSnapShotDataObject)of AdminServiceImpl class");
	}

	public List<PPMemberActivityDataObject> select_dt_con_opp_from_pp_membership_ppmemberactivity(
			int partnerid, String query) {
		logger
				.info(" Inside List<PPMemberActivityDataObject>=select_dt_con_opp_from_pp_membership_ppmemberactivity(int,String) from AdminServiceImpl class ");

		List<PPMemberActivityDataObject> ppMemberActivityDataObjectList = null;
		try {
			ppMemberActivityDataObjectList = (List) jdbcTemplate.query(query,
					new ResultSetExtractor() {
						List<PPMemberActivityDataObject> list = new ArrayList<PPMemberActivityDataObject>();
						PPMemberActivityDataObject ppMemberActivityDataObject = null;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							while (rs.next()) {
								ppMemberActivityDataObject = new PPMemberActivityDataObject();
								ppMemberActivityDataObject.setCon(rs
										.getInt("con"));
								ppMemberActivityDataObject.setDt(rs
										.getString("dt"));
								ppMemberActivityDataObject.setOpp(rs
										.getInt("opp"));
								ppMemberActivityDataObject.setConv(rs
										.getFloat("Conv %"));
								ppMemberActivityDataObject.setLastTimeStamp(rs
										.getDate("last_time_stamp"));
								list.add(ppMemberActivityDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("AdminServiceImpl::select_dt_con_from_pp_membership_ppmemberactivity::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("AdminServiceImpl::select_dt_con_from_pp_membership_ppmemberactivity::Exception is::"
							, e);
		}
		logger
				.info(" Inside List<PPMemberActivityDataObject>=select_dt_con_opp_from_pp_membership_ppmemberactivity(int,String) from AdminServiceImpl class ");
		return ppMemberActivityDataObjectList;
	}

	public List<PPPartnerMemberActivityDataObject> select_from_pp_partner_member_activity(
			String query) {
		logger
				.info(" Inside List<PPPartnerMemberActivityDataObject>=select_from_pp_partner_member_activity(String) from AdminServiceImpl class ");

		List<PPPartnerMemberActivityDataObject> ppMemberActivityDataObjectList = null;
		try {
			ppMemberActivityDataObjectList = (List) jdbcTemplate.query(query,
					new ResultSetExtractor() {
						List<PPPartnerMemberActivityDataObject> list = new ArrayList<PPPartnerMemberActivityDataObject>();
						PPPartnerMemberActivityDataObject pPPartnerMemberActivityDataObject = null;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							while (rs.next()) {
								pPPartnerMemberActivityDataObject = new PPPartnerMemberActivityDataObject();
								pPPartnerMemberActivityDataObject
										.setActivity_code(rs
												.getString("activity_count"));
								pPPartnerMemberActivityDataObject
										.setNetwork_code(rs
												.getString("network_code"));
								pPPartnerMemberActivityDataObject
										.setActivity_count(rs
												.getInt("activity_count"));
								list.add(pPPartnerMemberActivityDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("AdminServiceImpl::select_from_pp_partner_member_activity::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("AdminServiceImpl::select_from_pp_partner_member_activity::Exception is::"
							, e);
		}
		logger
				.info(" Inside List<PPPartnerMemberActivityDataObject>=select_from_pp_partner_member_activity(String) from AdminServiceImpl class ");
		return ppMemberActivityDataObjectList;
	}

	public List<PPOpsSnapShotDataObject> select_reportfieldsfrom_pp_ops_snapshort(
			String query) {
		logger
				.info(" Inside List<PPOpsSnapShotDataObject>=select_reportfieldsfrom_pp_ops_snapshort(String) from AdminServiceImpl class ");

		List<PPOpsSnapShotDataObject> pPOpsSnapShotDataObjectList = null;
		try {
			pPOpsSnapShotDataObjectList = (List) jdbcTemplate.query(query,
					new ResultSetExtractor() {
						List<PPOpsSnapShotDataObject> list = new ArrayList<PPOpsSnapShotDataObject>();
						PPOpsSnapShotDataObject ppOpsSnapShotDataObject = null;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							while (rs.next()) {
								ppOpsSnapShotDataObject = new PPOpsSnapShotDataObject();
								ppOpsSnapShotDataObject.setMax_users(rs
										.getInt("max_users"));
								ppOpsSnapShotDataObject.setAverage_users(rs
										.getDouble("avg_users"));
								ppOpsSnapShotDataObject.setMax_milis(rs
										.getInt("max_millis"));
								ppOpsSnapShotDataObject.setAverage_milis(rs
										.getDouble("average_millis"));
								ppOpsSnapShotDataObject.setFree_memory(rs
										.getInt("min_free"));
								list.add(ppOpsSnapShotDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("AdminServiceImpl::select_reportfieldsfrom_pp_ops_snapshort::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("AdminServiceImpl::select_reportfieldsfrom_pp_ops_snapshort::Exception is::"
							, e);
		}
		logger
				.info(" Inside List<PPOpsSnapShotDataObject>=select_reportfieldsfrom_pp_ops_snapshort(String) from AdminServiceImpl class ");
		return pPOpsSnapShotDataObjectList;
	}

}
