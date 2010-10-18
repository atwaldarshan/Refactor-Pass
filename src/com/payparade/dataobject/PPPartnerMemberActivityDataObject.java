package com.payparade.dataobject;

public class PPPartnerMemberActivityDataObject {

	private int partner_domain_id;
	private String network_code;
	private int member_social_network_id;
	private String timestamp;
	private String activity_code;
	private String page_url;
	private String targer_url;
	private int pp_id;
	private int activity_count;

	public int getActivity_count() {
		return activity_count;
	}

	public void setActivity_count(int activityCount) {
		activity_count = activityCount;
	}

	public int getPartner_domain_id() {
		return partner_domain_id;
	}

	public void setPartner_domain_id(int partnerDomainId) {
		partner_domain_id = partnerDomainId;
	}

	public int getMember_social_network_id() {
		return member_social_network_id;
	}

	public void setMember_social_network_id(int memberSocialNetworkId) {
		member_social_network_id = memberSocialNetworkId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNetwork_code() {
		return network_code;
	}

	public void setNetwork_code(String networkCode) {
		network_code = networkCode;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activityCode) {
		activity_code = activityCode;
	}

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String pageUrl) {
		page_url = pageUrl;
	}

	public String getTarger_url() {
		return targer_url;
	}

	public void setTarger_url(String targerUrl) {
		targer_url = targerUrl;
	}

	public int getPp_id() {
		return pp_id;
	}

	public void setPp_id(int ppId) {
		pp_id = ppId;
	}

}
