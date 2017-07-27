package com.geemeta.core.biz.rules;

import com.geemeta.core.Manager;
import com.geemeta.core.template.JSTemplateManager;
import com.geemeta.core.template.JSTemplateManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.CompiledScript;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public class BizRuleManager extends Manager {

    private static Logger logger = LoggerFactory.getLogger(BizRuleManager.class);
    private Map<String, CompiledScript> compiledScriptMap = new HashMap<>();
    private JSTemplateManager JSTemplateManager = JSTemplateManagerFactory.get("rule");


    /**
     * 从文件中加载规则配置
     *
     * @param file
     * @throws IOException
     */
    @Override
    public void parseFile(File file) throws IOException {
        JSTemplateManager.parseFile(file);
    }

    @Override
    public void parseStream(InputStream is) throws IOException {
        JSTemplateManager.parseStream(is);
    }

    @Override
    public void loadDb(String sqlId) {

    }


}
