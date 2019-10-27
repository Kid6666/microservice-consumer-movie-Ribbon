package com.lejx.microserviceconsumermovie.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "MICROSERVICE-PROVIDER-USER")
public interface UserFeignClient {

    @RequestMapping(value = "/{id}",method = RequestMethod.GET )
    public User findByID(@PathVariable("id") long id);
}