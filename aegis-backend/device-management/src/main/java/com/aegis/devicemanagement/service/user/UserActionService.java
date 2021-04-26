package com.aegis.devicemanagement.service.user;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.exception.user.UserActionMismatch;
import com.aegis.devicemanagement.exception.user.UserNotFound;
import com.aegis.devicemanagement.model.user.UserAction;
import com.aegis.devicemanagement.repository.useraction.UserActionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Setter
@Getter
@Transactional
public class UserActionService {
    @Autowired
    UserActionRepository userActionRepository;

    @Autowired
    UserService userService;

    public UserAction createAction(String userId, UserAction.ActionType actionType, Map<String, String> details,String userEmail) {
        UserAction userAction = new UserAction();
        userAction.setActionType(actionType);
        userAction.setUserId(userId);
        userAction.setCreatedAt(new Date());
        userAction.setCreatedBy(userEmail);
        userAction.setDetails((HashMap<String, String>) details);
        userAction.setStatus(UserAction.ActionStatus.COMPLETED);
        return userActionRepository.save(userAction);
    }

    public List<UserAction> getActions(String userId, String deviceId) throws UserNotFound, SystemException, DeviceNotFound {
        userService.checkIfUserExists(userId, deviceId);
        List<UserAction> userActions;
        try {
            userActions = userActionRepository.findByUserId(userId);
        } catch (Exception systemException) {
            throw new SystemException("system exception");
        }
        return userActions;
    }

    public UserAction getAction(String userId, String deviceId, String actionId) throws UserNotFound, ActionNotFound, UserActionMismatch, DeviceNotFound {
        userService.checkIfUserExists(userId, deviceId);
        Optional<UserAction> optionalDeviceAction = userActionRepository.findById(actionId);
        if (!optionalDeviceAction.isPresent()) {
            throw new ActionNotFound("action not found");
        }
        UserAction userAction = optionalDeviceAction.get();
        if (!userAction.getUserId().equals(userId)) {
            throw new UserActionMismatch("action user mismatch");
        }
        return userAction;
    }
}
