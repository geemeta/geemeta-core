package com.geemeta.core.gql.meta;

import java.io.Serializable;

/**
 * @author itechgee@126.com
 * @date 14-3-23.
 */
public class FieldMeta implements Serializable{
    private ColumnMeta columnMeta;
    private String fieldName;
    //@TODO java类中的类型？
    private Class fieldType;

    public FieldMeta(String columnName, String fieldName, String title) {
        columnMeta = new ColumnMeta();
        columnMeta.setName(columnName);
        columnMeta.setTitle(title);
        this.fieldName = fieldName;
    }

    public ColumnMeta getColumn() {
        return columnMeta;
    }

    public String getColumnName() {
        return columnMeta.getName();
    }

    public void setColumnName(String columnName) {
        this.columnMeta.setName(columnName);
    }

    /**
     * 驼峰式
     * columnMeta.getName()是数据库中的字段格式
     * @return
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }

    public String getTitle() {
        return this.getColumn().getTitle();
    }

    public void setTitle(String title) {
        this.getColumn().setTitle(title);
    }



    /**
     * 列名、字段名是否一致
     * 如果columnName或fieldName为空，则返回false
     *
     * @return
     */
    public boolean isEquals() {
        if (this.getColumn().getName() == null || fieldName == null)
            return false;
        return this.getColumn().getName().equals(fieldName);
    }
}
