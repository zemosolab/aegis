package com.aegis.devicemanagement.service.device;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.helper.device.DeviceActionStore;
import com.aegis.devicemanagement.helper.device.DeviceStore;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.pojos.deviceconfig.Alarm;
import com.aegis.devicemanagement.pojos.deviceconfig.DeviceBasicConfig;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.repository.device.DeviceRepository;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import com.aegis.devicemanagement.utils.DeviceCommandManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class DeviceServiceTest {

    @Mock
    DeviceRepository deviceRepository;

    @Mock
    DeviceCommandManager deviceCommandManager;

    @Mock
    DeviceActionRepository deviceActionRepository;

    DeviceService deviceService;

    DeviceActionService deviceActionService;

    private DeviceStore deviceStore = new DeviceStore();
    private DeviceActionStore deviceActionStore = new DeviceActionStore();

    @Before
    public void setUp() throws DeviceNotFound {
        Mockito.when(deviceRepository.save(any())).thenAnswer(r -> {
            Device device = r.getArgument(0);
            return deviceStore.addToStore(device);
        });
        Mockito.when(deviceRepository.findAll()).thenAnswer(r -> deviceStore.getAllFromStore());
        Mockito.when(deviceRepository.findById(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return Optional.ofNullable(deviceStore.getFromStore(id));
        });
        Mockito.when(deviceCommandManager.httpRequestGenerator(any(), any(), any())).thenReturn(new HashMap<>());
        Mockito.when(deviceRepository.findByIpAddress(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return deviceStore.findByIp(id);
        });
        Mockito.doAnswer(r -> {
            deviceStore.delete(r.getArgument(0));
            return null;
        }).when(deviceRepository).deleteById(any());
        Mockito.when(deviceActionRepository.save(any())).thenAnswer(r -> {
            DeviceAction deviceAction = r.getArgument(0);
            return deviceActionStore.addToStore(deviceAction);
        });
        deviceService = new DeviceService();
        deviceService.setDeviceRepository(deviceRepository);
        deviceActionService = new DeviceActionService();
        deviceActionService.setDeviceActionRepository(deviceActionRepository);
        deviceActionService.setDeviceService(deviceService);
        deviceService.setDeviceActionService(deviceActionService);
        deviceService.setDeviceCommandManager(deviceCommandManager);
    }

    @Test
    public void createDevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            deviceService.createDevice(deviceRequest, "");
            DeviceAction deviceAction = deviceActionStore.getAllFromStore().get(0);
            Assert.assertEquals(deviceAction.getActionType(), DeviceAction.ActionType.CREATE);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getDeviceById() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");
            Device device1 = deviceService.getDevice(device.getId());
            Assert.assertEquals(device1, device);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void updateDevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            deviceService.createDevice(deviceRequest, "");

            Device device = deviceStore.getAllFromStore().get(0);
            Alarm alarm = (Alarm) Helper.populate(new Alarm(), Alarm.class);
            deviceRequest.setAlarm(alarm);
            Device device1 = deviceService.updateDevice(deviceRequest, device.getId(), "");
            Assert.assertEquals(device1.getAlarm().getAlarm(), alarm.getAlarm());
            Assert.assertEquals(deviceActionStore.getDeviceActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void deleteDevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            deviceService.createDevice(deviceRequest, "");
            Device device = deviceStore.getAllFromStore().get(0);
            deviceService.deleteDevice(device.getId(), "");
            Assert.assertEquals(deviceStore.getAllFromStore().size(), 0);
            Assert.assertEquals(deviceActionStore.getDeviceActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getAllDevices() {
        try {
            for (int i = 0; i < 1; i++) {
                DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
                DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                        DeviceBasicConfig.class);
                deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
                deviceService.createDevice(deviceRequest, "");
            }
            List<Device> devices = deviceService.getAllDevice();
            Assert.assertEquals(devices.size(), 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}