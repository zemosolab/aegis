package com.aegis.devicemanagement.controller.enrollment;

import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import com.aegis.devicemanagement.service.enrollment.EnrollmentActionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EnrollmentActionController.class)
public class EnrollmentActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EnrollmentActionService enrollmentActionService;

    @Test
    public void getEnrollmentActions() throws Exception {
        List<EnrollmentAction> enrollmentActions = new ArrayList<>();
        EnrollmentAction enrollmentAction = new EnrollmentAction();
        enrollmentAction.setEnrollmentId("123");
        enrollmentActions.add(enrollmentAction);
        when(enrollmentActionService.getActions(any(), any())).thenReturn(enrollmentActions);
        mockMvc.perform(get("/api/devices/Nikhil/enrollment/123/actions")).andExpect(status().isOk());
    }

    @Test
    public void getEnrollmentActionByActionId() throws Exception {
        EnrollmentAction enrollmentAction = new EnrollmentAction();
        enrollmentAction.setEnrollmentId("Nikhil");
        when(enrollmentActionService.getAction(Mockito.anyString(), Mockito.any(), any())).thenReturn(enrollmentAction);
        mockMvc.perform(get("/api/devices/Nikhil/enrollment/123/actions/456")).andExpect(status().isOk());
    }
}