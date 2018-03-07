package com.harmony.service.impl;

import com.harmony.entity.PrintServiceDTO;
import com.harmony.entity.emu.PrintStatus;
import com.harmony.service.BaseService;
import com.harmony.util.CommmUtil;
import com.harmony.util.JdbcUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 2017/9/13 15:31.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 票机服务
 */
public class PrintService implements BaseService<PrintServiceDTO, Long> {

    /**
     * 通过ip查询
     *
     * @param ip
     * @return
     * @throws SQLException
     */
    public PrintServiceDTO selectByIp(String ip) throws SQLException {
        QueryRunner queryRunner = JdbcUtil.getQueryRunner();
        String sql = "select * from PRINT_SERVICE_CONFIG where WEBADDRESS ='" + ip + "'";
        return queryRunner.query(sql, new BeanHandler<PrintServiceDTO>(PrintServiceDTO.class));
    }

    /**
     * 通过ips查询
     *
     * @param ips
     * @return
     * @throws SQLException
     */
    public List<PrintServiceDTO> selectByIps(String ips) throws SQLException {
        QueryRunner queryRunner = JdbcUtil.getQueryRunner();
        String sql = "select * from PRINT_SERVICE_CONFIG where WEBADDRESS in (" + ips + ")";
        return queryRunner.query(sql, new BeanListHandler<PrintServiceDTO>(PrintServiceDTO.class));
    }

    /**
     * 更新票机的状态
     *
     * @param printStatus
     * @param ip
     * @throws SQLException
     */
    public void updateStatusByIp(PrintStatus printStatus, String ip) throws SQLException {
        QueryRunner queryRunner = JdbcUtil.getQueryRunner();
        String sql = "UPDATE PRINT_SERVICE_CONFIG SET CURRENTOPERATING =" + printStatus.getStatus() + " WHERE WEBADDRESS = '" + ip + "'";
        queryRunner.update(sql);
    }

    /**
     * 更新票机余额
     */
    public void updateMoney(String ip, double balance) throws SQLException {
        String sql = "update print_service_config set balance=balance - ?, balanceprinttime=? where WEBADDRESS=?";
        Object[] objects = new Object[]{balance, CommmUtil.getTime(CommmUtil.yyyyMMddHHmmss), ip};
        JdbcUtil.getQueryRunner().update(sql, objects);
    }

    /**
     * 更新票机状态
     *
     * @param currentOperating
     * @param ip
     * @throws SQLException
     */
    public void updateOperation(int currentOperating, String ip) throws SQLException {
        String sql = "update print_service_config  set currentOperating=? where WEBADDRESS=?";
        Object[] objects = new Object[]{currentOperating, ip};
        JdbcUtil.getQueryRunner().update(sql, objects);
    }

    @Override
    public void add(PrintServiceDTO printServiceDTO) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(PrintServiceDTO printServiceDTO) {

    }

    @Override
    public PrintServiceDTO select(Long aLong) {
        return null;
    }

    public static void main(String[] args) throws SQLException {
        PrintService printService = new PrintService();
        PrintServiceDTO printServiceDTO = printService.selectByIp("192.168.9.209");
        List<PrintServiceDTO> printServiceDTOs = printService.selectByIps("'192.168.9.209','192.168.9.163'");
        System.out.println(printServiceDTO);
    }
}
