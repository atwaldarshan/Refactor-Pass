package com.payparade.dataobject;

import java.sql.Date;

public class PPOpsSnapShotDataObject {

	private int id;
	private Date current_time_stamp;
	private int num_of_users;
	private int max_connections;
	private int num_of_request;
	private int max_milis;
	private int total_milis;
	private double average_milis;
	private long used_memeory;
	private long free_memory;
	private double max_users;
	private double average_users;

	public double getAverage_milis() {
		return average_milis;
	}

	public void setAverage_milis(double averageMilis) {
		average_milis = averageMilis;
	}

	public double getMax_users() {
		return max_users;
	}

	public void setMax_users(double maxUsers) {
		max_users = maxUsers;
	}

	public double getAverage_users() {
		return average_users;
	}

	public void setAverage_users(double averageUsers) {
		average_users = averageUsers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCurrent_time_stamp() {
		return current_time_stamp;
	}

	public void setCurrent_time_stamp(Date currentTimeStamp) {
		current_time_stamp = currentTimeStamp;
	}

	public int getNum_of_users() {
		return num_of_users;
	}

	public void setNum_of_users(int numOfUsers) {
		num_of_users = numOfUsers;
	}

	public int getMax_connections() {
		return max_connections;
	}

	public void setMax_connections(int maxConnections) {
		max_connections = maxConnections;
	}

	public int getNum_of_request() {
		return num_of_request;
	}

	public void setNum_of_request(int numOfRequest) {
		num_of_request = numOfRequest;
	}

	public int getMax_milis() {
		return max_milis;
	}

	public void setMax_milis(int maxMilis) {
		max_milis = maxMilis;
	}

	public int getTotal_milis() {
		return total_milis;
	}

	public void setTotal_milis(int totalMilis) {
		total_milis = totalMilis;
	}

	public long getUsed_memeory() {
		return used_memeory;
	}

	public void setUsed_memeory(long usedMemeory) {
		used_memeory = usedMemeory;
	}

	public long getFree_memory() {
		return free_memory;
	}

	public void setFree_memory(long freeMemory) {
		free_memory = freeMemory;
	}

}
