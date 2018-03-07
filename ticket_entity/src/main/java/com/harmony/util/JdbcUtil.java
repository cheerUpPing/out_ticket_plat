package com.harmony.util;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 2017/9/13 11:35.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 数据库类
 */
public class JdbcUtil {

    //机房数据源
    private static DataSource ds = null;

    static {
        try {
            ds = getDataSource("db.properties");
        } catch (Exception e) {
            LogUtil.info(JdbcUtil.class, "初始化数据源", "失败" + LogUtil.getStackTrace(e));
        }
    }

    /**
     * 从配置文件里面获取数据源
     *
     * @param config
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static DataSource getDataSource(String config) throws IOException, SQLException {
        Properties pro = CommmUtil.getProperties(config);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(pro.getProperty("driver"));
        dataSource.setUsername(pro.getProperty("username"));
        dataSource.setPassword(pro.getProperty("password"));
        dataSource.setUrl(pro.getProperty("url"));
        dataSource.setInitialSize(Integer.parseInt(pro.getProperty("initialsize", "5")));
        dataSource.setMinIdle(Integer.parseInt(pro.getProperty("minidle", "1")));
        dataSource.setMaxActive(Integer.parseInt(pro.getProperty("maxactive", "20")));
        dataSource.setFilters("stat");// 启用监控统计功能
        return dataSource;
    }

    /**
     * 获取机房数据源
     *
     * @return
     */
    public static DataSource getDataSource() {
        return ds;
    }

    /**
     * 获取查询 更新 新增 删除 执行器[本地机房]
     *
     * @return
     */
    public static QueryRunner getQueryRunner() {
        if (getDataSource() == null) {
            LogUtil.info(JdbcUtil.class, "初始化错误", "数据源错误...");
            throw new RuntimeException("初始化错误...数据源错误...");
        }
        return new QueryRunner(ds);
    }

    /**
     * 从数据源获取执行器
     *
     * @param dataSource
     * @return
     */
    public static QueryRunner getQueryRunner(DataSource dataSource) {
        return dataSource == null ? null : new QueryRunner(dataSource);
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (getDataSource() == null) {
            LogUtil.info(JdbcUtil.class, "初始化错误", "数据源错误...");
            throw new RuntimeException("初始化错误...数据源错误...");
        }
        return ds.getConnection();
    }

    /**
     * 获取数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) {
        System.out.println(getQueryRunner());
    }
}
