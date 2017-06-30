package com.geemeta.core.gql.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geemeta.core.gql.MetaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author itechgee
 *         解析json字符串，并返回参数map
 */
public class JsonTextQueryParser {

    private static Logger logger = LoggerFactory.getLogger(JsonTextQueryParser.class);
    private static MetaManager metaManager = MetaManager.singleInstance();
    //page_num即offset，记录位置
    private final static String KEYWORD_FLAG = "@";
    private final static String FILTER_FLAG = "\\|";
    private final static String SUB_ENTITY_FLAG = "~";
    private final static String KW_PAGE = "@p";
    private final static String KW_FIELDS = "@fs";
    private final static String KW_ORDER_BY = "@order";
    private final static String KW_GROUP_BY = "@group";
    private final static String KW_HAVING = "@having";
    private final static String KW_KEY = "@key";

    private static Map<String, String> orderMap = null;

    static {
        orderMap = new HashMap<>(2);
        orderMap.put("+", "asc");
        orderMap.put("-", "desc");
    }

    public QueryCommand parse(String queryJsonText) {
        JSONObject jo = JSON.parseObject(queryJsonText);
        CommandValidator validator = new CommandValidator();
        if (jo.size() != 1) {
            validator.appendMessage("查询的jsonText格式有误，有且只有一个根元素。");
            Assert.isTrue(validator.isSuccess(), validator.getMessage());
        }
        String key = jo.keySet().iterator().next();
        return parse(key, jo.getJSONObject(key), validator);
    }

    private QueryCommand parse(String commandName, JSONObject jo, CommandValidator validator) {
        Assert.isTrue(validator.validateEntity(commandName), validator.getMessage());

        QueryCommand command = new QueryCommand();
        command.setEntityName(commandName);
//        if (jo.keySet().size() == 0) return command;

        FilterGroup fg = new FilterGroup();
        command.setWhere(fg);

//        HashMap<String,Object> params = new HashMap<>();

        jo.keySet().forEach(key -> {
            if (key.startsWith(KEYWORD_FLAG) && StringUtils.hasText(jo.getString(key))) {
                String[] segments = jo.getString(key).split(",");
                //关键字
                switch (key) {
                    case KW_FIELDS:
                        validator.validateField(segments, KW_FIELDS);
                        command.setFields(segments);
                        break;
                    case KW_ORDER_BY:
                        StringBuilder sb = new StringBuilder();
                        for (String order : segments) {
                            String[] strs = order.split(FILTER_FLAG);
                            if (strs.length == 2 && orderMap.containsKey(strs[1])) {
                                validator.validateField(strs[0], KW_ORDER_BY);
                                sb.append(sb.length() == 0 ? " " : ",");
                                sb.append(strs[0]);
                                sb.append(" ");
                                sb.append(orderMap.get(strs[1]));
                            } else {
                                validator.appendMessage(KW_ORDER_BY);
                                validator.appendMessage("的值格式有误，正确如：age|+,name|-。");
                            }
                        }
                        if (sb.length() > 0)
                            command.setOrderBy(sb.toString());
                        break;
                    case KW_GROUP_BY:
                        validator.validateField(segments, KW_GROUP_BY);
                        command.setGroupBy(jo.getString(key));
                        break;
                    case KW_HAVING:
                        //@TODO
                        break;
                    case KW_PAGE:
                        command.setQueryForList(true);
                        String[] page = jo.getString(KW_PAGE).split("[ ]*,[ ]*");
                        if (page.length != 2) {
                            validator.appendMessage("[");
                            validator.appendMessage(KW_PAGE);
                            validator.appendMessage("]格式有误,正确格式如：1,10；");
                        } else {
                            command.setPage(Integer.parseInt(page[0]));
                            command.setSize(Integer.parseInt(page[1]));
                        }
                        break;
                    default:
                        validator.appendMessage("[");
                        validator.appendMessage(key);
                        validator.appendMessage("]");
                        validator.appendMessage("不支持;");
                }
            } else if (key.startsWith(SUB_ENTITY_FLAG)) {
                //解析子实体
                command.getCommands().add(parse(key.substring(1), jo.getJSONObject(key), validator));
            } else {
                //where子句过滤条件
                String[] ary = key.split(FILTER_FLAG);
                String field = ary[0];
                validator.validateField(field, "where");
                if (ary.length == 1) {
                    //等值
                    fg.addFilter(field, FilterGroup.Operator.eq, jo.getString(key));
                } else if (ary.length == 2) {
                    //大于、小于...
                    String fn = ary[1];
                    FilterGroup.Operator operator = null;
                    switch (fn) {
                        case "eq":
                            operator = FilterGroup.Operator.eq;
                            break;
                        case "neq":
                            operator = FilterGroup.Operator.neq;
                            break;
                        case "lt":
                            operator = FilterGroup.Operator.lt;
                            break;
                        case "lte":
                            operator = FilterGroup.Operator.lte;
                            break;
                        case "gt":
                            operator = FilterGroup.Operator.gt;
                            break;
                        case "sw":
                            operator = FilterGroup.Operator.startWith;
                            break;
                        case "ew":
                            operator = FilterGroup.Operator.endWith;
                            break;
                        case "c":
                            operator = FilterGroup.Operator.contains;
                            break;
//                        case "in":
//                            //TODO 暂不支持
//                            break;
                        default:
                            validator.appendMessage("[");
                            validator.appendMessage(key);
                            validator.appendMessage("]");
                            validator.appendMessage("不支持");
                            validator.appendMessage(fn);
                            validator.appendMessage(";");
                    }
                    fg.addFilter(field, operator, jo.getString(key));
                } else {
                    //TODO 格式不对 throw
                }
            }
        });

        Assert.isTrue(validator.isSuccess(), validator.getMessage());
        return command;
    }


}
