package com.payparade.dataobject;

import java.sql.Date;

public class PPMemberShipDataObject {
	private int id;
	private int partner_domain_id;
	private int memeber_id;
	private String partners_social_key;
	private Date connected_time_stamp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPartner_domain_id() {
		return partner_domain_id;
	}

	public void setPartner_domain_id(int partnerDomainId) {
		partner_domain_id = partnerDomainId;
	}

	public int getMemeber_id() {
		return memeber_id;
	}

	public void setMemeber_id(int memeberId) {
		memeber_id = memeberId;
	}

	public String getPartners_social_key() {
		return partners_social_key;
	}

	public void setPartners_social_key(String partnersSocialKey) {
		partners_social_key = partnersSocialKey;
	}

	public Date getConnected_time_stamp() {
		return connected_time_stamp;
	}

	public void setConnected_time_stamp(Date connectedTimeStamp) {
		connected_time_stamp = connectedTimeStamp;
	}

}
