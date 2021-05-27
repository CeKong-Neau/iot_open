package com.flyjava.data.pojo;

import java.io.Serializable;

public class Location implements Serializable {
	//经度
	private Double longitude;
	//纬度
	private Double latitude;
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
