package com.geemeta.core.biz.rules;

import com.geemeta.core.template.sql.SqlTemplateParser;

import javax.script.*;
import java.util.HashMap;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public class BizEngine {
    protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    protected HashMap<String, BizRule> bizRules = new HashMap<>();

    /**
     * biz
     */
    private String name;


    /**
     * @param ruleScript
     */
    public void compile(String ruleScript) throws ScriptException {
        //从ruleScript中解析出fun
        //TODO
        String fun = "";

        BizRule bizRule = new BizRule();
        bizRule.setName(fun);
        bizRule.setScript(compile(fun, ruleScript));
        bizRules.put(fun, bizRule);
    }

    public Object invoke(String bizJsonText) {


        return null;
    }


    private CompiledScript compile(String funName, String jsFunText) throws ScriptException {
        ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
        return ((Compilable) engine).compile(jsFunText + ";" + funName + "(" + SqlTemplateParser.VAL_NAME + ");");
    }

}
