package com.geemeta.core.gql.parser;

import java.util.Map;

/**
 * 用于数据库insert、update操作
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
public class DeleteCommand extends BaseCommand<DeleteCommand> {

    public DeleteCommand(){
        setCommandType(CommandType.Delete);
    }

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


}
