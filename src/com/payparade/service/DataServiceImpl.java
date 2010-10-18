package com.payparade.service;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.payparade.dataobject.PPConnectionDataObject;
import com.payparade.dataobject.PPDashBoardDataObject;
import com.payparade.dataobject.PPMemberActivityDataObject;
import com.payparade.util.ConstantQuery;

public class DataServiceImpl implements DataService {
	Logger logger = Logger.getLogger(DataServiceImpl.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insertPPConnection(
			final PPConnectionDataObject ppConnectionDataObject) {
		logger
				.info(" Inside insertPPConnection(PPConnectionDataObject) of DataServiceImpl class  ");
		try {
			for (String friend_id : ppConnectionDataObject.getFriend_id()) {
				jdbcTemplate.update(ConstantQuery.insert_pp_connection,
						new Object[] {
								ppConnectionDataObject.getNetwork_code_id(),
								ppConnectionDataObject.getSocial_id().trim(),
								friend_id.trim() });
			}

		} catch (DataAccessException sql) {
			logger
					.error("DataServiceImpl::insertPPConnection exception::",
							sql);
		} catch (Exception e) {
			logger.error("DataServiceImpl::insertPPConnection exception::", e);
		}
		logger
				.info(" Outside insertPPConnection(PPConnectionDataObject) of DataServiceImpl class  ");
	}

	public List<PPDashBoardDataObject> dashBoardDataObjectList(String sqlquery,
			final String subType) {
		logger
				.info("Inside List<PPDashBoardDataObject>=dashBoardDataObjectList() of DataServiceImpl class");
		logger.info("sqlquery::" + sqlquery);
		logger.info("subType:::" + subType);
		List<PPDashBoardDataObject> dashBoardDataObjectList = null;

		try {
			dashBoardDataObjectList = (List) jdbcTemplate.query(sqlquery,
					new ResultSetExtractor() {
						List<PPDashBoardDataObject> dashBoardDataObjectList = new ArrayList<PPDashBoardDataObject>();
						int index = 1;

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							PPDashBoardDataObject ppDashBoardDataObject = null;
							List<PPDashBoardDataObject> list = new ArrayList<PPDashBoardDataObject>();
							String networkname = "";
							while (arg0.next()) {
								logger
										.info("Record is found successfully.................");
								ppDashBoardDataObject = new PPDashBoardDataObject();
								ppDashBoardDataObject.setActivity(arg0
										.getInt("total"));
								ppDashBoardDataObject.setAverage(arg0
										.getInt("average"));
								ppDashBoardDataObject.setCurrentPeriod(arg0
										.getString("period"));
								ppDashBoardDataObject.setSubType(arg0
										.getString(subType));
								ppDashBoardDataObject.setId(index);
								index++;
								/*
								 * if(subType!=null &&
								 * subType.equalsIgnoreCase("network_code_id")
								 * && !subType.equalsIgnoreCase("0")) { try{
								 * networkname
								 * =(String)jdbcTemplate.queryForObject(
								 * ConstantQuery
								 * .setSelect_network_name_from_networkcodeid
								 * ().toString(), new
								 * Object[]{ppDashBoardDataObject.getSubType()},
								 * new RowMapper() {
								 * 
								 * public Object mapRow(ResultSet arg0, int
								 * arg1) throws SQLException { // TODO
								 * Auto-generated method stub if(arg0.next()){
								 * arg0.previous(); return
								 * arg0.getString("network_code"); }else{ return
								 * ""; } } }); }catch(DataAccessException dae){
								 * logger
								 * .error("DataServiceImpl::dashBoardDataObjectList"
								 * ,dae); }catch(Exception e){logger.error(
								 * "DataServiceImpl::dashBoardDataObjectList"
								 * ,e); }finally{
								 * ConstantQuery.clearStringBuffer
								 * (ConstantQuery.
								 * select_network_name_from_networkcodeid); } }
								 */

								ppDashBoardDataObject
										.setPublic_network_name(networkname);
								list.add(ppDashBoardDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger.error(
					"DataServiceImpl::dashBoardDataObjectList::Exception is",
					dae);
		} catch (Exception e) {
			logger
					.error(
							"DataServiceImpl::dashBoardDataObjectList::Exception is",
							e);

		}

		List<PPDashBoardDataObject> list = new ArrayList<PPDashBoardDataObject>();
		String networkname = "";
		for (PPDashBoardDataObject ppDashBoardDataObject2 : dashBoardDataObjectList) {
			if (ppDashBoardDataObject2.getSubType() != null
					&& ppDashBoardDataObject2.getSubType().equalsIgnoreCase(
							"network_code_id")
					&& !ppDashBoardDataObject2.getSubType().equalsIgnoreCase(
							"0")) {
				try {
					networkname = (String) jdbcTemplate
							.queryForObject(
									ConstantQuery
											.setSelect_network_name_from_networkcodeid()
											.toString(),
									new Object[] { ppDashBoardDataObject2
											.getSubType() }, new RowMapper() {

										public Object mapRow(ResultSet arg0,
												int arg1) throws SQLException {
											// TODO Auto-generated method stub
											if (arg0.next()) {
												arg0.previous();
												return arg0
														.getString("network_code");
											} else {
												return "";
											}
										}
									});
				} catch (DataAccessException dae) {
					logger.error("DataServiceImpl::dashBoardDataObjectList",
							dae);
				} catch (Exception e) {
					logger.error("DataServiceImpl::dashBoardDataObjectList", e);
				} finally {
					ConstantQuery
							.clearStringBuffer(ConstantQuery.select_network_name_from_networkcodeid);
				}
			}
			ppDashBoardDataObject2.setPublic_network_name(networkname);
			list.add(ppDashBoardDataObject2);

		}

		logger
				.info("Outside List<PPDashBoardDataObject>=dashBoardDataObjectList() of DataServiceImpl class");
		return list;
	}

	public void select_FROM_pp_ops_snapshot(final PrintWriter output) {
		logger
				.info(" Inside select_FROM_pp_ops_snapshot() of DataServiceImpl class ");
		try {
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_FROM_pp_ops_snapshot().toString(),
					new ResultSetExtractor() {
						List list = new ArrayList();
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData md = rs.getMetaData();
							output
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							output.printf("<data>\n");
							while (rs.next()) {
								displayedResult(md, rs, output, row,
										columnName, value);
								row++;
								value = true;
							}
							output.printf("</data>\n");
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							"DataServiceImpl class::select_FROM_pp_ops_snapshot::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"DataServiceImpl class::select_FROM_pp_ops_snapshot::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_FROM_pp_ops_snapshot);
		}
		logger
				.info(" Outside select_FROM_pp_ops_snapshot() of DataServiceImpl class ");
	}

	public void displayedResult(ResultSetMetaData md, ResultSet rs,
			PrintWriter output, int row, String columnName, boolean value) {
		logger
				.info(" Inside displayedResult(ResultSetMetaData,ResultSet,PrintWriter,int,String,boolean) of DataServiceImpl class ");
		logger.trace("Row " + (++row));
		int col = 0;
		try {
			output.printf("\t<%s>\n", md.getTableName(1));
			logger.info("Table Name is:::------->" + md.getTableName(1));
			for (col = 1; col <= md.getColumnCount(); col++) {
				logger.info("  Column " + col + " " + md.getColumnTypeName(col)
						+ "(" + md.getColumnType(col) + ")");
				output.printf("\t\t<%s>", md.getColumnName(col));

				columnName = md.getColumnName(col);
				switch (md.getColumnType(col)) {
				case -5:
					output.printf("%d", rs.getInt(col));
					break; // TODO this is big int - improve handling
				case -1:
					if (rs.getString(col) != null)
						output.printf("%s", rs.getString(col));
					else
						output.printf("%s", "");
					break;
				case 1:
					if (rs.getString(col) != null)
						output.printf("%s", rs.getString(col));
					else
						output.printf("%s", "");
					break;
				case 4:
					output.printf("%d", rs.getInt(col));
					break;
				case 8:
					output.printf("%f", rs.getDouble(col));
					break; // logger_.info(md.getColumnName(col)+"("+md.getColumnClassName(col)+")   Data:"+rs.getDouble(col))
							// ; break ;
				case 12:
					if (rs.getString(col) != null)
						output.printf("%s", rs.getString(col));
					else
						output.printf("%s", "");
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
		} catch (SQLException sql) {
			logger.error("DataServiceImpl:::displayedResult::Exception is::",
					sql);
		} catch (Exception e) {
			logger
					.error("DataServiceImpl:::displayedResult::Exception is::",
							e);
		}
		value = true;
		logger
				.info(" Outside displayedResult(ResultSetMetaData,ResultSet,PrintWriter,int,String,boolean) of DataServiceImpl class ");
	}

	public int select_id_from_pp_partner_domain(String partnerdomain) {
		logger
				.info(" Inside int=select_id_from_pp_partner_domain(String) from DataServiceImpl class ");
		logger.info(" Value of partnerdomain is:::" + partnerdomain);
		int partner_domain_id = 0;
		try {
			partner_domain_id = jdbcTemplate.queryForInt(ConstantQuery
					.getSectidfrompppdomain().toString(),
					new Object[] { partnerdomain });
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_partner_domain::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_partner_domain::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.selectidfrompppdomain);
		}
		logger
				.info(" Outside int=select_id_from_pp_partner_domain(String) from DataServiceImpl class ");
		return partner_domain_id;
	}

	public int select_id_from_pp_activity(String activity_name) {
		logger
				.info(" Inside int=select_id_from_pp_activity(String) from DataServiceImpl class ");
		logger.info(" Value of activity is:::" + activity_name);
		int activity_id = 0;
		try {
			activity_id = jdbcTemplate.queryForInt(ConstantQuery
					.setSelect_id_from_pp_activity().toString(),
					new Object[] { activity_name });
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_activity::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_id_from_pp_activity::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_id_from_pp_activity);
		}
		logger
				.info(" Outside int=select_id_from_pp_activity(String) from DataServiceImpl class ");
		return activity_id;
	}

	public void select_FROM_pp_member_activiy(final String query,
			final PrintWriter output) {
		logger
				.info(" Inside select_FROM_pp_ops_snapshot(String,PrintWriter) of DataServiceImpl class ");
		try {
			List list = (List) jdbcTemplate.query(query,
					new ResultSetExtractor() {
						List list = new ArrayList();
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData md = rs.getMetaData();
							output
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							output.printf("<data>\n");
							while (rs.next()) {
								displayedResult(md, rs, output, row,
										columnName, value);
								row++;
							}
							output.printf("</data>\n");
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							" BatchSeriveImpl:select_FROM_pp_member_activiy::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" BatchSeriveImpl:select_FROM_pp_member_activiy::Exception is::",
							e);
		}
		logger
				.info(" Outside select_FROM_pp_ops_snapshot(String,PrintWriter) of DataServiceImpl class ");
	}

	public int select_id_from_pp_network(String networkcode) {
		logger
				.info(" Inside int=select_id_from_pp_network(String) from DataServiceImpl class ");
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
				.info(" Outside int=select_id_from_pp_partner_domain(String) from DataServiceImpl class ");
		return networkcodeid;
	}

	public void setSelect_distinct_social_id_friend_id_from_pp_connection(
			final int networkcodeid, final String membersocialnetworkid,
			final PrintWriter output) {
		logger
				.info(" Inside setSelect_distinct_social_id_friend_id_from_pp_connection(int,String,PrintWriter) of DataServiceImpl class ");
		try {
			logger.info("Network Code Id is:::" + networkcodeid);
			logger.info("Member Social Network Id::" + membersocialnetworkid);
			List list = (List) jdbcTemplate
					.query(
							ConstantQuery
									.setSelect_distinct_social_id_friend_id_from_pp_connection()
									.toString(), new Object[] { networkcodeid,
									membersocialnetworkid },
							new ResultSetExtractor() {
								List list = new ArrayList();
								int row = 0;
								String columnName = "";
								boolean value = true;

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									ResultSetMetaData md = rs.getMetaData();
									output
											.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
									output.printf("<data>\n");
									while (rs.next()) {
										displayedResult(md, rs, output, row,
												columnName, value);
										row++;
									}
									output.printf("</data>\n");
									return list;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:setSelect_distinct_social_id_friend_id_from_pp_connection::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:setSelect_distinct_social_id_friend_id_from_pp_connection::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_logo_from_pp_partner_options);
		}
		logger
				.info(" Outside setSelect_distinct_social_id_friend_id_from_pp_connection(int,String,PrintWriter) of DataServiceImpl class ");
	}

	public void setSelect_logo_from_pp_partneroption(final int partnerdomainid,
			final PrintWriter output) {
		logger
				.info(" Inside setSelect_logo_from_pp_partneroption(int,PrintWriter) of DataServiceImpl class ");
		try {
			logger.info("Partner Domain Id:::" + partnerdomainid);
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_logo_from_pp_partner_options().toString(),
					new Object[] { partnerdomainid }, new ResultSetExtractor() {
						List list = new ArrayList();
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData md = rs.getMetaData();
							output
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							output.printf("<data>\n");
							while (rs.next()) {
								displayedResult(md, rs, output, row,
										columnName, value);
								row++;
							}
							output.printf("</data>\n");
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:setSelect_logo_from_pp_partneroption::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:setSelect_logo_from_pp_partneroption::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_logo_from_pp_partner_options);
		}
		logger
				.info(" Outside setSelect_logo_from_pp_partneroption(int,PrintWriter) of DataServiceImpl class ");
	}

	public void setSelect_from_pp_partnernetwork(final int partnerdomainid,
			final PrintWriter output) {
		logger
				.info(" Inside setSelect_from_pp_partnernetwork(int,PrintWriter) of DataServiceImpl class ");
		try {
			logger.info(ConstantQuery.setSelect_from_pp_partner_network()
					.toString());
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_network);
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_pp_partner_network().toString(),
					new Object[] { partnerdomainid, partnerdomainid },
					new ResultSetExtractor() {
						List list = new ArrayList();
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData md = rs.getMetaData();
							output
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							output.printf("<data>\n");
							while (rs.next()) {
								displayedResult(md, rs, output, row,
										columnName, value);
								row++;
							}
							output.printf("</data>\n");
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:setSelect_from_pp_partnernetwork::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:setSelect_from_pp_partnernetwork::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_network);
		}
		logger
				.info(" Outside setSelect_from_pp_partnernetwork(int,PrintWriter) of DataServiceImpl class ");
	}

	public void select_from_pp_partner_network_where_partner_domain_idand_network_code_id(
			final int partnerdomainid, final int networkcodeid,
			final PrintWriter output) {
		logger
				.info(" Inside select_from_pp_partner_network_where_partner_domain_idand_network_code_id(int,int,PrintWriter) of DataServiceImpl class ");
		try {
			List list = (List) jdbcTemplate
					.query(
							ConstantQuery
									.setSelect_from_pp_partner_network_where_partner_domain_idand_network_code_id()
									.toString(),

							new Object[] { partnerdomainid, networkcodeid,
									partnerdomainid, networkcodeid },
							new ResultSetExtractor() {
								List list = new ArrayList();
								int row = 0;
								String columnName = "";
								boolean value = true;

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									ResultSetMetaData md = rs.getMetaData();
									output
											.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
									output.printf("<data>\n");
									while (rs.next()) {
										displayedResult(md, rs, output, row,
												columnName, value);
										row++;
									}
									output.printf("</data>\n");
									return list;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_network_where_partner_domain_idand_network_code_id::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_network_where_partner_domain_idand_network_code_id::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_network_where_partner_domain_idand_network_code_id);
		}
		logger
				.info(" Outside select_from_pp_partner_network_where_partner_domain_idand_network_code_id(int,int,PrintWriter) of DataServiceImpl class ");
	}

	public void select_from_pp_partner_options_WHERE_partner_domain(
			final int partnerdomainid, final PrintWriter output) {
		// here come back
		logger
				.info(" Inside select_from_pp_partner_options_WHERE_partner_domain(int,PrintWriter) of DataServiceImpl class ");
		try {
			logger.info("Partner Domain Id::" + partnerdomainid);
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_pp_partner_options_WHERE_partner_domain()
					.toString(), new Object[] { partnerdomainid,
					partnerdomainid }, new ResultSetExtractor() {
				List list = new ArrayList();
				int row = 0;
				String columnName = "";
				boolean value = true;

				public Object extractData(ResultSet rs) throws SQLException,
						DataAccessException {
					ResultSetMetaData md = rs.getMetaData();
					output
							.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					output.printf("<data>\n");
					while (rs.next()) {
						displayedResult(md, rs, output, row, columnName, value);
						row++;
					}
					output.printf("</data>\n");
					return list;
				}
			});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_options_WHERE_partner_domain::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_options_WHERE_partner_domain::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_options_WHERE_partner_domain);
		}
		logger
				.info(" Outside select_from_pp_partner_options_WHERE_partner_domain(int,PrintWriter) of DataServiceImpl class ");
	}

	public void select_from_pp_partner_options_where_usermail(
			final String useremail, final PrintWriter output) {
		logger
				.info(" Inside select_from_pp_partner_options_where_usermail(String,PrintWriter) of DataServiceImpl class ");
		try {
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_pp_partner_options_where_usermail()
					.toString(), new Object[] { useremail },
					new ResultSetExtractor() {
						List list = new ArrayList();
						int row = 0;
						String columnName = "";
						boolean value = true;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							ResultSetMetaData md = rs.getMetaData();
							output
									.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
							output.printf("<data>\n");
							while (rs.next()) {
								logger.info("Record is found........");
								displayedResult(md, rs, output, row,
										columnName, value);
								row++;
							}
							output.printf("</data>\n");
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_options_where_usermail::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_from_pp_partner_options_where_usermail::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_pp_partner_options_where_usermail);
		}
		logger
				.info(" Outside select_from_pp_partner_options_where_usermail(String,PrintWriter) of DataServiceImpl class ");
	}

	public void select_from_ppcustomer_rank_wherepartnerdomain_id(
			final int partnerdomainid, final PrintWriter output) {
		logger
				.info(" Inside select_from_ppcustomer_rank_wherepartnerdomain_id(int,PrintWriter) of DataServiceImpl class ");
		try {
			List list = (List) jdbcTemplate.query(ConstantQuery
					.setSelect_from_ppcustomer_rank_wherepartnerdomain_id()
					.toString(), new Object[] { partnerdomainid,
					partnerdomainid }, new ResultSetExtractor() {
				List list = new ArrayList();
				int row = 0;
				String columnName = "";
				boolean value = true;

				public Object extractData(ResultSet rs) throws SQLException,
						DataAccessException {
					ResultSetMetaData md = rs.getMetaData();
					output
							.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
					output.printf("<data>\n");
					while (rs.next()) {
						displayedResult(md, rs, output, row, columnName, value);
						row++;
					}
					output.printf("</data>\n");
					return list;
				}
			});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_from_ppcustomer_rank_wherepartnerdomain_id::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_from_ppcustomer_rank_wherepartnerdomain_id::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_ppcustomer_rank_wherepartnerdomain_id);
		}
		logger
				.info(" Outside select_from_ppcustomer_rank_wherepartnerdomain_id(int,PrintWriter) of DataServiceImpl class ");
	}

	public void select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude(
			final int partnerdomainid, final PrintWriter output,
			final double... latitude_longitude) {
		logger
				.info("Inside select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude(int,PrintWriter,final double...) of DataServiceImpl class ");
		try {
			
			List list = (List) jdbcTemplate
					.query(
							ConstantQuery
									.setSelect_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude()
									.toString(), new Object[] {
									latitude_longitude[0],
									latitude_longitude[1],
									latitude_longitude[2],
									latitude_longitude[3] },
							new ResultSetExtractor() {
								List list = new ArrayList();
								int row = 0;
								String columnName = "";
								boolean value = true;

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									ResultSetMetaData md = rs.getMetaData();
									output
											.printf("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
									output.printf("<data>\n");
									while (rs.next()) {
										displayedResult(md, rs, output, row,
												columnName, value);
										row++;
									}
									output.printf("</data>\n");
									return list;
								}
							});
		} catch (DataAccessException dae) {
			logger
					.error(
							" DataSeriveImpl:select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							" DataSeriveImpl:select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude::Exception is::",
							e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude);
		}
		logger
				.info(" Outside select_from_ppcustomer_rank_wherepartnerdomain_idandlatitude_longitude(int,PrintWriter,final double...) of DataServiceImpl class ");
	}

	public void updatedynamictable(String sql) {
		logger
				.info(" Inside updatedynamictable(String) of DataServiceImpl class ");
		try {
			jdbcTemplate.update(sql, new Object[] {});
		} catch (DataAccessException dae) {
			logger.error("DataServiceImpl::updatedynamictable::Exception is::",
					dae);
		} catch (Exception e) {
			logger.error("DataServiceImpl::updatedynamictable::Exception is::",
					e);
		}
		logger
				.info(" Inside updatedynamictable(String) of DataServiceImpl class ");
	}

	public List<PPMemberActivityDataObject> select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity(
			String query) {
		logger
				.info(" Inside List=select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity() of DataServiceImpl class ");
		List<PPMemberActivityDataObject> listPPMemberActivityDataObjectlist = null;
		try {
			listPPMemberActivityDataObjectlist = (List) jdbcTemplate.query(
					query, new ResultSetExtractor() {
						List<PPMemberActivityDataObject> list = new ArrayList<PPMemberActivityDataObject>();
						PPMemberActivityDataObject ppMemberActivityDataObject = null;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							while (rs.next()) {
								ppMemberActivityDataObject = new PPMemberActivityDataObject();
								ppMemberActivityDataObject.setActivityCodeId(rs
										.getInt("code"));
								ppMemberActivityDataObject
										.setGeneral_variable(rs.getInt("val"));
								list.add(ppMemberActivityDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							"DataServiceImpl::select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"DataServiceImpl::select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity::Exception is::",
							e);
		}
		logger
				.info(" Outside List=select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity() of DataServiceImpl class ");
		return listPPMemberActivityDataObjectlist;
	}

	public int SELECT_count_val_from_pp_membership(String sqlquery) {
		logger
				.info(" Inside int=SELECT_count_val_from_pp_membership(String) of DataServiceImpl class ");
		int counter = 0;
		try {
			counter = jdbcTemplate.queryForInt(sqlquery);
		} catch (DataAccessException dae) {
			logger
					.error(
							"DataServiceImpl::SELECT_count_val_from_pp_membership::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"DataServiceImpl::SELECT_count_val_from_pp_membership::Exception is::",
							e);
		}
		logger
				.info(" Outside int=SELECT_count_val_from_pp_membership(String) of DataServiceImpl class::Counter is::"
						+ counter);
		return counter;
	}

	public List<PPMemberActivityDataObject> select_distinctmembersession_id_val_FROM_pp_member_activity(
			String query) {
		logger
				.info(" Inside List=select_activitycodeid_count_distinctmembersession_id_val_FROM_pp_member_activity() of DataServiceImpl class ");
		List<PPMemberActivityDataObject> listPPMemberActivityDataObjectlist = null;
		try {
			listPPMemberActivityDataObjectlist = (List) jdbcTemplate.query(
					query, new ResultSetExtractor() {
						List<PPMemberActivityDataObject> list = new ArrayList<PPMemberActivityDataObject>();
						PPMemberActivityDataObject ppMemberActivityDataObject = null;

						public Object extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							while (rs.next()) {
								ppMemberActivityDataObject = new PPMemberActivityDataObject();
								ppMemberActivityDataObject
										.setGeneral_variable(rs.getInt("val"));
								list.add(ppMemberActivityDataObject);
							}
							return list;
						}
					});
		} catch (DataAccessException dae) {
			logger
					.error(
							"DataServiceImpl::select_distinctmembersession_id_val_FROM_pp_member_activity::Exception is::",
							dae);
		} catch (Exception e) {
			logger
					.error(
							"DataServiceImpl::select_distinctmembersession_id_val_FROM_pp_member_activity::Exception is::",
							e);
		}
		logger
				.info(" Outside List=select_distinctmembersession_id_val_FROM_pp_member_activity() of DataServiceImpl class ");
		return listPPMemberActivityDataObjectlist;
	}
}
