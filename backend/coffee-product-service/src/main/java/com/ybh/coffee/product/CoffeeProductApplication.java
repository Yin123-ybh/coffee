package com.ybh.coffee.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ybh.coffee.product.mapper")
@SpringBootApplication(scanBasePackages = "com.ybh.coffee")
public class CoffeeProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeProductApplication.class, args);
    }
}
