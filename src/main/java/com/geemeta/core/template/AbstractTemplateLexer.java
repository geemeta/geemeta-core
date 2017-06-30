package com.geemeta.core.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public abstract class AbstractTemplateLexer {
    private Logger logger = LoggerFactory.getLogger(AbstractTemplateLexer.class);

    /**
     * 简单解析，解析出模板内容段，便于下一步对模板内容段进一步解析
     *
     * @param lines
     * @return
     */
    public List<TemplateStatement> lex(List<String> lines) {
        String statementId = null;
        List<TemplateStatement> templateStatements = new ArrayList<>();
        TemplateStatement templateStatement = null;
        for (String l : lines) {
            String line = l.trim();
            if (line.length() == 0)
                continue;
            Matcher matcher = getSplitPattern().matcher(line);
            if (matcher.find()) {
                if (logger.isDebugEnabled())
                    logger.debug("matcher:{}", matcher.group());
                //当前分行
                if (statementId != null) {
                    //新的语句行，先保存已有的TemplateStatement
                    if (templateStatement != null && templateStatement.getContent() != null && templateStatement.getContent().size() > 0)
                        templateStatements.add(templateStatement);
                }
                templateStatement = new TemplateStatement();
                templateStatement.setContent(new ArrayList());
                statementId=parseStatementId(line);
                templateStatement.setId(statementId);
                //如果匹配的statementId行同时是内容行时
                if (statementIdLineIsContent()) {
                    templateStatement.getContent().add(line);
                }
            } else {
                //丢弃注解行，不进行add(line)
                switch (line.charAt(0)) {
                    case '*':
                        continue;
                    case '/':
                        continue;
                    case '-':
                        if (line.charAt(1) == '-')
                            continue;
                }

                if (templateStatement != null)
                    templateStatement.getContent().add(line);
            }
        }
        //添加最后一个
        if (templateStatement != null && templateStatement.getContent() != null && templateStatement.getContent().size() > 0)
            templateStatements.add(templateStatement);
        return templateStatements;
    }

    /**
     * 匹配的分割行的正则表达式
     *
     * @return
     */
    protected abstract Pattern getSplitPattern();

    /**
     * 匹配的分割行，statementId就在行内
     *
     * @param matchedSplitLine
     * @return
     */
    protected abstract String parseStatementId(String matchedSplitLine);


    /**
     * 匹配行是否需加入有效的内容中(匹配行用于解析statementId，有时该行是
     * 有效的内容行，如js脚本的解析,function foo(){}是statementId行，
     * 同时也是内容行)。若是返回true，若不是返回false。
     *
     * @return
     */
    protected abstract boolean statementIdLineIsContent();
}
