package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.List;

public class EchartSearchGranaryResult implements Serializable{
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
	
	
	private List temperatureData;
	private List humidityData;
	
	
	public List getCategories() {
		return categories;
	}
	public void setCategories(List categories) {
		this.categories = categories;
	}
	public List getTemperatureData() {
		return temperatureData;
	}
	public void setTemperatureData(List temperatureData) {
		this.temperatureData = temperatureData;
	}
	public List getHumidityData() {
		return humidityData;
	}
	public void setHumidityData(List humidityData) {
		this.humidityData = humidityData;
	}
	
	
	
	
	
	
}
