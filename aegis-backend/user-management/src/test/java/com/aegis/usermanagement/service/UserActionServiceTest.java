package com.aegis.usermanagement.service;

import com.aegis.usermanagement.helper.Helper;
import com.aegis.usermanagement.helper.UserActionStore;
import com.aegis.usermanagement.helper.UserStore;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.pojos.UserActionRequest;
import com.aegis.usermanagement.pojos.UserRequest;
import com.aegis.usermanagement.repository.UserActionRepository;
import com.aegis.usermanagement.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserActionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActionRepository userActionRepository;

    public UserStore userStore = new UserStore();
    private UserActionStore userActionStore = new UserActionStore();
    private UserService userService;
    private UserActionService userActionService;

    @Before
    public void init() {
        Mockito.when(userRepository.save(any())).thenAnswer(r -> {
            User user = r.getArgument(0);
            user.setId(UUID.randomUUID());
            return userStore.addToStore(user);
        });
        Mockito.when(userRepository.findById(any())).thenAnswer(r -> {
            UUID uuid = r.getArgument(0);
            return Optional.ofNullable(userStore.getFromStore(uuid));
        });
        Mockito.when(userActionRepository.save(any())).thenAnswer(r -> {
            UserAction userAction = r.getArgument(0);
            return userActionStore.addToStore(userAction);

        });
        Mockito.when(userActionRepository.findById(any())).thenAnswer(r -> {
            UUID uuid = r.getArgument(0);
            return Optional.ofNullable(userActionStore.getFromStore(uuid));
        });
        userService = new UserService();
        userService.setUserRepository(userRepository);
        userActionService = new UserActionService();
        userActionService.setUserActionRepository(userActionRepository);
        userService.setUserActionService(userActionService);
        userActionService.setUserService(userService);
    }

    @Test
    public void createUserAction() {
        try {
            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            userRequest.setPhoneNo("7337470452");
            userRequest.setUserType(User.UserType.GUEST);
            User user = null;
            user = userService.createUser(userRequest, "@GMAIL");
            UserActionRequest userActionRequest = (UserActionRequest) Helper.populate(new UserActionRequest(), UserActionRequest.class);
            userActionRequest.setActionType(UserAction.ActionType.BLOCK);
            userActionService.createUserAction(userActionRequest, "", user.getId());
            Assert.assertEquals(userActionStore.getAllFromStore().size(), 2);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getAllActions() {
        try {
            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            userRequest.setPhoneNo("7337470452");
            userRequest.setUserType(User.UserType.GUEST);
            User user = null;
            user = userService.createUser(userRequest, "@GMAIL");
            userActionService.getAllActions(user.getId());
            Assert.assertEquals(userActionStore.getAllFromStore().get(0).getUser().getPhoneNo(), user.getPhoneNo());
            Assert.assertEquals(userActionStore.getAllFromStore().size(), 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getActionByIdTest() {
        try {
            UserRequest userRequest = (UserRequest) Helper.populate(new UserRequest(), UserRequest.class);
            userRequest.setPhoneNo("7337470452");
            userRequest.setUserType(User.UserType.GUEST);
            User user = null;
            user = userService.createUser(userRequest, "@GMAIL");
            UserAction userAction = userActionService.getAction(user.getId(), userActionStore.getAllFromStore().get(0).getId());
            Assert.assertEquals(userAction.getActionType(), UserAction.ActionType.CREATE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}