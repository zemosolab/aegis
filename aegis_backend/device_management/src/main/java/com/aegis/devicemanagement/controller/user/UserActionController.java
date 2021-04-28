package com.aegis.devicemanagement.controller.user;

import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.user.UserActionMismatch;
import com.aegis.devicemanagement.exception.user.UserNotFound;
import com.aegis.devicemanagement.model.user.UserAction;
import com.aegis.devicemanagement.service.user.UserActionService;
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
public class UserActionController {
    @Autowired
    UserActionService userActionService;

    @GetMapping("/devices/{deviceId}/users/{userId}/actions")
    public ResponseEntity<List<UserAction>> getUserActions(@PathVariable String deviceId, @PathVariable String userId) {
        List<UserAction> userActions = null;
        try {
            userActions = userActionService.getActions(userId, deviceId);
        } catch (UserNotFound userNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user not found", userNotFound);
        } catch (Exception systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(userActions);
    }


    @GetMapping("/devices/{deviceId}/users/{userId}/actions/{actionId}")
    public ResponseEntity<UserAction> getUserActionByActionId(@PathVariable String deviceId, @PathVariable String userId, @PathVariable String actionId) {
        UserAction userAction = null;
        try {
            userAction = userActionService.getAction(userId, deviceId, actionId);
        } catch (UserNotFound userNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user not found", userNotFound);
        } catch (ActionNotFound actionNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action not found", actionNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (UserActionMismatch userActionMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "user action mismatch", userActionMismatch);
        }
        return new ResponseEntity<UserAction>(userAction, HttpStatus.OK);
    }

}
