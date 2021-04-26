package com.aegis.usermanagement.service;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.helper.Helper;
import com.aegis.usermanagement.helper.UserActionStore;
import com.aegis.usermanagement.helper.UserStore;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.pojos.UserRequest;
import com.aegis.usermanagement.pojos.Users;
import com.aegis.usermanagement.repository.UserActionRepository;
import com.aegis.usermanagement.repository.UserRepository;
import com.aegis.usermanagement.utils.DeviceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.event.internal.DefaultEvictEventListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActionRepository userActionRepository;

    @Mock
    private DeviceManager deviceManager;

    public UserStore userStore= new UserStore();
    private UserActionStore userActionStore= new UserActionStore();
    private UserService userService;
    private UserActionService userActionService;

    @Before
    public void init() throws UserNotFound, JsonProcessingException {


        Mockito.doAnswer(r->{
            return null;
        }).when(deviceManager).registerUserWithDevice(any(),any());

        Mockito.doAnswer(r->{
            return null;
        }).when(deviceManager).deleteUserFromDevice(any(),any());
        Mockito.when(userRepository.save( any())).thenAnswer(r->{
            User user=r.getArgument(0);
            user.setId(UUID.randomUUID());
            return userStore.addToStore(user);
        });
        Mockito.when(userRepository.findById(any())).thenAnswer(r -> {
            UUID uuid = r.getArgument(0);
            return Optional.ofNullable(userStore.getFromStore(uuid));
        });
        Mockito.when(userRepository.findAll()).thenAnswer(r -> userStore.getAllFromStore());
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
        UserActionService userActionService = new UserActionService();
        userActionService.setUserActionRepository(userActionRepository);
        userService.setUserActionService(userActionService);
        userActionService.setUserService(userService);
        userActionService.setDeviceManager(deviceManager);
    }

    @Test
    public void createUserTest() {
        UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        userRequest.setPhoneNo("7337470452");
        userRequest.setUserType(User.UserType.GUEST);
        userRequest.setUserEmail(null);
        try {
            User user;
            user = userService.createUser(userRequest, "@GMAIL");
            List<User> userList = userStore.getAllFromStore();
            Assert.assertEquals(userList.size(), 1);
            Assert.assertEquals(userActionStore.getAllFromStore().size(), 1);
            Assert.assertEquals(userList.get(0).getPhoneNo(), userRequest.getPhoneNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateUserTest() {
        userStore.clear();
        UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        UserRequest userRequest1 = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        userRequest.setPhoneNo("7337470452");
        userRequest.setUserType(User.UserType.GUEST);
        userRequest.setUserEmail("a@gmail.com");
        userRequest1.setPhoneNo("7337470453");
        userRequest1.setUserType(User.UserType.GUEST);
        userRequest1.setUserEmail("b@gmail.com");
        try {
            User user = userService.createUser(userRequest, "@GMAIL");
            userService.updateUser(userRequest1, user.getId(), "");
            Assert.assertEquals(userRequest1.getUserEmail(), user.getUserEmail());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void createUsersTest() {
        userStore.clear();
        Users users = new Users();
        List<UserRequest> userRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            userRequest.setPhoneNo("7337470452");
            userRequest.setUserType(User.UserType.GUEST);
            userRequest.setUserEmail("a@gmail.com");
            userRequests.add(userRequest);
        }
        users.setUsersList(userRequests);
        try {
            userService.createUsers(users, "actioncreator@gmail.com");
            Assert.assertEquals(userStore.getAllFromStore().size(), 10);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Test
    public void deleteUser() {
        userStore.clear();
        UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        userRequest.setPhoneNo("7337470452");
        userRequest.setUserType(User.UserType.GUEST);
        userRequest.setUserEmail("a@gmail.com");
        try {
            User user = userService.createUser(userRequest, "@emao;");
            userService.deleteUser(user.getId(), "");
            Assert.assertEquals(userStore.getAllFromStore().size(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserTest() {
        userStore.clear();
        UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        userRequest.setPhoneNo("7337470452");
        userRequest.setUserType(User.UserType.GUEST);
        userRequest.setUserEmail("a@gmail.com");
        try {
            User user = userService.createUser(userRequest, "@Email");
            User user1 = userService.getUser(user.getId());
            Assert.assertEquals(user.getId(), user1.getId());
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Test
    public void getAllUsersTest() {
        userStore.clear();
        UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
        userRequest.setPhoneNo("7337470452");
        userRequest.setUserType(User.UserType.GUEST);
        userRequest.setUserEmail("a@gmail.com");
        try {
            User user = userService.createUser(userRequest, "@Email");
            Assert.assertEquals(userService.getAllUsers().size(), 1);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
}