package com.payparade.dataobject;

import java.util.Date;

public class PPConnectionDataObject {
	private long id;
	private short network_code_id;
	private String[] friend_id;
	private String social_id;
	private Date current_time_stamp;

	public void memoryFriendId(int length) {
		friend_id = new String[length];
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public short getNetwork_code_id() {
		return network_code_id;
	}

	public void setNetwork_code_id(short networkCodeId) {
		network_code_id = networkCodeId;
	}

	public String[] getFriend_id() {
		return friend_id;
	}

	public void setFriend_id(String[] friendId) {
		friend_id = friendId;
	}

	public String getSocial_id() {
		return social_id;
	}

	public void setSocial_id(String socialId) {
		social_id = socialId;
	}

	public Date getCurrent_time_stamp() {
		return current_time_stamp;
	}

	public void setCurrent_time_stamp(Date currentTimeStamp) {
		current_time_stamp = currentTimeStamp;
	}
}
