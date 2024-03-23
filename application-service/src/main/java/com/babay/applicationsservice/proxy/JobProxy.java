package com.babay.applicationsservice.proxy;

import com.babay.applicationsservice.Model.Job;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name = "jobs")
@RibbonClient(name = "jobs")
public interface JobProxy {

    @RequestMapping(value = "/api/centers/{username}/jobs/{jobId}", method = RequestMethod.GET)
    Job getJob(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username, @PathVariable long jobId);

}
