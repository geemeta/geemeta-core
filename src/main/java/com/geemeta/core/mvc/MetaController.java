package com.geemeta.core.mvc;

import com.geemeta.core.orm.Dao;
import com.geemeta.core.orm.PageApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * get:
 *
 * @author itechgee@126.com
 * @date 2017/6/3.
 */
@Controller
@RequestMapping(value = "/api/meta/")
public class MetaController {

    @Autowired
    protected Dao dao;

    private static Logger logger = LoggerFactory.getLogger(MetaController.class);

    /**
     * e.g.:http://localhost:8080/api/meta/list/
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"list", "list/*"}, method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public DataTablesResult list(@RequestParam(defaultValue = "0") int draw, HttpServletRequest request) {
        //PageResult<List<Map<String, Object>>>
        String gql = getGql(request);
        PageApiResult page = dao.queryForMapList(gql);
        DataTablesResult dataTablesResult = new DataTablesResult();
        dataTablesResult.setDraw(draw);
        dataTablesResult.setRecordsTotal(page.getTotal());
        dataTablesResult.setRecordsFiltered(page.getDataSize());
        dataTablesResult.setData(page.getData());
        return dataTablesResult;
    }


    /**
     * e.g.:http://localhost:8080/api/meta/list/
     *
     * @param request
     * @return 主键值
     */
    @RequestMapping(value = {"save", "save/*"}, method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public SaveResult save(HttpServletRequest request) {
        String gql = getGql(request);
        SaveResult saveResult = new SaveResult();
        saveResult.setData(dao.save(gql));
        return saveResult;
    }

    private String getGql(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = request.getReader();
        } catch (IOException e) {
            logger.error("未能从httpServletRequest中获取gql的内容", e);
        }

        String str;
        try {
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            logger.error("未能从httpServletRequest中获取gql的内容", e);
        }
        return stringBuilder.toString();
    }
}
