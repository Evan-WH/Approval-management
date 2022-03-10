package com.examine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * 启动程序
 * 
 * @author Evan-WH
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })

public class RuoYiApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  审批管理后台启动成功   ლ(´ڡ`ლ)ﾞ  \n" );

    }
}
