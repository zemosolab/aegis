package com.aegis.devicemanagement.controller.enrollment;

import com.aegis.devicemanagement.exception.device.DeviceActionMismatch;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.exception.enrollment.DeviceEnrollmentMismatch;
import com.aegis.devicemanagement.exception.enrollment.EnrollmentNotFound;
import com.aegis.devicemanagement.exception.generic.SystemException;
import com.aegis.devicemanagement.model.enrollment.Enrollment;
import com.aegis.devicemanagement.pojos.request.EnrollmentRequest;
import com.aegis.devicemanagement.service.enrollment.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class EnrollmentController {
    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping("/{deviceId}/enrollments")
    public ResponseEntity<Enrollment> createEnrollment(@PathVariable String deviceId, @RequestBody EnrollmentRequest enrollmentRequest, HttpServletRequest httpServletRequest) {
        Enrollment enrollment;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            enrollment = enrollmentService.createEnrollment(enrollmentRequest, deviceId,userEmail);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
    }

    @PutMapping("/{deviceId}/enrollments/{enrollmentId}")
    public ResponseEntity<Enrollment> updateEnrollment(@RequestBody EnrollmentRequest enrollmentRequest, @PathVariable String deviceId, @PathVariable String enrollmentId, HttpServletRequest httpServletRequest) {
        Enrollment enrollment;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            enrollment = enrollmentService.updateEnrollment(enrollmentRequest, deviceId, enrollmentId,userEmail);
        } catch (EnrollmentNotFound enrollmentNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment not found", enrollmentNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceActionMismatch deviceActionMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device action mismatch", deviceActionMismatch);
        }
        return new ResponseEntity<>(enrollment, HttpStatus.ACCEPTED);
    }


    @GetMapping("/{deviceId}/enrollments")
    public ResponseEntity<List<Enrollment>> getAll(@PathVariable String deviceId) {
        List<Enrollment> enrollments;
        try {
            enrollments = enrollmentService.getAllEnrollmentsFromADevice(deviceId);
        } catch (SystemException systemException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "system exception", systemException);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        }
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/{deviceId}/enrollments/{enrollmentId}")
    public ResponseEntity<Enrollment> getEnrollment(@PathVariable String deviceId, @PathVariable String enrollmentId) {
        Enrollment enrollment;
        try {
            enrollment = enrollmentService.getEnrollment(enrollmentId, deviceId);
        } catch (EnrollmentNotFound enrollmentNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment not found", enrollmentNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceEnrollmentMismatch deviceEnrollmentMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device enrollment mismatch", deviceEnrollmentMismatch);
        }
        return new ResponseEntity<>(enrollment, HttpStatus.OK);
    }


    @DeleteMapping("/{deviceId}/enrollments/{enrollmentId}")
    public ResponseEntity deleteEnrollment(@PathVariable String deviceId, @PathVariable String enrollmentId, HttpServletRequest httpServletRequest) {
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            enrollmentService.deleteEnrollment(enrollmentId, deviceId,userEmail);
        } catch (EnrollmentNotFound enrollmentNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "enrollment not found", enrollmentNotFound);
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (DeviceEnrollmentMismatch deviceEnrollmentMismatch) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device enrollment mismatch", deviceEnrollmentMismatch);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
