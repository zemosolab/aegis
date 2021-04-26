package com.aegis.cardmanagement.utils;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.pojos.CardRequest;

import java.util.HashMap;
import java.util.Map;


public class Helper {

    public static Card copyCardDetails(CardRequest cardRequest, Card card){
        if(cardRequest.getHardwareId()!=null)
        card.setHardwareId(cardRequest.getHardwareId());
        if(cardRequest.getCardName()!=null)
        card.setCardName(cardRequest.getCardName());
        return card;
    }

    public static CardActionRequest getActionRequestDetails(Card card, CardAction.ActionType actionType){
        CardActionRequest cardActionRequest= new CardActionRequest();
        cardActionRequest.setActionType(actionType);
        Map<String,String> details= new HashMap<>();
        details.put("hardwareId",card.getHardwareId());
        if(actionType.equals(CardAction.ActionType.UPDATE)) {
            details.put("cardName", card.getCardName());
        }
        cardActionRequest.setDetails((HashMap<String, String>) details);
        return cardActionRequest;
    }
}
