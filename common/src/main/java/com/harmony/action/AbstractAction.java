package com.harmony.action;

import com.harmony.entity.Communication;

/**
 * 2017/9/13 14:59.
 * <p>
 * Email: cheerUpPing@163.com
 */
public abstract class AbstractAction {

    /**
     * 不需要返回数据到客户端 那么就固定返回null
     *
     * @param communication
     * @return
     */
    public abstract Communication doBusiness(Communication communication) throws Exception;

}
