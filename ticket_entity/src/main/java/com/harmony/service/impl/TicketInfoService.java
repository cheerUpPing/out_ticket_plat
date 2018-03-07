package com.harmony.service.impl;

import com.harmony.entity.TicketInfo;
import com.harmony.service.BaseService;
import com.harmony.util.CommmUtil;
import com.harmony.util.JdbcUtil;

import javax.sql.rowset.serial.SerialBlob;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * 2017/9/22 17:45.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class TicketInfoService implements BaseService<TicketInfo, Long> {

    public void insert(String tempId, String ticketInfo) throws UnsupportedEncodingException, SQLException {
        SerialBlob serialBlob = new SerialBlob(ticketInfo.getBytes("gbk"));//这里可以用utf-8
        String sql = "insert into TICKETINFO (TEMPID,TICKETIMAGE,TICKETIMAGE)values(?,?,?)";
        Object[] objects = new Object[]{tempId, serialBlob, Long.parseLong(CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss))};
        JdbcUtil.getQueryRunner().update(sql, objects);
    }

    @Override
    public void add(TicketInfo ticketInfo) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(TicketInfo ticketInfo) {

    }

    @Override
    public TicketInfo select(Long aLong) {
        return null;
    }
}
