package com.aegis.cardmanagement.pojos;

import com.aegis.cardmanagement.modal.CardAction;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class CardActionRequest {
    private CardAction.ActionType actionType;
    private String reason;
    private HashMap<String,String> details;
}
