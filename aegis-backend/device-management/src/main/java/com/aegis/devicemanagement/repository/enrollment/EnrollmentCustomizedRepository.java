package com.aegis.devicemanagement.repository.enrollment;

public interface EnrollmentCustomizedRepository<T, ID> {
    void deleteById(ID id);
}
