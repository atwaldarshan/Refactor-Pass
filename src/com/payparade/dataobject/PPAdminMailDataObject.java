package com.payparade.dataobject;

public class PPAdminMailDataObject {
	private int id;
	private String from_user_id;
	private String to_user_id;
	private String subject;
	private String content;
	private String propertykey;
	private String propertyvalue;
	private String protocolname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFrom_user_id() {
		return from_user_id;
	}

	public void setFrom_user_id(String fromUserId) {
		from_user_id = fromUserId;
	}

	public String getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(String toUserId) {
		to_user_id = toUserId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPropertykey() {
		return propertykey;
	}

	public void setPropertykey(String propertykey) {
		this.propertykey = propertykey;
	}

	public String getPropertyvalue() {
		return propertyvalue;
	}

	public void setPropertyvalue(String propertyvalue) {
		this.propertyvalue = propertyvalue;
	}

	public String getProtocolname() {
		return protocolname;
	}

	public void setProtocolname(String protocolname) {
		this.protocolname = protocolname;
	}

}
