package com.ggeit.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;




@SpringBootApplication

public class MobileGuidApplication extends SpringBootServletInitializer{


	@Override
	//要使用tomcat来支持加载JSP必须继承SpringBootServletInitializer类并重写其中configure方法
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MobileGuidApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(MobileGuidApplication.class, args);
	}
	

}

