package com.geemeta.core.template.js;

import com.geemeta.core.template.AbstractTemplateLexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public class JsTemplateLexer extends AbstractTemplateLexer {

    private Logger logger = LoggerFactory.getLogger(JsTemplateLexer.class);
    //  function funName(
    private static Pattern splitPattern = Pattern.compile("[ ]*function[ ]+[\\w]*[ ]*\\(");


    @Override
    protected Pattern getSplitPattern() {
        return splitPattern;
    }

    @Override
    protected String parseStatementId(String matchedSplitLine) {
        int index = matchedSplitLine.indexOf("(");
        Assert.isTrue(index > 1, "格式有误：" + matchedSplitLine);
        return matchedSplitLine.substring(0, index).replace("function", "").trim();
    }

    @Override
    protected boolean statementIdLineIsContent() {
        return true;
    }
}
