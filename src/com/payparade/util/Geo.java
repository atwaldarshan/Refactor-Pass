package com.payparade.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.payparade.dataobject.PPIPDataObject;

public class Geo {
	protected static Logger logger_ = Logger.getLogger(Geo.class
			.getSimpleName());
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public String country_ = "unknown";
	public String region_ = "unknown";
	public String city_ = "unknown";
	public String postal_ = "unknown";
	public String lattitude_ = "unknown";
	public String longitude_ = "unknown";
	public String metro_ = "unknown";
	public String area_ = "unknown";
	public String isp_ = "unknown";
	public String org_ = "unknown";
	public String error_ = "unknown";
	private Database db_ = null;
	private String ip_ = null;

	void Geo() {
	}

	public void geoCodeIp(String ip_address) {
		logger_.info(" Inside geoCodeIp(String) of Geo class ");
		ip_ = ip_address;
		try {
			if (!(ip_ == null)) {
				// DatabaseResult result =
				// db_.executeQuery("SELECT * FROM pp_ip WHERE ip_address = '"+ip_address+"'")
				// ;
				String query = "SELECT * FROM pp_ip WHERE ip_address =?";
				// DataSet data = result.getDataSet() ;
				PPIPDataObject ppipDataObject = null;
				try {
					ppipDataObject = (PPIPDataObject) jdbcTemplate.query(query,
							new Object[] { ip_address },
							new ResultSetExtractor() {
								PPIPDataObject ppipDataObject = null;

								public Object extractData(ResultSet rs)
										throws SQLException,
										DataAccessException {
									if (rs.next()) {
										ppipDataObject = new PPIPDataObject();
										ppipDataObject.setArea_code(rs
												.getString("area_code"));
										ppipDataObject.setCity(rs
												.getString("city"));
										ppipDataObject.setCountry(rs
												.getString("country"));
										ppipDataObject.setIp_address(rs
												.getString("ip_address"));
										ppipDataObject.setIsp(rs
												.getString("isp"));
										ppipDataObject.setLatitude(rs
												.getString("latitude"));
										ppipDataObject.setLongitude(rs
												.getString("longitude"));
										ppipDataObject.setMetro_code(rs
												.getString("metro_code"));
										ppipDataObject.setOrg(rs
												.getString("org"));
										ppipDataObject.setPostal(rs
												.getString("postal"));
										ppipDataObject.setRegion(rs
												.getString("region"));
										return ppipDataObject;
									} else {
										return null;
									}
								}
							});
				} catch (DataAccessException dae) {
					logger_.error("Geo::geoCodeIp::Exception is::" + dae);
				} catch (Exception e) {
					logger_.error("Geo::geoCodeIp::Exception is::" + e);
				}
				if (ppipDataObject == null) {
					Client client = new Client();
					try {
						client
								.webFile("http://geoip1.maxmind.com/f?l=sPdwFswGkr27&i="
										+ ip_address);

						String resp = (String) client.getContent();
						logger_.info("geoCodeIp request (" + ip_address
								+ ") retrieved(" + resp + ")");
						parseResponse(resp);
						saveLocation();
					} catch (MalformedURLException e) {
						logger_.error("Malformed (" + ip_address + ") caused "
								+ e.getMessage());
					} catch (IOException e) {
						logger_.error("ioex caused " + e.getMessage());
					}
				} else {
					parseDataRow(ppipDataObject);
				}
			} else {
				logger_.warn("null ip received");
			}
		} catch (Exception e) {
			logger_.error("Geo::geoCodeIp::Exception is::" + e);
		}
		logger_.info(" Outside geoCodeIp(String) of Geo class ");
	}

	/*
	 * public void geoCodeIp(String ip_address) { ip_ = ip_address ; if ( !(ip_
	 * == null) ) { DatabaseResult result =
	 * db_.executeQuery("SELECT * FROM pp_ip WHERE ip_address = '"
	 * +ip_address+"'") ; DataSet data = result.getDataSet() ; if ( data.size()
	 * == 0 ) { Client client = new Client() ; try {
	 * client.webFile("http://geoip1.maxmind.com/f?l=sPdwFswGkr27&i="
	 * +ip_address) ;
	 * 
	 * String resp = (String) client.getContent() ;
	 * logger_.info("geoCodeIp request ("+ip_address+") retrieved("+resp+")");
	 * parseResponse(resp) ; saveLocation() ; } catch (MalformedURLException e)
	 * { logger_.error("Malformed ("+ip_address+") caused "+e.getMessage() ) ; }
	 * catch (IOException e) {logger_.error("ioex caused "+e.getMessage() ) ; }
	 * } else { parseDataRow(data.get(0)) ; } } else {
	 * logger_.warn("null ip received") ; } }
	 */
	private void parseDataRow(PPIPDataObject ppipDataObject) {
		logger_.info(" Inside parseDataRow(PPIPDataObject) of Geo Class ");
		country_ = ppipDataObject.getCountry();
		region_ = ppipDataObject.getRegion();
		city_ = ppipDataObject.getCity();
		postal_ = ppipDataObject.getPostal();
		lattitude_ = ppipDataObject.getLatitude();
		longitude_ = ppipDataObject.getLongitude();
		metro_ = ppipDataObject.getMetro_code();
		area_ = ppipDataObject.getArea_code();
		isp_ = ppipDataObject.getIsp();
		org_ = ppipDataObject.getOrg();
		logger_.info(" Outside parseDataRow(PPIPDataObject) of Geo Class ");
	}

	private void parseResponse(String resp) {
		logger_.info(" Inside parseResponse(string) of Geo class ");
		String data[] = resp.split(",");
		country_ = data[0];
		region_ = data[1];
		city_ = data[2];
		postal_ = data[3];
		lattitude_ = data[4];
		longitude_ = data[5];
		metro_ = data[6];
		area_ = data[7];
		isp_ = data[8];
		org_ = data[9];
		logger_.info(" Outside parseResponse(string) of Geo class ");
		// error_ = data[10] ;
	}

	/*
	 * public boolean saveLocation() { int num =db_.executeUpdate(
	 * "INSERT INTO pp_ip (ip_address, country, region, city, postal, lattitude, longitude, metro_code, area_code, isp, org) "
	 * + "VALUES ("+ "\""+ip_+"\""+","+ "\""+country_+"\""+","+
	 * "\""+region_+"\""+","+ "\""+city_+"\""+","+ "\""+postal_+"\""+","+
	 * "\""+lattitude_+"\""+","+ "\""+longitude_+"\""+","+ "\""+metro_+"\""+","+
	 * "\""+area_+"\""+","+ "\""+isp_.replaceAll("\"", "")+"\""+","+
	 * "\""+org_.replaceAll("\"", "")+"\""+ "); ") ; return (num == 1 ) ; }
	 */

	public boolean saveLocation() {
		logger_.info(" Inside saveLocation() of Geo class ");
		boolean issave = true;
		try {
			jdbcTemplate.update(ConstantQuery.setInsertIntoPPID().toString(),
					new Object[] { ip_, country_, region_, city_, postal_,
							lattitude_, longitude_, metro_, area_,
							isp_.replaceAll("\"", ""),
							org_.replaceAll("\"", "") });
		} catch (DataAccessException dae) {
			issave = false;
			logger_.error("Geo:::SaveLocation::Exception is::" + dae);

		} catch (Exception e) {
			issave = false;
			logger_.error("Geo:::SaveLocation::Exception is::" + e);
		} finally {
			ConstantQuery.clearStringBuffer(ConstantQuery.insertIntoPPIP);
		}
		logger_.info(" Outside saveLocation() of Geo class::value of issave::"
				+ issave);
		return issave;
	}
}
