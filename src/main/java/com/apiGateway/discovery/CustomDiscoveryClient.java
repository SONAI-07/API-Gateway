package com.apiGateway.discovery;



import com.apiGateway.registry.model.ServiceInstance;
import com.apiGateway.registry.service.RegisterService;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


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

                for (ServiceInstance serviceinstance : innerMap.values()) {

                   // map the default instances ....

                    
                    return Flux.fromIterable(list);
                }
            }

        }

        return Flux.empty();


    }





}