package com.geemeta.core.entity;

import com.geemeta.core.gql.meta.Entity;


/**
 * Created by hongxq on 2015/11/25.
 */
@Entity(name = "md_data_dynamic")
public class DataDynamic extends BaseEntity{

    private String name; //实体名称
    private String tableName;//database tableName
    private String idField;//实体维一主键值字段名称
    private Long idValue;//实体维一主键值字段值
    private String entity;//class，可能为空
    private String action;//创建create、修改update、删除delete、读取read
    private String description;//{xx:{from:xx,to:Pxx}}
    private String subjectName;//更新该数据的主体名称，如人名、系统名称


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public Long getIdValue() {
        return idValue;
    }

    public void setIdValue(Long idValue) {
        this.idValue = idValue;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
