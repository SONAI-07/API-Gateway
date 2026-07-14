package com.apiGateway.discovery;



import com.apiGateway.registry.model.ServiceInstance;
import com.apiGateway.registry.service.RegisterService;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cloud.client.DefaultServiceInstance;



@Component
 class CustomDiscoveryClient implements ReactiveDiscoveryClient {


    private RegisterService registerService;

    public CustomDiscoveryClient(RegisterService registerService) {
        this.registerService = registerService;
    }


    @Override
    public String description() {
        return "Returns the List of Active instances to the embedded Load Balancer";
    }




    @Override
    public Flux<String> getServices() {

        Set<String> iterator = (registerService.serviceFinder).keySet();

        Flux<String> iteratorFlux = Flux.fromIterable(iterator);

        return iteratorFlux;
    }







    @Override
    public Flux<org.springframework.cloud.client.ServiceInstance> getInstances(String serviceName) {

        if (registerService.serviceFinder.containsKey(serviceName)) {

            if (registerService.serviceFinder.get(serviceName) != null) {

                List<org.springframework.cloud.client.ServiceInstance> list = new ArrayList<>();

                ConcurrentHashMap<String, ServiceInstance> innerMap = registerService.serviceFinder.get(serviceName);




                for (ServiceInstance copyInstance : innerMap.values()) {

                    HashMap<String,String> metadata = new HashMap<>();

                    int instanceWeight = copyInstance.getWeight() > 0 ? copyInstance.getWeight() : 1;
                    metadata.put("weight", String.valueOf(instanceWeight));
                    metadata.put("activeConnections", String.valueOf(copyInstance.getActiveConnections()));
                    int port = Math.toIntExact(copyInstance.getPort());


                    DefaultServiceInstance instance = new DefaultServiceInstance(
                            copyInstance.getInstanceID(),
                            copyInstance.getServiceName() ,
                            copyInstance.getHost() ,
                            port,
                            false,
                            metadata
                    );

                    list.add(instance);

                }

                return Flux.fromIterable(list);


                }

             }

        return Flux.empty();

    }

}