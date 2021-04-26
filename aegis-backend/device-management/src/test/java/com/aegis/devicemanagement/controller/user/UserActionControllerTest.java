package com.aegis.devicemanagement.controller.user;

import com.aegis.devicemanagement.model.user.UserAction;
import com.aegis.devicemanagement.service.user.UserActionService;

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
@WebMvcTest(UserActionController.class)
public class UserActionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserActionService userActionService;

    @Test
    public void getUserActions() throws Exception {
        List<UserAction> userActions = new ArrayList<>();
        UserAction userAction = new UserAction();
        userAction.setUserId("123");
        userActions.add(userAction);
        when(userActionService.getActions(any(), any())).thenReturn(userActions);
        mockMvc.perform(get("/api/devices/Nikhil/users/123/actions")).andExpect(status().isOk());
    }

    @Test
    public void getUserActionByActionId() throws Exception {
        UserAction userAction = new UserAction();
        userAction.setUserId("Nikhil");
        when(userActionService.getAction(Mockito.anyString(), Mockito.any(), any())).thenReturn(userAction);
        mockMvc.perform(get("/api/devices/Nikhil/users/123/actions/456")).andExpect(status().isOk());
    }
}