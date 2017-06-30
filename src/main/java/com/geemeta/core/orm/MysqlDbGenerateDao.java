package com.geemeta.core.orm;

import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.meta.ColumnMeta;
import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.meta.FieldMeta;
import com.geemeta.utils.SqlParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongxq on 2017/5/21.
 */
@Component
public class MysqlDbGenerateDao {

    private static Logger logger = LoggerFactory.getLogger(MysqlDbGenerateDao.class);
    private static HashMap<String, Integer> defaultColumnLengthMap;
    @Autowired
    private Dao dao;

    private MetaManager metaManager = MetaManager.singleInstance();


    public MysqlDbGenerateDao() {
        defaultColumnLengthMap = new HashMap<>();
        defaultColumnLengthMap.put("description", 1024);
    }

    /**
     * 基于元数据管理，需元数据据管理器已加载、扫描元数据
     *
     * @param isForce 存在表时，是否drop
     */
    public void createAllTables(boolean isForce) {
        Collection<EntityMeta> entityMetas = metaManager.getAll();
        if (entityMetas == null) {
            logger.warn("实体元数据为空，可能还未解析元数据，请解析之后，再执行该方法(createAllTables)");
            return;
        }
        for (EntityMeta em : entityMetas) {
            if (isForce) {
                logger.info("  drop table " + em.getTableName());
                dao.execute("dropOneTable", SqlParams.map("tableName", em.getTableName()));
            }
            logger.info("  create table " + em.getTableName());

            ArrayList<ColumnMeta> addList = new ArrayList<>();
            ArrayList<ColumnMeta> uniqueList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", em.getTableName());
            map.put("addList", addList);
            map.put("uniqueList", uniqueList);

            for (FieldMeta fm : em.getFieldMetas()) {
                try {
                    if (defaultColumnLengthMap.containsKey(fm.getColumnName())) {
                        int len = defaultColumnLengthMap.get(fm.getColumnName()).intValue();
                        fm.getColumn().setCharMaxlength(len);
                        fm.getColumn().setNumericPrecision(len);
                        fm.getColumn().afterSet();
                    }
                    //创建表的语句中已有id，这里不再重复创建。改用应用程序的guid，这里还是加上id
//                    if (!fm.getColumn().getName().toLowerCase().equals("id"))
                    addList.add(fm.getColumn());
                    if (fm.getColumn().isUnique())
                        uniqueList.add(fm.getColumn());
                } catch (Exception e) {
                    if (e.getMessage().indexOf("Duplicate column name") != -1)
                        logger.info("column " + fm.getColumnName() + " is exists，ignore.");
                    else throw e;
                }
            }
            dao.execute("createOneTable", map);
        }
    }
}
