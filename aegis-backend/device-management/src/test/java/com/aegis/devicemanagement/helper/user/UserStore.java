package com.aegis.devicemanagement.helper.user;

import com.aegis.devicemanagement.model.user.User;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserStore {
    private Map<String, User> userStore = new HashMap<>();

    public User addToStore(User user) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        user.setId(id);
        userStore.put(id, user);
        return userStore.get(id);
    }

    public User getFromStore(String id) {
        return userStore.get(id);
    }

    public List<User> getAllFromStore() {
        return new ArrayList<>(userStore.values());
    }

    public void delete(String id) {
        userStore.remove(id);
    }

    public List<User> findByDeviceId(String deviceId) {
        return getAllFromStore().stream().filter(user -> user.getDeviceId().equals(deviceId)).collect(Collectors.toList());
    }

    public void clear() {
        userStore = new HashMap<>();
    }
}
