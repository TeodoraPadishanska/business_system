package com.example.bussinessSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RequestMapping("/bussinessSystem")
public class BussinessSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BussinessSystemApplication.class, args);
	}

}
