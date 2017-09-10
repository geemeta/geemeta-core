package com.geemeta.core.template.sql;

import com.geemeta.core.template.AbstractTemplateLexer;
import com.geemeta.core.template.TemplateStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author itechgee@126.com
 * @date 2017/5/31.
 */
public class SqlTemplateLexer extends AbstractTemplateLexer {
    private static Logger logger = LoggerFactory.getLogger(SqlTemplateLexer.class);
    private static String SQL_OPEN_FLAG = "@sql";
    private static String KW_START_FLAG = "@";
    private static String KW_END_FLAG = "@/";
    // -- @sql
    private static Pattern splitPattern = Pattern.compile("[ ]*--[ ]*@sql[\\s]+");
    private static Pattern keywordStartPattern = Pattern.compile("[ ]*@[\\w]+[\\s]*");
    private static Pattern keywordEndPattern = Pattern.compile("[ ]*@[\\/\\w]+[\\s]*");

    public HashMap<String, List<Token>> lexDeep(List<String> list) {
        return lexSqlContent(lex(list));
    }

//    public List<TemplateStatement> lex(List<String> lines) {
//        String sqlId = "";
//        List<TemplateStatement> templateStatements = new ArrayList<>();
//        TemplateStatement TemplateStatement = null;
//        for (String l : lines) {
//            String line = l.trim();
//            if (line.length() == 0)
//                continue;
//            Matcher matcher = splitPattern.matcher(line);
//            if (matcher.find()) {
//                logger.debug("matcher:{}", matcher.group());
//                //当前是sql语句分行
//                if (sqlId != null) {
//                    //新的sql语句行，先保存已有的sqlToken
//                    if (TemplateStatement != null && TemplateStatement.getTree() != null && TemplateStatement.getTree().size() > 0)
//                        templateStatements.add(TemplateStatement);
//                }
//                sqlId = line.replace(SQL_OPEN_FLAG, "").replace("-", "").trim();
//                TemplateStatement = new TemplateStatement();
//                TemplateStatement.setTree(new ArrayList());
//                TemplateStatement.setId(sqlId);
//            } else {
//                //丢弃注解行，不进行add(line)
//                switch (line.charAt(0)) {
//                    case '*':
//                        continue;
//                    case '/':
//                        continue;
//                    case '-':
//                        if (line.charAt(1) == '-')
//                            continue;
//                }
//
//                if (TemplateStatement != null)
//                    TemplateStatement.getTree().add(line);
//            }
//        }
//        //添加最后一个
//        if (TemplateStatement != null && TemplateStatement.getTree() != null && TemplateStatement.getTree().size() > 0)
//            templateStatements.add(TemplateStatement);
//        return templateStatements;
//    }

    public HashMap<String, List<Token>> lexSqlContent(List<TemplateStatement> templateStatements) {
        HashMap<String, List<Token>> map = new HashMap<>(templateStatements.size());
        for (TemplateStatement TemplateStatement : templateStatements) {
            map.put(TemplateStatement.getId(), lexTokens(TemplateStatement.getContent()));
        }
        return map;
    }

    private List<Token> lexTokens(List<String> contentList) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        for (String l : contentList) {
            String line = l.trim();
            Token token = new Token();
            token.setValue(line);

            if (line.startsWith(KW_START_FLAG)) {
                Matcher startMatcher = keywordStartPattern.matcher(line);
                Matcher endMatcher = keywordEndPattern.matcher(line);
                if (startMatcher.find()) {
                    token.setTokenType(TokenType.Open);
                    token.setKeyword(startMatcher.group().replace(KW_START_FLAG, "").trim());
                } else if (endMatcher.find()) {
                    token.setTokenType(TokenType.Close);
                    token.setKeyword(endMatcher.group().replace(KW_END_FLAG, "").trim());
                } else {
                    token.setTokenType(TokenType.Value);
                    token.setKeyword("");
                }
            } else {
                token.setTokenType(TokenType.Value);
                token.setKeyword("");
            }

            token.setIndex(index++);
            tokens.add(token);
        }
        return tokens;
    }

    @Override
    protected Pattern getSplitPattern() {
        return splitPattern;
    }

    @Override
    protected String parseStatementId(String matchedSplitLine) {
        return matchedSplitLine.replace(SQL_OPEN_FLAG, "").replace("-", "").trim();
    }

    @Override
    protected boolean statementIdLineIsContent() {
        return false;
    }
}
