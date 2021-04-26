package com.aegis.devicemanagement.helper.device;

import com.aegis.devicemanagement.model.device.DeviceAction;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class DeviceActionStore {
    private Map<String, DeviceAction> deviceActionStore = new HashMap<>();

    public DeviceAction addToStore(DeviceAction deviceAction) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        deviceAction.setId(id);
        deviceActionStore.put(id, deviceAction);
        return deviceActionStore.get(id);
    }

    public DeviceAction getFromStore(String id) {
        return deviceActionStore.get(id);
    }

    public List<DeviceAction> getAllFromStore() {
        return new ArrayList<>(deviceActionStore.values());
    }

    public void delete(String id) {
        deviceActionStore.remove(id);
    }

    public void clear() {
        deviceActionStore = new HashMap<>();
    }
}
