package com.lejx.microserviceconsumermovie.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable long id){
        return restTemplate.getForObject("http://MICROSERVICE-PROVIDER-USER/"+id,User.class);
    }

    @GetMapping("/log-user-instance")
    public  void logUserInstance(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("MICROSERVICE-PROVIDER-USER");
        logger.info("{}:{}:{}",serviceInstance.getServiceId(),serviceInstance.getHost(),serviceInstance.getPort());
//        System.out.println(serviceInstance.getServiceId()+" : "+serviceInstance.getHost()+" : "+serviceInstance.getPort());
    }

}
