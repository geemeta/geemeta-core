package com.geemeta.core.gql.parser;

/**
 * @author itechgee@126.com
 */
public class QueryCommand extends BaseCommand<QueryCommand>{
    /**
     * TODO 客户端生成的唯一标识，用于缓存
     */
    private String key;
    private boolean queryForList = false;
    /**
     * @param pageNum 开始记录数 =page(客户端请求参数中)=offset(in mysql)，第一条记录从1开始，而不是从0开始
     */
    private int page = -1;
    /**
     * @param pageSize 每页记录数 =pageSize(客户端请求参数中)=limit(in mysql)
     */
    private int size = -1;

    private String groupBy;
    private String orderBy;
    private FilterGroup having;

    public QueryCommand(){
        setCommandType(CommandType.Query);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 默认值为false
     */
    public boolean isQueryForList() {
        return queryForList;
    }

    public void setQueryForList(boolean queryForList) {
        this.queryForList = queryForList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        //orderBy中的列，应该出现在group by子句中
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public FilterGroup getHaving() {
        return having;
    }

    public void setHaving(FilterGroup having) {
        this.having = having;
    }

}
