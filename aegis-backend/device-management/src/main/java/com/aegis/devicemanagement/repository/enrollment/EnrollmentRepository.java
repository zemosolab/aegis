package com.aegis.devicemanagement.repository.enrollment;

import com.aegis.devicemanagement.model.enrollment.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String>, EnrollmentCustomizedRepository<Enrollment, String> {
    @Query("{isDeleted:false}")
    List<Enrollment> findAll();

    @Query("{isDeleted:false,id:?0}")
    Optional<Enrollment> findById(String id);

    @Query("{isDeleted:false,deviceId:?0}")
    List<Enrollment> findByDeviceId(String deviceId);
}
