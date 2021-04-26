package com.aegis.devicemanagement.controller.device;

import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.service.device.DeviceActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceActionController {

    @Autowired
    DeviceActionService deviceActionService;

    @GetMapping("/devices/{deviceId}/actions")
    public ResponseEntity<List<DeviceAction>> getDeviceActions(@PathVariable String deviceId){
        List<DeviceAction> deviceActions=null;
        try{
            deviceActions=deviceActionService.getActions(deviceId);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(deviceActions);
    }


    @GetMapping("/devices/{deviceId}/actions/{actionId}")
    public ResponseEntity<DeviceAction> getDeviceAction(@PathVariable String deviceId,@PathVariable String actionId){
        DeviceAction deviceAction=null;
        try{
            deviceAction=deviceActionService.getAction(deviceId,actionId);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        catch (ActionNotFound actionNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action not found", actionNotFound);
        }
        catch (DeviceActionMismatch deviceActionMismatch){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action not found", deviceActionMismatch);
        }
        return new ResponseEntity<DeviceAction>(deviceAction,HttpStatus.OK);
    }

}
