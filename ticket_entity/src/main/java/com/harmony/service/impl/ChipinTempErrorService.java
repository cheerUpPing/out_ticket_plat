package com.harmony.service.impl;

import com.harmony.entity.ChipinTempErrorDTO;
import com.harmony.service.BaseService;
import com.harmony.service.ServiceFactory;
import com.harmony.util.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 2017/9/25 10:52.
 * <p>
 * Email: cheerUpPing@163.com
 */
public class ChipinTempErrorService implements BaseService<ChipinTempErrorDTO, Long> {

    /**
     * 保存blob字段
     *
     * @param tempId
     * @param terminaltype
     * @param exceptioninfo
     * @param printlatertime
     * @param voteproccesstime
     * @param ticketimage
     * @throws SQLException
     */
    public void insert(String tempId, String terminaltype, String exceptioninfo, long printlatertime, long voteproccesstime, String ticketimage) throws SQLException {
        String sql = "insert into T_CHIPIN_TEMP_ERROR (FROMSERIALNO,TERMINALTYPE,EXCEPTIONINFO,PRINTLATERTIME,VOTEPROCCESSTIME,TICKETIMAGE)values(?,?,?,?,?,EMPTY_BLOB())";
        Object[] objects = new Object[]{tempId, terminaltype, exceptioninfo, printlatertime, voteproccesstime};
        Connection connection = JdbcUtil.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(connection,sql,objects);
        Long tempid = queryRunner.query(connection,"SELECT LAST_INSERT_ID()",new ScalarHandler<Long>(1));
        System.out.println(tempid);

    }

    public static void main(String[] args) throws SQLException {
        ChipinTempErrorService chipinTempErrorService = (ChipinTempErrorService) ServiceFactory.getService("chipinTempErrorService");
        chipinTempErrorService.insert("1000000561103974","C8","错错错",20170925122123l,20170925122124l,"");
        System.out.println("ddddd");
    }


    @Override
    public void add(ChipinTempErrorDTO chipinTempErrorDTO) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(ChipinTempErrorDTO chipinTempErrorDTO) {

    }

    @Override
    public ChipinTempErrorDTO select(Long aLong) {
        return null;
    }
}
