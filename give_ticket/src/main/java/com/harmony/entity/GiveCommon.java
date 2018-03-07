package com.harmony.entity;

import com.harmony.thread.PrintThread;
import com.harmony.util.CommmUtil;
import com.harmony.util.LogUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 2017/9/13 14:18.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 分发模块常量
 */
public class GiveCommon {

    //配置文件对象
    public static Properties properties = null;

    //本地ip
    public static String ip;

    //本机socket服务端口
    public static int port = 0;

    //服务端读空闲时间[单位s]
    public static int read_idle = 0;

    //保存正常连接的票机ip[发送/接收串口没有出现问题] 票机ip -- 出票socket端ip
    public static Map<String, String> normalPrints = new HashMap<String, String>();
    //对应锁 用于 正常连接的票机normalPrints 增/删/改/查/遍历
    public static final Object normalPrintsLock = new Object();

    //socket客户端ip -- socket通道
    private static Map<String, Channel> channelMap = new HashMap<String, Channel>();

    //保存票机的当前操作状态 票机ip -- 票机对象
    private static Map<String, PrintServiceDTO> printLatest = new ConcurrentHashMap<String, PrintServiceDTO>();

    //保存票机的工作线程 票机ip -- 票机工作线程
    private static Map<String, PrintThread> printThreadMap = new HashMap<String, PrintThread>();
    private static final Object printThreadMapLock = new Object();

    static {
        properties = CommmUtil.getProperties("config.properties");
        port = Integer.parseInt(properties.getProperty("port", "8086"));
        read_idle = Integer.parseInt(properties.getProperty("read_idle", "15"));
    }

    /**
     * 如果正常票机不存在那么就增加，否则就不增加
     *
     * @param printIp 接收/发送串口正常连接的票机ip
     * @param ip      出票端pc的ip
     */
    public static void addPrintIfAbsent(String printIp, String ip) {
        synchronized (normalPrintsLock) {
            if (normalPrints.get(printIp) == null) {
                normalPrints.put(printIp, ip);
            }
        }
    }

    /**
     * 获取 正常票机
     *
     * @param printIp
     */
    public static String getPrint(String printIp) {
        synchronized (normalPrintsLock) {
            return normalPrints.get(printIp);
        }
    }

    /**
     * 移除 接收/发送串口出问题的票机
     *
     * @param printIp
     */
    public static void deletePrint(String printIp) {
        synchronized (normalPrintsLock) {
            normalPrints.remove(printIp);
        }
    }

    /**
     * 增加通道
     *
     * @param ip
     * @param channel
     */
    public synchronized static void addChannel(String ip, Channel channel) {
        channelMap.put(ip, channel);
    }

    /**
     * 获取通道
     *
     * @param ip
     */
    public synchronized static Channel getChannel(String ip) {
        return channelMap.get(ip);
    }

    /**
     * 删除通道
     *
     * @param ip
     */
    public synchronized static void deleteChannel(String ip) {
        channelMap.remove(ip);
    }

    /**
     * 移除掉线的socket通道
     *
     * @param channel
     */
    public synchronized static void removeOffLineChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel savedChannel = entry.getValue();
            if (savedChannel == channel) {
                String ip = entry.getKey();
                channelMap.remove(ip);
                LogUtil.info(GiveCommon.class, "移除掉线的socket通道", "出票端" + ip + "通道" + channel + "因为掉线被移除");
                break;
            }
        }
    }

    /**
     * 添加最新票机
     *
     * @param printServiceDTO
     */
    public synchronized static void addLatestPrint(PrintServiceDTO printServiceDTO) {
        printLatest.put(printServiceDTO.getWebAddress(), printServiceDTO);
    }

    /**
     * 删除最新票机
     *
     * @param printIp
     */
    public synchronized static void deleteLatestPrint(String printIp) {
        printLatest.remove(printIp);
    }

    /**
     * 查询最新票机
     *
     * @param printIp
     */
    public synchronized static PrintServiceDTO getLatestPrint(String printIp) {
        return printLatest.get(printIp);
    }

    /**
     * 添加票机工作线程
     *
     * @param printIP
     * @param printThread
     */
    public static void addPrintThread(String printIP, PrintThread printThread) {
        synchronized (printThreadMapLock) {
            printThreadMap.put(printIP, printThread);
        }
    }

    /**
     * 获取工作票机线程
     *
     * @param printIp
     * @return
     */
    public static PrintThread getPrintThread(String printIp) {
        synchronized (printThreadMapLock) {
            return printThreadMap.get(printIp);
        }
    }

    /**
     * 移除工作票机线程
     *
     * @param printIp
     * @return
     */
    public static void deletePrintThread(String printIp) {
        synchronized (printThreadMapLock) {
            printThreadMap.remove(printIp);
        }
    }

    /**
     * 票机掉线
     *
     * @param printIp
     */
    public static void printOffLine(String printIp) {
        synchronized (printThreadMapLock) {
            if (printThreadMap.get(printIp) != null) {
                PrintThread printThread = printThreadMap.get(printIp);
                printThread.printCanWork = false;
                printThreadMap.remove(printIp);
            }
        }
    }

}
