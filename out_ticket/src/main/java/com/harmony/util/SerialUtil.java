package com.harmony.util;

import com.harmony.comm.OutCommon;
import com.harmony.entity.Contants;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.thread.CheckSerialThread;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;

/**
 * 2017/9/14 16:24.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 串口工具类
 */
public class SerialUtil {

    /**
     * 初始化每个票机的串口[发送/接收]
     * 这个方法不会向外抛出异常
     *
     * @param printServiceDTO
     */
    public static void initSerial(PrintServiceDTO printServiceDTO) {
        String sendCom = "COM" + printServiceDTO.getSendCom();
        String receiveCom = "COM" + printServiceDTO.getReceiveCom();
        if (connectSerial(printServiceDTO.getWebAddress(), sendCom, true)) {
            LogUtil.info(SerialUtil.class, "初始化票机发送串口", "票机" + printServiceDTO.getWebAddress() + "发送串口" + sendCom + "连接成功");
        } else {
            LogUtil.info(SerialUtil.class, "初始化票机发送串口", "票机" + printServiceDTO.getWebAddress() + "发送串口" + sendCom + "连接失败");
        }
        if (connectSerial(printServiceDTO.getWebAddress(), receiveCom, false)) {
            LogUtil.info(SerialUtil.class, "初始化票机接收串口", "票机" + printServiceDTO.getWebAddress() + "接收串口" + receiveCom + "连接成功");
        } else {
            LogUtil.info(SerialUtil.class, "初始化票机接收串口", "票机" + printServiceDTO.getWebAddress() + "接收串口" + receiveCom + "连接失败");
        }

    }

    /**
     * 第一次要连接串口成功，否则后面不会重连
     * 这个方法异常不会抛出来
     *
     * @param printIp
     * @param serialCom
     * @param isSend
     * @return
     */
    public static boolean connectSerial(String printIp, String serialCom, boolean isSend) {
        String printType = OutCommon.printServiceDTOMap.get(printIp).getTerminalType();
        int baudrate = isSend ? OutCommon.baudrate_send : OutCommon.baudrateMap_received.get(printType);
        SerialPort channel = isSend ? OutCommon.getSendChannel(printIp) : OutCommon.getReceivedChannel(printIp);
        try {
            //和串口通信的通道
            if (channel != null) {

            } else {
                if (isExist(printIp, serialCom)) {
                    channel = openPort(serialCom, baudrate);
                    //启动串口是否掉线轮询器
                    new CheckSerialThread(printIp, isSend, channel).start();
                    //把串口通道保存到缓存里面
                    if (isSend) {
                        OutCommon.addSendChannel(printIp, channel);
                    } else {
                        OutCommon.addReceivedChannel(printIp, channel);
                    }
                    Object otherChannel = (isSend ? OutCommon.getReceivedChannel(printIp) : OutCommon.getSendChannel(printIp));
                    //如果发送和结束串口都连接成功，那么就通知socket服务端 票机连接成功
                    if (otherChannel != null) {
                        LogUtil.info(SerialUtil.class, "票机串口连接", "票机" + printIp + "发送和接收串口都连接成功");
                        //通知连接成功
                        SocketUtil.sendSocketToServer(Contants.Action.A0004, printIp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return (channel != null);
        }
    }

    /**
     * 检测串口是否存在
     *
     * @param comm
     * @param printIp
     * @return
     */
    private static boolean isExist(String printIp, String comm) {
        boolean isExist = true;
        CommPortIdentifier commPortIdentifier = null;
        try {
            commPortIdentifier = CommPortIdentifier.getPortIdentifier(comm);
            if (commPortIdentifier == null) {
                isExist = false;
            }
        } catch (NoSuchPortException e) {
            isExist = false;
            LogUtil.info(SerialUtil.class, "初始化串口", "票机" + printIp + "串口【" + comm + "】不存在," + LogUtil.getStackTrace(e));
        }
        return isExist;
    }

    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public static ArrayList<String> findPort() {

        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<>();
        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     */
    public static final SerialPort openPort(String portName, int baudrate) throws Exception {
        //通过端口名识别端口
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
        CommPort commPort = portIdentifier.open(portName, 2000);
        SerialPort serialPort = null;
        //判断是不是串口
        if (commPort instanceof SerialPort) {
            serialPort = (SerialPort) commPort;
            try {
                //设置一下串口的波特率等参数
                serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                //serialPort.setDTR(true);
                //serialPort.setRTS(true);
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }
        }
        return serialPort;
    }

    /**
     * 关闭串口
     */
    public static void closePort(SerialPort serialPort) {


        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * 往串口发送数据
     * 如果操作键盘,那么就移除监听器,否则就监听键盘
     *
     * @param serialPort 串口对象
     * @param order      待发送数据
     */
    public static void sendToPort(String printIp, SerialPort serialPort, byte[] order) throws Exception {
        OutputStream out = null;
        try {
            out = serialPort.getOutputStream();
            for (byte b : order) {
                out.write(b);
                TimeUnit.MILLISECONDS.sleep(50);
            }
            //out.write(order);
            out.flush();
        } catch (IOException e) {
            LogUtil.info(SerialUtil.class, "发送数据", "票机" + printIp + "发送串口出现异常");
            SerialUtil.printSerialOffLine(printIp, true, serialPort);
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    public static byte[] readFromPort(SerialPort serialPort) throws Exception {
        InputStream in = null;
        byte[] bytes = null;
        try {
            in = serialPort.getInputStream();
            int bufflenth = in.available();        //获取buffer里的数据长度
            while (bufflenth != 0) {
                bytes = new byte[bufflenth];    //初始化byte数组为buffer中数据的长度
                in.read(bytes);
                bufflenth = in.available();
            }
        } catch (IOException e) {
            throw new IOException();
        }
        return bytes;
    }

    /**
     * 添加监听器
     *
     * @param port     串口对象
     * @param listener 串口监听器
     * @throws TooManyListenersException 监听类对象过多
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener) throws TooManyListenersException {
        //给串口添加监听器
        port.addEventListener(listener);
        //设置当有数据到达时唤醒监听接收线程
        port.notifyOnDataAvailable(true);
        //设置当通信中断时唤醒中断线程
        port.notifyOnBreakInterrupt(true);
    }

    /**
     * 票机串口掉线
     *
     * @param printIp
     * @param serialPort
     */
    public static void printSerialOffLine(String printIp, boolean isSend, SerialPort serialPort) {
        String sendOrRecived = isSend ? "发送" : "接收";
        LogUtil.info(SerialUtil.class, "串口掉线", "票机" + printIp + sendOrRecived + "串口掉线");
        //关闭串口资源
        if (serialPort != null) {
            serialPort.close();
        }
        //从串口缓存中删除异常的端口
        if (isSend && OutCommon.getSendChannel(printIp) != null) {
            OutCommon.deleteSendChannel(printIp);
            //这里是否需要重连


        }
        if (!isSend && OutCommon.getReceivedChannel(printIp) != null) {
            OutCommon.deleteReceivedChannel(printIp);
            //这里是否需要重连


        }
        SocketUtil.sendSocketToServer(Contants.Action.A0005, printIp);


    }

    public static void main(String[] args) throws Exception {
        try {
            SerialPort serialPort = openPort("COM12", 9600);
            //byte[] bytes = {(byte) 0xcb, 0x09, (byte) 0xcb, (byte) 0xcb, 0x09, (byte) 0xcb};
            byte[] bytes = {(byte) 0xdb, 0x09, (byte) 0xdb, (byte) 0xb4, 0x09, (byte) 0xb4, (byte) 0xd4, 0x09, (byte) 0xd4, (byte) 0xc3, 0x09, (byte) 0xc3, (byte) 0xb4, 0x09, (byte) 0xb4, (byte) 0xf4, 0x09, (byte) 0xf4};
            String keys = "5 1 5 1 4 0 0 1 0 F1 4 0 0 2 1 F1 F2 0 2 ENTER";
            byte[] keyss = KeyUtil.getKeyCodesByKeys(keys);
            //完全可以和票机通信
            //sendToPort(serialPort, keyss);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
