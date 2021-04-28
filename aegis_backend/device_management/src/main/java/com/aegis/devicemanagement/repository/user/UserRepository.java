package com.aegis.devicemanagement.repository.user;

import com.aegis.devicemanagement.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserCustomizedRepository<User, String> {

    @Query("{isDeleted:false}")
    List<User> findAll();

    @Query("{isDeleted:false,deviceId:?0}")
    List<User> findByDeviceId(String deviceId);

    @Query("{isDeleted:false,id:?0}")
    Optional<User> findById(String id);

    @Query("{isDeleted:false,'deviceUser.userId':?0}")
    List<User> findByDeviceUserId(String userId);
}