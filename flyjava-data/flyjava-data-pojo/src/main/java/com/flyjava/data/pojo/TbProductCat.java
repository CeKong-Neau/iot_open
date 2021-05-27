package com.flyjava.data.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbProductCat implements Serializable{
    private Integer productCatId;

    private String name;

    private Byte status;

    private Date created;

    private Date updated;

    public Integer getProductCatId() {
        return productCatId;
    }

    public void setProductCatId(Integer productCatId) {
        this.productCatId = productCatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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