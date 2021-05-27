package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbData  implements Serializable{
    private Long id;

    private Long productId;

    private Integer productCatId;

    private String data;

    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getProductCatId() {
        return productCatId;
    }

    public void setProductCatId(Integer productCatId) {
        this.productCatId = productCatId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}