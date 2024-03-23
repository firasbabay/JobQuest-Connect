package com.babay.search.Repository;

import com.babay.search.model.Job;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.LinkedList;
import java.util.List;

public class JobsRepositoryCustomImpl implements JobsRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager ;
    @Override
    public List<Job> findJobByQuery(String q, String location) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> query = cb.createQuery(Job.class);
        Root<Job> jobEntity = query.from(Job.class);
        List<Predicate> queryPredicates = new LinkedList<>();


        List<Predicate> termPredicates = new LinkedList<>();
        if (q != null && !q.isEmpty()) {
            Path<String> description = jobEntity.get("jobDescription");
            Path<String> position = jobEntity.get("position");
            Path<String> companyName = jobEntity.get("companyName");
            for (String term : q.split(" ")) {
                termPredicates.add(cb.like(cb.upper(description), "%" + term.toUpperCase() + "%"));
                termPredicates.add(cb.like(cb.upper(position), "%" + term.toUpperCase() + "%"));
                termPredicates.add(cb.like(cb.upper(companyName), "%" + term.toUpperCase() + "%"));
            }
            queryPredicates.add(cb.or(termPredicates.toArray(new Predicate[0])));
        }


        if (location != null && !location.isEmpty()) {
            Path<String> loc = jobEntity.get("location");
            queryPredicates.add(cb.like(cb.upper(loc), "%" + location.toUpperCase() + "%"));
        }

        if (!queryPredicates.isEmpty()) {
            query.select(jobEntity)
                    .where(cb.and(queryPredicates.toArray(new Predicate[0])))
                    .orderBy(cb.desc(jobEntity.get("dateCreation")));
        } else {
            query.select(jobEntity)
                    .orderBy(cb.desc(jobEntity.get("dateCreation")));
        }

        return entityManager.createQuery(query).getResultList();
    }
}
