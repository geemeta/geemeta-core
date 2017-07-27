package com.geemeta.core.template;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public class SQLTemplateManagerFactory {
    private static Lock lock = new ReentrantLock();
    private static HashMap<String, SQLTemplateManager> map = new HashMap<>();

    private SQLTemplateManagerFactory(){}
    public static SQLTemplateManager get(String name) {
        lock.lock();
        if (!map.containsKey(name))
            map.put(name, new SQLTemplateManager());
        lock.unlock();
        return map.get(name);
    }
}
