package com.geemeta.core.entity;

import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;

/**
 * @author itechgee@126.com
 * @date 2017/5/27.
 */
@Entity(name = "platform_page_config", table = "platform_page_config")
@Title(title = "页面配置")
public class PageConfig extends BaseEntity {

    private String code;
    private String content;

    @Col(name = "code", nullable = false)
    @Title(title = "配置编码")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Col(name = "content", nullable = false)
    @Title(title = "配置内容")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
