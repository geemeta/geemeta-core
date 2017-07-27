package com.geemeta.core.orm;

/**
 * @author itechgee@126.com
 * @date 2017/7/13.
 */
public class PageApiResult<E> extends ApiResult {
    private long total;
    private long page;
    private int size;
    private int dataSize;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
}
