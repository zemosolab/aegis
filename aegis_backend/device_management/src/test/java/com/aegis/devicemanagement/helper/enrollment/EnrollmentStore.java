package com.aegis.devicemanagement.helper.enrollment;

import com.aegis.devicemanagement.model.enrollment.Enrollment;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class EnrollmentStore {

    private Map<String, Enrollment> enrollmentStore = new HashMap<>();

    public Enrollment addToStore(Enrollment enrollment) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        enrollment.setId(id);
        enrollmentStore.put(id, enrollment);
        return enrollmentStore.get(id);
    }

    public Enrollment getFromStore(String id) {
        return enrollmentStore.get(id);
    }

    public List<Enrollment> getAllFromStore() {
        return new ArrayList<>(enrollmentStore.values());
    }

    public void delete(String id) {
        enrollmentStore.remove(id);
    }

    public List<Enrollment> findByDeviceId(String deviceId) {
        return getAllFromStore().stream().filter(enrollment -> enrollment.getDeviceId().equals(deviceId)).collect(Collectors.toList());
    }

    public void clear() {
        enrollmentStore = new HashMap<>();
    }

}
