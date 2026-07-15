package com.apiGateway.loadbalancer;



import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;
import java.util.List;





public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {


    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceName;


    public CustomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider
            , String serviceName) {

        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceName = serviceName;
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);


        if (supplier == null) {
            return Mono.just(new EmptyResponse());
        }

        // Step 2 & 3: Get the Flux and grab the first emitted list
        return supplier.get(request)
                .next() // Converts Flux<List<ServiceInstance>> to Mono<List<ServiceInstance>>
                .map(instances -> {
                    // Step 5: Check if the list is empty
                    if (instances == null || instances.isEmpty()) {
                        return new EmptyResponse();
                    }

                    // Step 4: Map that list through your mathematical algorithms
                    ServiceInstance winningInstance = activeLeastConnection(instances);

                    // Step 5: Wrap the winning instance
                    return new DefaultResponse(winningInstance);
                })
                // Handle cases where the Flux completes without emitting any list
                .defaultIfEmpty(new EmptyResponse());
    }


    private ServiceInstance activeLeastConnection(List<ServiceInstance> instances) {

        return instances.get(0);
    }


}