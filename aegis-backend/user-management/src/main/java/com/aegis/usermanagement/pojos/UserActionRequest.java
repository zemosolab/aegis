package com.aegis.usermanagement.pojos;

import com.aegis.usermanagement.model.UserAction;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class UserActionRequest {
    private UserAction.ActionType actionType;
    private String reason;
    private HashMap<String,String> details;
}
