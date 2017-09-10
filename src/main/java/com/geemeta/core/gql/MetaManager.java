package com.geemeta.core.gql;

import com.geemeta.core.gql.meta.*;
import com.geemeta.utils.ClassScanner;
import com.geemeta.utils.MapUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 14-3-23.
 */
public class MetaManager {

    private static Lock lock = new ReentrantLock();
    private static MetaManager instance;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MetaManager.class);
    private HashMap<String, EntityMeta> entityMetadataMap = new HashMap<String, EntityMeta>();
    //TODO 多库同表名的场景需怎么处理
    private HashMap<String, EntityMeta> tableNameMetadataMap = new HashMap<String, EntityMeta>();
    private static HashMap<String, String> entityFieldNameTitleMap = new HashMap<String, String>();
    private static HashMap<String, String> defaultNameTitleMap = new HashMap<String, String>();

    static {
        defaultNameTitleMap.put("name", "名称");
        defaultNameTitleMap.put("type", "类型");
        defaultNameTitleMap.put("creator", "创建者");
        defaultNameTitleMap.put("updater", "更新者");
        defaultNameTitleMap.put("create_at", "创建日期");
        defaultNameTitleMap.put("update_at", "更新日期");
        defaultNameTitleMap.put("description", "描述");
        defaultNameTitleMap.put("id", "序号");
        defaultNameTitleMap.put("title", "标题");
        defaultNameTitleMap.put("password", "密码");
        defaultNameTitleMap.put("login_name", "登录名");
    }


    private MetaManager() {
        //解析内置的类
        logger.info("解析内置的类包含注解{}的实体！！", Entity.class);
        parseOne(ColumnMeta.class);
        parseOne(TableMeta.class);
    }

    public static MetaManager singleInstance() {
        lock.lock();
        if (instance == null) instance = new MetaManager();
        lock.unlock();
        return instance;
    }

//    public void setApplicationContext(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//        if (dao == null) dao = applicationContext.getBean(JdbcTemplate.class);
//    }

    public EntityMeta get(Class clazz) {
        String entityName = MetaRelf.getEntityName(clazz);
        if (entityMetadataMap.containsKey(entityName)) {
            return entityMetadataMap.get(entityName);
        } else {
            Iterator<String> it = entityMetadataMap.keySet().iterator();
            logger.warn("Key({}) not found in entityMetadataMap.keySet:", clazz.getName());
            while (it.hasNext()) {
                logger.warn(it.next());
            }
            return null;
        }
    }

    public Map<String,Object> newDefaultEntity(String entityName){
        return newDefaultEntity(getByEntityName(entityName));
    }

    public Map<String,Object> newDefaultEntity(Class clazz){
        return newDefaultEntity(get(clazz));
    }

    /**
     * 基于元数据，创建默认实体（Map），并以各字段的默认值填充
     * @param em
     * @return
     */
    public Map<String,Object> newDefaultEntity(EntityMeta em){
        HashMap<String,Object> map = new HashMap(em.getFieldMetas().size());
        for(FieldMeta fm : em.getFieldMetas()){
            ColumnMeta cm = fm.getColumn();
            map.put(fm.getFieldName(),cm.getDefaultValue());
        }
        return map;
    }


    /**
     * 若是java元数据，则entityName为长类名（包名+类名）
     *
     * @param entityName
     * @return
     */
    public EntityMeta getByEntityName(String entityName) {
        if (entityMetadataMap.containsKey(entityName)) {
            return entityMetadataMap.get(entityName);
        } else {
            Iterator<String> it = entityMetadataMap.keySet().iterator();
            logger.warn("Key({}) not found in entityMetadataMap.keySet:", entityName);
            while (it.hasNext()) {
                logger.warn(it.next());
            }
            return null;
        }
    }

    public boolean containsEntity(String entityName) {
        return entityMetadataMap.containsKey(entityName);
    }

    public EntityMeta get(String tableName) {
        if (tableNameMetadataMap.containsKey(tableName)) {
            return tableNameMetadataMap.get(tableName);
        } else {
            Iterator<String> it = tableNameMetadataMap.keySet().iterator();
            logger.warn("Key({}) not found in tableNameMetadataMap.keySet:", tableName);
            while (it.hasNext()) {
                logger.warn(it.next());
            }
            return null;
        }
    }

    public Collection<EntityMeta> getAll() {
        return entityMetadataMap.values();
    }

//    public Class getMappedEntity(String tableName) {
//        if (tableNameMetadataMap.containsKey(tableName)) {
//            return tableNameMetadataMap.get(tableName).getEntityType();
//        } else {
//            Iterator<String> it = tableNameMetadataMap.keySet().iterator();
//            logger.warn("Key({}) not found in tableNameMetadataMap.keySet:", tableName);
//            while (it.hasNext()) {
//                logger.warn(it.next());
//            }
//            return null;
//        }
//    }

    /**
     * 检索批定包名中包含所有的包javax.persistence.Entity的类，并进行解析
     *
     * @param parkeName
     */
    private void scanAndParse(String parkeName) {
        logger.debug("开始从包{}中扫描到包含注解{}的实体......", parkeName, Entity.class);
        List<Class<?>> classes = ClassScanner.scan(parkeName, true, Entity.class);
        if (classes == null) {
            logger.info("从包{}中未扫描到包含注解{}的实体！！", parkeName, Entity.class);
            return;
        }
        for (Class<?> clazz : classes) {
            parseOne(clazz);
        }
    }

    /**
     * @param parkeName
     * @param isUpdateMetadataFormDb 是否同时从数据库的元数据表中更新元数据信息，如字段长度
     */
    public void scanAndParse(String parkeName, boolean isUpdateMetadataFormDb) {
        scanAndParse(parkeName);
        //@TODO updateMetadataFromDbAfterParse的取值
        if (isUpdateMetadataFormDb) updateMetadataFromDbAfterParse(null);
    }

    /**
     * 从数据库的元数据表中更新元数据信息，如字段长度
     * 注：需在scanAndParse之后执行才有效
     */
    public void updateMetadataFromDbAfterParse(List<HashMap> columns) {
        for (HashMap map : columns) {
            String TABLE_NAME = map.get("TABLE_NAME").toString();
            EntityMeta entityMapping = null;
            for (EntityMeta obj : entityMetadataMap.values()) {
                if (obj.getTableName().equalsIgnoreCase(TABLE_NAME)) {
                    entityMapping = obj;
                    break;
                }
            }
//                Metadata metadata = entityMetadataMap.values().stream().filter(p -> p.getTableName().equalsIgnoreCase(TABLE_NAME)).findFirst().get();
            if (entityMapping == null) {
                continue;
            }
            String COLUMN_NAME = map.get("COLUMN_NAME").toString();
            String COLUMN_COMMENT = MapUtils.getOrDefaultString(map, "COLUMN_COMMENT", "");
            String ORDINAL_POSITION = MapUtils.getOrDefaultString(map, "ORDINAL_POSITION", "");
            String COLUMN_DEFAULT = MapUtils.getOrDefaultString(map, "COLUMN_DEFAULT", "");
            String IS_NULLABLE = MapUtils.getOrDefaultString(map, "IS_NULLABLE", "NO");
            String DATA_TYPE = MapUtils.getOrDefaultString(map, "DATA_TYPE", "varchar");
            String CHARACTER_MAXIMUM_LENGTH = MapUtils.getOrDefaultString(map, "CHARACTER_MAXIMUM_LENGTH", "20");
            String CHARACTER_OCTET_LENGTH = MapUtils.getOrDefaultString(map, "CHARACTER_OCTET_LENGTH", "24");
            String NUMERIC_PRECISION = MapUtils.getOrDefaultString(map, "NUMERIC_PRECISION", "8");
            String NUMERIC_SCALE = MapUtils.getOrDefaultString(map, "NUMERIC_SCALE", "2");
            String DATETIME_PRECISION = MapUtils.getOrDefaultString(map, "DATETIME_PRECISION", "");
            String COLUMN_TYPE = MapUtils.getOrDefaultString(map, "COLUMN_TYPE", "");
            String COLUMN_KEY = MapUtils.getOrDefaultString(map, "COLUMN_KEY", "");
            String EXTRA = MapUtils.getOrDefaultString(map, "EXTRA", "");

            FieldMeta fm = entityMapping.getFieldMetaByColumn(COLUMN_NAME);
            if (fm != null) {
                //TODO 确认还有哪些字段属性需设置
//                logger.debug("字段" + TABLE_NAME + "." + fm.getColumn().getName() + "长度：" + Integer.parseInt(CHARACTER_MAXIMUM_LENGTH));
                fm.getColumn().setCharMaxLength(Integer.parseInt(CHARACTER_MAXIMUM_LENGTH));
                fm.getColumn().setNullable("NO".equalsIgnoreCase(IS_NULLABLE) ? false : true);
                fm.getColumn().setExtra(EXTRA);
//                        fm.getColumn().setDefaultValue();
                fm.getColumn().setNumericPrecision(Integer.parseInt(NUMERIC_PRECISION));
                fm.getColumn().setNumericScale(Integer.parseInt(NUMERIC_SCALE));
                fm.getColumn().setComment(COLUMN_COMMENT);
                if (StringUtils.isEmpty(fm.getColumn().getTitle())) fm.getColumn().setTitle(COLUMN_COMMENT);
                fm.getColumn().setOrdinalPosition(Integer.parseInt(ORDINAL_POSITION));

            }


        }
    }

    private void parseOne(Class clazz) {
        String entityName = MetaRelf.getEntityName(clazz);
        if (!entityMetadataMap.containsKey(entityName)) {
            EntityMeta entityMeta = MetaRelf.getEntityMeta(clazz);
            entityMetadataMap.put(entityMeta.getEntityName(), entityMeta);
            tableNameMetadataMap.put(entityMeta.getTableName(), entityMeta);
            if (logger.isDebugEnabled()) {
                logger.debug("success in parsing class:{}", clazz.getName());
                for (FieldMeta fm : entityMeta.getFieldMetas()) {
                    if (!entityFieldNameTitleMap.containsKey(fm.getFieldName()))
                        entityFieldNameTitleMap.put(fm.getFieldName(), fm.getTitle());
                    if (!entityFieldNameTitleMap.containsKey(fm.getColumnName()))
                        entityFieldNameTitleMap.put(fm.getColumnName(), fm.getTitle());
//                    if (logger.isDebugEnabled())
//                        logger.debug("field:column >>>" + fm.getFieldName() + ":" + fm.getColumnName());
                }
            }
        }
    }


    public String toTitle(String field) {
        if (defaultNameTitleMap.containsKey(field)) return defaultNameTitleMap.get(field);
        if (entityFieldNameTitleMap.containsKey(field)) return entityFieldNameTitleMap.get(field);
        return field;
    }
}
