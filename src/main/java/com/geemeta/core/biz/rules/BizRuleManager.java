package com.geemeta.core.biz.rules;

import com.geemeta.core.Manager;
import com.geemeta.core.template.TemplateManager;
import com.geemeta.core.template.TemplateManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.CompiledScript;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public class BizRuleManager extends Manager {

    private static Logger logger = LoggerFactory.getLogger(BizRuleManager.class);
    private Map<String, CompiledScript> compiledScriptMap = new HashMap<>();
    private TemplateManager templateManager = TemplateManagerFactory.get("rule");


    /**
     * 从文件中加载规则配置
     * @param file
     * @throws IOException
     */
    @Override
    public void parseFile(File file) throws IOException {
        templateManager.parseFile(file);
    }

    @Override
    public void loadDb(String sqlId) {

    }


}
