package com.payparade.dataobject;

import java.sql.Date;

public class PPMemberSessionDataObject {
	private int id;
	private String member_social_id;
	private String memeber_session_id;
	private String browser_id;
	private Date current_time_stamp;
	private int network_code_id;
	private String reference_url;
	private int num_of_friends;
	private String user_ip;
	private int pp_member_id;
	private int pp_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMember_social_id() {
		return member_social_id;
	}

	public void setMember_social_id(String memberSocialId) {
		member_social_id = memberSocialId;
	}

	public String getMemeber_session_id() {
		return memeber_session_id;
	}

	public void setMemeber_session_id(String memeberSessionId) {
		memeber_session_id = memeberSessionId;
	}

	public String getBrowser_id() {
		return browser_id;
	}

	public void setBrowser_id(String browserId) {
		browser_id = browserId;
	}

	public Date getCurrent_time_stamp() {
		return current_time_stamp;
	}

	public void setCurrent_time_stamp(Date currentTimeStamp) {
		current_time_stamp = currentTimeStamp;
	}

	public int getNetwork_code_id() {
		return network_code_id;
	}

	public void setNetwork_code_id(int networkCodeId) {
		network_code_id = networkCodeId;
	}

	public String getReference_url() {
		return reference_url;
	}

	public void setReference_url(String referenceUrl) {
		reference_url = referenceUrl;
	}

	public int getNum_of_friends() {
		return num_of_friends;
	}

	public void setNum_of_friends(int numOfFriends) {
		num_of_friends = numOfFriends;
	}

	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String userIp) {
		user_ip = userIp;
	}

	public int getPp_member_id() {
		return pp_member_id;
	}

	public void setPp_member_id(int ppMemberId) {
		pp_member_id = ppMemberId;
	}

	public int getPp_id() {
		return pp_id;
	}

	public void setPp_id(int ppId) {
		pp_id = ppId;
	}
}
