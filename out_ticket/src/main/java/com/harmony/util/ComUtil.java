package com.harmony.util;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComUtil {
    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     */
    public static SerialPort openPort(String portName, int baudrate) {
        SerialPort serialPort = null;
        try {

            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            //判断是不是串口
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                //设置一下串口的波特率等参数
                serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            }
        } catch (Exception e) {
            System.out.println("串口:" + portName + "不存在或者串口被占用");
            e.printStackTrace();
        } finally {
            return serialPort;
        }
    }
    //PortInUseException 端口已经被占用  IOException串口断开了
    public static void main(String[] args) throws IOException {
        Map<String, Object> sucess = new ConcurrentHashMap<>();
        String com18 = "COM18";
        String com9 = "COM9";
        int baudrate = 9600;
        Object serialPort18 = openPort(com18, baudrate);
        Object serialPort9 = openPort(com9, baudrate);
        sucess.put("serialPort18", serialPort18);
        sucess.put("serialPort9", serialPort9);
        while (true) {
            try {
                serialPort18 = sucess.get("serialPort18");
                serialPort9 = sucess.get("serialPort9");
                if (serialPort18 instanceof SerialPort) {
                    System.out.println("不为空--------------------serialPort18:" + serialPort18);
                    ((SerialPort)serialPort18).getOutputStream().write(1);
                } else {
                    System.out.println("空--------------------serialPort18:" + serialPort18);
                }
                System.out.println("-------------------###########-------------------------");
                if (serialPort9 instanceof SerialPort) {
                    System.out.println("不为空--------------------serialPort9:" + serialPort9);
                    ((SerialPort)serialPort9).getOutputStream().write(1);
                } else {
                    System.out.println("空--------------------serialPort9:" + serialPort9);
                }

                Thread.sleep(2000);
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println("串口出现异常,请重新连接");
                if (sucess.get("serialPort9") instanceof SerialPort) {
                    System.out.println("关闭串口");
                    ((SerialPort) sucess.get("serialPort9")).close();
                    sucess.put("serialPort9", new Object());
                }
                new ReConnectThread(com9, baudrate, sucess).start();
            }
            System.out.println("============================================================");
        }

    }

}

class ReConnectThread extends Thread {

    private String com;

    private int baudrate;

    private Map<String, Object> sucess;

    ReConnectThread(String com, int baudrate, Map<String, Object> sucess) {
        this.com = com;
        this.baudrate = baudrate;
        this.sucess = sucess;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(5000);
                System.out.println("准备重连");
                SerialPort serialPort = ComUtil.openPort(com, baudrate);
                if (serialPort != null){
                    sucess.put("serialPort", serialPort == null ? new Object() : serialPort);
                    sucess.put("in", serialPort == null ? new Object() : serialPort.getInputStream());
                    System.out.println("重连成功");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}