package com.geemeta.core.biz.rules;


import org.apache.commons.codec.language.bm.Rule;

import javax.script.CompiledScript;

/**
 * 一规则对应一function
 *
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public class BizRule {

    private String name;
    private CompiledScript script;
    private Rule rule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompiledScript getScript() {
        return script;
    }

    public void setScript(CompiledScript script) {
        this.script = script;
    }
}
