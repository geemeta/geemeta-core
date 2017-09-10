package com.geemeta.m.platform.entity.designer;

import com.geemeta.core.entity.BaseEntity;
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

    private Long extendId;
    private String code;
    private String content;

    @Col(name = "extend_id", nullable = true)
    @Title(title = "扩展信息", description = "扩展id，如对应的叶子节点id")
    public Long getExtendId() {
        return extendId;
    }

    public void setExtendId(Long extendId) {
        this.extendId = extendId;
    }

    @Col(name = "code", nullable = false)
    @Title(title = "配置编码")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Col(name = "content", nullable = false, dataType = "longText")
    @Title(title = "配置内容")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
