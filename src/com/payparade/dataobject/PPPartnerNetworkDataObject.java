package com.payparade.dataobject;

import java.sql.Date;

public class PPPartnerNetworkDataObject {

	private long id;
	private int partner_domain_id;
	private short network_code_id;
	private String id_key;
	private String api_key;
	private String secret;
	private boolean is_connected;
	private Date connected_time_stamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPartner_domain_id() {
		return partner_domain_id;
	}

	public void setPartner_domain_id(int partnerDomainId) {
		partner_domain_id = partnerDomainId;
	}

	public short getNetwork_code_id() {
		return network_code_id;
	}

	public void setNetwork_code_id(short networkCodeId) {
		network_code_id = networkCodeId;
	}

	public String getId_key() {
		return id_key;
	}

	public void setId_key(String idKey) {
		id_key = idKey;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String apiKey) {
		api_key = apiKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public boolean isIs_connected() {
		return is_connected;
	}

	public void setIs_connected(boolean isConnected) {
		is_connected = isConnected;
	}

	public Date getConnected_time_stamp() {
		return connected_time_stamp;
	}

	public void setConnected_time_stamp(Date connectedTimeStamp) {
		connected_time_stamp = connectedTimeStamp;
	}

}
