package com.aegis.usermanagement.controller;
import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.exception.InvalidUser;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.pojos.UserRequest;
import com.aegis.usermanagement.pojos.Users;
import com.aegis.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;


    @CrossOrigin
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId){
        User user;
        try{
            user=userService.getUser(userId);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users;
        try{
            users=userService.getAllUsers();
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "system exception",systemException);
        }
        return ResponseEntity.ok(users);
    }


    @CrossOrigin
    @PostMapping("/users")
    public ResponseEntity<List<User>> createUsers(@RequestBody Users users,HttpServletRequest httpServletRequest){
        List<User> users1=null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try{
            users1=userService.createUsers(users,userEmail);
        }
        catch (InvalidUser invalidUser){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "one or more invalid users", invalidUser);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "system exception",systemException);
        }
        return new ResponseEntity<>(users1,HttpStatus.CREATED);
    }


    @CrossOrigin
    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId,@RequestBody UserRequest userRequest,HttpServletRequest httpServletRequest){
        User user1=null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        String authHeader = httpServletRequest.getHeader("Authorization");
        try{
            user1=userService.updateUser(userRequest,userId,userEmail);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "system exception",systemException);
        }
        return new ResponseEntity<User>(user1,HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId,HttpServletRequest httpServletRequest){
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        String authHeader = httpServletRequest.getHeader("Authorization");
        try{
            userService.deleteUser(userId,userEmail);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "system exception",systemException);
        }
         return new ResponseEntity(HttpStatus.OK);
    }

}
