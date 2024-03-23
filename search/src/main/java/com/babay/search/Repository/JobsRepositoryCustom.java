package com.babay.search.Repository;

import com.babay.search.model.Job;

import java.util.List;

public interface JobsRepositoryCustom {
    public List<Job> findJobByQuery(String q, String location);
}

