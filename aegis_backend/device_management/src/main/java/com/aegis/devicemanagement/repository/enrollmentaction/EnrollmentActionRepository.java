package com.aegis.devicemanagement.repository.enrollmentaction;

import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EnrollmentActionRepository extends MongoRepository<EnrollmentAction, String> {
    List<EnrollmentAction> findByEnrollmentId(String enrollmentId);
}
