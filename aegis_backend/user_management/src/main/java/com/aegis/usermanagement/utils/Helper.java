package com.aegis.usermanagement.utils;

import com.aegis.usermanagement.model.User;
import com.aegis.usermanagement.model.UserAction;
import com.aegis.usermanagement.pojos.UserActionRequest;
import com.aegis.usermanagement.pojos.UserRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static final String EMAIL_REGEX="^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String PHONE_REGEX="^\\+?[0-9. ()-]{10,25}$";


    public static User copyDetailsToUser(UserRequest userRequest, User user){
        if(userRequest.getPhoneNo()!=null)
        user.setPhoneNo(userRequest.getPhoneNo());
        if(userRequest.getUserEmail()!=null)
        user.setUserEmail(userRequest.getUserEmail());
        if(userRequest.getUserType()!=null)
        user.setUserType(userRequest.getUserType());
        if(userRequest.getDateOfJoining()!=null)
        user.setDateOfJoining(userRequest.getDateOfJoining());
        if(userRequest.getUsername()!=null)
        user.setUsername(userRequest.getUsername());
        if(userRequest.getEmployeeId()!=null)
            user.setEmployeeId(userRequest.getEmployeeId());
        return user;
    }


    public static boolean emailValidator(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean phoneNoValidator(String phoneNo) {
        if(phoneNo==null) return false;
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNo);
       return  matcher.matches();
    }


    public static boolean validateUserRequest(UserRequest userRequest){
        boolean isValid=false;
        if(userRequest.getUserType().equals(User.UserType.GUEST)){
         isValid=  phoneNoValidator(userRequest.getPhoneNo());
        }
        else{
         isValid=   emailValidator(userRequest.getUserEmail());
        }
        return isValid;

    }

    public static UserActionRequest copyActionDetails( User user, UserAction.ActionType actionType){
        UserActionRequest userActionRequest= new UserActionRequest();
        userActionRequest.setActionType(actionType);
        Map<String,String> details= new HashMap<>();
        userActionRequest.setDetails((HashMap<String, String>) details);
        return userActionRequest;
    }




}
