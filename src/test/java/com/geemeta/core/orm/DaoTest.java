package com.geemeta.core.orm;

import com.geemeta.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
@WebAppConfiguration
public class DaoTest {


    @Autowired
    protected Dao dao;

    @Test
    public void queryForMapList() throws Exception {
        dao.queryForMapList(getQueryGql());
    }

    private String getQueryGql(){
        StringBuilder sb = new StringBuilder();
        sb.append("{\n" +
                "  \"sys_user\": {\n" +
                "    \"@p\": \"1,10\",\n" +
//                "    \"loginName\": \"super_admin\",\n" +
//                "    \"name|sw\": \"超级\",\n" +
//                "    \"description|c\": \"管理员\",\n" +
//                "    \"name|ew\": \"员\",\n" +
                "    \"@fs\": \"id,loginName,name\",\n" +
                "    \"@order\": \"loginName|+,name|-\"\n" +
                "  }\n" +
                "}");
       return sb.toString();
    }
}