package com.aegis.devicemanagement.service.enrollment;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentActionMismatch;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentNotFound;
import com.aegis.devicemanagement.exception.generic.ActionNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import com.aegis.devicemanagement.repository.enrollmentaction.EnrollmentActionRepository;
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
public class EnrollmentActionService {
    @Autowired
    EnrollmentActionRepository enrollmentActionRepository;

    @Autowired
    EnrollmentService enrollmentService;

    public EnrollmentAction createAction(String enrollmentId, EnrollmentAction.ActionType actionType, Map<String, String> details,String userEmail) {
        EnrollmentAction enrollmentAction = new EnrollmentAction();
        enrollmentAction.setActionType(actionType);
        enrollmentAction.setEnrollmentId(enrollmentId);
        enrollmentAction.setCreatedAt(new Date());
        enrollmentAction.setCreatedBy(userEmail);
        enrollmentAction.setDetails((HashMap<String, String>) details);
        enrollmentAction.setStatus(EnrollmentAction.ActionStatus.COMPLETED);
        return enrollmentActionRepository.save(enrollmentAction);
    }

    public List<EnrollmentAction> getActions(String enrollmentId, String deviceId) throws EnrollmentNotFound, SystemException, DeviceNotFound {
        enrollmentService.checkIfEnrollmentExists(enrollmentId, deviceId);
        List<EnrollmentAction> enrollmentActions = null;
        try {
            enrollmentActions = enrollmentActionRepository.findByEnrollmentId(enrollmentId);
        } catch (Exception systemException) {
            throw new SystemException("system exception");
        }
        return enrollmentActions;
    }

    public EnrollmentAction getAction(String enrollmentId, String deviceId, String actionId) throws EnrollmentNotFound, ActionNotFound, EnrollmentActionMismatch, DeviceNotFound {
        enrollmentService.checkIfEnrollmentExists(enrollmentId, deviceId);
        Optional<EnrollmentAction> optionalEnrollmentAction = enrollmentActionRepository.findById(actionId);
        if (!optionalEnrollmentAction.isPresent()) {
            throw new ActionNotFound("action not found");
        }
        EnrollmentAction enrollmentAction = optionalEnrollmentAction.get();
        if (!enrollmentAction.getEnrollmentId().equals(enrollmentId)) {
            throw new EnrollmentActionMismatch("action enrollment mismatch");
        }
        return enrollmentAction;
    }
}
