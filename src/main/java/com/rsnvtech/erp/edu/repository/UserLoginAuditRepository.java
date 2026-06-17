package com.rsnvtech.erp.edu.repository;

import com.rsnvtech.erp.edu.entity.UserLoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginAuditRepository extends JpaRepository<UserLoginAudit,Long> {
}
