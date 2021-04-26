package com.aegis.eventmanagement.service;

import com.aegis.eventmanagement.exceptions.DeviceAlreadyExists;
import com.aegis.eventmanagement.exceptions.DeviceNotFound;
import com.aegis.eventmanagement.exceptions.SystemException;
import com.aegis.eventmanagement.model.Device;
import com.aegis.eventmanagement.pojos.request.DeviceRequest;
import com.aegis.eventmanagement.repository.device.DeviceRepository;
import com.aegis.eventmanagement.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    public void updateEventCounter(String deviceId, int count) throws SystemException, DeviceNotFound {
        checkIfDeviceExists(deviceId);
        try{
            deviceRepository.updateCount(deviceId, count);
        }
        catch (Exception systemException){
            throw new SystemException("system exception");
        }

    }

    public Device registerDevice(DeviceRequest deviceRequest) throws SystemException, DeviceAlreadyExists {
        List<Device> devices=deviceRepository.findByDeviceId(deviceRequest.getDeviceId());
        if(devices.size()>0){
            throw new DeviceAlreadyExists("device already exists");
        }
        try {
            Device device = new Device();
            Helper.copyDeviceDetails(device, deviceRequest);
            return deviceRepository.save(device);
        }
        catch (Exception systemException){
            throw new SystemException("system exception");
        }
    }


    public List<Device> getAllDevices() throws SystemException {
        List<Device> devices=null;
        try{
            devices=deviceRepository.findAll();
        }
        catch (Exception systemException){
            throw new SystemException("system exception");
        }
        return devices;
    }

    public void unregisterDevice(String deviceId) throws SystemException, DeviceNotFound {
        checkIfDeviceExists(deviceId);
        try{
            deviceRepository.deleteByDeviceId(deviceId);
        }
        catch (Exception systemException){
            throw new SystemException("system exception");
        }

    }

    public void checkIfDeviceExists(String deviceId) throws DeviceNotFound {
       List<Device> devices= deviceRepository.findByDeviceId(deviceId);
       if(devices.size()==0){
           throw new DeviceNotFound("device Not found");
       }
    }

}
