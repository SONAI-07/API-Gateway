package com.apiGateway.registry.service;


import com.apiGateway.registry.model.ServiceInstance;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Instant;




@Service
public class RegisterService {

    public ServiceInstance serviceinstance;


    public ConcurrentHashMap<String, ConcurrentHashMap<String, ServiceInstance>> serviceFinder
            = new ConcurrentHashMap<>();





    // registers the services and their Instance ID's
    // avoids race condition of multiple microservice threads
    public Mono<Void> register(ServiceInstance serviceinstance) {

        serviceinstance.setLastHeartBeatTime(Instant.ofEpochSecond(Instant.now().toEpochMilli()));

        String serviceName = serviceinstance.getServiceName();

        ConcurrentHashMap<String, ServiceInstance> innerMap = serviceFinder.computeIfAbsent(serviceName,
                k -> new ConcurrentHashMap<>()
        );

        innerMap.put(serviceName, serviceinstance);

        return Mono.empty();
    }







    public Mono<Boolean> renewHeartbeat (String serviceName , String instanceID) {

     




        return Mono.just(true);
    }








}













