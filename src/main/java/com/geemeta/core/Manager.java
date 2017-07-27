package com.geemeta.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public abstract class Manager {

    private static Logger logger = LoggerFactory.getLogger(Manager.class);
    protected final static String RN_WIN = "\r\n"; //in WinOS
    protected final static String RN_LINUX = "\n";    //in Linux
    protected ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public abstract void loadDb(String sqlId);

    /**
     * @param path 文件存放目录,多个目录用逗号分隔，递归加载子目录
     * @throws IOException
     */
    public void loadFiles(String path) throws IOException {
        String[] paths = path.split(",");
        for (String p : paths) {
            parseDirectory(new File(p));
        }
    }

    /**
     * @param file 文件存放目录,递归加载子目录
     * @throws IOException
     */
    public void loadFiles(File file) throws IOException {
        parseDirectory(file);
    }

    /**
     * @param file 文件存放目录,递归加载子目录
     * @throws IOException
     */
//    public void loadFiles(InputStream is) throws IOException {
//        Files.
//        parseDirectory(file);
//    }
    protected void parseDirectory(File file) throws IOException {
        Assert.isTrue(file.exists(), "不存在的目录：" + file.getPath());
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                parseDirectory(f);
            } else {
                parseFile(f);
            }
        }
    }

    public abstract void parseFile(File file) throws IOException;


    public abstract void parseStream(InputStream is) throws IOException;


    public void loadResource(String locationPattern) throws IOException {
        try {
            Resource[] resources = resolver.getResources(locationPattern);
            for (Resource resource : resources) {
                //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
                InputStream is = resource.getInputStream();
                parseStream(is);
            }
        } catch (IOException e) {
            logger.error("加载、处理数据（" + locationPattern + "）失败。", e);
        }
    }

    public List<String> readLines(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<String> lineList = new ArrayList<String>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineList.add(line);
            logger.info("line:{}", line);
        }
        return lineList;
    }
}
