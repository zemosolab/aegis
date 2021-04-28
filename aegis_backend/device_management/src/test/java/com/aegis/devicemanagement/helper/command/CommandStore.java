package com.aegis.devicemanagement.helper.command;

import com.aegis.devicemanagement.model.command.Command;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CommandStore {

    private Map<String, Command> commandStore = new HashMap<>();

    public Command addToStore(Command command) {
        String referenceString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder string = new StringBuilder();
        Random rnd = new Random();
        while (string.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * referenceString.length());
            string.append(referenceString.charAt(index));
        }
        String id = string.toString();
        command.setId(id);
        commandStore.put(id, command);
        return commandStore.get(id);
    }

    public Command getFromStore(String id) {
        return commandStore.get(id);
    }

    public List<Command> getAllFromStore() {
        return new ArrayList<>(commandStore.values());
    }

    public void delete(String id) {
        commandStore.remove(id);
    }

    public List<Command> findByDeviceId(String deviceId) {
        return getAllFromStore().stream().filter(command -> command.getDeviceId().equals(deviceId)).collect(Collectors.toList());
    }

    public void clear() {
        commandStore = new HashMap<>();
    }
}
