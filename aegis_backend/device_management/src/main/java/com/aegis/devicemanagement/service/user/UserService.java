package com.aegis.devicemanagement.service.user;

import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.exception.user.DeviceUserMismatch;
import com.aegis.devicemanagement.exception.user.UserAlreadyExists;
import com.aegis.devicemanagement.exception.user.UserNotFound;
import com.aegis.devicemanagement.model.user.User;
import com.aegis.devicemanagement.model.user.UserAction;
import com.aegis.devicemanagement.pojos.request.UserRequest;
import com.aegis.devicemanagement.repository.user.UserRepository;
import com.aegis.devicemanagement.service.device.DeviceService;
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
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserActionService userActionService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceCommandManager deviceCommandManager;

    @Transactional
    public User createUser(UserRequest userRequest, String deviceId,String userEmail) throws DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        User user = new User();
        Helper.copyUserDetails(user, userRequest, deviceId);
        User user1 = userRepository.save(user);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(user, Helper.ActionType.DEVICESET,deviceId);
        userActionService.createAction(user.getId(), UserAction.ActionType.CREATE, details,userEmail);
        return user1;
    }

    @Transactional
    public List<User> getAllUsersFromADevice(String deviceId) throws SystemException, DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        List<User> users;
        try {
            users = userRepository.findByDeviceId(deviceId);
        } catch (Exception systemException) {
            throw new SystemException("System exception");
        }
        return users;
    }

    @Transactional
    public User getUser(String userId, String deviceId) throws UserNotFound, DeviceNotFound, DeviceUserMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("user not found");
        }
        User user = optionalUser.get();
        if (!user.getDeviceId().equals(deviceId)) {
            throw new DeviceUserMismatch("User not found in this device");
        }

        return user;
    }


    @Transactional
    public User updateUser(UserRequest userRequest, String deviceId, String userId,String userEmail) throws UserNotFound, DeviceNotFound, DeviceActionMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("user not found");
        }
        User user = optionalUser.get();
        if (!user.getDeviceId().equals(deviceId)) {
            throw new DeviceActionMismatch("device action mismatch");
        }
        Helper.copyUserDetails(user, userRequest, deviceId);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(userRequest, Helper.ActionType.DEVICESET,deviceId);
        userActionService.createAction(userId, UserAction.ActionType.UPDATE, details,userEmail);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId, String deviceId,String userEmail) throws UserNotFound, DeviceNotFound, DeviceUserMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("user not found");
        }
        if (!optionalUser.get().getDeviceId().equals(deviceId)) {
            throw new DeviceUserMismatch("device user mismatch");
        }
        User user=optionalUser.get();
        userRepository.deleteById(userId);
        Map<String,String> queryParams= new HashMap<>();
        Map<String,String> httpRequestResponse= new HashMap<>();
        queryParams.put("userId",user.getDeviceUser().getUserId());
        deviceCommandManager.sendRequest("users",queryParams,httpRequestResponse, Helper.ActionType.DEVICEDELETE,deviceId);
        userActionService.createAction(userId, UserAction.ActionType.DELETE, null,userEmail);
    }

    @Transactional
    public void checkIfUserExists(String userId, String deviceId) throws UserNotFound, DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFound("user not found");
        }
    }
}
