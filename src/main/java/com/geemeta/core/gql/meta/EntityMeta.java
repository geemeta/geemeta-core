package com.geemeta.core.gql.meta;


import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/5/21.
 */
public class EntityMeta {

    private String entityName;
    private Class entityType;
    private FieldMeta id;
    private TableMeta tableMeta;
    private Collection<FieldMeta> fieldMetas;
    private String[] fieldNames;
    private Map<String, DictDataSource> dictDataSourceMap;
    //冗余，用于快速获取列信息
    private Map<String, FieldMeta> fieldMetaMap;
    //不更新的字段
    private Map<String, Boolean> ignoreUpdateFieldMap;

    public EntityMeta() {
        ignoreUpdateFieldMap = new HashMap<>();
        ignoreUpdateFieldMap.put("create_at", true);
        ignoreUpdateFieldMap.put("creator", true);
    }

    /**
     * 对于基于java类解析的实体，则返回类名（不包括包名）
     * 对于基于页面配置的实体，则返回配置的实体名称
     *
     * @return
     */
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * 对于基于java类解析的实体，则有具体的类类型
     * 对于基于页面配置的实体，则返回值为空
     *
     * @return
     */
    public Class getEntityType() {
        return entityType;
    }

    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }

    /**
     * 基于@Id获取实体中的主键字段名
     *
     * @return
     */
    public FieldMeta getId() {
        return id;
    }

    public void setId(FieldMeta id) {
        this.id = id;
        ignoreUpdateFieldMap.put(id.getFieldName(), true);
    }

    public TableMeta getTableMeta() {
        return tableMeta;
    }

    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    public Collection<FieldMeta> getFieldMetas() {
        return fieldMetas;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public void setFieldMetas(Collection<FieldMeta> fieldMetas) {
        if (fieldMetas == null) return;
        this.fieldMetas = fieldMetas;
        this.fieldNames = new String[fieldMetas.size()];
        int i = 0;
        for (FieldMeta fm : fieldMetas) {
            if (fieldMetaMap == null) fieldMetaMap = new HashMap<>(fieldMetas.size());
            fieldMetaMap.put(fm.getFieldName(), fm);
            this.fieldNames[i++] = fm.getFieldName();
        }
    }

    public Map<String, DictDataSource> getDictDataSourceMap() {
        return dictDataSourceMap;
    }

    public void setDictDataSourceMap(Map<String, DictDataSource> dictDataSourceMap) {
        this.dictDataSourceMap = dictDataSourceMap;
    }

    public String getTableName() {
        return tableMeta.getTableName();
    }

    public String getColumnName(String fieldName) {
        FieldMeta fm = getFieldMeta(fieldName);
        Assert.notNull(fm, "获取不到元数据，fieldName：" + fieldName);
        return getFieldMeta(fieldName).getColumnName();
    }

    public FieldMeta getFieldMeta(String fieldName) {
        return fieldMetaMap.get(fieldName);
    }

    public boolean containsField(String fieldName) {
        return fieldMetaMap.containsKey(fieldName);
    }

    public FieldMeta getFieldMetaByColumn(String columnName) {
        if (this.fieldMetas == null || this.fieldMetas.size() == 0) return null;
        for (FieldMeta fm : fieldMetas) {
            if (fm.getColumnName().equals(columnName)) return fm;
        }
        return null;
    }

    public boolean isIgnoreUpdateField(String field) {
        return ignoreUpdateFieldMap.containsKey(field);
    }
}
