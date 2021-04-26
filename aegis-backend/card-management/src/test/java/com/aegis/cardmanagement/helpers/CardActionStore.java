package com.aegis.cardmanagement.helpers;

import com.aegis.cardmanagement.modal.CardAction;
import lombok.Data;

import java.util.*;

@Data
public class CardActionStore {
    private Map<UUID, CardAction> cardActionStore=new HashMap<>();
    public CardAction addToStore(CardAction cardAction){

        UUID uuid=UUID.randomUUID();
        cardAction.setId(uuid);
        cardActionStore.put(uuid,cardAction);
        return cardActionStore.get(uuid);
    }

    public CardAction  getFromStore(UUID uuid){
        return cardActionStore.get(uuid);
    }

    public List<CardAction> getAllFromStore(){
        return new ArrayList<>(cardActionStore.values());
    }

    public void delete(UUID uuid){
        if(cardActionStore.containsKey(uuid)){
            cardActionStore.remove(uuid);
        }
    }

    public void clear(){
        cardActionStore=new HashMap<>();
    }

}
