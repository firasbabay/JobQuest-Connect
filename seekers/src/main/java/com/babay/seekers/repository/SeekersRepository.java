package com.babay.seekers.repository;

import com.babay.seekers.model.Seekers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeekersRepository extends JpaRepository<Seekers , Long> {
    Seekers findByUsername(String username);
}
