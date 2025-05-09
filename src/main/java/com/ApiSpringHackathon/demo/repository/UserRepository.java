package com.ApiSpringHackathon.demo.repository;

import com.ApiSpringHackathon.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByEmail(String correo);

}


