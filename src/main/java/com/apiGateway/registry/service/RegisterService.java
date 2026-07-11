package com.apiGateway.registry.service;


import com.apiGateway.registry.model.ServiceInstance;
import com.apiGateway.registry.model.TimeoutTask;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Instant;



@Service
public class RegisterService {


    private final instanceEviction instanceEviction;

    public ConcurrentHashMap<String, ConcurrentHashMap<String, ServiceInstance>> serviceFinder
            = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    public RegisterService(instanceEviction instanceEviction) {
        this.instanceEviction = instanceEviction;
    }


    // registers the services and their Instance ID's
    // avoids race condition of multiple microservice threads

    public Mono<Void> register(ServiceInstance serviceinstance) {

        serviceinstance.setLastHeartBeatTime(Instant.now());

        String serviceName = serviceinstance.getServiceName();
        String instanceID = serviceinstance.getInstanceID();

        ConcurrentHashMap<String, ServiceInstance> innerMap = serviceFinder.computeIfAbsent(serviceName,
                k -> new ConcurrentHashMap<>()
        );

        innerMap.put(instanceID, serviceinstance);

        return Mono.empty();
    }





    //services update their existence
    //carts are assigned their respective indexes

    public Mono<Boolean> renewHeartbeat(String serviceName, String instanceID) {

        ServiceInstance serviceinstance;
        Duration duration;


        if (serviceFinder.get(serviceName) != null) {

            serviceinstance = (serviceFinder.get(serviceName)).get(instanceID);


            if (serviceinstance != null && serviceinstance.getLastHeartBeatTime() != null) {

                duration = Duration.between(serviceinstance.getLastHeartBeatTime(), Instant.now());


                if (duration.toSeconds() < 90) {
                    
                    serviceinstance.setLastHeartBeatTime(Instant.now());

                    TimeoutTask oldTask = serviceinstance.getTimeoutReference();



                    if (oldTask != null) {

                        oldTask.isCancelled = true;

                        TimeoutTask newTask = instanceEviction.scheduleTimeout(instanceID, 90);

                        serviceinstance.setTimeoutReference(newTask);
                    }

                    return Mono.just(true);
                }
            }
            else
            {
                logger.info("Re-register the service instance :{}", instanceID);

                    return Mono.just(false);
            }


        }

        else
            logger.info("Register the Service over the Eureka : {}", serviceName);



        return Mono.just(false);


    }


}


