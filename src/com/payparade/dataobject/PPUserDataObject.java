package com.payparade.dataobject;

import java.sql.Date;

public class PPUserDataObject {

	private int id;
	private String useremail;
	private String password_hash;
	private Date first_seen;
	private short metrics_access;
	private short config_access;
	private short admin_access;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String passwordHash) {
		password_hash = passwordHash;
	}

	public Date getFirst_seen() {
		return first_seen;
	}

	public void setFirst_seen(Date firstSeen) {
		first_seen = firstSeen;
	}

	public short getMetrics_access() {
		return metrics_access;
	}

	public void setMetrics_access(short metricsAccess) {
		metrics_access = metricsAccess;
	}

	public short getConfig_access() {
		return config_access;
	}

	public void setConfig_access(short configAccess) {
		config_access = configAccess;
	}

	public short getAdmin_access() {
		return admin_access;
	}

	public void setAdmin_access(short adminAccess) {
		admin_access = adminAccess;
	}

}
