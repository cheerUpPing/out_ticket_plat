package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.entity.Communication;
import com.harmony.entity.GiveCommon;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.ResponseEntity;
import com.harmony.service.impl.PrintService;
import com.harmony.service.ServiceFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 2017/9/13 17:53.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 获取票机信息
 */
public class GetPrintsAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws SQLException {
        PrintService printService = (PrintService) ServiceFactory.getService("printService");
        String ips = (String) communication.getData();
        List<PrintServiceDTO> printServiceDTOs = printService.selectByIps(ips);
        return new ResponseEntity<List<PrintServiceDTO>>(communication.getActionCode(), printServiceDTOs, GiveCommon.ip);
    }
}
