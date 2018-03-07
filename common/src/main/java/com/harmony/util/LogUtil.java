package com.harmony.util;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 2017/4/20 9:53.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 把错误信息打印到日志文件
 */
public class LogUtil {

    /**
     * 获取错误堆栈信息
     *
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        StringBuffer buffer = stringWriter.getBuffer();
        if (t != null){
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
        }
        return buffer.toString();
    }

    /**
     * 打印日志信息info
     *
     * @param cls
     * @param title
     * @param msg
     */
    public static void info(Class cls, String title, String msg) {
        Logger.getLogger(cls).info(title + "----------------->" + msg);
    }

    /**
     * 打印日志信息error
     *
     * @param cls
     * @param title
     * @param msg
     */
    public static void error(Class cls, String title, String msg) {
        Logger.getLogger(cls).error(title + "----------------->" + msg);
    }
}
