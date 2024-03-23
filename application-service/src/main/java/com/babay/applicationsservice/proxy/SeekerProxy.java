package com.babay.applicationsservice.proxy;

import com.babay.applicationsservice.Model.Seeker;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Repository
@FeignClient(name = "seekers")
@RibbonClient(name = "seekers")
public interface SeekerProxy {

    @RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.GET)
    public ResponseEntity<Seeker> getJobSeeker(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);
}
