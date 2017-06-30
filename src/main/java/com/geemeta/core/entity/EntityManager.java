package com.geemeta.core.entity;

import com.geemeta.core.entity.parser.EntitySaveParser;
import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.execute.BoundSql;
import com.geemeta.core.gql.parser.CommandType;
import com.geemeta.core.gql.parser.SaveCommand;
import com.geemeta.core.gql.provider.MetaDeleteSqlProvider;
import com.geemeta.core.gql.provider.MetaInsertSqlProvider;
import com.geemeta.core.gql.provider.MetaQuerySqlProvider;
import com.geemeta.core.gql.provider.MetaUpdateSqlProvider;
import com.geemeta.core.mvc.Ctx;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 2017/6/21.
 */
public class EntityManager {
    private static Lock lock = new ReentrantLock();
    private static EntityManager instance;
    private MetaManager metaManager = MetaManager.singleInstance();
    private EntitySaveParser entitySaveParser = new EntitySaveParser();
    private MetaQuerySqlProvider metaQuerySqlProvider = new MetaQuerySqlProvider();
    private MetaInsertSqlProvider metaInsertSqlProvider = new MetaInsertSqlProvider();
    private MetaUpdateSqlProvider metaUpdateSqlProvider = new MetaUpdateSqlProvider();
    private MetaDeleteSqlProvider metaDeleteSqlProvider = new MetaDeleteSqlProvider();

    public static EntityManager singleInstance() {
        lock.lock();
        if (instance == null) instance = new EntityManager();
        lock.unlock();
        return instance;
    }

    public BoundSql generateSaveSql(IdEntity entity, Ctx ctx){
        SaveCommand command = entitySaveParser.parse(entity,ctx);
        if(command.getCommandType()== CommandType.Update){
            return metaUpdateSqlProvider.generate(command);
        }else{
            return metaInsertSqlProvider.generate(command);
        }
    }
}
