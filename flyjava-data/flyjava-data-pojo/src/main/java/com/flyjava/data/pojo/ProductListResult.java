package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.List;

public class ProductListResult implements Serializable {

	//存放每次 安装一定规则(每页显示10条记录,查询第2页)查询的所有pojo 集合
	private List<Product> productList;
	//查询记录总数
	private long recordCount;
	//按照每页显示条数 一共可以显示多少页
	private long pageCount;
	
	
	
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	
	
}
