package com.aegis.devicemanagement.controller.device;

import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.service.device.DeviceService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DeviceService deviceService;

    @Test
    public void createDevice() throws Exception {
        Device device = new Device();
        device.setId("Nikhil");
        when(deviceService.createDevice(Mockito.any(DeviceRequest.class),any())).thenReturn(device);
        mockMvc.perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(device)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateDevice() throws Exception {
        Device device = new Device();
        device.setId("Nikhil");
        when(deviceService.updateDevice(Mockito.any(DeviceRequest.class), Mockito.any(String.class),anyString())).thenReturn(device);
        mockMvc.perform(put("/api/devices/Nikhil")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(device)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void getAll() throws Exception {
        List<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setId("Nikhil");
        devices.add(device);
        when(deviceService.getAllDevice()).thenReturn(devices);
        mockMvc.perform(get("/api/devices")).andExpect(status().isOk());
    }

    @Test
    public void getDevice() throws Exception {
        Device device = new Device();
        device.setId("Nikhil");
        when(deviceService.getDevice(Mockito.anyString())).thenReturn(device);
        mockMvc.perform(get("/api/devices/Nikhil")).andExpect(status().isOk());
    }

    @Test
    public void deleteDevice() throws Exception {
        Mockito.doAnswer(r -> null).when(deviceService).deleteDevice(any(),any());
        mockMvc.perform(delete("/api/devices/Nikhil")).andExpect(status().isNoContent());
    }

    @Test
    public void health() {
    }
}