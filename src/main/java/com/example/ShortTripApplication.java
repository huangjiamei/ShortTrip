package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
启动类
 */
@SpringBootApplication
public class ShortTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortTripApplication.class, args);
		//SpringApplication.run(HelloController.class, args);
	}
}
