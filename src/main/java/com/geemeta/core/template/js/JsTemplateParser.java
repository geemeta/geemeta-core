package com.geemeta.core.template.js;

import com.geemeta.core.template.AbstractParser;
import com.geemeta.core.template.TemplateStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/1.
 */
public class JsTemplateParser extends AbstractParser<JsTemplateLexer> {

    private static Logger logger = LoggerFactory.getLogger(JsTemplateParser.class);
    private JsTemplateLexer lexer = new JsTemplateLexer();

    /**
     * @param lines javascript文件的行列表
     */
    @Override
    public Map<String, String> parse(List<String> lines) {
        HashMap<String, String> map = new HashMap<>();
        for (TemplateStatement ts : getLexer().lex(lines)) {
            map.put(ts.getId(), ts.getContentString());
            if (logger.isDebugEnabled()) {
                logger.debug(ts.getContentString());
            }
        }
        return map;
    }

    @Override
    public Class<JsTemplateLexer> getLexerType() {
        return JsTemplateLexer.class;
    }

}
