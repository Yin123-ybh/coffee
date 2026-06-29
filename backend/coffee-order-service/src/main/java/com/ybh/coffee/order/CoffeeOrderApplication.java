package com.ybh.coffee.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ybh.coffee.order.mapper")
@SpringBootApplication(scanBasePackages = "com.ybh.coffee")
public class CoffeeOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeOrderApplication.class, args);
    }
}
