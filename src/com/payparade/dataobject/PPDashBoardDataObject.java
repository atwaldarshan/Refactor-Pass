package com.payparade.dataobject;

public class PPDashBoardDataObject {
	private String currentPeriod;
	private String subType;
	private int activity;
	private int average;
	private String public_network_name;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPublic_network_name() {
		return public_network_name;
	}

	public void setPublic_network_name(String publicNetworkName) {
		public_network_name = publicNetworkName;
	}

	public String getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(String currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}
}
