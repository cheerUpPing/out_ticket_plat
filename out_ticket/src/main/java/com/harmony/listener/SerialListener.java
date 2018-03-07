package com.harmony.listener;

import com.harmony.util.LogUtil;
import com.harmony.util.SerialUtil;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;

/**
 * 2017/9/21 14:48.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 串口监听器
 * 只要有数据到来,那么就直接关闭串口
 * 因为程序只有在 操作键盘的时候 才移除监听器，其他时候都打开监听器
 */
@Deprecated
public class SerialListener implements SerialPortEventListener {

    //票机ip
    private String printIp = null;
    //是否是发送串口
    private boolean isSend = true;

    private SerialPort serialPort = null;


    //串口是否运行有数据到来[非正常状态下是不允许有数据到来的,否则就判定为异常]
    private volatile boolean isReading = false;

    private Object isReadingLock = new Object();

    public SerialListener(String printIp, boolean isSend, SerialPort serialPort) throws IOException {
        this.printIp = printIp;
        this.isSend = isSend;
        this.serialPort = serialPort;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        String sendOrReceived = isSend ? "发送" : "接收";
        switch (serialPortEvent.getEventType()) {

            case SerialPortEvent.BI: // 10 通讯中断
                SerialUtil.printSerialOffLine(printIp, isSend, serialPort);
                break;

            case SerialPortEvent.OE: // 7 溢位（溢出）错误

            case SerialPortEvent.FE: // 9 帧错误

            case SerialPortEvent.PE: // 8 奇偶校验错误

            case SerialPortEvent.CD: // 6 载波检测

            case SerialPortEvent.CTS: // 3 清除待发送数据

            case SerialPortEvent.DSR: // 4 待发送数据准备好了

            case SerialPortEvent.RI: // 5 振铃指示

            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
                LogUtil.info(SerialListener.class, "票机掉线", "票机" + printIp + "串口" + sendOrReceived + "缓冲区已清空");
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
                try {
                    byte[] bytes = SerialUtil.readFromPort(serialPort);
                    String rec = new String(bytes, "gbk");
                    System.out.println("rec" + rec);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
