package com.aegis.devicemanagement.helper.enrollment;

import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import lombok.Data;

import java.util.*;

@Data
public class EnrollmentActionStore {
    private Map<String, EnrollmentAction> enrollmentActionStore = new HashMap<>();

    public EnrollmentAction addToStore(EnrollmentAction deviceAction) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        deviceAction.setId(id);
        enrollmentActionStore.put(id, deviceAction);
        return enrollmentActionStore.get(id);
    }

    public EnrollmentAction getFromStore(String id) {
        return enrollmentActionStore.get(id);
    }

    public List<EnrollmentAction> getAllFromStore() {
        return new ArrayList<>(enrollmentActionStore.values());
    }

    public void delete(String id) {
        enrollmentActionStore.remove(id);
    }

    public void clear() {
        enrollmentActionStore = new HashMap<>();
    }

}
