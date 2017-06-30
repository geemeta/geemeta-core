package com.geemeta.core.mvc;

import com.geemeta.core.gql.execute.Page;
import com.geemeta.core.orm.Dao;
import com.sun.deploy.net.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 *
 * 基于sqlId,参数调用sql template 语句,key指sqlId
 *
 * @author itechgee@126.com
 * @date 2017/6/3.
 */
@Controller
@RequestMapping(value = "/api/key/")
public class KeyController {

    @Autowired
    protected Dao dao;
    public HashMap queryForList(HttpRequest request){


        return null;
    }

    /**
     * e.g.:http://localhost:8080/api/meta/list/
     * @param request
     * @return
     */
    @RequestMapping(value={"list","list/*"},method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public Page list(HttpServletRequest request) {

//        queryKey = convertQueryKey(queryKey);
//        param.setQueryKey(queryKey);
//        param.setQueryType(entityQueryKeyManager.getQueryType(queryKey));
//        return dao.query(param);

        return null;
    }
}
