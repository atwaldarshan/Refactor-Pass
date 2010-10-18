package com.payparade.dataobject;

public class PPPartnerServerDataObject {
	private int id;
	private String partner_host;
	private String partner_server;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPartner_host() {
		return partner_host;
	}

	public void setPartner_host(String partnerHost) {
		partner_host = partnerHost;
	}

	public String getPartner_server() {
		return partner_server;
	}

	public void setPartner_server(String partnerServer) {
		partner_server = partnerServer;
	}
}
