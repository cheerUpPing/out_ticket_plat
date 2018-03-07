package com.harmony.action;

import com.harmony.action.impl.HeartBackAction;
import com.harmony.action.impl.OutTicketAciton;
import com.harmony.action.impl.ReConnectionAction;
import com.harmony.action.impl.ReceviedPrintAction;
import com.harmony.entity.Contants;

import java.util.HashMap;
import java.util.Map;

/**
 * 2017/9/13 14:58.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class ActionOutFactory {

    //动作缓存 动作码action -- 对应动作
    private static Map<String, AbstractAction> actionMap = new HashMap<String, AbstractAction>();

    static {
        //获取票机列表
        actionMap.put(Contants.Action.A0002, new ReceviedPrintAction());
        //心跳
        actionMap.put(Contants.Action.A0003, new HeartBackAction());
        //出票动作
        actionMap.put(Contants.Action.A0006, new OutTicketAciton());
        //票机重连
        actionMap.put(Contants.Action.A0007, new ReConnectionAction());

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
