package com.harmony.service;

import com.harmony.service.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 2017/9/13 15:52.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class ServiceFactory {

    //服务缓存 动作码action -- 对应服务
    private static Map<String, BaseService> serviceMap = new HashMap<String, BaseService>();

    static {
        serviceMap.put("printService", new PrintService());
        serviceMap.put("chipinTempService", new ChipinTempService());
        serviceMap.put("systemExceptionService", new SystemExceptionService());
        serviceMap.put("ticketInfoService", new TicketInfoService());
        serviceMap.put("chipinTempErrorService", new ChipinTempErrorService());
    }

    /**
     * 获取对应的服务
     *
     * @param serviceName
     * @return
     */
    public static BaseService getService(String serviceName) {
        BaseService action = serviceMap.get(serviceName);
        if (action == null) {
            throw new RuntimeException(serviceName + "服务不存在");
        }
        return action;
    }


}
