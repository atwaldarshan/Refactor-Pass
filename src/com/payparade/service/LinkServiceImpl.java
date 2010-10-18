package com.payparade.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.payparade.dataobject.PPShareLinkDataObject;
import com.payparade.util.ConstantQuery;

public class LinkServiceImpl implements LinkService {
	static Logger logger = Logger.getLogger(LinkServiceImpl.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_shared_id(
			String sid) {
		logger
				.info(" Inside List=setSelect_ppsharedlink_where_shared_id() of LinkServiceImpl class ");

		List<PPShareLinkDataObject> listPPSharedLinkDataObjectlist = null;
		try{
		listPPSharedLinkDataObjectlist = (List) jdbcTemplate.query(
				ConstantQuery.setSelect_from_ppshared_link()
						.toString(), new Object[] { sid },
				new ResultSetExtractor() {
					List<PPShareLinkDataObject> list = new ArrayList<PPShareLinkDataObject>();
					PPShareLinkDataObject ppShareLinkDataObject = null;

					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						while (rs.next()) {
							ppShareLinkDataObject = new PPShareLinkDataObject();
							ppShareLinkDataObject.setId(rs.getInt("id"));
							ppShareLinkDataObject.setPp_id(rs.getInt("pp_id"));
							ppShareLinkDataObject.setNetwork_code_id(rs
									.getInt("network_code_id"));
							ppShareLinkDataObject.setPartner_domain_id(rs
									.getInt("partner_domain_id"));
							ppShareLinkDataObject.setShare_social_id(rs
									.getString("share_social_id"));
							ppShareLinkDataObject.setShared_content(rs
									.getString("shared_content"));
							ppShareLinkDataObject.setShared_image(rs
									.getString("shared_image"));
							ppShareLinkDataObject.setShared_title(rs
									.getString("shared_title"));
							ppShareLinkDataObject.setShared_url(rs
									.getString("shared_url"));
							list.add(ppShareLinkDataObject);
						}
						return list;
					}
				});
		}catch(DataAccessException dae){
			logger.error("LinkServiceImpl::setSelect_ppsharedlink_where_shared_id::Exceptio is::",dae);
		}catch(Exception e){
			logger.error("LinkServiceImpl::setSelect_ppsharedlink_where_shared_id::Exceptio is::",e);
		}finally{
			ConstantQuery.clearStringBuffer(ConstantQuery.select_from_ppshared_link);
		}
		logger
				.info(" Outside List=setSelect_ppsharedlink_where_shared_id() of LinkServiceImpl class ");
		return listPPSharedLinkDataObjectlist;
	}

	public int select_id_from_pp_partner_domain(String partnerdomain) {
		logger
				.info(" Inside int=select_id_from_pp_partner_domain(String) from LinkServiceImpl class ");
		logger.info(" Value of partnerdomain is:::" + partnerdomain);
		int partner_domain_id = 0;
		try {
			partner_domain_id = jdbcTemplate.queryForInt(ConstantQuery
					.getSectidfrompppdomain().toString(),
					new Object[] { partnerdomain });
		} catch (DataAccessException dae) {
			logger
					.error(" LinkSeriveImpl:select_id_from_pp_partner_domain::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error(" LinkSeriveImpl:select_id_from_pp_partner_domain::Exception is::"
							, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside int=select_id_from_pp_partner_domain(String) from LinkServiceImpl class ");
		return partner_domain_id;
	}

	// Get Network Id Facebook,Twitter,myspace
	public short getNetworkId(final String networkurl) {

		logger
				.info(" Inside getNetworkId(String) of LinkServiceImpl class networkurl::"
						+ networkurl);
		short networkid = 0;
		try {
			networkid = (short) jdbcTemplate.queryForInt(
					" select id from pp_network where network_code=?",
					new Object[] { networkurl });
		} catch (DataAccessException dae) {
			logger.error(" PassServiceImpl:getNetworkId:::" , dae);
		}catch(Exception e){
			logger.error(" PassServiceImpl:getNetworkId:::" , e);
		}
		logger
				.info(" Outside getNetworkId(String) of LinkServiceImpl class networkid::"
						+ networkid);
		return networkid;

	}

	public void Insert_into_ppshared_activity(String sid, String id) {
		logger
				.info(" Inside into setInsert_into_ppshared_activity(int,int) from LinkServiceImpl class ");
		try {
			jdbcTemplate.update(ConstantQuery
					.setInsert_into_ppshared_activity().toString(),
					new Object[] { sid, id });
		} catch (DataAccessException dae) {
			logger
					.error("LinkServiceIMpl::setInsert_into_ppshared_activity(int,int)::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("LinkServiceIMpl::setInsert_into_ppshared_activity(int,int)::Exception is::"
							, e);
		}
		logger
				.info(" Outside into setInsert_into_ppshared_activity(int,int) from LinkServiceImpl class ");
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
			logger.error("LinkSeriveImpl:insert_pp_shared_link::Exception is::"
					, dae);
		} catch (Exception e) {
			logger.error("LinkSeriveImpl:insert_pp_shared_link::Exception is::"
					, e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.insert_pp_shared_link);
		}
		logger
				.info("Outside insert_pp_shared_link(PPShareLinkDataObject) of GadgetServiceImpl class  ");
		return keyHolder.getKey().intValue();
	}

	public void displayedResult(ResultSetMetaData md, ResultSet rs,
			PrintWriter output, int row, String columnName, boolean value) {
		logger
				.info(" Inside displayedResult(ResultSetMetaData,ResultSet,PrintWriter,int,String,boolean) of DataServiceImpl class ");
		logger.trace("Row " + (++row));
		int col = 0;
		try {
			output.printf("\t<%s>\n", md.getTableName(1));
			for (col = 1; col <= md.getColumnCount(); col++) {
				logger.trace("  Column " + col + " "
						+ md.getColumnTypeName(col) + "("
						+ md.getColumnType(col) + ")");
				output.printf("\t\t<%s>", md.getColumnName(col));

				columnName = md.getColumnName(col);
				switch (md.getColumnType(col)) {
				case -5:
					output.printf("%d", rs.getInt(col));
					break; // TODO this is big int - improve handling
				case -1:
					output.printf("%s", rs.getString(col));
					break;
				case 1:
					output.printf("%s", rs.getString(col));
					break;
				case 4:
					output.printf("%d", rs.getInt(col));
					break;
				case 8:
					output.printf("%f", rs.getDouble(col));
					break; // logger_.info(md.getColumnName(col)+"("+md.getColumnClassName(col)+")   Data:"+rs.getDouble(col))
							// ; break ;
				case 12:
					output.printf("%s", rs.getString(col));
					break;
				case 93:
					output.printf("%s", rs.getTimestamp(col));
					break; // logger_.info(md.getColumnName(col)+"("+md.getColumnClassName(col)+")   Data:"+rs.getTimestamp(col))
							// ; break ;
				default:
					logger.info("  Column " + col + "(" + columnName
							+ ") Type:" + md.getColumnTypeName(col) + "("
							+ md.getColumnType(col) + ") was not coded.");
				}

				output.printf("</%s>\n", md.getColumnName(col));
			}
			output.printf("\t</%s>\n", md.getTableName(1));
		} catch (SQLException e) {
			logger
					.error("DataServiceImpl:::select_FROM_pp_ops_snapshot::Exception is::"
							 ,e);
		}catch (Exception e) {
			logger
			.error("DataServiceImpl:::select_FROM_pp_ops_snapshot::Exception is::"
					 ,e);
}
		value = true;
		logger
				.info(" Inside displayedResult(ResultSetMetaData,ResultSet,PrintWriter,int,String,boolean) of DataServiceImpl class ");
	}

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_shared_id(
			String sid, final PrintWriter out) {
		logger
				.info(" Inside List=setSelect_ppsharedlink_where_shared_id(String,PrintWriter) of LinkServiceImpl class ");

		List<PPShareLinkDataObject> listPPSharedLinkDataObjectlist = null;
		try {
			listPPSharedLinkDataObjectlist = (List) jdbcTemplate.query(
					ConstantQuery.setSelect_ppsharedlink_where_shared_id()
							.toString(),new Object[]{sid}, new ResultSetExtractor() {
						List<PPShareLinkDataObject> list = new ArrayList<PPShareLinkDataObject>();
						PPShareLinkDataObject ppShareLinkDataObject = null;
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {

							ResultSetMetaData md = rs.getMetaData();
							out
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							while (rs.next()) {
								displayedResult(md, rs, out, row, columnName,
										value);
								row++;
								value = true;
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error("LinkServiceImpl:::setSelect_ppsharedlink_where_shared_id::Exception is::"
							, dae);
		} catch (Exception e) {
			logger
					.error("LinkServiceImpl:::setSelect_ppsharedlink_where_shared_id::Exception is::"
							, e);
		}finally{
			ConstantQuery.clearStringBuffer(ConstantQuery.select_ppsharedlink_where_shared_id);
		}
		logger
				.info(" Outside List=setSelect_ppsharedlink_where_shared_id(String,PrintWriter) of LinkServiceImpl class ");
		return listPPSharedLinkDataObjectlist;
	}

	public List<PPShareLinkDataObject> setSelect_ppsharedlink_where_ppid(
			String id) {
		logger
				.info(" Inside List=setSelect_ppsharedlink_where_shared_id() of LinkServiceImpl class ");

		List<PPShareLinkDataObject> listPPSharedLinkDataObjectlist = null;
		try{
		listPPSharedLinkDataObjectlist = (List) jdbcTemplate.query(
				ConstantQuery.setSelect_ppsharedlink_where_ppid()
						.toString(), new Object[] { id },
				new ResultSetExtractor() {
					List<PPShareLinkDataObject> list = new ArrayList<PPShareLinkDataObject>();
					PPShareLinkDataObject ppShareLinkDataObject = null;

					public Object extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						while (rs.next()) {
							ppShareLinkDataObject = new PPShareLinkDataObject();
							ppShareLinkDataObject.setId(rs.getInt("id"));

							ppShareLinkDataObject.setShared_url(rs
									.getString("shared_url"));
							ppShareLinkDataObject.setClick(rs.getInt("clicks"));
							ppShareLinkDataObject.setPp_id(rs.getInt("ppid"));
							list.add(ppShareLinkDataObject);
						}
						return list;
					}
				});
		}catch(DataAccessException dae){
			logger
			.error("LinkServiceImpl:::setSelect_ppsharedlink_where_shared_id::Exception is::"
					, dae);
} catch (Exception e) {
	logger
			.error("LinkServiceImpl:::setSelect_ppsharedlink_where_shared_id::Exception is::"
					, e);
}finally{
			ConstantQuery.clearStringBuffer(ConstantQuery.select_ppsharedlink_where_ppid);
}
		logger
				.info(" Outside List=setSelect_ppsharedlink_where_shared_id() of LinkServiceImpl class ");
		return listPPSharedLinkDataObjectlist;
	}

}
