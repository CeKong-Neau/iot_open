package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.List;

public class GranaryTables implements Serializable{
        
	private List<DataTables> data;

	public List<DataTables> getData() {
		return data;
	}

	public void setData(List<DataTables> data) {
		this.data = data;
	}
}
