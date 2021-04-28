package com.aegis.usermanagement.helper;

import com.aegis.usermanagement.model.UserAction;

import java.util.*;

public class UserActionStore {
    private Map<UUID, UserAction> userActionStore=new HashMap<>();


    public UserAction addToStore(UserAction userAction){
        UUID uuid=UUID.randomUUID();
        userAction.setId(uuid);
        userActionStore.put(uuid,userAction);
        return userActionStore.get(uuid);
    }
    public UserAction  getFromStore(UUID uuid){
        return userActionStore.get(uuid);
    }


    public List<UserAction> getAllFromStore(){
        return new ArrayList<>(userActionStore.values());
    }

    public void delete(UUID uuid){
        if(userActionStore.containsKey(uuid)){
            userActionStore.remove(uuid);
        }
    }

    public void clear(){
        userActionStore=new HashMap<>();
    }
}
