package com.babay.Authentication.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "seekers")
@RibbonClient(name = "seekers")
public interface SeekerProxy {
    @RequestMapping(value = "/api/seekers", method = RequestMethod.POST)
    public ResponseEntity createInstance(Seeker seeker);

    @RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSeeker(@RequestHeader("X-User-Header") String loggedUser,
                                       @PathVariable String username);

    @RequestMapping(value = "/api/seekers/{username}", method = RequestMethod.PUT)
    public ResponseEntity changeJobSeeker(@RequestHeader("X-User-Header") String loggedUser,
                                       @PathVariable String username,
                                       @RequestBody Seeker seekerEntity);

}
