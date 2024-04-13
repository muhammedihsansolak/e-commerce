package com.cydeo;

import com.cydeo.swagger.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ECommerceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ECommerceApplication.class);
        application.addListeners(new SwaggerConfiguration());
        application.run(args);
    }



}
