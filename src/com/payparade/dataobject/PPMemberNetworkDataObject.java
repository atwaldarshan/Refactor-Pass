package com.payparade.dataobject;

public class PPMemberNetworkDataObject {

	private int id;
	private int memeber_id;
	private int network_code_id;
	private String member_social_network_id;
	private short is_connected;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemeber_id() {
		return memeber_id;
	}

	public void setMemeber_id(int memeberId) {
		memeber_id = memeberId;
	}

	public int getNetwork_code_id() {
		return network_code_id;
	}

	public void setNetwork_code_id(int networkCodeId) {
		network_code_id = networkCodeId;
	}

	public String getMember_social_network_id() {
		return member_social_network_id;
	}

	public void setMember_social_network_id(String memberSocialNetworkId) {
		member_social_network_id = memberSocialNetworkId;
	}

	public short getIs_connected() {
		return is_connected;
	}

	public void setIs_connected(short isConnected) {
		is_connected = isConnected;
	}

}
