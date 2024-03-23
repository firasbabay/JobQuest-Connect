package com.babay.jobCenters.Repository;

import com.babay.jobCenters.Model.jobcenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCenterRepo extends JpaRepository<jobcenter, Long> {
    jobcenter findByName(String name);
    boolean existsByName(String name);
    jobcenter findByUsername(String username);
    void deleteByUsername(String username);
}
