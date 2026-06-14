package com.rsnvtech.erp.edu.repository;

import com.rsnvtech.erp.edu.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin,Long> {
    UserLogin findByUserEmail(String userEmail);
    UserLogin findByToken(String token);
}
