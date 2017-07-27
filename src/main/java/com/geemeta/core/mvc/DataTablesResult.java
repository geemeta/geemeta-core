package com.geemeta.core.mvc;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author itechgee@126.com
 * @date 2017/7/13.
 */
public class DataTablesResult {
    private int draw;

    private long recordsTotal;

    private long recordsFiltered;

    private Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
