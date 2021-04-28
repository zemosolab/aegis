package com.aegis.devicemanagement.repository.user;

public interface UserCustomizedRepository<T,ID> {
    void deleteById(ID id);
}
