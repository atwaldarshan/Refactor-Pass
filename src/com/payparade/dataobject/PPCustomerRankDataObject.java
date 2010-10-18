package com.payparade.dataobject;

import java.sql.Date;

public class PPCustomerRankDataObject {

	private int id;
	private int pp_id;
	private int partner_domain_id;
	private short network_code_id;
	private String network_member_social_id;
	private Date measurement_dt;
	private Date last_seen;
	private int brand_affinity;
	private int influence;
	private int friends;
	private int latitude;
	private int longitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPp_id() {
		return pp_id;
	}

	public void setPp_id(int ppId) {
		pp_id = ppId;
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

	public String getNetwork_member_social_id() {
		return network_member_social_id;
	}

	public void setNetwork_member_social_id(String networkMemberSocialId) {
		network_member_social_id = networkMemberSocialId;
	}

	public Date getMeasurement_dt() {
		return measurement_dt;
	}

	public void setMeasurement_dt(Date measurementDt) {
		measurement_dt = measurementDt;
	}

	public Date getLast_seen() {
		return last_seen;
	}

	public void setLast_seen(Date lastSeen) {
		last_seen = lastSeen;
	}

	public int getBrand_affinity() {
		return brand_affinity;
	}

	public void setBrand_affinity(int brandAffinity) {
		brand_affinity = brandAffinity;
	}

	public int getInfluence() {
		return influence;
	}

	public void setInfluence(int influence) {
		this.influence = influence;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

}
