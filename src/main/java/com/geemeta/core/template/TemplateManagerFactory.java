package com.geemeta.core.template;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public class TemplateManagerFactory {
    private static Lock lock = new ReentrantLock();
    private static HashMap<String, TemplateManager> map = new HashMap<>();

    private TemplateManagerFactory(){}
    public static TemplateManager get(String name) {
        lock.lock();
        if (!map.containsKey(name))
            map.put(name, new TemplateManager());
        lock.unlock();
        return map.get(name);
    }
}
