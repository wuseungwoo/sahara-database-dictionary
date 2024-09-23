package com.sahara.dictionary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.sahara.dictionary")
public class DataBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataBaseApplication.class, args);
    }
}
