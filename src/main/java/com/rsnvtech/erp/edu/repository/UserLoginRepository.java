package com.rsnvtech.erp.edu.repository;

import com.rsnvtech.erp.edu.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {

    Optional<UserLogin> findByUserEmail(String userEmail);
    UserLogin findByToken(String token);
}
