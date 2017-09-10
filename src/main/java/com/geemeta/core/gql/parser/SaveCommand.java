package com.geemeta.core.gql.parser;

import java.util.Map;

/**
 * 用于数据库insert、update操作
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
public class SaveCommand extends BaseCommand<SaveCommand> {

    private String PK;
    private Map<String, Object> valueMap;

    /**
     * 与fields同步，冗余
     * @return
     */
    public Map<String, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, Object> valueMap) {
        this.valueMap = valueMap;
    }

    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }
}
