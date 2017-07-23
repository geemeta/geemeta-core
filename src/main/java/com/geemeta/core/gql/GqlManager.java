package com.geemeta.core.gql;

import com.geemeta.core.gql.execute.BoundPageSql;
import com.geemeta.core.gql.execute.BoundSql;
import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.parser.*;
import com.geemeta.core.gql.provider.MetaDeleteSqlProvider;
import com.geemeta.core.gql.provider.MetaInsertSqlProvider;
import com.geemeta.core.gql.provider.MetaQuerySqlProvider;
import com.geemeta.core.gql.provider.MetaUpdateSqlProvider;
import com.geemeta.core.mvc.Ctx;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于元数据的sql语句管理器
 * 元数据可来源于java类注解，也可来源于数据库配置的元数据信息
 *
 * @author itechgee@126.com
 * @date 2017/6/3.
 */
public class GqlManager {
    private static Lock lock = new ReentrantLock();
    private static GqlManager instance;
    private MetaManager metaManager = MetaManager.singleInstance();
    private MetaQuerySqlProvider metaQuerySqlProvider = new MetaQuerySqlProvider();
    private MetaInsertSqlProvider metaInsertSqlProvider = new MetaInsertSqlProvider();
    private MetaUpdateSqlProvider metaUpdateSqlProvider = new MetaUpdateSqlProvider();
    private MetaDeleteSqlProvider metaDeleteSqlProvider = new MetaDeleteSqlProvider();

    private JsonTextQueryParser jsonTextQueryParser = new JsonTextQueryParser();
    private JsonTextSaveParser jsonTextSaveParser = new JsonTextSaveParser();

    public static GqlManager singleInstance() {
        lock.lock();
        if (instance == null) instance = new GqlManager();
        lock.unlock();
        return instance;
    }


    //========================================================
    //                  基于元数据  gql                      ==
    //========================================================
    public BoundSql generateQuerySql(String jsonText, Ctx ctx) {
        return metaQuerySqlProvider.generate(jsonTextQueryParser.parse(jsonText));
    }

    public BoundPageSql generatePageQuerySql(String jsonText, Ctx ctx) {
        QueryCommand command = jsonTextQueryParser.parse(jsonText);
        BoundPageSql boundPageSql = new BoundPageSql();
        boundPageSql.setBoundSql(metaQuerySqlProvider.generate(command));
        boundPageSql.setCountSql(metaQuerySqlProvider.buildCountSql(command));
        return boundPageSql;
    }

    public BoundSql generateSaveSql(String jsonText, Ctx ctx) {
        SaveCommand command = jsonTextSaveParser.parse(jsonText, ctx);
        if (command.getCommandType() == CommandType.Update) {
            return metaUpdateSqlProvider.generate(command);
        } else {
            return metaInsertSqlProvider.generate(command);
        }
    }

    public BoundSql generateDeleteSql(String jsonText, Ctx ctx) {
        return metaDeleteSqlProvider.generate(jsonTextSaveParser.parse(jsonText, ctx));
    }

    //========================================================
    //                  基于元数据  entity                   ==
    //========================================================
    public <T> BoundSql generateQueryForObjectOrMapSql(Class<T> clazz, FilterGroup filterGroup) {
        return generateQuerySql(clazz, false, filterGroup);
    }

    public <T> BoundSql generateQueryForListSql(Class<T> clazz, FilterGroup filterGroup) {
        return generateQuerySql(clazz, true, filterGroup);
    }

    private <T> BoundSql generateQuerySql(Class<T> clazz, boolean isArray, FilterGroup filterGroup) {
        QueryCommand queryCommand = new QueryCommand();
        EntityMeta em = metaManager.get(clazz);
        queryCommand.setEntityName(em.getEntityName());
        queryCommand.setFields(em.getFieldNames());
        queryCommand.setQueryForList(isArray);
        queryCommand.setWhere(filterGroup);
        return metaQuerySqlProvider.generate(queryCommand);
    }


}