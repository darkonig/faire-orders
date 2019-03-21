package com.faire.orders.faireorders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FaireOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaireOrdersApplication.class, args);
	}

}
