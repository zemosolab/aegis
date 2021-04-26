package com.aegis.cardmanagement.helpers;

import com.aegis.cardmanagement.modal.Card;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CardStore {

    private Map<UUID, Card> cardStore=new HashMap<>();
    public Card addToStore(Card card){

        UUID uuid=UUID.randomUUID();
        card.setId(uuid);
        cardStore.put(uuid,card);
        return cardStore.get(uuid);
    }

    public Card  getFromStore(UUID uuid){
        return cardStore.get(uuid);
    }

    public List<Card> getAllFromStore(){
        return new ArrayList<>(cardStore.values());
    }

    public void delete(UUID uuid){
        if(cardStore.containsKey(uuid)){
            cardStore.remove(uuid);
        }
    }

    public List<Card> findByHardwareId(String hardwareId){
        return  getAllFromStore().stream()
                .filter(card -> card.getHardwareId().equals(hardwareId))
                .collect(Collectors.toList());
    }

    public void clear(){
        cardStore=new HashMap<>();
    }
}
