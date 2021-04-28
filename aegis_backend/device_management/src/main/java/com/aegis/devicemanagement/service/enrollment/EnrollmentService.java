package com.aegis.devicemanagement.service.enrollment;

import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.enrollment.DeviceEnrollmentMismatch;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.model.enrollment.Enrollment;
import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import com.aegis.devicemanagement.pojos.request.EnrollmentRequest;
import com.aegis.devicemanagement.repository.enrollment.EnrollmentRepository;
import com.aegis.devicemanagement.service.device.DeviceService;
import com.aegis.devicemanagement.utils.DeviceCommandManager;
import com.aegis.devicemanagement.utils.Helper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Setter
@Getter
public class EnrollmentService {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    EnrollmentActionService enrollmentActionService;

    @Autowired
    DeviceCommandManager deviceCommandManager;

    @Autowired
    DeviceService deviceService;

    public Enrollment createEnrollment(EnrollmentRequest enrollmentRequest, String deviceId,String userEmail) throws DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        Enrollment enrollment = new Enrollment();
        Helper.copyEnrollmentDetails(enrollment, enrollmentRequest, deviceId);
        Enrollment enrollment1 = enrollmentRepository.save(enrollment);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(enrollment, Helper.ActionType.DEVICESET,deviceId);
        enrollmentActionService.createAction(enrollment.getId(), EnrollmentAction.ActionType.CREATE, details,userEmail);
        return enrollment1;
    }

    public List<Enrollment> getAllEnrollmentsFromADevice(String deviceId) throws SystemException, DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        List<Enrollment> enrollments;
        try {
            enrollments = enrollmentRepository.findByDeviceId(deviceId);
        } catch (Exception systemException) {
            throw new SystemException("System exception");
        }
        return enrollments;
    }

    public Enrollment getEnrollment(String enrollmentId, String deviceId) throws EnrollmentNotFound, DeviceNotFound, DeviceEnrollmentMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        if (!optionalEnrollment.isPresent()) {
            throw new EnrollmentNotFound("enrollment not found");
        }
        Enrollment enrollment = optionalEnrollment.get();
        if (!enrollment.getDeviceId().equals(deviceId)) {
            throw new DeviceEnrollmentMismatch("Enrollment not found in this device");
        }
        return enrollment;
    }


    public Enrollment updateEnrollment(EnrollmentRequest enrollmentRequest, String deviceId, String enrollmentId,String userEmail) throws EnrollmentNotFound, DeviceNotFound, DeviceActionMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        if (!optionalEnrollment.isPresent()) {
            throw new EnrollmentNotFound("enrollment not found");
        }
        Enrollment enrollment = optionalEnrollment.get();
        if (!enrollment.getDeviceId().equals(deviceId)) {
            throw new DeviceActionMismatch("device action mismatch");
        }
        Helper.copyEnrollmentDetails(enrollment, enrollmentRequest, deviceId);
        Map<String, String> details = deviceCommandManager.httpRequestGenerator(enrollmentRequest, Helper.ActionType.DEVICESET,deviceId);
        enrollmentActionService.createAction(enrollmentId, EnrollmentAction.ActionType.UPDATE, details,userEmail);
        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(String enrollmentId, String deviceId,String userEmail) throws EnrollmentNotFound, DeviceNotFound, DeviceEnrollmentMismatch {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        if (!optionalEnrollment.isPresent()) {
            throw new EnrollmentNotFound("enrollment not found");
        }
        if (!optionalEnrollment.get().getDeviceId().equals(deviceId)) {
            throw new DeviceEnrollmentMismatch("device enrollment mismatch");
        }
        enrollmentActionService.createAction(enrollmentId, EnrollmentAction.ActionType.DELETE, null,userEmail);
        enrollmentRepository.deleteById(enrollmentId);
    }

    public void checkIfEnrollmentExists(String enrollmentId, String deviceId) throws EnrollmentNotFound, DeviceNotFound {
        deviceService.checkIfDeviceExists(deviceId);
        Optional<Enrollment> optionalEnrollment = enrollmentRepository.findById(enrollmentId);
        if (!optionalEnrollment.isPresent()) {
            throw new EnrollmentNotFound("enrollment not found");
        }
    }
}
