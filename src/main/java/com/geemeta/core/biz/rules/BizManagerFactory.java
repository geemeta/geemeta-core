package com.geemeta.core.biz.rules;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author itechgee@126.com
 * @date 2017/6/6.
 */
public class BizManagerFactory {
    private static Lock lock = new ReentrantLock();
    private static HashMap<String, BizRuleManager> map = new HashMap<>();

    private BizManagerFactory() {
    }

    public static BizRuleManager get(String name) {
        lock.lock();
        if (!map.containsKey(name))
            map.put(name, new BizRuleManager());
        lock.unlock();
        return map.get(name);
    }
}
