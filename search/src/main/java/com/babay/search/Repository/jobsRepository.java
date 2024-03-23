package com.babay.search.Repository;

import com.babay.search.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface jobsRepository extends JpaRepository<Job, Long>, JobsRepositoryCustom {
}
