package com.geemeta.m.platform.entity;

import com.geemeta.core.entity.BaseEntity;
import com.geemeta.core.gql.meta.Col;
import com.geemeta.core.gql.meta.Entity;
import com.geemeta.core.gql.meta.Title;


/**
 * Created by hongxueqian on 14-5-2.
 */
@Entity(name = "sys_app")
@Title(title = "应用")
public class App extends BaseEntity {
    private String name;
    private String code;
    private int seq;
    private String href;
    private String icon;
    private String menu;
    private String dependAppCode;
    private String description;

    public App() {
    }

    public App(Long id) {
        this.setId(id);
    }

    @Col(name = "name",unique = true)
    @Title(title = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Col(name = "code",unique = true)
    @Title(title = "编码")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Title(title = "首页链接",description = "加载模块之后打开的首页面")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Title(title = "图标")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    @Title(title = "次序")
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Col(name="menu",dataType = "json")
    @Title(title="菜单",description = "JSON格式，例如：[{\n" +
            "                \"text\": \"系统配置\", \"expanded\": true, \"items\": [\n" +
            "                    {\"text\": \"个人信息\", \"linkTo\": \"\\#/m/sys/profile\"},\n" +
            "                    {\"text\": \"应用管理\", \"linkTo\": \"\\#/m/sys/app/index\"}\n" +
            "                ]\n" +
            "            }]")
    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Col(name = "depend_app_code")
    @Title(title = "依赖的应用",description = "依赖的应用模块编码，可多个，格式如：dev,sys")
    public String getDependAppCode() {
        return dependAppCode;
    }

    public void setDependAppCode(String dependAppCode) {
        this.dependAppCode = dependAppCode;
    }

    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
