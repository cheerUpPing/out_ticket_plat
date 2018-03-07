package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.Communication;
import com.harmony.util.LogUtil;

/**
 * 2017/9/15 12:38.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 收到服务端心跳返回
 */
public class HeartBackAction extends AbstractAction {
    public Communication doBusiness(Communication communication) throws Exception {
        String msg = (String) communication.getData();
        LogUtil.info(HeartBackAction.class, "客户端收到心跳回馈", "心跳信息" + msg);
        return null;
    }
}
