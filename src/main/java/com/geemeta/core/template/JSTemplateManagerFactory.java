package com.geemeta.core.template;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public class JSTemplateManagerFactory {
    private static Lock lock = new ReentrantLock();
    private static HashMap<String, JSTemplateManager> map = new HashMap<>();

    private JSTemplateManagerFactory(){}
    public static JSTemplateManager get(String name) {
        lock.lock();
        if (!map.containsKey(name))
            map.put(name, new JSTemplateManager());
        lock.unlock();
        return map.get(name);
    }
}
