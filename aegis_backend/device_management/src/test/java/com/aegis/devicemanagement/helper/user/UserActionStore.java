package com.aegis.devicemanagement.helper.user;

import com.aegis.devicemanagement.model.user.UserAction;
import lombok.Data;

import java.util.*;

@Data
public class UserActionStore {
    private Map<String, UserAction> userActionStore = new HashMap<>();

    public UserAction addToStore(UserAction deviceAction) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        deviceAction.setId(id);
        userActionStore.put(id, deviceAction);
        return userActionStore.get(id);
    }

    public UserAction getFromStore(String id) {
        return userActionStore.get(id);
    }

    public List<UserAction> getAllFromStore() {
        return new ArrayList<>(userActionStore.values());
    }

    public void delete(String id) {
        userActionStore.remove(id);
    }

    public void clear() {
        userActionStore = new HashMap<>();
    }
}
