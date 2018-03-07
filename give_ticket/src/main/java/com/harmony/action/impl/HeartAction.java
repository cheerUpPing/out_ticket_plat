package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.Communication;
import com.harmony.entity.GiveCommon;
import com.harmony.entity.RequestEntity;
import com.harmony.entity.ResponseEntity;
import com.harmony.util.LogUtil;

/**
 * 2017/9/14 17:13.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 心跳动作
 */
public class HeartAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        RequestEntity<String> requestEntity = (RequestEntity<String>) communication;
        String msg = requestEntity.getData();
        LogUtil.info(HeartAction.class, "接收心跳", "服务端接收到客户端" + requestEntity.getIp() + "心跳,消息是" + msg);
        return new ResponseEntity<String>(communication.getActionCode(), "pong", GiveCommon.ip);
    }
}
