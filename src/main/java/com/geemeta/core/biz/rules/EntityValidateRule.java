package com.geemeta.core.biz.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

/**
 * @author itechgee@126.com
 * @date 2017/6/5.
 */
public class EntityValidateRule extends BasicRule {

    public EntityValidateRule() {
        super("EntityValidateRule", "实体校验规则。");
    }

    @Override
    public boolean evaluate(Facts facts) {
        //my rule conditions
        return true;
    }

    @Override
    public void execute(Facts facts) throws Exception {
        //my rule actions
    }
}
