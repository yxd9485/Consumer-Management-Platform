package com.dbt.platform.home.homeService;

import com.dbt.framework.datadic.bean.ServerInfo;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class ItemDBHelper {
    public static final String name = "com.mysql.jdbc.Driver";
    public Connection conn = null;
    public Statement stmt = null;

    public ItemDBHelper() {
        try {
            Class.forName(name);//指定连接类型
            String url= "jdbc:mysql://rm-2ze8x8r5gzno056q6.mysql.rds.aliyuncs.com:3306/vjifen_main?" +
                    "useSSL=false&useAffectedRows=true&Unicode=true&characterEncoding=utf-8&" +
                    "zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true";
            String user="vjifen";
            String password="Vjifen1@#hnqp";
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("useSSL", "false");
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("allowMultiQueries", "true");
            conn = DriverManager.getConnection(url,properties);//获取连接
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            this.stmt.close();
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(String sql, List<List<String>> data) throws SQLException {
        if(data.size() == 1) {
            execute(sql, data.get(0));
        } else {
            executeBatch(sql, data);
        }
    }

    private void execute(String sql, List<String> data) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement(sql);
        for (int i = 1; i <= data.size(); i++) {
            stat.setString(i, data.get(i - 1));
        }
        stat.execute();
        stat.close();
    }

    private void executeBatch(String sql, List<List<String>> data) throws SQLException {
        PreparedStatement stat = this.conn.prepareStatement(sql);
        for(List<String> row: data) {
            for (int i = 1; i <= row.size(); i++) {
                stat.setString(i, row.get(i - 1));
            }
            stat.addBatch();
        }
        stat.executeBatch();
        stat.close();
    }
}
