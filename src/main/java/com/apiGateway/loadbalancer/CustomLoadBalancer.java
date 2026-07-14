package com.apiGateway.loadbalancer;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;


public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final String serviceName;



    public CustomLoadBalancer ( ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider
    , String serviceName) {

        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceName = serviceName;
    }



    @Override
    public Mono<Response<ServiceInstance>> choose(Request request)
    {

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);


        supplier.get(request)



    }







}
