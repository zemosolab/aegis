package com.aegis.devicemanagement.service.device;

import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Setter
@Getter
public class DeviceActionService {

    @Autowired
    DeviceActionRepository deviceActionRepository;

    @Autowired
    DeviceService deviceService;

    public void createAction(String deviceId, DeviceAction.ActionType actionType, Map<String, String> details,String userEmail) {
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setActionType(actionType);
        deviceAction.setDeviceId(deviceId);
        deviceAction.setCreatedAt(new Date());
        deviceAction.setCreatedBy(userEmail);
        deviceAction.setDetails((HashMap<String, String>) details);
        deviceAction.setStatus(DeviceAction.ActionStatus.COMPLETED);
        deviceActionRepository.save(deviceAction);
    }

    public List<DeviceAction> getActions(String deviceId) throws DeviceNotFound, SystemException {
        deviceService.checkIfDeviceExists(deviceId);
        List<DeviceAction> deviceActions;
        try {
            deviceActions = deviceActionRepository.findByDeviceId(deviceId);
        } catch (Exception systemException) {
            throw new SystemException("system exception");
        }
        return deviceActions;
    }

    public DeviceAction getAction(String deviceId, String actionId) throws DeviceNotFound, ActionNotFound, DeviceActionMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<DeviceAction> optionalDeviceAction = deviceActionRepository.findById(actionId);
        if (!optionalDeviceAction.isPresent()) {
            throw new ActionNotFound("action not found");
        }
        DeviceAction deviceAction = optionalDeviceAction.get();
        if (!deviceAction.getDeviceId().equals(deviceId)) {
            throw new DeviceActionMismatch("action device mismatch");
        }
        return deviceAction;
    }


}
