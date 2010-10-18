package com.payparade.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.payparade.dataobject.PPPartnerNetworkDataObject;
import com.payparade.util.ConstantQuery;

public class PartnerNetworkServiceImpl implements PartnerNetworkService {
	Logger logger = Logger.getLogger(PartnerNetworkServiceImpl.class);
	private DataSource dataSource = null;
	private JdbcTemplate jdbcTemplate;
	private Map<Integer, PPPartnerNetworkDataObject> partner_network_map;

	public Map<Integer, PPPartnerNetworkDataObject> getPartner_network_map() {
		return partner_network_map;
	}

	public void setPartner_network_map(
			Map<Integer, PPPartnerNetworkDataObject> partnerNetworkMap) {
		partner_network_map = partnerNetworkMap;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void init() {
		logger.info(" Inside init() of PaternerNetworkServiceImpl class ");
		jdbcTemplate = new JdbcTemplate(dataSource);
		Map temp_map = null;
		try {
			temp_map = (Map) jdbcTemplate.query(ConstantQuery
					.getSelect_frompp_partner_network().toString(),
					new ResultSetExtractor() {

						public Object extractData(ResultSet arg0)
								throws SQLException, DataAccessException {
							PPPartnerNetworkDataObject partnerNetworkDataObject = null;
							Map map = new HashMap<Integer, PPPartnerNetworkDataObject>();
							while (arg0.next()) {
								partnerNetworkDataObject = new PPPartnerNetworkDataObject();
								partnerNetworkDataObject.setId(arg0
										.getLong("id"));
								partnerNetworkDataObject.setApi_key(arg0
										.getString("api_key"));
								partnerNetworkDataObject.setId_key(arg0
										.getString("id_key"));
								partnerNetworkDataObject.setIs_connected(arg0
										.getShort("is_connected") == 1 ? true
										: false);
								partnerNetworkDataObject
										.setNetwork_code_id(arg0
												.getShort("network_code_id"));
								partnerNetworkDataObject
										.setPartner_domain_id(arg0
												.getInt("partner_domain_id"));
								System.out.println(" Id is::"
										+ arg0.getLong("id"));
								map.put(partnerNetworkDataObject
										.getPartner_domain_id(),
										partnerNetworkDataObject);
							}
							return map;
						}
					});
			partner_network_map = temp_map;
		} catch (DataAccessException dae) {
			logger.error("PartnerNetworkServiceImpl::init()::Exception is::"
					+ dae);
		} catch (Exception e) {
			logger.error("PartnerNetworkServiceImpl::init()::Exception is::"
					+ e);
		} finally {
			ConstantQuery
					.clearStringBuffer(ConstantQuery.select_frompp_partner_network);
		}
		logger.info(" Outside init() of PaternerNetworkServiceImpl class ");
	}

	public Map<Integer, PPPartnerNetworkDataObject> getPPPartnerNetwork() {

		return getPartner_network_map();
	}

}
