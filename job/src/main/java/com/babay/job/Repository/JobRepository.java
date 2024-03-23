package com.babay.job.Repository;

import com.babay.job.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository  extends JpaRepository<Job ,Long> {
    public List<Job> findAllByUsername(String username) ;
}
