package com.geemeta.core.mvc;

import com.geemeta.core.entity.PageConfig;
import com.geemeta.core.orm.ApiResultCode;
import com.geemeta.core.orm.Dao;
import com.geemeta.core.orm.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author itechgee@126.com
 * @date 2017/7/7.
 */
@Controller
@RequestMapping(value = "/api/page/")
public class PageController {
    @Autowired
    protected Dao dao;

    private static Logger logger = LoggerFactory.getLogger(PageController.class);

    @RequestMapping(value = {"{pageCode}", "{pageCode}/*"}, method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
    @ResponseBody
    public ApiResult pageConfig(@PathVariable String pageCode) {
        ApiResult apiResult = new ApiResult();
        try {
            apiResult.setData(dao.queryForObject(PageConfig.class, "code", pageCode));
            if (apiResult.getData() == null) {
                apiResult.setCode(ApiResultCode.FAIL);
                apiResult.setMsg("未能获取到pageCode(" + pageCode + ")对应配置信息。");
            }
        } catch (Exception e) {
            apiResult.setCode(ApiResultCode.FAIL);
            apiResult.setMsg("未能获取到pageCode(" + pageCode + ")对应配置信息。");
            logger.error("pageCode：" + pageCode, e);
        }
        return apiResult;
    }

}
