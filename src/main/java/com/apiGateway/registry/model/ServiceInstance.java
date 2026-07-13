package com.apiGateway.registry.model;

import lombok.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor

public class ServiceInstance
{
    //eg: OrderService ,InventoryService
    private String serviceName ;

    //eg: OrderService-123.456.7.8.8081 , OrderService-123.456.7.8.8082
    private String instanceID;

    //IP address of the running machine/server
    private String host;

    private volatile TimeoutTask timeoutReference;

    //eg: Port-8081
    private Long Port;

    private int weight;

    private int activeConnections;


    private Instant lastHeartBeatTime;


}
