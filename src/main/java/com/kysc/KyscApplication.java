package com.kysc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("com.kysc.dao")
public class KyscApplication {

	public static void main(String[] args) {
		SpringApplication.run(KyscApplication.class, args);
	}
}
