package com.aegis.usermanagement.helper;

import com.aegis.usermanagement.model.User;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserStore {

    private Map<UUID, User> userStore=new HashMap<>();
    public User addToStore(User user){
        UUID uuid=UUID.randomUUID();
        user.setId(uuid);
        userStore.put(uuid,user);
        return userStore.get(uuid);
    }
    public User  getFromStore(UUID uuid){
        return userStore.get(uuid);
    }

    public List<User> findByPhoneNo(String phoneNo){
        List<User> users= getAllFromStore();
        return users.stream().filter(user -> user.getPhoneNo().equals(phoneNo)).collect(Collectors.toList());
    }

    public List<User> findByUserEmail(String userEmail){
        List<User> users= getAllFromStore();
        return users.stream().filter(user -> user.getUserEmail().equals(userEmail)).collect(Collectors.toList());
    }

    public List<User> getAllFromStore(){
        return new ArrayList<>(userStore.values());
    }

    public void delete(UUID uuid){
        if(userStore.containsKey(uuid)){
            userStore.remove(uuid);
        }
    }

    public void clear(){
        userStore=new HashMap<>();
    }



}