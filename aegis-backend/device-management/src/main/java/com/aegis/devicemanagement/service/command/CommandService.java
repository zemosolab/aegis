package com.aegis.devicemanagement.service.command;

import com.aegis.devicemanagement.exception.command.CommandNotSupported;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.model.command.Command;
import com.aegis.devicemanagement.repository.command.CommandRepository;
import com.aegis.devicemanagement.service.device.DeviceService;
import com.aegis.devicemanagement.utils.DeviceCommandManager;
import com.aegis.devicemanagement.utils.Helper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Setter
@Getter
@Service
@Transactional
public class CommandService {

    @Autowired
    CommandRepository commandRepository;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceCommandManager deviceCommandManager;

    public Command sendCommandToDevice(String type, String deviceId, String requestType) throws DeviceNotFound, CommandNotSupported {
        deviceService.checkIfDeviceExists(deviceId);
        Command command = new Command();
        command.setType(type);
        command.setDeviceId(deviceId);
        HashMap<String, String> params = new HashMap<>();
        HashMap<String, String> details = new HashMap<>();
        command.setDetails(details);
        if (requestType.equals("POST")) {
            switch (Helper.ActionType.valueOf(type.toUpperCase())) {
                case CLEARALARM:
                case ACKNOWLEDGEALARM:
                case LOCKDOOR:
                case UNLOCKDOOR:
                case NORMALIZEDOOR:
                case SYSTEMDEFAULT:
                    break;
                case OPENDOOR:
                case DENYUSER:
                case DOORPANIC:
                    params.put("extra-info1", "ABCD");
                case DELETECREDENTIAL:
                    params.put("type", "0");
                case GETCOUNT:
                case GETUSERCOUNT:
                case GETEVENTCOUNT:
                    throw new CommandNotSupported("Command Not Supported");
            }
        } else if (requestType.equals("GET")) {
            switch (Helper.ActionType.valueOf(type.toUpperCase())) {
                case CLEARALARM:
                case ACKNOWLEDGEALARM:
                case LOCKDOOR:
                case UNLOCKDOOR:
                case NORMALIZEDOOR:
                case SYSTEMDEFAULT:
                case OPENDOOR:
                case DENYUSER:
                case DOORPANIC:
                case DELETECREDENTIAL:
                    throw new CommandNotSupported("Command Not Supported");
                case GETCOUNT:
                case GETUSERCOUNT:
                case GETEVENTCOUNT:
                    break;
            }
        }
        deviceCommandManager.sendRequest("command", params, command.getDetails(), Helper.ActionType.valueOf(type.toUpperCase()), deviceId);
        return commandRepository.save(command);
    }
}