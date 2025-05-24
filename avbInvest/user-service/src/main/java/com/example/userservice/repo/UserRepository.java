package com.example.userservice.repo;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.companyId = :companyId")
    void deleteUsersByCompanyId(@Param("companyId") Integer companyId);

    List<User> getUsersByCompanyId(Integer companyId);
}