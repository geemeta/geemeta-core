package com.geemeta.core;


import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public abstract class Manager {

    protected final static String RN_WIN = "\r\n"; //in WinOS
    protected final static String RN_LINUX = "\n";    //in Linux

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

    protected void parseDirectory(File file) throws IOException {
        Assert.isTrue(file.exists(),"不存在的目录："+file.getPath());
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
}
