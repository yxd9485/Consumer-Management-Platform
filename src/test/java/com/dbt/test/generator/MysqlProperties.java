package com.dbt.test.generator;

/**
 * @author bing_huang
 */
public class MysqlProperties {
    public static final String MYSQL_URL = "jdbc:mysql://123.127.55.8:3307/vjifen_mengniu?Unicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true&useAffectedRows=true";
    public static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_USERNAME = "root";
    public static final String MYSQL_PASSWORD = "ZS3l4H86";
    public static final String SUPER_ENTITY_CLASS = "com.dbt.boot.admin.domain.model.entity.BaseDomain";
    public static final String SUPER_SERVICE_IMPL_CLASS = "com.dbt.boot.admin.domain.service.impl.SuperBaseServiceImpl";
    public static final String SUPER_SERVICE_CLASS="com.dbt.boot.admin.domain.service.IBaseService";
    public static final String[] SUPER_ENTITY_COLUMNS = new String[]{"create_user", "create_time", "update_user", "update_time", "is_enabled", "del_flag", "version"};
    public static final String SUPER_CONTROLLER_CLASS = "com.dbt.framework.base.action.BaseAction";
    public static final String VERSION_FILE_NAME = "version";
    public static final String LOGIC_DELETE_FILE_NAME = "del_flag";
}
