package com.geemeta.core.template;

import com.geemeta.core.Manager;
import com.geemeta.core.template.js.JsProvider;
import com.geemeta.core.template.js.JsTemplateParser;
import com.geemeta.core.template.sql.SqlTemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * 管理动态sql文件，解析并提供按sqlId、参数构建sql语句
 * 加载资源目录下的sql文件
 * TODO 加载数据库表中配置的sql
 * TODO 分析哪些是静态的语句块，解析成静态脚本，以便采用prepareStatement，提高sql执行性能
 *
 * @author itechgee@126.com
 * @date 2017/5/30.
 */
public class JSTemplateManager extends Manager {
    private Logger logger = LoggerFactory.getLogger(JSTemplateManager.class);

    private JsTemplateParser jsTemplateParser = new JsTemplateParser();
    private JsProvider jsProvider = new JsProvider();


    /**
     * 解析*.js、*.sql的文件，支持两种格式
     *
     * @param file
     */
    @Override
    public void parseFile(File file) throws IOException {
        compileJs(jsTemplateParser.parse(Files.readAllLines(Paths.get(file.getPath()))));
    }

    public void parseStream(InputStream is) throws IOException {
        compileJs(jsTemplateParser.parse(readLines(is)));
    }

    private void compileJs(Map<String, String> jsFuncMap) {
        try {
            jsProvider.compile(jsFuncMap);
        } catch (ScriptException e) {
            logger.error("", e);
        }
    }


    /**
     * @param id       sqlId or functionName
     * @param paramMap key value(key value)，值Object为key value的对象或字符串、数字等基本类型
     * @return
     * @throws ScriptException
     */
    public <T> T generate(String id, Map<String, Object> paramMap) {
        if (jsProvider.contain(id))
            try {
                T sql = jsProvider.generate(id, paramMap);
                if (logger.isInfoEnabled()) {
                    logger.info("sql {} : {}", id, sql);
                }
                return sql;
            } catch (ScriptException e) {
                logger.error("sql脚本构建失败。", e);
                return null;
            }
        else {
            Assert.isTrue(false, "未找到sqlId：" + id + "，对应的语句。");
            return null;
        }
    }

//    @Override
//    public void loadFiles(String path) {
//
//    }

    @Override
    public void loadDb(String sqlId) {

    }
}
