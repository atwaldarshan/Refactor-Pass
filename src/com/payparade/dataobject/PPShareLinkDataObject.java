package com.payparade.dataobject;

import java.sql.Date;

public class PPShareLinkDataObject {
	private int id;
	private int pp_id;
	private String share_social_id;
	private int network_code_id;
	private String shared_url;
	private String shared_title;
	private String shared_content;
	private String shared_image;
	private int partner_domain_id;
	private int click;

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	private Date current_time_stamp;

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

	

	public String getShare_social_id() {
		return share_social_id;
	}

	public void setShare_social_id(String shareSocialId) {
		share_social_id = shareSocialId;
	}

	public int getNetwork_code_id() {
		return network_code_id;
	}

	public void setNetwork_code_id(int networkCodeId) {
		network_code_id = networkCodeId;
	}

	public String getShared_url() {
		return shared_url;
	}

	public void setShared_url(String sharedUrl) {
		shared_url = sharedUrl;
	}

	public String getShared_title() {
		return shared_title;
	}

	public void setShared_title(String sharedTitle) {
		shared_title = sharedTitle;
	}

	public String getShared_content() {
		return shared_content;
	}

	public void setShared_content(String sharedContent) {
		shared_content = sharedContent;
	}

	public String getShared_image() {
		return shared_image;
	}

	public void setShared_image(String sharedImage) {
		shared_image = sharedImage;
	}

	public int getPartner_domain_id() {
		return partner_domain_id;
	}

	public void setPartner_domain_id(int partnerDomainId) {
		partner_domain_id = partnerDomainId;
	}

	public Date getCurrent_time_stamp() {
		return current_time_stamp;
	}

	public void setCurrent_time_stamp(Date currentTimeStamp) {
		current_time_stamp = currentTimeStamp;
	}

}
