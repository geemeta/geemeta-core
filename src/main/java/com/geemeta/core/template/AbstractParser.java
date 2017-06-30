package com.geemeta.core.template;

import java.util.List;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/1.
 */
public abstract class AbstractParser<E extends AbstractTemplateLexer> {

    protected E lexer;

    /**
     * @param lines toParse lines
     * @return key:sqlId or functionName,value:jsFunctionText
     */
    public abstract Map<String, String> parse(List<String> lines);

    public abstract Class<E> getLexerType();

    protected <T extends E> T getLexer() {
        if (lexer == null)
            try {
                lexer = getLexerType().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        return (T) lexer;
    }
}
