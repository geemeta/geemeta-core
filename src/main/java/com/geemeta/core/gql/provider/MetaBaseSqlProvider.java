package com.geemeta.core.gql.provider;

import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.TypeConverter;
import com.geemeta.core.gql.execute.BoundSql;
import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.parser.BaseCommand;
import com.geemeta.core.gql.parser.FilterGroup;
import org.apache.shiro.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
public abstract class MetaBaseSqlProvider<E extends BaseCommand> {
    private static Logger logger = LoggerFactory.getLogger(MetaBaseSqlProvider.class);
    protected static final Map<FilterGroup.Operator, String> enumToSignString = new HashMap<FilterGroup.Operator, String>();
    protected MetaManager metaManager = MetaManager.singleInstance();

    protected static String convertToSignString(FilterGroup.Operator operator) {
        if (operator == FilterGroup.Operator.eq) return "=";
        else if (operator == FilterGroup.Operator.neq) return "!=";
        else if (operator == FilterGroup.Operator.lt) return "<";
        else if (operator == FilterGroup.Operator.lte) return "<=";
        else if (operator == FilterGroup.Operator.gt) return ">";
        else if (operator == FilterGroup.Operator.gte) return ">=";
        return "=";
    }


    static {
        for (FilterGroup.Operator operator : FilterGroup.Operator.values()) {
            enumToSignString.put(operator, convertToSignString(operator));
        }
    }

    /**
     * 对command进行递归解析，创建BoundSql对象及其BoundSql子对象
     *
     * @param command QueryCommand、UpdateCommand等
     * @return
     */
    public BoundSql generate(E command) {
        BoundSql boundSql = new BoundSql();
        boundSql.setName(command.getEntityName());
        boundSql.setSql(buildOneSql(command));
        boundSql.setParams(buildParams(command));
        boundSql.setTypes(buildTypes(command));
        //queryCommand.getEntityQueryMeta().getParams();
        // 解析子级的command
        if (command.getCommands() != null) {
            command.getCommands().forEach(item -> {
                BoundSql subBoundSql = generate((E) item);
                if (boundSql.getBoundSqlMap() == null)
                    boundSql.setBoundSqlMap(new HashMap<>());
                boundSql.getBoundSqlMap().put(subBoundSql.getName(), subBoundSql);
            });
        }
        boundSql.setCommand(command);
        return boundSql;
    }

    protected abstract Object[] buildParams(E command);

    protected abstract int[] buildTypes(E command);

    /**
     * 构建一条语句，如insert、save、select、delete语句，在子类中实现
     *
     * @param command
     * @return 带参数（?）或无参数的完整sql语句
     */
    protected abstract String buildOneSql(E command);


    /**
     * 基于条件部分，构建参数值对像数组
     * 对于update、insert、delete的sql provider，即结合字段设值部分的需要，组合调整
     *
     * @param command
     * @return
     */
    protected Object[] buildWhereParams(E command) {
        if (command.getWhere() == null || command.getWhere().getFilters() == null || command.getWhere().getFilters().size() == 0)
            return new Object[0];
        Object[] params = new Object[command.getWhere().getFilters().size()];
        int i = 0;
        for (FilterGroup.Filter filter : command.getWhere().getFilters()) {
            params[i] = filter.getValue();
            i++;
        }
        return params;
    }

    /**
     * 基于条件部分，构建参数类型数组
     * 对于update、insert、delete的sql provider，即结合字段设值部分的需要，组合调整
     *
     * @param command
     * @return
     */
    protected int[] buildWhereTypes(E command) {
        if(command.getWhere()==null||command.getWhere().getFilters()==null)
            return new int[0];
        EntityMeta em = getEntityMeta(command);
        int[] types = new int[command.getWhere().getFilters().size()];
        int i = 0;
        for (FilterGroup.Filter filter : command.getWhere().getFilters()) {
            types[i] = TypeConverter.toSqlType(em.getFieldMeta(filter.getField()).getColumn().getDataType());
            i++;
        }
        return types;
    }

    /**
     * 只构建当前实体的查询条件!isRefField
     *
     * @param sb
     * @param md
     * @param list
     * @param logic
     */
    protected void buildConditions(StringBuilder sb, EntityMeta md, List<FilterGroup.Filter> list, FilterGroup.Logic logic) {
        if (list != null && list.size() > 0) {
            Iterator<FilterGroup.Filter> iterator = list.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                FilterGroup.Filter filter = iterator.next();
                //只构建当前实体的查询条件
                if (filter.isRefField()) continue;
                if (index > 0) {
                    sb.append(" ");
                    sb.append(logic.getText());
                    sb.append(" ");
                }
                buildConditionSegment(sb, md, filter);
                index += 1;
            }
        }
    }


    protected void buildConditionSegment(StringBuilder sb, EntityMeta md, FilterGroup.Filter filter) {

        String columnName = md.getColumnName(filter.getField());
        FilterGroup.Operator operator = filter.getOperator();
        if (operator == FilterGroup.Operator.eq || operator == FilterGroup.Operator.neq || operator == FilterGroup.Operator.lt || operator == FilterGroup.Operator.lte || operator == FilterGroup.Operator.gt || operator == operator.gte) {
            sb.append(columnName);
            sb.append(enumToSignString.get(operator));
            sb.append("?");
        } else if (operator == FilterGroup.Operator.startWith) {
            sb.append(columnName);
            sb.append(" like CONCAT('',?,'%')");
        } else if (operator == FilterGroup.Operator.endWith) {
            sb.append(columnName);
            sb.append(" like CONCAT('%',?,'')");
        } else if (operator == FilterGroup.Operator.contains) {
            sb.append(columnName);
            sb.append(" like CONCAT('%',?,'%')");
        }
    }

    public EntityMeta getEntityMeta(E command) {
        EntityMeta md = metaManager.getByEntityName(command.getEntityName());
        Assert.notNull(md, "未能通过entityName：" + command.getEntityName() + ",获取元数据信息EntityMeta。");
        return md;
    }
}
