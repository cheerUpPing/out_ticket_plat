package com.harmony.thread;

import com.harmony.util.LogUtil;
import com.harmony.util.SerialUtil;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 2017/9/22 15:10.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 轮训串口状态线程
 */
public class CheckSerialThread extends Thread {

    private String printIp;

    private SerialPort serialPort;

    private boolean isSend;

    private int loopTime = 5;

    private InputStream in;

    private int available;

    public CheckSerialThread(String printIp, boolean isSend, SerialPort serialPort) {
        this.printIp = printIp;
        this.serialPort = serialPort;
        this.isSend = isSend;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(loopTime);
                if (serialPort != null) {
                    in = serialPort.getInputStream();
                    available = in.available();
                }
                String sendOrReceive = isSend ? "发送" : "接收";
                LogUtil.info(CheckSerialThread.class, "票机串口状态轮训", "票机" + printIp + sendOrReceive + "串口正常");
            } catch (Exception e) {
                if (e instanceof IOException) {
                    SerialUtil.printSerialOffLine(printIp, isSend, serialPort);
                    break;
                }
            }
        }
    }
}
