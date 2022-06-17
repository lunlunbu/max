package com.lunlunbu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lunlunbu.mapper")
public class MaxApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaxApplication.class,args);
    }
}
