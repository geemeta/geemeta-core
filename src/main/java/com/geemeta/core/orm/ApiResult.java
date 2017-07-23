package com.geemeta.core.orm;

/**
 * @author itechgee@126.com
 * @date 2017/6/3.
 */
public class ApiResult<E> {
    private String msg = "";
    private String code = ApiResultCode.SUCCESS;
    private E data;
    private Object meta;

    public ApiResult() {
    }

    public ApiResult(E data, String msg, String code) {
        setCode(code);
        setMsg(msg);
        setData(data);
    }

    public String getMsg() {
        return msg;
    }

    public ApiResult<E> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ApiResult<E> setCode(String code) {
        this.code = code;
        return this;
    }

    public E getData() {
        return data;
    }

    public ApiResult<E> setData(E data) {
        this.data = data;
        return this;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }
}
