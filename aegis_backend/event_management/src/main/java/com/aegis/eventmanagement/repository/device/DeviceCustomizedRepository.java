package com.aegis.eventmanagement.repository.device;

public interface DeviceCustomizedRepository<T, ID> {
    void updateCount(String deviceId, int count);
    void deleteByDeviceId(String deviceId);
}
