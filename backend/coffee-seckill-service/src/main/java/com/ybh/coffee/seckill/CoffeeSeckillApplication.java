package com.ybh.coffee.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ybh.coffee.seckill.mapper")
@SpringBootApplication(scanBasePackages = "com.ybh.coffee")
public class CoffeeSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeSeckillApplication.class, args);
    }
}
