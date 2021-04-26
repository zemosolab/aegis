package com.aegis.devicemanagement.controller.command;

import com.aegis.devicemanagement.exception.command.CommandNotSupported;
import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.model.command.Command;
import com.aegis.devicemanagement.service.command.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/devices")
public class CommandController {

    @Autowired
    CommandService commandService;

    @PostMapping("/{deviceId}/command")
    public ResponseEntity<Command> sendCommand(@PathVariable String deviceId, @RequestParam String commandType) {
        Command command;
        try {
            command = commandService.sendCommandToDevice(commandType, deviceId, "POST");
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (CommandNotSupported commandNotSupported) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Command Not Supported", commandNotSupported);
        }
        return new ResponseEntity<>(command, HttpStatus.CREATED);
    }

    @GetMapping("/{deviceId}/command")
    public ResponseEntity<Command> getCommandInfo(@PathVariable String deviceId, @RequestParam String commandType) {
        Command command;
        try {
            command = commandService.sendCommandToDevice(commandType, deviceId, "GET");
        } catch (DeviceNotFound deviceNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "device not found", deviceNotFound);
        } catch (CommandNotSupported commandNotSupported) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Command Not Supported", commandNotSupported);
        }
        return new ResponseEntity<>(command, HttpStatus.OK);
    }
}