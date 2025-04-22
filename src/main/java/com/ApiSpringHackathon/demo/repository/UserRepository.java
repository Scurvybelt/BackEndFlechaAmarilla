package com.ApiSpringHackathon.demo.repository;

import com.ApiSpringHackathon.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByUsername(String username);

}


