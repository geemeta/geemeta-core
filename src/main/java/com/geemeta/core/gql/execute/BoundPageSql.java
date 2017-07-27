package com.geemeta.core.gql.execute;

/**
 * @author itechgee@126.com
 * @date 2017/7/13.
 */
public class BoundPageSql{

    private BoundSql boundSql;

    private String countSql;

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public void setBoundSql(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    /**
     * 统计总数的sql，如select count(id) from t1 where ...
     * @return
     */
    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }
}
