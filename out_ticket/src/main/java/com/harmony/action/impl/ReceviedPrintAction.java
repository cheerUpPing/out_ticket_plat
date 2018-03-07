package com.harmony.action.impl;

import com.harmony.action.AbstractAction;
import com.harmony.comm.OutCommon;
import com.harmony.entity.Communication;
import com.harmony.entity.Print;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.ResponseEntity;
import com.harmony.util.LogUtil;
import com.harmony.util.SerialUtil;

import java.util.List;

/**
 * 2017/9/14 15:24.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 接收服务端返回的票机列表信息
 */
public class ReceviedPrintAction extends AbstractAction {

    public Communication doBusiness(Communication communication) throws Exception {
        ResponseEntity<List<PrintServiceDTO>> responseEntity = (ResponseEntity<List<PrintServiceDTO>>) communication;
        List<PrintServiceDTO> printServiceDTOList = responseEntity.getData();
        //保存票机信息到出票端
        for (PrintServiceDTO printServiceDTO : printServiceDTOList) {
            OutCommon.printServiceDTOMap.put(printServiceDTO.getWebAddress(), printServiceDTO);
        }
        checkPrint(printServiceDTOList);
        //初始化票机发/送接收串口连接
        for (PrintServiceDTO printServiceDTO : printServiceDTOList) {
            SerialUtil.initSerial(printServiceDTO);
        }
        LogUtil.info(ReceviedPrintAction.class, "初始化票机串口", "所有的票机串口初始化完成");
        return null;
    }

    /**
     * 检测出票端配置的票机数据是否服务器全部包含
     *
     * @param printServiceDTOList
     */
    private void checkPrint(List<PrintServiceDTO> printServiceDTOList) {
        StringBuilder sb = new StringBuilder();
        if (printServiceDTOList != null && printServiceDTOList.size() > 0) {
            if (printServiceDTOList.size() != OutCommon.prints.size()) {
                for (Print print : OutCommon.prints) {
                    for (int i = 0; i < printServiceDTOList.size(); i++) {
                        PrintServiceDTO printServiceDTO = printServiceDTOList.get(i);
                        if (print.getIp().equals(printServiceDTO.getWebAddress())) {
                            break;
                        } else if (i == printServiceDTOList.size() - 1) {
                            sb.append(print.getIp()).append(",");
                        }
                    }
                }
                if (sb.length() > 0 && sb.lastIndexOf(",") != -1) {
                    sb.deleteCharAt(sb.lastIndexOf(","));
                    LogUtil.info(ReceviedPrintAction.class, "接收到服务器票机信息", "数据库不存在票机" + sb.toString());
                }
            } else {
                LogUtil.info(ReceviedPrintAction.class, "接收到服务器票机信息", "配置文件print.json配置的票机服务器全部存在");
            }
        } else {
            LogUtil.info(ReceviedPrintAction.class, "接收到服务器票机信息", "服务器不存在配置文件print.json配置的票机");
        }
    }
}
