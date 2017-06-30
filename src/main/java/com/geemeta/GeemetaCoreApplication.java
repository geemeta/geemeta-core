package com.geemeta;

import com.geemeta.core.biz.rules.BizManagerFactory;
import com.geemeta.core.gql.MetaManager;
import com.geemeta.core.gql.meta.MetaRelf;
import com.geemeta.core.orm.Dao;
import com.geemeta.core.orm.MysqlDbGenerateDao;
import com.geemeta.core.orm.SqlFiles;
import com.geemeta.core.template.TemplateManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
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
//        String path = applicationContext.getEnvironment().getProperty("xgee.init.sqls.path").trim();
        ////解析元数据
        MetaRelf.setApplicationContext(applicationContext);
        MetaManager.singleInstance().scanAndParse("com.geemeta", false);
        ////解析脚本：sql、业务规则
        //String path =applicationContext.getEnvironment().getProperty("geemeta.res.path").trim();
        String path = this.getClass().getClassLoader().getResource("//").getPath();
        //--sql
        TemplateManagerFactory.get(Dao.SQL_TEMPLATE_MANAGER).loadFiles(path + "/geeMeta/sql/");
        //--业务规则
        BizManagerFactory.get("rule").loadFiles(path + "/geeMeta/rule/");
        // 创建表结构
        mysqlDbGenerateDao.createAllTables(true);
        // 初始化表数据
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("\\geeMeta\\data\\init.sql");
        SqlFiles.loadAndExecute(is, jdbcTemplate, isWinOS);
    }

    public static void main(String[] args) {
        SpringApplication.run(GeemetaCoreApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
