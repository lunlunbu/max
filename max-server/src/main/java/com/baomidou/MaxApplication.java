package com.baomidou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baomidou.mapper")
public class MaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaxApplication.class,args);
    }
}
