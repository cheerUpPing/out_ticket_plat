package com.harmony.service.impl;

import com.harmony.entity.ChipinTempDTO;
import com.harmony.entity.Contants;
import com.harmony.entity.PrintServiceDTO;
import com.harmony.service.BaseService;
import com.harmony.util.JdbcUtil;
import com.harmony.util.LogUtil;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * 2017/9/18 9:58.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 出票表服务类
 */
public class ChipinTempService implements BaseService<ChipinTempDTO, Integer> {

    /**
     * 通过tempid查询
     *
     * @param tempid
     * @return
     * @throws SQLException
     */
    public ChipinTempDTO selectByTempid(long tempid) throws SQLException {
        String sql = "SELECT * FROM T_CHIPIN_TEMP WHERE TEMPID = " + tempid;
        return JdbcUtil.getQueryRunner().query(sql, new BeanHandler<ChipinTempDTO>(ChipinTempDTO.class));
    }

    /**
     * 给对应纪录加入错误信息，并且更新票状态
     *
     * @param errorMsg
     * @param tempid
     * @throws SQLException
     */
    public void updateException(String errorMsg, int status, long tempid) throws SQLException {
        String sql = "update t_chipin_temp td set td.exceptionInfo ='" + errorMsg + "', td.status = " + status + "  where td.tempId =" + tempid;
        JdbcUtil.getQueryRunner().update(sql);
    }

    /**
     * 获取到已经分配好的票
     *
     * @param printServiceDTO
     * @param overage
     * @return
     */
    public List<ChipinTempDTO> findNoAllocationPrint(PrintServiceDTO printServiceDTO, long overage, long systemTime) throws SQLException {

        String sqlQuery = "";
        if (printServiceDTO.getVersion().equals(Contants.Version_2) || printServiceDTO.getVersion().equals(Contants.Version_3)) {
            //String tempSQL = " AND T.gameCode not in (509,510) ";//现阶段新机器不支持混投投注单,临时修改
            String tempSQL = "";
            if (printServiceDTO.getBigType().equals(Contants.JC)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0 AND (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND (T.lotterytype ='jc' OR T.lotterytype='lq') AND T.postcount <2 " + tempSQL + " AND T.lastscreeningtime > " + systemTime + "  ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.BD)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0 AND  (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND T.lotterytype ='bd' AND T.postcount <2 AND T.lastscreeningtime > " + systemTime + "   ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.GP)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0  AND (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND T.lotterytype ='gp' AND T.postcount < 2 AND T.lastscreeningtime > " + systemTime + "   ORDER BY T.priorPrint DESC, T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.CT)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName  WHERE T.status=0 AND  (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND T.lotterytype ='ct' AND T.postcount < 2 AND T.lastscreeningtime >  " + systemTime + "   ORDER BY T.priorPrint DESC,T.lastscreeningtime ASC,  T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.JC_ZU)) {//JC_ZU
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0 AND   (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND (T.lotterytype ='jc') AND T.gameCode in (501,502,503,504,509,511,512) AND T.postcount <2 " + tempSQL + " AND T.lastscreeningtime > " + systemTime + "    ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.JC_LAN)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0 AND  (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND (T.lotterytype='lq') AND T.gameCode in (505,506,507,508,510) AND T.postcount <2 " + tempSQL + " AND T.lastscreeningtime > " + systemTime + "    ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.JC_CT)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  LEFT JOIN  t_user_info U ON T.userNickName=U.userNickName WHERE T.status=0 AND   (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND (T.lotterytype ='jc' OR T.lotterytype ='ct' OR T.lotterytype='lq') AND T.postcount <2 " + tempSQL + " AND T.lastscreeningtime > " + systemTime + "    ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, NVL(U.weight,0) DESC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            } else if (printServiceDTO.getBigType().equals(Contants.JC360)) {
                sqlQuery = "SELECT * FROM (SELECT T.* FROM t_chipin_temp T  WHERE T.status=0 AND T.userNickName='qihu360'  AND  (T.printservicenumber='" + printServiceDTO.getTerminalNumber() + "')" +
                        " AND T.bets< " + printServiceDTO.getMaxAmount() + " AND T.bets>= " + printServiceDTO.getMixAmount() + "  AND (T.lotterytype ='jc' OR T.lotterytype ='ct' OR T.lotterytype='lq') AND T.postcount <2 " + tempSQL + " AND T.lastscreeningtime > " + systemTime + "    ORDER BY  T.priorPrint DESC,T.lastscreeningtime ASC, T.voteproccesstime ASC, T.bets DESC) WHERE ROWNUM<=" + overage + "";
            }
        }
        LogUtil.info(ChipinTempService.class, "准备分票", "准备给票机" + printServiceDTO.getWebAddress() + "分票,执行的sql:" + sqlQuery);
        List<ChipinTempDTO> chipinTempDTOS = null;
        if (StringUtils.isNotEmpty(sqlQuery)) {
            chipinTempDTOS = JdbcUtil.getQueryRunner().query(sqlQuery, new BeanListHandler<ChipinTempDTO>(ChipinTempDTO.class));
        }
        return chipinTempDTOS;
    }

    @Override
    public void add(ChipinTempDTO chipinTempDTO) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(ChipinTempDTO chipinTempDTO) {

    }

    @Override
    public ChipinTempDTO select(Integer integer) {
        return null;
    }

    public static void main(String[] args) {

    }
}
