package com.aegis.devicemanagement.repository.device;

import com.aegis.devicemanagement.model.device.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository
        extends MongoRepository<Device, String>, DeviceCustomizedRepository<Device, String> {

    @Query("{isDeleted:false}")
    List<Device> findAll();

    @Query("{isDeleted:false,id:?0}")
    Optional<Device> findById(String id);


    @Query("{isDeleted:false,ipAddress:?0}")
    List<Device> findByIpAddress(String ipAddress);

}