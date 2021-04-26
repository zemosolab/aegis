package com.aegis.devicemanagement.controller.device;

import com.aegis.devicemanagement.exception.device.DeviceAlreadyExists;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.service.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @PostMapping("/devices")
    public ResponseEntity<Device> createDevice(@RequestBody DeviceRequest deviceRequest, HttpServletRequest httpServletRequest) {
        Device device=null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            device=deviceService.createDevice(deviceRequest,userEmail);
        }
        catch (DeviceAlreadyExists deviceAlreadyExists){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device already exists", deviceAlreadyExists);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "system exception", systemException);
        }
        return new ResponseEntity<Device>(device, HttpStatus.CREATED);
    }

    @PutMapping("/devices/{deviceId}")
    public ResponseEntity<Device> updateDevice(@RequestBody DeviceRequest deviceRequest,@PathVariable String deviceId, HttpServletRequest httpServletRequest){
        Device device=null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            device=deviceService.updateDevice(deviceRequest,deviceId,userEmail);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return new ResponseEntity<>(device,HttpStatus.ACCEPTED);
    }


    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAll(){
        List<Device> devices;

        try{
            devices=deviceService.getAllDevice();
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "system exception", systemException);
        }
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/devices/{deviceId}")
    public ResponseEntity<Device> getDevice(@PathVariable String deviceId){
        Device device=null;
        try{
           device= deviceService.getDevice(deviceId);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return new ResponseEntity<Device>(device,HttpStatus.OK);
    }


    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity deleteDevice(@PathVariable String deviceId, HttpServletRequest httpServletRequest){
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try{
        deviceService.deleteDevice(deviceId,userEmail);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);

        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/health/liveness")
    public ResponseEntity<String> health(){
        return new ResponseEntity<String>("alive",HttpStatus.OK);
    }
}
