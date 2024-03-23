package com.babay.applicationsservice.Repository;

import com.babay.applicationsservice.Model.Application;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application , Long> {
    public List<Application> findAllByUsername(String username);
    public Application findByUsernameAndJobId(String username ,long JobId);
    public List<Application> findAllByJobId(long jobId);
    public void deleteAllByJobId(long jobId);
    public void deleteAllByUsername(String username);

    public List<Application> findAllByCenterUsername(String username);
}
