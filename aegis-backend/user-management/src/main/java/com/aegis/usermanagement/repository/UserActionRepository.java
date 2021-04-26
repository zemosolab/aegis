package com.aegis.usermanagement.repository;

import com.aegis.usermanagement.model.UserAction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, UUID> {

    List<UserAction> findByUserId(UUID userId);


}
