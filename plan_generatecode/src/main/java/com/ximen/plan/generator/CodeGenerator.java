package com.ximen.plan.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {

    public static void main(String[] args) {
        //1.全局配置
        GlobalConfig config = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        config.setActiveRecord(true)//是否支持AR模式
                .setAuthor("damoncai")//作者
                .setOutputDir(projectPath + "/plan_generatecode/src/main/java")//生成路径
                .setFileOverride(true)//文件覆盖
                .setIdType(IdType.AUTO)//主键策略
                .setServiceName("%sService") //设置生成service接口的名字的首字母是否为I
                //IEmployeeService
                .setBaseResultMap(true)
                .setBaseColumnList(true);

        //2.数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/plan?serverTimezone=UTC")
                .setUsername("root")
                .setPassword("prinfo");

        //3.策略配置
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true) //全局大写命名
                .setNaming(NamingStrategy.underline_to_camel) //数据库表映射到实体的命名策略
//                .setTablePrefix("tbl_")
                .setEntityLombokModel(true)
                .setInclude("sys_role")
                .setInclude("sys_user")
                .setInclude("sys_permission")
                .setInclude("sys_role_permission")
                .setInclude("sys_user_role");


        //4.包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.ximen.plan")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setEntity("entity")
                .setXml("mapper");
        //5.整合配置
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine());
        //6.执行
        ag.execute();
    }
}
