package com.aegis.devicemanagement.controller.enrollment;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentActionMismatch;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentNotFound;
import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import com.aegis.devicemanagement.service.enrollment.EnrollmentActionService;
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
@RequestMapping("/api/devices")
public class EnrollmentActionController {
    @Autowired
    EnrollmentActionService enrollmentActionService;

    @GetMapping("/{deviceId}/enrollment/{enrollmentId}/actions")
    public ResponseEntity<List<EnrollmentAction>> getEnrollmentActions(@PathVariable String deviceId, @PathVariable String enrollmentId) {
        List<EnrollmentAction> enrollmentActions;
        try {
            enrollmentActions = enrollmentActionService.getActions(enrollmentId, deviceId);
        } catch (EnrollmentNotFound enrollmentNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment not found", enrollmentNotFound);
        } catch (Exception systemException) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(enrollmentActions);
    }


    @GetMapping("/{deviceId}/enrollment/{enrollmentId}/actions/{actionId}")
    public ResponseEntity<EnrollmentAction> getEnrollmentActionByActionId(@PathVariable String deviceId, @PathVariable String enrollmentId, @PathVariable String actionId) {
        EnrollmentAction enrollmentAction;
        try {
            enrollmentAction = enrollmentActionService.getAction(enrollmentId, deviceId, actionId);
        } catch (EnrollmentNotFound enrollmentNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment not found", enrollmentNotFound);
        } catch (ActionNotFound actionNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action not found", actionNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (EnrollmentActionMismatch enrollmentActionMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment action mismatch", enrollmentActionMismatch);
        }
        return new ResponseEntity<>(enrollmentAction, HttpStatus.OK);
    }
}
