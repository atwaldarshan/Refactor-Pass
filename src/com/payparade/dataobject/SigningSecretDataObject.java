package com.payparade.dataobject;

public class SigningSecretDataObject {

	private int id;
	private String sitename;
	private String signing_secret;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getSigning_secret() {
		return signing_secret;
	}

	public void setSigning_secret(String signingSecret) {
		signing_secret = signingSecret;
	}

}
