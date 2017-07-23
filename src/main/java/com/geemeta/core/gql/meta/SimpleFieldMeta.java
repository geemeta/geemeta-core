package com.geemeta.core.gql.meta;

import java.io.Serializable;

/**
 * 用于对外发布元数据服务信息，去掉数据库的部分元数据信息，如表名等，详细的字段元数据参见@see FieldMeta
 * @author itechgee@126.com
 * @date 2017/7/6.
 */
public class SimpleFieldMeta implements Serializable {
    private String name;
    private String type;
    private String title;
    private String comment;
    private boolean nullable;
    private int charMaxLength;
    private int precision;
    private int scale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getCharMaxLength() {
        return charMaxLength;
    }

    public void setCharMaxLength(int charMaxLength) {
        this.charMaxLength = charMaxLength;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
