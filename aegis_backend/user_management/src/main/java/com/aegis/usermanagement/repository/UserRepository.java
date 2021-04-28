package com.aegis.usermanagement.repository;


import com.aegis.usermanagement.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByPhoneNo(String phoneNo);

    List<User> findByUserEmail(String userEmail);

    @Query("SELECT P FROM User P WHERE P.isDeleted=false and P.id=:userId")
    Optional<User> findById(@Param("userId") UUID userId);


    @Query("SELECT P FROM User P WHERE P.isDeleted=false ")
    List<User> findAll();

    @Modifying
    @Query("UPDATE User c SET c.isDeleted=true WHERE c.id=:userId")
    void deleteById(@Param("userId") UUID userId);



}