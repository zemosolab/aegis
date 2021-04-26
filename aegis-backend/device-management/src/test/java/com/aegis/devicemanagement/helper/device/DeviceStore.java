package com.aegis.devicemanagement.helper.device;

import com.aegis.devicemanagement.model.device.Device;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
public class DeviceStore {

    private Map<String, Device> deviceStore = new HashMap<>();

    public Device addToStore(Device device) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        device.setId(id);
        deviceStore.put(id, device);
        return deviceStore.get(id);
    }

    public Device getFromStore(String id) {
        return deviceStore.get(id);
    }

    public List<Device> getAllFromStore() {
        return new ArrayList<>(deviceStore.values());
    }

    public void delete(String id) {
        deviceStore.remove(id);
    }

    public List<Device> findByDeviceId(String deviceId) {
        return getAllFromStore().stream()
                .filter(device -> device.getId().equals(deviceId))
                .collect(Collectors.toList());
    }

    public List<Device> findByIp(String ipAddress) {
        return getAllFromStore().stream()
                .filter(device -> device.getIpAddress().equals(ipAddress))
                .collect(Collectors.toList());

    }

    public void clear() {
        deviceStore = new HashMap<>();
    }
}
