package com.aegis.eventmanagement.controller;


import com.aegis.eventmanagement.exceptions.DeviceAlreadyExists;
import com.aegis.eventmanagement.exceptions.DeviceNotFound;
import com.aegis.eventmanagement.model.Device;
import com.aegis.eventmanagement.pojos.request.DeviceRequest;
import com.aegis.eventmanagement.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @PostMapping("/events/devices")
    public ResponseEntity<Device> registerDevice(@RequestBody DeviceRequest deviceRequest){
        Device device=null;
        try{
            device=deviceService.registerDevice(deviceRequest);
        }
        catch (DeviceAlreadyExists deviceAlreadyExists){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", deviceAlreadyExists);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }

    return new ResponseEntity<>(device,HttpStatus.CREATED);
    }


    @GetMapping("/events/devices")
    public ResponseEntity<List<Device>> getRegisterDevices(){
        List<Device> devices=null;
        try{
            devices=deviceService.getAllDevices();
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }

        return new ResponseEntity<>(devices,HttpStatus.OK);
    }

    @DeleteMapping("/events/devices/{deviceId}")
    public ResponseEntity unregisterDevice(@PathVariable String deviceId){
        try{
            deviceService.unregisterDevice(deviceId);
        }
        catch (DeviceNotFound deviceNotFound){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", deviceNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return (ResponseEntity) ResponseEntity.ok();
    }

}
