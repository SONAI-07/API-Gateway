package com.apiGateway.registry.model;

import lombok.*;
import java.time.Instant;


@Getter
@Setter



public class ServiceInstance
{
    //eg: OrderService ,InventoryService
    private String serviceName ;

    //eg: OrderService-123.456.7.8.8081 , OrderService-123.456.7.8.8082
    private String instanceID;

    //eg: Port-8081
    private Long Port;

    private String ipAddress;

    private Instant lastHeartBeatTime;

}
