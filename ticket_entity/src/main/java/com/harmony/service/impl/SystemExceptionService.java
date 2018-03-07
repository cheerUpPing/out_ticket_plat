package com.harmony.service.impl;

import com.harmony.entity.SystemExceptionDTO;
import com.harmony.service.BaseService;
import com.harmony.util.CommmUtil;
import com.harmony.util.JdbcUtil;
import com.harmony.util.LogUtil;

import java.sql.SQLException;

/**
 * 2017/9/18 15:04.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class SystemExceptionService implements BaseService<SystemExceptionDTO, Long> {

    /**
     * 存储异常
     *
     * @param message
     */
    public void insert(String message, Exception e) throws SQLException {
        /*SystemExceptionDTO systemExceptionDTO = new SystemExceptionDTO();
        systemExceptionDTO.setMessage(message + (e == null ? "" : e.getMessage()));
        systemExceptionDTO.setThrowable(LogUtil.getStackTrace(e));
        systemExceptionDTO.setIsRead("0");
        systemExceptionDTO.setSystemTime(CommmUtil.getTime(CommmUtil.yyyy_MM_dd_HH_mm_ss));*/
        String sql = "insert into SYSTEM_EXCEPTION_MESSAGE(MESSAGE,THROWABLE,ISREAD,SYSTEMTIME) values(?,?,?,?)";
        Object params[] = {message + (e == null ? "" : e.getMessage()), LogUtil.getStackTrace(e), "0", CommmUtil.getTime(CommmUtil.yyyy_MM_dd_HH_mm_ss)};
        JdbcUtil.getQueryRunner().update(sql, params);
    }

    @Override
    public void add(SystemExceptionDTO systemExceptionDTO) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(SystemExceptionDTO systemExceptionDTO) {

    }

    @Override
    public SystemExceptionDTO select(Long aLong) {
        return null;
    }

    public static void main(String[] args) throws SQLException {
        SystemExceptionService systemExceptionService = new SystemExceptionService();
        systemExceptionService.insert("abcdefghijklmn", null);
    }
}
