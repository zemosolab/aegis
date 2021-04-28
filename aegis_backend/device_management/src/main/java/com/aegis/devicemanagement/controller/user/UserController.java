package com.aegis.devicemanagement.controller.user;

import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.exception.user.DeviceUserMismatch;
import com.aegis.devicemanagement.exception.user.UserAlreadyExists;
import com.aegis.devicemanagement.exception.user.UserNotFound;
import com.aegis.devicemanagement.model.user.User;
import com.aegis.devicemanagement.pojos.request.UserRequest;
import com.aegis.devicemanagement.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/devices/{deviceId}/users")
    public ResponseEntity<User> createUser(@PathVariable String deviceId, @RequestBody UserRequest userRequest, HttpServletRequest httpServletRequest) {
        User user = null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            user = userService.createUser(userRequest, deviceId,userEmail);
        }catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @PutMapping("/devices/{deviceId}/users/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody UserRequest userRequest, @PathVariable String deviceId, @PathVariable String userId, HttpServletRequest httpServletRequest) {
        User user = null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            user = userService.updateUser(userRequest, deviceId, userId,userEmail);
        } catch (UserNotFound userNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user not found", userNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceActionMismatch deviceActionMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device action mismatch", deviceActionMismatch);
        }
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }


    @GetMapping("/devices/{deviceId}/users")
    public ResponseEntity<List<User>> getAll(@PathVariable String deviceId) {
        List<User> users;
        try {
            users = userService.getAllUsersFromADevice(deviceId);
        } catch (SystemException systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/devices/{deviceId}/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String deviceId, @PathVariable String userId) {
        User user = null;
        try {
            user = userService.getUser(userId, deviceId);
        } catch (UserNotFound userNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user not found", userNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceUserMismatch deviceUserMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device user mismatch", deviceUserMismatch);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @DeleteMapping("/devices/{deviceId}/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable String deviceId, @PathVariable String userId, HttpServletRequest httpServletRequest) {
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            userService.deleteUser(userId, deviceId,userEmail);
        } catch (UserNotFound userNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user not found", userNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceUserMismatch deviceUserMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device user mismatch", deviceUserMismatch);
        }
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
