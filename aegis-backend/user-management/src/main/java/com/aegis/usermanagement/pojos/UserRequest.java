package com.aegis.usermanagement.pojos;

import com.aegis.usermanagement.model.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
    private String username;
    private Date dateOfJoining;
    private String userEmail;
    private String phoneNo;
    private User.UserType userType;
    private String employeeId;
    private String reason;


}
