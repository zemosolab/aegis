package com.aegis.usermanagement.service;


import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.exception.InvalidUser;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.pojos.UserActionRequest;
import com.aegis.usermanagement.pojos.UserRequest;
import com.aegis.usermanagement.pojos.Users;
import com.aegis.usermanagement.repository.UserRepository;
import com.aegis.usermanagement.utils.DeviceManager;
import com.aegis.usermanagement.utils.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Setter
@Getter
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;


    @Autowired
    UserActionService userActionService;



    public User createUser(UserRequest userRequest, String userEmail) throws UserNotFound, InvalidUser, DataIntegrityViolationException, JsonProcessingException {
        if(!Helper.validateUserRequest(userRequest)){
            throw new InvalidUser("invalid user request");
        }
        User user= Helper.copyDetailsToUser(userRequest,new User());
        user= userRepository.save(user);

        UserActionRequest userActionRequest=Helper.copyActionDetails(user, UserAction.ActionType.CREATE);
        userActionService.createUserAction(userActionRequest,userEmail,user.getId());
        return user;
    }



    public List<User> createUsers(Users users,String userEmail) throws InvalidUser, UserNotFound, JsonProcessingException {
        List<UserRequest> userRequestList=users.getUsersList();
        List<User> userList=new ArrayList<>();
        for(UserRequest userRequest:userRequestList){
          User user=createUser(userRequest,userEmail);
          userList.add(user);
        }
        return userList;
    }

    public User getUser(UUID userId) throws UserNotFound {
        Optional<User> user=userRepository.findById(userId);
        if(!user.isPresent()){
            throw new UserNotFound("user not found");
        }
        return user.get();
    }

    public void deleteUser(UUID userId,String userEmail) throws UserNotFound, JsonProcessingException {
        Optional<User> optionalUser=userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new UserNotFound("user not found");
        }
        UserActionRequest userActionRequest=Helper.copyActionDetails(optionalUser.get(), UserAction.ActionType.REMOVE);
        userActionService.createUserAction(userActionRequest,userEmail,userId);
        userRepository.deleteById(userId);
    }


    public User updateUser(UserRequest userRequest,UUID userId,String userEmail) throws UserNotFound, JsonProcessingException {
        Optional<User> optionalUser=userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new UserNotFound("user not found");
        }
        User user=Helper.copyDetailsToUser(userRequest,optionalUser.get());
        user=userRepository.save(user);
        UserActionRequest userActionRequest=Helper.copyActionDetails(user, UserAction.ActionType.UPDATE);
        userActionService.createUserAction(userActionRequest,userEmail,userId);
        return user;
    }


    public User updateDeviceUserId(User user,String deviceUserId){
        user.setDeviceUserId(deviceUserId);
        return userRepository.save(user);
    }

    public User updateUserStatus(UUID userId, User.Status status) throws UserNotFound{
        Optional<User> optionalUser=userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new UserNotFound("user not found");
        }
        User user=optionalUser.get();
        user.setStatus(status);
        return userRepository.save(user);
    }




    public List<User> getAllUsers() throws Exception{
        List<User> users=null;
        try {
            users = userRepository.findAll();
        }
        catch (Exception systemException){
            throw new Exception();
        }
        return users;
    }


    public void checkIfUserExists(UUID userId) throws UserNotFound{
        Optional<User> optionalUser=userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new UserNotFound("user not found");
        }
    }
}
