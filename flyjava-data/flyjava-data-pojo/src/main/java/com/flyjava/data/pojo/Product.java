package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable{
	    private Long productId;

	    private Long hostId;

	    private String productName;

	    private Byte status;

	    private Date created;

	    private Date updated;
	    
	    private int productCatId;
	    



		public int getProductCatId() {
			return productCatId;
		}

		public void setProductCatId(int productCatId) {
			this.productCatId = productCatId;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public Long getHostId() {
			return hostId;
		}

		public void setHostId(Long hostId) {
			this.hostId = hostId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public Byte getStatus() {
			return status;
		}

		public void setStatus(Byte status) {
			this.status = status;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}

		public Date getUpdated() {
			return updated;
		}

		public void setUpdated(Date updated) {
			this.updated = updated;
		}

		
	    

}
