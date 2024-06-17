package com.dbt.platform.autocode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/**
 * txt转mdb文件Util
 * @author hanshimeng
 *
 */
public class TxtConvertMdbUtil {
    private Connection connection;
    private Statement statement;
    
    // 空白mdb文件名
    private String blankMdbFileName = "blank.mdb";
    
    // 需要保存到的新的mdb文件路径和名
    private String savedMdbFilePathAndName = "";
    
    // 新mdb文件路径
    public static final String defaultSavedMdbFilePath = "/com/hongen/mdb/output/";
    
    // 新mdb文件名
    public static final String defaultSavedMdbFileName = "data.mdb";
    
    // mdb文件后缀
    public static final String defaultSavedMdbFileExtension = ".mdb";
    
    // 标准的单件模式
    private static TxtConvertMdbUtil instance = new TxtConvertMdbUtil();

    private TxtConvertMdbUtil() {
    }

    public static TxtConvertMdbUtil getInstance() {
        return instance;
    }
    
    /**
     * 
     Description: 设置待保存的新的mdb文件路径和名
     */
    public void setSavedFileName(File file) {
        this.savedMdbFilePathAndName = file.getPath().replace("1source", "3convert").replace(".txt", ".mdb");
    }

    /**
     * 
     Description: 设置待保存的新的mdb文件路径和名
     */
    public void setSavedFilePathAndName(String newFilePathAndName) {
        this.savedMdbFilePathAndName = newFilePathAndName;
    }

    /**
     * 
     Description: 删除已经存在的mdb文件
     */
    public void deleteOldMdbFile() throws Exception {
        File oldTargetFile = new File(savedMdbFilePathAndName);
        if (oldTargetFile.exists()) {
            oldTargetFile.delete();
        }
    }

    /**
     * 将空白mdb文件拷贝到特定目录
     * filePath:blank.mdb文件所属目录
     */
    public void copyBlankMdbFile(String filePath) throws Exception {
        InputStream is = new FileInputStream(new File(filePath + File.separator + blankMdbFileName));
        OutputStream out = new FileOutputStream(savedMdbFilePathAndName);
        byte[] buffer = new byte[1024];
        int numRead;
        while ((numRead = is.read(buffer)) != -1) {
            out.write(buffer, 0, numRead);
        }
        is.close();
        out.close();
    }

    /**
     * 
     Description: 打开对mdb文件的jdbc-odbc连接
     */
    public void connetAccessDB() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="
                + savedMdbFilePathAndName;
    	
//    	String dbUr1 = "jdbc:access:////" + savedMdbFilePathAndName;
//    	Class.forName("com.hxtt.sql.access.AccessDriver").newInstance();
        connection = DriverManager.getConnection(database, "", "");
        statement = connection.createStatement();
    }

    /**
     * 
     Description: 执行特定sql语句
     */
    public void executeSql(String sql) throws Exception {
        statement.execute(sql);
    }

    /**
     * 
     Description: 关闭连接
     */
    public void closeConnection() throws Exception {
        statement.close();
        connection.close();
    }
}
