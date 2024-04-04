package com.babay.Authentication.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;



@Repository
@FeignClient(name="JOBCENTERS")
@RibbonClient(name="jobcenters")
public interface JobCenterProxy {

    @RequestMapping(value = "/api/centers", method = RequestMethod.POST)
    public ResponseEntity createJobCenter(@RequestBody JobCenter jobCenterEntity);

    @RequestMapping(value="/api/centers/{centerName}", method = RequestMethod.GET)
    public boolean existsCenter(@PathVariable("centerName") String centerName);

    @RequestMapping(value = "/api/centers/{username}", method = RequestMethod.DELETE)
    public ResponseEntity deleteJobCenter(@RequestHeader("X-User-Header") String loggedUser, @PathVariable String username);

    @RequestMapping(value = "/api/centers/{username}", method = RequestMethod.PUT)
    public ResponseEntity<JobCenter> updateJobCenter(@RequestHeader("X-User-Header") String loggedUser,
                                                        @PathVariable String username,
                                                        @RequestBody JobCenter jobCenter);
}
