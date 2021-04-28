package com.aegis.devicemanagement.service.device;

import com.aegis.devicemanagement.exception.device.DeviceAlreadyExists;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.repository.device.DeviceRepository;
import com.aegis.devicemanagement.utils.DeviceCommandManager;
import com.aegis.devicemanagement.utils.Helper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Setter
@Getter
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceActionService deviceActionService;

    @Autowired
    DeviceCommandManager deviceCommandManager;

    @Transactional
    public Device createDevice(DeviceRequest deviceRequest,String userEmail) throws DeviceAlreadyExists, DeviceNotFound {
        List<Device> optionalDevice = deviceRepository.findByIpAddress(deviceRequest.getIpAddress());
        if (optionalDevice.size() > 0) {
            throw new DeviceAlreadyExists("device already exists");
        }
        Device device = new Device();
        Helper.copyDeviceDetails(device, deviceRequest);
        String deviceLink=Helper.generateDeviceLink(device.getIpAddress(),device.getPortNo());
        device.setDeviceLink(deviceLink);
        Device device1 = deviceRepository.save(device);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(device, Helper.ActionType.DEVICESET,device1.getId());
        deviceActionService.createAction(device.getId(), DeviceAction.ActionType.CREATE, details,userEmail);
        return device1;
    }


    @Transactional
    public Device getDevice(String deviceId) throws DeviceNotFound {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new DeviceNotFound("device not found");
        }
        return optionalDevice.get();
    }

    @Transactional
    public Device updateDevice(DeviceRequest deviceRequest, String deviceId,String userEmail) throws DeviceNotFound {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new DeviceNotFound("device not found");
        }
        Device device = optionalDevice.get();
        Helper.copyDeviceDetails(device, deviceRequest);
        String deviceLink=Helper.generateDeviceLink(device.getIpAddress(),device.getPortNo());
        device.setDeviceLink(deviceLink);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(deviceRequest, Helper.ActionType.DEVICESET,device.getId());
        deviceActionService.createAction(deviceId, DeviceAction.ActionType.UPDATE, details,userEmail);
        return deviceRepository.save(device);
    }

    @Transactional
    public void deleteDevice(String deviceId,String userEmail) throws DeviceNotFound {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new DeviceNotFound("device not found");
        }
        deviceRepository.deleteById(deviceId);
        deviceActionService.createAction(deviceId, DeviceAction.ActionType.DELETE, new HashMap<>(),userEmail);
    }

    @Transactional
    public void checkIfDeviceExists(String deviceId) throws DeviceNotFound {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (!optionalDevice.isPresent()) {
            throw new DeviceNotFound("device not found");
        }
    }


    @Transactional
    public List<Device> getAllDevice() throws Exception {
        List<Device> devices;
        try {
            devices = deviceRepository.findAll();
        } catch (Exception systemException) {
            throw new Exception();
        }
        return devices;
    }
}
