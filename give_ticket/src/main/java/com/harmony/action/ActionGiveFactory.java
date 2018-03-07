package com.harmony.action;

import com.harmony.action.impl.*;
import com.harmony.entity.Contants;

import java.util.HashMap;
import java.util.Map;

/**
 * 2017/9/13 14:58.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class ActionGiveFactory {

    //动作缓存 动作码action -- 对应动作
    private static Map<String, AbstractAction> actionMap = new HashMap<String, AbstractAction>();

    static {
        //出票响应动作[回传出票数据]
        actionMap.put(Contants.Action.A0001, new OutTicketResponseAction());
        //获取票机列表
        actionMap.put(Contants.Action.A0002, new GetPrintsAction());
        //心跳
        actionMap.put(Contants.Action.A0003, new HeartAction());
        //票机登录
        actionMap.put(Contants.Action.A0004, new PrintLoginAction());
        //票机掉线
        actionMap.put(Contants.Action.A0005, new PrintLogoutAction());
        //出票数据返回
        actionMap.put(Contants.Action.A0008, new BackTicketAction());

    }

    /**
     * 获取对应的服务
     *
     * @param actionCode
     * @return
     */
    public static AbstractAction getAction(String actionCode) {
        return actionMap.get(actionCode);
    }

}
