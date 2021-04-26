package com.aegis.devicemanagement.service.command;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.helper.command.CommandStore;
import com.aegis.devicemanagement.helper.device.DeviceStore;
import com.aegis.devicemanagement.model.command.Command;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.pojos.deviceconfig.DeviceBasicConfig;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.repository.command.CommandRepository;
import com.aegis.devicemanagement.repository.device.DeviceRepository;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import com.aegis.devicemanagement.service.device.DeviceActionService;
import com.aegis.devicemanagement.service.device.DeviceService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class CommandServiceTest {

    @Mock
    CommandRepository commandRepository;

    @Mock
    DeviceRepository deviceRepository;

    @Mock
    DeviceCommandManager deviceCommandManager;

    @Mock
    DeviceActionRepository deviceActionRepository;

    DeviceService deviceService;

    DeviceActionService deviceActionService;

    CommandService commandService;

    private DeviceStore deviceStore = new DeviceStore();

    private CommandStore commandStore = new CommandStore();

    @Before
    public void setUp() throws DeviceNotFound {
        Mockito.when(deviceRepository.save(any())).thenAnswer(r -> {
            Device device = r.getArgument(0);
            return deviceStore.addToStore(device);
        });
        Mockito.when(deviceRepository.findById(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return Optional.ofNullable(deviceStore.getFromStore(id));
        });
        Mockito.when(deviceRepository.findByIpAddress(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return deviceStore.findByIp(id);
        });
        Mockito.when(commandRepository.save(any())).thenAnswer(r -> {
            Command command = r.getArgument(0);
            return commandStore.addToStore(command);
        });
        Mockito.when(deviceCommandManager.httpRequestGenerator(any(), any(), any())).thenReturn(new HashMap<>());
        commandService = new CommandService();
        commandService.setCommandRepository(commandRepository);

        deviceService = new DeviceService();
        deviceService.setDeviceRepository(deviceRepository);
        deviceActionService = new DeviceActionService();
        deviceActionService.setDeviceActionRepository(deviceActionRepository);
        deviceActionService.setDeviceService(deviceService);
        deviceService.setDeviceActionService(deviceActionService);
        deviceService.setDeviceCommandManager(deviceCommandManager);
        commandService.setDeviceCommandManager(deviceCommandManager);
        commandService.setDeviceService(deviceService);
    }

    @Test
    public void sendCommandToDevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(), DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            Command command = commandService.sendCommandToDevice("lockdoor", device.getId(), "POST");
            Command command1 = commandStore.getFromStore(command.getId());
            Assert.assertEquals(com.aegis.devicemanagement.utils.Helper.ActionType.valueOf(command.getType()), com.aegis.devicemanagement.utils.Helper.ActionType.valueOf(command1.getType()));
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getCommandFromDevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(), DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            Command command = commandService.sendCommandToDevice("lockdoor", device.getId(), "POST");
            Command command1 = commandService.sendCommandToDevice("GETEVENTCOUNT", device.getId(), "GET");
            Assert.assertNotEquals(command, command1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}