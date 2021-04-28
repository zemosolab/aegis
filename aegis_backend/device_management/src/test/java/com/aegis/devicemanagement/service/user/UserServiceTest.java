package com.aegis.devicemanagement.service.user;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.helper.device.DeviceStore;
import com.aegis.devicemanagement.helper.user.UserActionStore;
import com.aegis.devicemanagement.helper.user.UserStore;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.model.user.User;
import com.aegis.devicemanagement.model.user.UserAction;
import com.aegis.devicemanagement.pojos.deviceconfig.DeviceBasicConfig;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.pojos.request.UserRequest;
import com.aegis.devicemanagement.pojos.userConfig.Credential;
import com.aegis.devicemanagement.pojos.userConfig.DeviceUser;
import com.aegis.devicemanagement.repository.device.DeviceRepository;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import com.aegis.devicemanagement.repository.user.UserRepository;
import com.aegis.devicemanagement.repository.useraction.UserActionRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserActionRepository userActionRepository;

    @Mock
    DeviceRepository deviceRepository;
    @Mock
    DeviceActionRepository deviceActionRepository;

    @Mock
    DeviceCommandManager deviceCommandManager;

    DeviceService deviceService;

    DeviceActionService deviceActionService;

    UserService userService;

    UserActionService userActionService;

    private DeviceStore deviceStore = new DeviceStore();
    private UserStore userStore = new UserStore();
    private UserActionStore userActionStore = new UserActionStore();

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

        Mockito.doAnswer(r -> {
            deviceStore.delete(r.getArgument(0));
            return null;
        }).when(deviceRepository).deleteById(any());
        Mockito.when(deviceCommandManager.httpRequestGenerator(any(), any(), any())).thenReturn(new HashMap<>());
        Mockito.when(userRepository.save(any())).thenAnswer(r -> {
            User user = r.getArgument(0);
            return userStore.addToStore(user);
        });
        Mockito.when(userRepository.findById(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return Optional.ofNullable(userStore.getFromStore(id));
        });
        Mockito.when(userRepository.findByDeviceId(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return userStore.findByDeviceId(id);
        });
        Mockito.doAnswer(r -> {
            userStore.delete(r.getArgument(0));
            return null;
        }).when(userRepository).deleteById(any());
        Mockito.when(userActionRepository.save(any())).thenAnswer(r -> {
            UserAction userAction = r.getArgument(0);
            return userActionStore.addToStore(userAction);
        });
        userService = new UserService();
        userService.setUserRepository(userRepository);
        userActionService = new UserActionService();
        userActionService.setUserActionRepository(userActionRepository);
        userActionService.setUserService(userService);
        userService.setUserActionService(userActionService);

        deviceService = new DeviceService();
        deviceService.setDeviceRepository(deviceRepository);
        deviceActionService = new DeviceActionService();
        deviceActionService.setDeviceActionRepository(deviceActionRepository);
        deviceActionService.setDeviceService(deviceService);
        deviceService.setDeviceActionService(deviceActionService);
        deviceService.setDeviceCommandManager(deviceCommandManager);
        userService.setDeviceCommandManager(deviceCommandManager);
        deviceCommandManager.setDeviceService(deviceService);
        userService.setDeviceService(deviceService);
    }

    @Test
    public void createUser() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            DeviceUser deviceUser = (DeviceUser) Helper.populate(new DeviceUser(), DeviceUser.class);
            userRequest.setDeviceUser(deviceUser);
            userService.createUser(userRequest, device.getId(), "");
            UserAction userAction = userActionStore.getAllFromStore().get(0);
            Assert.assertEquals(userAction.getActionType(), UserAction.ActionType.CREATE);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getAllUsersFromADevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");
            for (int i = 0; i < 5; i++) {
                UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
                DeviceUser deviceUser = (DeviceUser) Helper.populate(new DeviceUser(), DeviceUser.class);
                userRequest.setDeviceUser(deviceUser);
                userService.createUser(userRequest, device.getId(), "");
            }
            List<User> users = userService.getAllUsersFromADevice(device.getId());
            Assert.assertEquals(users.size(), 5);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getUser() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            DeviceUser deviceUser = (DeviceUser) Helper.populate(new DeviceUser(), DeviceUser.class);
            userRequest.setDeviceUser(deviceUser);
            User user = userService.createUser(userRequest, device.getId(), "");
            User user1 = userService.getUser(user.getId(), device.getId());
            Assert.assertEquals(user, user1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void updateUser() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            DeviceUser deviceUser = (DeviceUser) Helper.populate(new DeviceUser(), DeviceUser.class);
            userRequest.setDeviceUser(deviceUser);
            userService.createUser(userRequest, device.getId(), "");
            User user1 = userStore.getAllFromStore().get(0);
            Credential credential = (Credential) Helper.populate(new Credential(), Credential.class);
            userRequest.setCredential(credential);
            User user2 = userService.updateUser(userRequest, device.getId(), user1.getId(), "");
            Assert.assertEquals(credential.getType(), user2.getCredential().getType());
            Assert.assertEquals(userActionStore.getUserActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void deleteUser() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            DeviceUser deviceUser = (DeviceUser) Helper.populate(new DeviceUser(), DeviceUser.class);
            userRequest.setDeviceUser(deviceUser);
            userService.createUser(userRequest, device.getId(), "");

            User user = userStore.getAllFromStore().get(0);
            userService.deleteUser(user.getId(), device.getId(), "");
            Assert.assertEquals(userStore.getAllFromStore().size(), 0);
            Assert.assertEquals(userActionStore.getUserActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }
}