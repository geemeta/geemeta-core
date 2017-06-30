package com.geemeta.core.gql.provider;

import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.parser.FilterGroup;
import com.geemeta.core.gql.parser.SaveCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author itechgee@126.com
 */
@Component
public class MetaDeleteSqlProvider extends MetaBaseSqlProvider<SaveCommand> {
    private static Logger logger = LoggerFactory.getLogger(MetaDeleteSqlProvider.class);

    @Override
    protected Object[] buildParams(SaveCommand command) {
        return buildWhereParams(command);
    }

    @Override
    protected int[] buildTypes(SaveCommand command) {
        return buildWhereTypes(command);
    }

    /**
     * DELETE FROM 表名称 WHERE 列名称 = 值
     * DELETE FROM Person WHERE LastName = 'Wilson'
     *
     * @param command
     * @return
     */
    protected String buildOneSql(SaveCommand command) {
        StringBuilder sb = new StringBuilder();
        EntityMeta md = getEntityMeta(command);
        sb.append("delete from ");
        sb.append(md.getTableName());
        //where
        FilterGroup fg = command.getWhere();
        if (fg != null && fg.getFilters() != null) {
            sb.append(" where ");
            buildConditions(sb, md, fg.getFilters(), fg.getLogic());
        }
        return sb.toString();
    }

}
