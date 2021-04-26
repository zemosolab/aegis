package com.aegis.devicemanagement.controller.device;

import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.service.device.DeviceActionService;

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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DeviceActionController.class)
public class DeviceActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DeviceActionService deviceActionService;

    @Test
    public void getDeviceActions() throws Exception {
        List<DeviceAction> deviceActions = new ArrayList<>();
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setDeviceId("123");
        deviceActions.add(deviceAction);
        when(deviceActionService.getActions(Mockito.any())).thenReturn(deviceActions);
        mockMvc.perform(get("/api/devices/Nikhil/actions")).andExpect(status().isOk());
    }

    @Test
   public void getDeviceAction() throws Exception {
        DeviceAction deviceAction = new DeviceAction();
        deviceAction.setDeviceId("Nikhil");
        when(deviceActionService.getAction(Mockito.anyString(), Mockito.any())).thenReturn(deviceAction);
        mockMvc.perform(get("/api/devices/Nikhil/actions/1234")).andExpect(status().isOk());
    }
}