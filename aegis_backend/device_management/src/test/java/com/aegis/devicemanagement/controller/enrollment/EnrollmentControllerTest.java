package com.aegis.devicemanagement.controller.enrollment;

import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.model.enrollment.Enrollment;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollUser;
import com.aegis.devicemanagement.pojos.request.EnrollmentRequest;
import com.aegis.devicemanagement.service.enrollment.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EnrollmentController.class)
public class EnrollmentControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EnrollmentService enrollmentService;

    @Test
    public void createEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment();
        EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
        enrollment.setEnrollUser(enrollUser);
        when(enrollmentService.createEnrollment(Mockito.any(EnrollmentRequest.class), Mockito.any(),any())).thenReturn(enrollment);
        mockMvc.perform(post("/api/devices/DEVICEID/enrollments").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(enrollment)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment();
        enrollment.setDeviceId("DEVICEID");
        when(enrollmentService.updateEnrollment(Mockito.any(EnrollmentRequest.class), Mockito.any(String.class), Mockito.any(),any())).thenReturn(enrollment);
        mockMvc.perform(put("/api/devices/DEVICEID/enrollments/123")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(enrollment)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void getAll() throws Exception {
        List<Enrollment> enrollments = new ArrayList<>();
        Enrollment enrollment = new Enrollment();
        EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
        enrollment.setEnrollUser(enrollUser);
        enrollments.add(enrollment);
        when(enrollmentService.getAllEnrollmentsFromADevice(Mockito.any())).thenReturn(enrollments);
        mockMvc.perform(get("/api/devices/DEVICEID/enrollments")).andExpect(status().isOk());
    }

    @Test
    public void getEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment();
        EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
        enrollment.setEnrollUser(enrollUser);
        when(enrollmentService.getEnrollment(Mockito.any(), Mockito.anyString())).thenReturn(enrollment);
        mockMvc.perform(get("/api/devices/DEVICEID/enrollments/123")).andExpect(status().isOk());
    }

    @Test
    public void deleteEnrollment() throws Exception {
        Mockito.doAnswer(r -> null).when(enrollmentService).deleteEnrollment(any(), any(),any());
        mockMvc.perform(delete("/api/devices/DEVICEID/enrollments/123")).andExpect(status().isNoContent());
    }
}