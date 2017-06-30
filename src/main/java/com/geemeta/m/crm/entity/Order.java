package com.geemeta.m.crm.entity;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Entity;

/**
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
@Entity(name = "crm_order")
public class Order extends BaseEntity {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
