package com.aegis.devicemanagement.repository.deviceaction;

import com.aegis.devicemanagement.model.device.DeviceAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceActionRepository  extends MongoRepository<DeviceAction,String> {
List<DeviceAction> findByDeviceId(String deviceId);
}
