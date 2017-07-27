package com.geemeta.core.orm;

import com.geemeta.core.entity.CommonRowMapper;
import com.geemeta.core.entity.DataDynamic;
import com.geemeta.core.entity.EntityManager;
import com.geemeta.core.entity.IdEntity;
import com.geemeta.core.gql.GqlManager;
import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.execute.BoundPageSql;
import com.geemeta.core.gql.execute.BoundSql;
import com.geemeta.core.gql.meta.EntityMeta;
import com.geemeta.core.gql.parser.FilterGroup;
import com.geemeta.core.gql.parser.QueryCommand;
import com.geemeta.core.gql.parser.SaveCommand;
import com.geemeta.core.mvc.Ctx;
import com.geemeta.core.template.SQLTemplateManager;
import com.geemeta.core.template.SQLTemplateManagerFactory;
import com.geemeta.m.platform.security.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author itechgee@126.com
 * @date 2017/6/2.
 */
@Component
public class Dao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static Logger logger = LoggerFactory.getLogger(Dao.class);
    private static Map<String, Object> defaultParams = new HashMap<>();
    public final static String SQL_TEMPLATE_MANAGER = "sql";
    private MetaManager metaManager = MetaManager.singleInstance();
    private SQLTemplateManager sqlTemplateManager = SQLTemplateManagerFactory.get(SQL_TEMPLATE_MANAGER);
    private GqlManager gqlManager = GqlManager.singleInstance();
    private EntityManager entityManager = EntityManager.singleInstance();
    private static HashMap<String, String> ignoreFieldsMap = new HashMap<String, String>(1);


    static {
        ignoreFieldsMap.put("createDate", "createDate");
    }

    /**
     * 若需执行自己构建的语句，可以获取jdbcTemplate
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    //========================================================
    //                  基于sqlId                           ==
    //========================================================
    public void execute(String sqlId, Map<String, Object> paramMap) {
        jdbcTemplate.execute((String) sqlTemplateManager.generate(sqlId, paramMap));
    }

    public Map<String, Object> queryForMap(String sqlId, Map<String, Object> paramMap) throws DataAccessException {
        return jdbcTemplate.queryForMap(sqlTemplateManager.generate(sqlId, paramMix(paramMap)));
    }

    public <T> T queryForObject(String sqlId, Map<String, Object> paramMap, Class<T> requiredType) throws DataAccessException {
        return jdbcTemplate.queryForObject(sqlTemplateManager.generate(sqlId, paramMix(paramMap)), requiredType);
    }

    public List<Map<String, Object>> queryForMapList(String sqlId, Map<String, Object> paramMap) {
        return jdbcTemplate.queryForList(sqlTemplateManager.generate(sqlId, paramMix(paramMap)));
    }

    public <T> List<T> queryForOneColumnList(String sqlId, Map<String, Object> paramMap, Class<T> elementType) throws DataAccessException {
        return jdbcTemplate.queryForList(sqlTemplateManager.generate(sqlId, paramMix(paramMap)), elementType);
    }

    public int save(String sqlId, Map<String, Object> paramMap) {
        return jdbcTemplate.update((String) sqlTemplateManager.generate(sqlId, paramMix(paramMap)));
    }

    /**
     * 加入默认上下文参数：
     * 1、userId：当前用户id
     *
     * @param paramMap
     * @return
     */
    private Map<String, Object> paramMix(Map<String, Object> paramMap) {
        if (paramMap == null || paramMap.size() == 0)
            return defaultParams;
        paramMap.put("$ctx", defaultParams.get("$ctx"));
        return paramMap;
    }

    //========================================================
    //                  基于元数据  gql                      ==
    //========================================================
    public Map<String, Object> queryForMap(String gql) throws DataAccessException {
        BoundSql boundSql = gqlManager.generateQuerySql(gql, getSessionCtx());
        return jdbcTemplate.queryForMap(boundSql.getSql(), boundSql.getParams());
    }

    public <T> T queryForObject(String gql, Class<T> requiredType) throws DataAccessException {
        BoundSql boundSql = gqlManager.generateQuerySql(gql, getSessionCtx());
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParams(), requiredType);
    }

    public PageApiResult queryForMapList(String gql) {
        BoundPageSql boundPageSql = gqlManager.generatePageQuerySql(gql, getSessionCtx());
        QueryCommand command = (QueryCommand) boundPageSql.getBoundSql().getCommand();
        List list = jdbcTemplate.queryForList(boundPageSql.getBoundSql().getSql(), boundPageSql.getBoundSql().getParams());
        PageApiResult result = new PageApiResult();
        result.setData(list);
        result.setTotal(jdbcTemplate.queryForObject(boundPageSql.getCountSql(), boundPageSql.getBoundSql().getParams(), Long.class));
        result.setPage(command.getPageNum());
        result.setSize(command.getPageSize());
        result.setDataSize(list != null ? list.size() : 0);
        result.setMeta(metaManager.getByEntityName(command.getEntityName()).getSimpleFieldMetas(command.getFields()));
        return result;
    }

    public <T> List<T> queryForOneColumnList(String gql, Class<T> elementType) throws DataAccessException {
        BoundSql boundSql = gqlManager.generateQuerySql(gql, getSessionCtx());
        return jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParams(), elementType);
    }

    public int save(String gql) {
        BoundSql boundSql = gqlManager.generateSaveSql(gql, getSessionCtx());
        return jdbcTemplate.update(boundSql.getSql(), boundSql.getParams());
    }

    //========================================================
    //                  基于元数据  entity                   ==
    //========================================================

    /**
     * 依据单个条件查询
     *
     * @param entityType 实体类型
     * @return
     */
    public <T> T queryForObject(Class<T> entityType, Object PKValue) {
        return queryForObject(entityType, metaManager.get(entityType).getId().getFieldName(), PKValue);
    }

    /**
     * 依据单个条件查询
     *
     * @param entityType 实体类型
     * @param fieldName  实体的属性名
     * @param value      实体属性的值
     * @return
     */
    public <T> T queryForObject(Class<T> entityType, String fieldName, Object value) {
        FilterGroup filterGroup = new FilterGroup().addFilter(fieldName, value.toString());
        BoundSql boundSql = gqlManager.generateQueryForObjectOrMapSql(entityType, filterGroup);
        if (logger.isDebugEnabled())
            logger.debug("{}", boundSql);
        return jdbcTemplate.queryForObject(boundSql.getSql(), boundSql.getParams(), new CommonRowMapper<T>());
    }

    public Map queryForMap(Class entityType, String fieldName, Object value) {
        FilterGroup filterGroup = new FilterGroup().addFilter(fieldName, value.toString());
        BoundSql boundSql = gqlManager.generateQueryForObjectOrMapSql(entityType, filterGroup);
        return jdbcTemplate.queryForMap(boundSql.getSql(), boundSql.getParams());
    }

    public List<Map<String, Object>> queryForMapList(Class entityType, String fieldName, Object value) {
        FilterGroup filterGroup = new FilterGroup().addFilter(fieldName, value.toString());
        return queryForMapList(entityType, filterGroup);
    }

    public List<Map<String, Object>> queryForMapList(Class entityType, FilterGroup filterGroup) {
        BoundSql boundSql = gqlManager.generateQueryForListSql(entityType, filterGroup);
        return jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParams());
    }

    public List<Map<String, Object>> queryForMapList(Class entityType) {
        BoundSql boundSql = gqlManager.generateQueryForListSql(entityType, null);
        return jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParams());
    }

    public <T> List<T> queryForOneColumnList(Class<T> entityType, String fieldName, Object value) {
        FilterGroup filterGroup = new FilterGroup().addFilter(fieldName, value.toString());
        return queryForOneColumnList(entityType, filterGroup);
    }

    public <T> List<T> queryForOneColumnList(Class<T> entityType, FilterGroup filterGroup) {
        BoundSql boundSql = gqlManager.generateQueryForListSql(entityType, filterGroup);
        return jdbcTemplate.queryForList(boundSql.getSql(), boundSql.getParams(), entityType);
    }

    public <E extends IdEntity> Map save(E entity) {
        BoundSql boundSql = entityManager.generateSaveSql(entity, getSessionCtx());
        jdbcTemplate.update(boundSql.getSql(), boundSql.getParams());
        SaveCommand command = (SaveCommand) boundSql.getCommand();
        return command.getValueMap();
    }

//    protected Object getObject(Class clazz) {
//        if (!beanCache.containsKey(clazz.toString()))
//            try {
//                beanCache.put(clazz.toString(), clazz.newInstance());
//            } catch (InstantiationException e) {
//                logger.error("创建实例失败。", e);
//            } catch (IllegalAccessException e) {
//                logger.error("创建实例失败。", e);
//            }
//        return beanCache.get(clazz.toString());
//
//    }


    private <E extends IdEntity> DataDynamic genDataDynamic(E entity) {
        EntityMeta entityMeta = metaManager.get(entity.getClass());
        DataDynamic dd = new DataDynamic();
        dd.setIdField("id");
        dd.setIdValue(entity.getId());
//        dd.setAction("");
//        dd.setDescription("");
        dd.setEntity(entity.getClass().getName());
        dd.setName(entityMeta.getTableMeta().getTitle());
        dd.setTableName(entityMeta.getTableName());
        dd.setCreateAt(new Date());
        dd.setCreateAt(dd.getUpdateAt());
        dd.setCreator(SecurityHelper.getCurrentUserId());
        dd.setUpdater(SecurityHelper.getCurrentUserId());
        dd.setSubjectName(SecurityHelper.getCurrentUserName());
        return dd;
    }

    protected Ctx getSessionCtx() {
        Ctx ctx = new Ctx();
        //TODO 从会话中获取
        ctx.put("userId", String.valueOf(1));
        return ctx;
    }

}
