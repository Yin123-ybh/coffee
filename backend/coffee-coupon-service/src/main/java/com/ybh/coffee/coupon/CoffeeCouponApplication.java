package com.ybh.coffee.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ybh.coffee.coupon.mapper")
@SpringBootApplication(scanBasePackages = "com.ybh.coffee")
public class CoffeeCouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeCouponApplication.class, args);
    }
}
