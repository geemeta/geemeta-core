package com.geemeta.m.platform.entity.designer;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;

/**
 * @author itechgee@126.com
 * @date 2017/9/8.
 */
@Entity(name = "platform_tree_node", table = "platform_tree_node")
@Title(title = "树节点")
public class TreeNode extends BaseEntity {

    private Long treeId;
    private Long parentId;
    private String type;
    private String text;
    private String icon;
    private Long extendId;
    private String meta;

    @Col(name = "tree_id", nullable = false)
    @Title(title = "节点所属树Id")
    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

    @Col(name = "type", nullable = false)
    @Title(title = "节点类型")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Col(name = "text", nullable = false)
    @Title(title = "节点名称")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Col(name = "icon", nullable = true)
    @Title(title = "节点图标")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Col(name = "pid", nullable = true)
    @Title(title = "父节点Id")
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Col(name = "extend_id", nullable = true)
    @Title(title = "扩展信息", description = "扩展id，如叶子节点对应的文件id")
    public Long getExtendId() {
        return extendId;
    }

    public void setExtendId(Long extendId) {
        this.extendId = extendId;
    }

    @Col(name = "meta", nullable = true)
    @Title(title = "节点扩展元信息", description = "更多的扩展信息，json格式字符串")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
