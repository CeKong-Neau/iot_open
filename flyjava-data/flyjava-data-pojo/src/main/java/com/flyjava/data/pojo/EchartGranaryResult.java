package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EchartGranaryResult implements Serializable{
	//x轴上的时间 格式如下
	/* "categories": [ "时间1","时间2","时间3","时间4", "时间5"]*/
	private List categories;
	//y轴上的多条记录
	/*[
	{
	"name": "Temp1",
	"type": "line",
	"stack": "总量", 
	"data": [1555,132,101,134,90,230,90]
	},
	*/
	private List data;
	
	
	public List getCategories() {
		return categories;
	}
	public void setCategories(List categories) {
		this.categories = categories;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	
	
	
	
}
