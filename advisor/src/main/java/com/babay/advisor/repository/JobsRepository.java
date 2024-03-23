package com.babay.advisor.repository;

import com.babay.advisor.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobsRepository extends JpaRepository<Job, Long> {
    public List<Job> findAllByLocation(String location);
}
