package com.harmony.comm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmony.entity.LookUp;
import com.harmony.entity.Print;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.emu.PrintType;
import com.harmony.util.CommmUtil;
import com.harmony.util.LogUtil;
import gnu.io.SerialPort;
import io.netty.channel.Channel;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 2017/9/13 17:45.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 出票模块,常量类
 */
public class OutCommon {

    //保存pc机 配置文件配置的票机数量
    public static List<Print> prints = new ArrayList<Print>();

    //出票模块pc的ip
    public static String ip = null;

    //远程socket服务器ip
    public static String remoteIp;
    //远程socket服务器端口
    public static int port;

    //客户端读和写空闲[单位s]
    public static int read_write_idle = 0;

    //配置文件config.properties
    public static Properties properties = null;

    //保存服务端返回的票机数量[这个变量不需要锁同步] 票机ip -- 票机对象
    public static Map<String, PrintServiceDTO> printServiceDTOMap = new HashMap<>(prints.size());

    //连接分票模块
    public static Channel toGiveChannel = null;

    //保存串口发送channel printIp -- sendChannel
    private static Map<String, SerialPort> serialSendChannnel = new HashMap<>(prints.size());
    public static final Object serialSendChannnelLock = new Object();

    //保存串口接收channel printIp -- receivedChannel
    private static Map<String, SerialPort> serialReceivedChannnel = new HashMap<>(prints.size());
    public static final Object serialReceivedChannnelLock = new Object();

    //接收串口 波特率
    public static Map<String, Integer> baudrateMap_received = new HashMap<>();
    //发送串口 波特率
    public static int baudrate_send = 9600;

    static {
        prints = getPrints();
        properties = CommmUtil.getProperties("config.properties");
        remoteIp = properties.getProperty("remote_server_ip", "127.0.0.1");
        port = Integer.parseInt(properties.getProperty("remote_server_port", "8086"));
        read_write_idle = Integer.parseInt(properties.getProperty("read_write_idle", "5"));
        baudrateMap_received.put(PrintType.HS.getType(), 57600);
        baudrateMap_received.put(PrintType.C8USB.getType(), 57600);
        baudrateMap_received.put(PrintType.SC6.getType(), 57600);
        baudrateMap_received.put(PrintType.LDR.getType(), 115200);
        baudrateMap_received.put(PrintType.C8.getType(), 9600);
        baudrateMap_received.put(PrintType.CP86.getType(), 9600);
        baudrateMap_received.put(PrintType.SC4.getType(), 9600);
        baudrateMap_received.put(PrintType.LOT.getType(), 9600);
        baudrateMap_received.put(PrintType.YTD.getType(), 9600);
    }

    /**
     * 添加 发送串口
     *
     * @param printIp
     * @param channel
     */
    public static void addSendChannel(String printIp, SerialPort channel) {
        synchronized (serialSendChannnelLock) {
            serialSendChannnel.put(printIp, channel);
        }
    }

    /**
     * 删除 发送串口
     *
     * @param printIp
     */
    public static void deleteSendChannel(String printIp) {
        synchronized (serialSendChannnelLock) {
            serialSendChannnel.remove(printIp);
        }
    }

    /**
     * 获取 发送串口
     *
     * @param printIp
     */
    public static SerialPort getSendChannel(String printIp) {
        synchronized (serialSendChannnelLock) {
            return serialSendChannnel.get(printIp);
        }
    }


    /**
     * 添加 接收串口
     *
     * @param printIp
     * @param channel
     */
    public static void addReceivedChannel(String printIp, SerialPort channel) {
        synchronized (serialReceivedChannnelLock) {
            serialReceivedChannnel.put(printIp, channel);
        }
    }

    /**
     * 删除 接收串口
     *
     * @param printIp
     */
    public static void deleteReceivedChannel(String printIp) {
        synchronized (serialReceivedChannnelLock) {
            serialReceivedChannnel.remove(printIp);
        }
    }

    /**
     * 获取 发送串口
     *
     * @param printIp
     */
    public static SerialPort getReceivedChannel(String printIp) {
        synchronized (serialReceivedChannnelLock) {
            return serialReceivedChannnel.get(printIp);
        }
    }

    /**
     * 获取配置文件配置的票机
     *
     * @return
     */
    private static List<Print> getPrints() {
        InputStream inputStream = OutCommon.class.getClassLoader().getResourceAsStream("print.json");
        if (inputStream == null) {
            LogUtil.info(OutCommon.class, "加载配置文件print.json失败", "文件不存在");
            throw new RuntimeException("加载配置文件print.json失败,文件不存在");
        }
        String json = null;
        List<Print> printList = null;
        try {
            json = IOUtils.toString(inputStream, "utf-8");
            JSONObject jsonObject = JSON.parseObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("prints");
            printList = jsonArray.toJavaList(Print.class);
        } catch (IOException e) {
            LogUtil.info(OutCommon.class, "加载配置文件print.json失败", "配置内容出错" + LogUtil.getStackTrace(e));
            throw new RuntimeException("加载配置文件print.json失败,配置内容出错");
        }
        return printList;
    }

    /**
     * 查询通道对应的票机ip
     *
     * @param channel
     * @return
     */
    public static LookUp lookUpPrintIp(SerialPort channel) {
        LookUp lookUp = null;
        synchronized (serialSendChannnelLock) {
            lookUp = passBySet(channel, OutCommon.serialSendChannnel.entrySet(), true);
        }
        if (lookUp == null) {
            synchronized (serialReceivedChannnelLock) {
                lookUp = passBySet(channel, OutCommon.serialReceivedChannnel.entrySet(), false);
            }
        }
        return lookUp;
    }

    /**
     * 遍历 发送串口缓存map  接收串口缓存map
     * 查询对应通道的票机信息
     *
     * @param channel
     * @param entries
     * @param isSendCom
     * @return
     */
    public static LookUp passBySet(SerialPort channel, Set<Map.Entry<String, SerialPort>> entries, boolean isSendCom) {
        for (Map.Entry<String, SerialPort> entry : entries) {
            Object val = entry.getValue();
            //==如果没有重写object方法，那么就是指地址相等
            if (channel == val) {
                LookUp lookUp = new LookUp();
                lookUp.setPrintIp(entry.getKey());
                lookUp.setSendCom(isSendCom);
                return lookUp;
            }
        }
        return null;
    }

}
