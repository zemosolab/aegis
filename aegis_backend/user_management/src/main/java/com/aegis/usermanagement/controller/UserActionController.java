package com.aegis.usermanagement.controller;

import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.exception.ActionNotFound;
import com.aegis.usermanagement.exception.UserActionMismatch;
import com.aegis.usermanagement.exception.UserNotFound;
import com.aegis.usermanagement.pojos.UserActionRequest;
import com.aegis.usermanagement.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserActionController {

    @Autowired
    UserActionService userActionService;


    @CrossOrigin
    @PostMapping("/users/{userId}/actions")
    public ResponseEntity<UserAction> createAction(
            @RequestBody UserActionRequest userActionRequest,
            @PathVariable UUID userId,
            HttpServletRequest httpServletRequest){
        UserAction userAction=null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try{
            userAction=userActionService.createUserAction(userActionRequest,userEmail,userId);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception",systemException);
        }
        return new ResponseEntity<UserAction>(userAction, HttpStatus.CREATED);

    }


    @CrossOrigin
    @GetMapping("/users/{userId}/actions")
    public ResponseEntity<List<UserAction>> getAllActions(@PathVariable UUID userId){
        List<UserAction> userActions=null;
        try{
            userActions=userActionService.getAllActions(userId);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception",systemException);
        }
        return new ResponseEntity<List<UserAction>>(userActions,HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/users/{userId}/actions/{actionId}")
    public ResponseEntity<UserAction> getAction(@PathVariable UUID userId,@PathVariable UUID actionId){

        UserAction userAction=null;
        try{
            userAction=userActionService.getAction(userId,actionId);
        }
        catch (UserNotFound userNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user does not exist", userNotFound);
        }
        catch (UserActionMismatch userActionMismatch){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action does not match user", userActionMismatch);
        }
        catch (ActionNotFound actionNotFound){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "action does not exist", actionNotFound);
        }
        return new ResponseEntity<UserAction>(userAction,HttpStatus.OK);
    }
}



