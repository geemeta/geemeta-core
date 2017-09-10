package com.geemeta.m.platform.entity.designer;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;

/**
 * @author itechgee@126.com
 * @date 2017/9/8.
 */
@Entity(name = "platform_dev_project", table = "platform_dev_project")
@Title(title = "开发项目")
public class DevProject extends BaseEntity {

    private String name;
    private String tree;

    @Col(name = "name", nullable = false)
    @Title(title = "项目名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Col(name = "tree", nullable = false, dataType = "longText")
    @Title(title = "文件树", description = "json字符串，如jsTree")
    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

}
