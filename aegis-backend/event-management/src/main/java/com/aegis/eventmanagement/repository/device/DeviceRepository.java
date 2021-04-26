package com.aegis.eventmanagement.repository.device;

import com.aegis.eventmanagement.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

;


@Repository
public interface DeviceRepository extends MongoRepository<Device, String>, DeviceCustomizedRepository<Device, String> {

    @Query("{isDeleted:false,deviceId:?0}")
    List<Device> findByDeviceId(String deviceId);

}
