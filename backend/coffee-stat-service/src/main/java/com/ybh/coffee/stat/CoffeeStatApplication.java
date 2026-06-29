package com.ybh.coffee.stat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ybh.coffee.stat.mapper")
@SpringBootApplication(scanBasePackages = "com.ybh.coffee")
public class CoffeeStatApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeStatApplication.class, args);
    }
}
