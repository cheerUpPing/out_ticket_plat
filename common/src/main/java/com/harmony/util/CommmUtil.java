package com.harmony.util;

import com.harmony.entity.Contants;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 2017/9/15 14:20.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class CommmUtil {

    public static String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取配置文件对象
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static Properties getProperties(String fileName) {
        Properties pro = new Properties();
        InputStream in = Contants.class.getClassLoader().getResourceAsStream(fileName);
        if (in == null) {
            LogUtil.info(Contants.class, "初始化错误", "配置文件" + fileName + "不存在");
            throw new RuntimeException("初始化错误...配置文件" + fileName + "不存在");
        }
        try {
            pro.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    /**
     * 获取对应时间格式
     *
     * @param timeStyle
     * @return
     */
    public static String getTime(String timeStyle) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeStyle);
        return sdf.format(new Date());
    }

    /**
     * 合并两个字节数组
     *
     * @param befores
     * @param lasts
     * @param lastNeedCopyLength 最后数据需要复制的长度
     * @return
     */
    public static byte[] mergeBytes(byte[] befores, byte[] lasts, int lastNeedCopyLength) {
        int beforeLength = (befores == null || befores.length == 0) ? 0 : befores.length;
        int lastLength = (lasts == null || lasts.length == 0) ? 0 : lastNeedCopyLength;
        byte[] result = new byte[beforeLength + lastLength];
        for (int i = 0; i < beforeLength; i++) {
            result[i] = befores[i];
        }
        for (int i = 0; i < lastLength; i++) {
            result[i + beforeLength] = lasts[i];
        }
        return result;
    }

    public static void main(String[] args) {
        byte[] a = {0x01, 0x02, 0x03};
        byte[] b = {0x11, 0x12, 0x13};
        byte[] c = mergeBytes(a, b, b.length);
        for (byte d : c) {
            System.out.println(d);
        }
    }
}
