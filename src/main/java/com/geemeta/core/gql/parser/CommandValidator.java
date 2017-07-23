package com.geemeta.core.gql.parser;

import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.meta.EntityMeta;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
public class CommandValidator {
    private MetaManager metaManager = MetaManager.singleInstance();
    private EntityMeta entityMeta = null;
    private String validateEntityName = null;
    private StringBuilder message = new StringBuilder();

    public boolean validateEntity(String entityName) {
        this.validateEntityName = entityName;
        if (!metaManager.containsEntity(entityName)) {
            message.append("不存在该实体。");
            return false;
        }
        this.entityMeta = metaManager.getByEntityName(entityName);
        return true;
    }

    public boolean validateField(String field, String fieldDescription) {
        Assert.notNull(entityMeta, "需先validateEntity，确保已有实体信息，才能进一步验证字段。");
        if (!entityMeta.containsField(field)) {
//            message.append("验证（");
//            message.append(entityMeta.getEntityName());
            message.append("[");
            message.append(fieldDescription);
            message.append("]");
//            message.append("");
            message.append("不存在");
            message.append(field);
            message.append("；");
            return false;
        }
        return true;
    }

    public boolean validateField(String[] fields, String fieldDescription) {
        Assert.notNull(fields, "待验证的字段数组不能为空。");
        boolean isFail = false;
        for (String field : fields) {
            if (!validateField(field, fieldDescription)) {
                isFail = true;
            }
        }
        return !isFail;
    }

    public boolean isSuccess() {
        return message.length() == 0;
    }

    public String getMessage() {
        if (!isSuccess() && validateEntityName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("验证实体");
            sb.append(validateEntityName);
            sb.append("：");
            sb.append(message.toString());
            return sb.toString();
        }
        return message.toString();
    }

    public void appendMessage(String message) {
        this.message.append(message);
    }

    public String getPK(){
        return  entityMeta.getId().getFieldName();
    }

    /**
     * 从fields中查询主健
     * @param fields
     * @return
     */
    public boolean hasPK(String[] fields){
        String name = entityMeta.getId().getFieldName();
        if(Arrays.asList(fields).contains(name))return true;
        return false;
    }

    /**
     * 实体是否有公共字段，如update_at
     * @param field
     * @return
     */
    public boolean hasKeyField(String field){

        return true;
    }
}
