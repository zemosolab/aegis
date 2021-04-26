package com.aegis.usermanagement.service;

import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.exception.ActionNotFound;
import com.aegis.usermanagement.exception.UserActionMismatch;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.pojos.UserActionRequest;
import com.aegis.usermanagement.repository.UserActionRepository;
import com.aegis.usermanagement.utils.DeviceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Getter
@Setter
public class UserActionService {
    @Autowired
    UserService userService;

    @Autowired
    UserActionRepository userActionRepository;

    @Autowired
    DeviceManager deviceManager;

    public UserAction createUserAction(UserActionRequest userActionRequest, String userEmail, UUID userId) throws UserNotFound, JsonProcessingException {
        User.Status userStatus=null;
        switch (userActionRequest.getActionType()){
            case CREATE:
            case REMOVE:
            case UPDATE:break;
            case UNBLOCK:userStatus= User.Status.ACTIVE;break;
            case BLOCK:userStatus= User.Status.BLOCKED;break;
        }

        User user=userService.getUser(userId);
        UserAction userAction= new UserAction();
        copyActionDetails(userActionRequest,userAction,userId);
        generateSystemValues(userAction,userEmail);
        if(userStatus!=null) {
            userService.updateUserStatus(userId, userStatus);
        }
        userActionRepository.save(userAction);
       // if(userActionRequest.getActionType().equals(UserAction.ActionType.CREATE))
       //     deviceManager.registerUserWithDevice(user,userAction);
       // if(userActionRequest.getActionType().equals(UserAction.ActionType.REMOVE))
       //     deviceManager.deleteUserFromDevice(user,userAction);
        return userAction;
    }

    public void copyActionDetails(UserActionRequest userActionRequest,UserAction userAction,UUID userId) throws UserNotFound{
        userAction.setActionType(userActionRequest.getActionType());
        userAction.setDetails(userActionRequest.getDetails());
        userAction.setReason(userActionRequest.getReason());
        userAction.setUser(userService.getUser(userId));
    }

    public void generateSystemValues(UserAction userAction,String userEmail){
        userAction.setCreatedAt(new Date());
        userAction.setCreatedBy(userEmail);
    }

    public List<UserAction> getAllActions(UUID userId) throws UserNotFound,Exception{
        List<UserAction> userActions=null;
        userService.checkIfUserExists(userId);
        try{
            userActions= userActionRepository.findByUserId(userId);
        }
        catch (Exception systemException){
            throw  new Exception();
        }
       return userActions;
    }

    public UserAction getAction(UUID userId,UUID actionId) throws UserNotFound,ActionNotFound,UserActionMismatch{
        userService.checkIfUserExists(userId);
        Optional<UserAction> userActionOptional=userActionRepository.findById(actionId);
        if(!userActionOptional.isPresent()) {
            throw new ActionNotFound("action not found");
        }
        UserAction userAction=userActionOptional.get();
        if(!userAction.getUser().getId().equals(userId)){
            throw new UserActionMismatch("action does not belong to this user");
        }
        return userAction;
    }



}
