package com.payparade.dataobject;

import java.util.Date;

public class PPMemberActivityDataObject {
	private int id;
	private String memberSocialNetworkId;
	private Date lastTimeStamp;
	private String memberSessionId;
	private int networkCodeId;
	private int activityCodeId;
	private String pageUrl;
	private String targetUrl;
	private int partnerDomainId;
	private int pp_id;
	private int idx;
	private int con;
	private String dt;
	private int opp;
	private float Conv;

	public int getCon() {
		return con;
	}

	public void setCon(int con) {
		this.con = con;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public int getOpp() {
		return opp;
	}

	public void setOpp(int opp) {
		this.opp = opp;
	}

	public float getConv() {
		return Conv;
	}

	public void setConv(float conv) {
		Conv = conv;
	}

	private Object general_variable;

	public Object getGeneral_variable() {
		return general_variable;
	}

	public void setGeneral_variable(Object generalVariable) {
		general_variable = generalVariable;
	}

	public int getPp_id() {
		return pp_id;
	}

	public void setPp_id(int ppId) {
		pp_id = ppId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMemberSocialNetworkId() {
		return memberSocialNetworkId;
	}

	public void setMemberSocialNetworkId(String memberSocialNetworkId) {
		this.memberSocialNetworkId = memberSocialNetworkId;
	}

	public Date getLastTimeStamp() {
		return lastTimeStamp;
	}

	public void setLastTimeStamp(Date lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
	}

	public String getMemberSessionId() {
		return memberSessionId;
	}

	public void setMemberSessionId(String memberSessionId) {
		this.memberSessionId = memberSessionId;
	}

	public int getNetworkCodeId() {
		return networkCodeId;
	}

	public void setNetworkCodeId(int networkCodeId) {
		this.networkCodeId = networkCodeId;
	}

	public int getActivityCodeId() {
		return activityCodeId;
	}

	public void setActivityCodeId(int activityCodeId) {
		this.activityCodeId = activityCodeId;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public int getPartnerDomainId() {
		return partnerDomainId;
	}

	public void setPartnerDomainId(int partnerDomainId) {
		this.partnerDomainId = partnerDomainId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}
}
