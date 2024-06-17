package com.dbt.test.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.generator.config.rules.DateType.ONLY_DATE;

/**用户角色
 * @author bing_huang
 */
public class GeneratorDemo {


    public static void main(String[] args) {
        //输出地址
        String outPath = "/Users/bz/Documents/gen/data/src/main/java";
        //表名
        String[] tableName = {
                "rank_report_all"
        };
        //模块名
        String moduleName = "mn.rank";
        //作者
        String author = "wangshuda";
        AutoGenerator mpg = new AutoGenerator();
        //数据源
        DataSourceConfig datasourceConfig = new DataSourceConfig();
        datasourceConfig.setDbType(DbType.MYSQL);
        datasourceConfig.setUrl(MysqlProperties.MYSQL_URL);
        datasourceConfig.setDriverName(MysqlProperties.DRIVER_NAME);
        datasourceConfig.setUsername(MysqlProperties.MYSQL_USERNAME);
        datasourceConfig.setPassword(MysqlProperties.MYSQL_PASSWORD);
        mpg.setDataSource(datasourceConfig);
        //数据库表配置
        StrategyConfig strategyConfig = new StrategyConfig();
        //表名
        strategyConfig.setInclude(tableName);
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setSuperControllerClass(MysqlProperties.SUPER_CONTROLLER_CLASS);
        strategyConfig.setEntityColumnConstant(true);
        strategyConfig.setEntityBuilderModel(true);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setRestControllerStyle(true);
        strategyConfig.setControllerMappingHyphenStyle(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        strategyConfig.setVersionFieldName(MysqlProperties.VERSION_FILE_NAME);
        strategyConfig.setLogicDeleteFieldName(MysqlProperties.LOGIC_DELETE_FILE_NAME);
        mpg.setStrategy(strategyConfig);
        //包名配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.dbt.platform");
        //自己的模块包名字
        packageConfig.setModuleName(moduleName);
        packageConfig.setEntity("bean");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("dao");
        packageConfig.setXml("bean");
        packageConfig.setController("action");
        mpg.setPackageInfo(packageConfig);
        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(outPath);
        globalConfig.setFileOverride(true);
        globalConfig.setOpen(true);
        globalConfig.setAuthor(author);
        globalConfig.setEntityName("%s");
        globalConfig.setMapperName("I%sDao");
        globalConfig.setServiceName("I%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sAction");
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setDateType(ONLY_DATE);
        globalConfig.setEnableCache(false);
        mpg.setGlobalConfig(globalConfig);

//        // 自定义配置
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
////                Map<String, Object> map = initImportPackageInfo(config);
////                this.setMap(map);
//            }
//        };
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
//        String templatePath = "/templates/mapper.xml.ftl";
        String templatePath = "mapper2.xml.vm";
//        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
////        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
//                return  "/Users/bz/Documents/gen/data/src/main/resources/mapper/" + packageConfig.getModuleName()
//                        + "/ibatis_" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.execute();
    }

}
