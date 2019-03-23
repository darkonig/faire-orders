package com.faire.orders.faireorders;

import feign.Contract;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class FaireOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaireOrdersApplication.class, args);
    }

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }
}
