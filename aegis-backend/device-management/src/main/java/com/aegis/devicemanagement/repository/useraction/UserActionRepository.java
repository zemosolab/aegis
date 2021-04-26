package com.aegis.devicemanagement.repository.useraction;

import com.aegis.devicemanagement.model.user.UserAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActionRepository extends MongoRepository<UserAction, String> {
    List<UserAction> findByUserId(String userId);
}
