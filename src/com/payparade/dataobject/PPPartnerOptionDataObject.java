package com.payparade.dataobject;

public class PPPartnerOptionDataObject {

	private int id;
	private int partner_domain_id;
	private String css_file;
	private String logo_file;
	private String badge_salutation;
	private String badge_message;
	private String link_domain;
	private String connect_style;
	private String connect_text;
	private short first_name_only;
	private String master_admin;
	private String salutation_style;
	private String message_style;
	private String display_name;

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

	public String getCss_file() {
		return css_file;
	}

	public void setCss_file(String cssFile) {
		css_file = cssFile;
	}

	public String getLogo_file() {
		return logo_file;
	}

	public void setLogo_file(String logoFile) {
		logo_file = logoFile;
	}

	public String getBadge_salutation() {
		return badge_salutation;
	}

	public void setBadge_salutation(String badgeSalutation) {
		badge_salutation = badgeSalutation;
	}

	public String getBadge_message() {
		return badge_message;
	}

	public void setBadge_message(String badgeMessage) {
		badge_message = badgeMessage;
	}

	public String getLink_domain() {
		return link_domain;
	}

	public void setLink_domain(String linkDomain) {
		link_domain = linkDomain;
	}

	public String getConnect_style() {
		return connect_style;
	}

	public void setConnect_style(String connectStyle) {
		connect_style = connectStyle;
	}

	public String getConnect_text() {
		return connect_text;
	}

	public void setConnect_text(String connectText) {
		connect_text = connectText;
	}

	public short getFirst_name_only() {
		return first_name_only;
	}

	public void setFirst_name_only(short firstNameOnly) {
		first_name_only = firstNameOnly;
	}

	public String getMaster_admin() {
		return master_admin;
	}

	public void setMaster_admin(String masterAdmin) {
		master_admin = masterAdmin;
	}

	public String getSalutation_style() {
		return salutation_style;
	}

	public void setSalutation_style(String salutationStyle) {
		salutation_style = salutationStyle;
	}

	public String getMessage_style() {
		return message_style;
	}

	public void setMessage_style(String messageStyle) {
		message_style = messageStyle;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String displayName) {
		display_name = displayName;
	}

}
