package com.harmony.thread;

import com.harmony.entity.Contants;
import com.harmony.util.CommmUtil;
import com.harmony.util.LogUtil;
import com.harmony.util.SerialUtil;
import com.harmony.util.SocketUtil;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 2017/9/22 11:01.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机接收串口 接收数据
 */
public class StreamReadThread extends Thread {

    private String printIp = null;

    private SerialPort serialPort = null;

    private Map<String, String> data = null;

    private InputStream in = null;

    private byte[] allBytes = null;

    public StreamReadThread(String printIp, String tempId, SerialPort serialPort) throws IOException {
        this.printIp = printIp;
        this.serialPort = serialPort;
        this.in = serialPort.getInputStream();
        data = new HashMap<>(2);
        data.put("tempId", tempId);//对应出票的tempid
        data.put("data", null);//为null的时候 那么就表明出票失败
    }

    @Override
    public void run() {
        try {
            byte[] readBytes = new byte[10240];
            //如果可用字节数大于零则开始循环并获取数据
            int readCount = 0;
            while ((in.available()) > 0 && (readCount = in.read(readBytes)) != -1) {
                //从串口的输入流对象中读入数据并将数据存放到缓存数组中
                allBytes = CommmUtil.mergeBytes(allBytes, readBytes, readCount);
            }
            //读取数据完毕
            if (allBytes != null && allBytes.length > 0) {//出票成功
                String rec = new String(allBytes, "gbk");
                LogUtil.info(StreamReadThread.class, "接收串口数据轮训", "票机" + printIp + "成功接收数据" + rec);
                data.put("data", rec);
                SocketUtil.<Map>sendSocketToServer(Contants.Action.A0008, data, printIp);
            }
        } catch (Exception e) {
            LogUtil.info(StreamReadThread.class, "接收串口数据轮训", "票机" + printIp + "接收串口出现问题" + LogUtil.getStackTrace(e));
            //通知票机掉线
            SerialUtil.printSerialOffLine(printIp, false, serialPort);
        }
    }
}
