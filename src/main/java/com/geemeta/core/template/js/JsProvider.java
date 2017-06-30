package com.geemeta.core.template.js;

import com.geemeta.core.template.sql.SqlTemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 先compile之后再调用generate
 *
 * @author itechgee@126.com
 * @date 2017/6/1.
 */
public class JsProvider {

    private static Logger logger = LoggerFactory.getLogger(JsProvider.class);
    protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private Map<String, CompiledScript> compiledScriptMap = new HashMap<>();


    public JsProvider() {
    }

    /**
     * 预解析script，若有多个script可分别执行一次，并组合所有的script，以sqlId作为唯一键
     *
     * @param jsFuncMap
     * @throws ScriptException
     */
    public void compile(Map<String, String> jsFuncMap) throws ScriptException {
        if (jsFuncMap == null) return;
        for (Map.Entry<String, String> entry : jsFuncMap.entrySet()) {
            if (compiledScriptMap.containsKey(entry.getKey())) {
                logger.error("存在同名称key：{},不进行解析！待解析内容：{}", entry.getKey(), entry.getValue());
            } else {
                compiledScriptMap.put(entry.getKey(), compile(entry.getKey(), entry.getValue()));
            }
        }
    }

    /**
     * @param funcName
     * @param scriptText 一个javascript function脚本，不支持多个
     * @return
     * @throws ScriptException
     */
    public CompiledScript compile(String funcName, String scriptText) throws ScriptException {
        if (logger.isInfoEnabled()) {
            logger.info("将*.sql文件中的语句转换成javascript脚本，每个语名片段对应一个function");
            logger.info("即sqlId：{} ，内容为:\r\n{}", funcName, scriptText);

        }
        ScriptEngine engine = scriptEngineManager.getEngineByName("javascript");
        //TODO 在解析时，将function中的参数名解析出来，在此构建时，则只可以默认构建调用方法
        return ((Compilable) engine).compile(scriptText + ";" + funcName + "(" + SqlTemplateParser.VAL_NAME + ");");
    }

    public boolean contain(String funcName) {
        return compiledScriptMap.containsKey(funcName);
    }

    public <T> T generate(String funcName, Map<String, Object> paramMap) throws ScriptException {
        SimpleBindings simpleBindings = new SimpleBindings();
        simpleBindings.put(SqlTemplateParser.VAL_NAME, paramMap);
        return (T) compiledScriptMap.get(funcName).eval(simpleBindings);
    }

    public String generate(String sqlId, SimpleBindings paramMap) throws ScriptException {
        return (String) compiledScriptMap.get(sqlId).eval(paramMap);
    }


}
