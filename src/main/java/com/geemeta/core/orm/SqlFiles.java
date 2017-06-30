package com.geemeta.core.orm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongxq on 2016/8/31.
 */
public class SqlFiles {
    private static Logger logger = LoggerFactory.getLogger(SqlFiles.class);


    public static void loadAndExcute(List<String> lines, JdbcTemplate jdbcTemplate, boolean isWinOS) {
        if (lines != null) {
            StringBuffer sb = new StringBuffer();
            for (String line : lines) {
                if (StringUtils.isBlank(line)) continue;
                line = line.trim();
                int index = line.trim().indexOf("--");
                if (index >= 0) {
                    //新的语句开始，若存在老的语句，执行老的语句并清空
                    if (sb.length() > 1) {
                        logger.info("execute sql :{}", sb.toString());
                        jdbcTemplate.execute(sb.toString());
                    }
                    sb = new StringBuffer();
                } else {
                    sb.append(line);
                    if (isWinOS)
                        sb.append("\r\n");
                    else
                        sb.append("\n");
                }
            }
            if (sb.length() > 1) {
                logger.info("execute sql :{}", sb.toString());
                jdbcTemplate.execute(sb.toString());
            }
        }
    }

    /**
     * 文件格式说明：每条语句之间必须用注释“--”进行分割
     *
     * @param is           SQL文件的输入流
     * @param jdbcTemplate
     * @param isWinOS
     */
    public static void loadAndExecute(InputStream is, JdbcTemplate jdbcTemplate, boolean isWinOS) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            loadAndExcute(lines, jdbcTemplate, isWinOS);
        } catch (IOException e) {
            logger.error("加载SQL流文件并执行出错！{}", e);
        }
    }

    /**
     * 文件格式说明：每条语句之间必须用注释“--”进行分割
     *
     * @param path         SQL文件物理位置,无法加载fatjar中的文件
     * @param jdbcTemplate
     * @param isWinOS
     */
    public static void loadAndExecute(String path, JdbcTemplate jdbcTemplate, boolean isWinOS) {
        try {
            if (Files.isExecutable(Paths.get(path))) {
                List<String> lines = Files.readAllLines(Paths.get(path));
                loadAndExcute(lines, jdbcTemplate, isWinOS);
            }
        } catch (IOException e) {
            logger.error("加载SQL文件并执行出错！\r\n文件：" + path + "\r\n", e);
        }
    }
}
