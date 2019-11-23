package com.lejx.microserviceconsumermovie.user;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @HystrixCommand(fallbackMethod = "findByIdFallback",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds",value = "10000")
    },threadPoolProperties = {
            @HystrixProperty(name = "coreSize",value = "1"),
            @HystrixProperty(name = "maxQueueSize",value = "10")
    })
    @GetMapping("/user/{id}")
    public User findById(@PathVariable long id){
        return restTemplate.getForObject("http://MICROSERVICE-PROVIDER-USER/"+id,User.class);
    }

    @GetMapping("/log-user-instance")
    public void logUserInstance(){

        ServiceInstance serviceInstance = this.loadBalancerClient.choose("MICROSERVICE-PROVIDER-USER");
        MovieController.logger.info("{}:{}:{}",serviceInstance.getServiceId(),
                serviceInstance.getHost(),serviceInstance.getPort());
    }

    public User findByIdFallback(long id,Throwable throwable){
        MovieController.logger.error("进入回退方法，异常: ",throwable);
        User user = new User();
        user.setId(-1L);
        user.setName("默认用户");
        return user;

    }


}
