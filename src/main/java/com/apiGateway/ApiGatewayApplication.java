package com.apiGateway;

import com.apiGateway.loadbalancer.CustomLoadBalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.scheduling.annotation.EnableScheduling;




@SpringBootApplication
@EnableScheduling
@LoadBalancerClients(defaultConfiguration = CustomLoadBalancerConfig.class)
public class ApiGatewayApplication {


     public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
