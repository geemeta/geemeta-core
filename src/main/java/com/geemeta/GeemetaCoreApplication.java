package com.geemeta;

import com.geemeta.core.biz.rules.BizManagerFactory;
import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.meta.MetaRelf;
import com.geemeta.core.orm.Dao;
import com.geemeta.core.orm.MysqlDbGenerateDao;
import com.geemeta.core.orm.SqlFiles;
import com.geemeta.core.template.SQLTemplateManagerFactory;
import org.apache.shiro.io.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class GeemetaCoreApplication implements CommandLineRunner, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(GeemetaCoreApplication.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MysqlDbGenerateDao mysqlDbGenerateDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean isWinOS;


    private void assertOS() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        logger.info("[操作系统]" + os);
        if (os.toLowerCase().startsWith("win"))
            isWinOS = true;
        else
            isWinOS = false;
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("配置文件：" + applicationContext.getEnvironment().getProperty("xgee.env"));
        logger.info("[启动应用]...");
        assertOS();
        initMeta();


        logger.info("[启动应用]...OK");
        //logEnvironment();
//		onStarted();
    }

    public void initMeta() throws IOException {

        InputStream inputStream = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache-shiro.xml");
        logger.info("inputStream.available:{}", inputStream.available());

        // 解析元数据
        MetaRelf.setApplicationContext(applicationContext);
        MetaManager.singleInstance().scanAndParse("com.geemeta", false);
        // 解析脚本：sql、业务规则
        if (this.getClass().getClassLoader() == null || this.getClass().getClassLoader().getResource("//") == null) {
            initFromFatJar();
        } else {
            initFromExploreFile();
        }
    }

    /**
     * 配置文件不打包在jar包中运行，可基于文件系统加载配置文件
     *
     * @throws IOException
     */
    private void initFromExploreFile() throws IOException {
        //String path =applicationContext.getEnvironment().getProperty("geemeta.res.path").trim();
        String path = this.getClass().getClassLoader().getResource("//").getPath();
        //由测试类启动时，修改资源目录为源码下的资源目录
        path = path.replace("test-classes", "classes");
        //--1、sql
        SQLTemplateManagerFactory.get(Dao.SQL_TEMPLATE_MANAGER).loadFiles(path + "/geeMeta/sql/");
        //--2、业务规则
        BizManagerFactory.get("rule").loadFiles(path + "/geeMeta/rule/");
        //--3、创建表结构
        mysqlDbGenerateDao.createAllTables(true);
        //--4、初始化表数据
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(getProperty("geeMeta.init.sql","/geeMeta/data/init.sql"));
        SqlFiles.loadAndExecute(is, jdbcTemplate, isWinOS);
    }

    /**
     * 打包成单个fatJar文件运行时，加载的资源不能采用文件系统加载，需采用流的方式加载
     *
     * @throws IOException
     */
    private void initFromFatJar() throws IOException {
        //--1、sql
        SQLTemplateManagerFactory.get(Dao.SQL_TEMPLATE_MANAGER).loadResource("/geeMeta/sql/**/*.sql");
        //--2、业务规则
        BizManagerFactory.get("rule").loadResource("/geeMeta/rule/**/*.js");
        //--3、创建表结构
        mysqlDbGenerateDao.createAllTables(true);
        //--4、初始化表数据
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String initSql = "/geeMeta/data/*.sql";
        try {
            Resource[] resources = resolver.getResources("/geeMeta/data/*.sql");
            for (Resource resource : resources) {
                InputStream is = resource.getInputStream();
                SqlFiles.loadAndExecute(is, jdbcTemplate, isWinOS);
            }
        } catch (IOException e) {
            logger.error("加载、初始化数据（" + initSql + "）失败。", e);
        }
    }

    private String getProperty(String key, String defaultValue) {
        String value = applicationContext.getEnvironment().getProperty(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public static void main(String[] args) {
        SpringApplication.run(GeemetaCoreApplication.class, args);
    }


}
