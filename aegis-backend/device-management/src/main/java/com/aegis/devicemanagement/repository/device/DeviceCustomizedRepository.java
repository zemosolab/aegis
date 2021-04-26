package com.aegis.devicemanagement.repository.device;

public interface DeviceCustomizedRepository<T,ID> {
    void deleteById(ID id);
}
