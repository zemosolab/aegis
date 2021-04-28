package com.aegis.devicemanagement.utils;

import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.model.enrollment.Enrollment;
import com.aegis.devicemanagement.model.user.User;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.pojos.request.EnrollmentRequest;
import com.aegis.devicemanagement.pojos.request.UserRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Helper {

    public enum ActionType {
        DEVICESET, DEVICEGET, DEVICEGETDEFAULT, DEVICESETDEFAULT, DEVICEDELETE, SET, GET, SETDEFAULT, GETDEFAULT,
        ENROLL, READ, WRITE, CLEARALARM, ACKNOWLEDGEALARM, LOCKDOOR, UNLOCKDOOR, NORMALIZEDOOR, OPENDOOR, DENYUSER,
        DOORPANIC, DELETECREDENTIAL, GETCOUNT, GETUSERCOUNT, GETEVENTCOUNT, SYSTEMDEFAULT
    }

    public static final String deviceLink = "http://IP_ADDRESS:PORT_NO/device.cgi/";

    public static void copyDeviceDetails(Device device, DeviceRequest deviceRequest) {
        if (deviceRequest.getIpAddress() != null)
            device.setIpAddress(deviceRequest.getIpAddress());
        if (deviceRequest.getPortNo() != null)
            device.setPortNo(deviceRequest.getPortNo());
        if (deviceRequest.getAlarm() != null)
            device.setAlarm(deviceRequest.getAlarm());
        if (deviceRequest.getDateTime() != null)
            device.setDateTime(deviceRequest.getDateTime());
        if (deviceRequest.getReaderConfig() != null)
            device.setReaderConfig(deviceRequest.getReaderConfig());
        if (deviceRequest.getDeviceBasicConfig() != null)
            device.setDeviceBasicConfig(deviceRequest.getDeviceBasicConfig());
        if (deviceRequest.getDoorFeature() != null)
            device.setDoorFeature(deviceRequest.getDoorFeature());
        if (deviceRequest.getEnrollment() != null)
            device.setEnrollment(deviceRequest.getEnrollment());
        if (deviceRequest.getTcpEvent() != null)
            device.setTcpEvent(deviceRequest.getTcpEvent());
        if (deviceRequest.getAuthKey() != null)
            device.setAuthKey(deviceRequest.getAuthKey());
    }

    public static void copyUserDetails(User user, UserRequest userRequest, String deviceId) {
        user.setDeviceId(deviceId);
        if (userRequest.getName() != null)
            user.setName(userRequest.getName());
        if (userRequest.getCredential() != null)
            user.setCredential(userRequest.getCredential());
        if (userRequest.getDeviceUser() != null)
            user.setDeviceUser(userRequest.getDeviceUser());
        if (userRequest.getName() != null)
            user.setName(userRequest.getName());
    }

    @Bean
    @Qualifier("deviceActionMap")
    public static Map<String, HashMap<String, String>> deviceActionMap() {
        HashMap<String, String> cardReadWrite = new HashMap<>();
        HashMap<String, String> enrollUser = new HashMap<>();
        HashMap<String, String> checkEnrollment = new HashMap<>();
        HashMap<String, String> users = new HashMap<>();
        HashMap<String, String> credential = new HashMap<>();
        HashMap<String, String> deviceConfig = new HashMap<>();
        HashMap<String, String> command = new HashMap<>();
        return new HashMap<String, HashMap<String, String>>() {
            {
                cardReadWrite.put("DEVICESET", "WRITE");
                cardReadWrite.put("DEVICEGET", "READ");
                enrollUser.put("DEVICESET", "ENROLL");
                checkEnrollment.put("DEVICEGET", "GET");
                users.put("DEVICESET", "SET");
                users.put("DEVICEGET", "GET");
                users.put("DEVICEDELETE", "DELETE");
                credential.put("DEVICESET", "SET");
                credential.put("DEVICEGET", "GET");
                deviceConfig.put("DEVICESET", "SET");
                deviceConfig.put("DEVICEGET", "GET");
                deviceConfig.put("DEVICESETDEFAULT", "SETDEFAULT");
                deviceConfig.put("DEVICEGETDEFAULT", "GETDEFAULT");
                command.put("CLEARALARM", "CLEARALARM");
                command.put("ACKNOWLEDGEALARM", "ACKNOWLEDGEALARM");
                command.put("LOCKDOOR", "LOCKDOOR");
                command.put("UNLOCKDOOR", "UNLOCKDOOR");
                command.put("NORMALIZEDOOR", "NORMALIZEDOOR");
                command.put("SYSTEMDEFAULT", "SYSTEMDEFAULT");
                command.put("OPENDOOR", "OPENDOOR");
                command.put("DENYUSER", "DENYUSER");
                command.put("DOORPANIC", "DOORPANIC");
                command.put("DELETECREDENTIAL", "DELETECREDENTIAL");
                command.put("GETCOUNT", "GETCOUNT");
                command.put("GETUSERCOUNT", "GETUSERCOUNT");
                command.put("GETEVENTCOUNT", "GETEVENTCOUNT");
                put("card-read-write", cardReadWrite);
                put("enrolluser", enrollUser);
                put("enrollspcard", enrollUser);
                put("check-enrollment", checkEnrollment);
                put("users", users);
                put("credential", credential);
                put("deviceBasicConfig", deviceConfig);
                put("alarm", deviceConfig);
                put("dateTime", deviceConfig);
                put("doorFeature", deviceConfig);
                put("readerConfig", deviceConfig);
                put("enrollment", deviceConfig);
                put("tcpEvent", deviceConfig);
                put("command", command);
            }
        };
    }

    public static String convertCamelCase(String camelCase) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c))
                stringBuilder.append("-" + Character.toLowerCase(c));
            else
                stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static String generateDeviceLink(String ipAddress, String portNo) {
        String link = deviceLink.replace("IP_ADDRESS", ipAddress);
        link = link.replace("PORT_NO", portNo);
        return link;
    }

    public static void copyEnrollmentDetails(Enrollment enrollment, EnrollmentRequest enrollmentRequest,
                                             String deviceId) {
        enrollment.setDeviceId(deviceId);
        if (enrollmentRequest.getCardReadWrite() != null)
            enrollment.setCardReadWrite(enrollmentRequest.getCardReadWrite());
        if (enrollmentRequest.getEnrollSpCard() != null)
            enrollment.setEnrollSpCard(enrollmentRequest.getEnrollSpCard());
        if (enrollmentRequest.getEnrollUser() != null)
            enrollment.setEnrollUser(enrollmentRequest.getEnrollUser());
    }

    @Bean
    public HashMap<String, String> classToDeviceMap() {
        return new HashMap<String, String>() {
            {
                put("deviceBasicConfig", "device-basic-config");
                put("alarm", "alarm");
                put("dateTime", "date-time");
                put("doorFeature", "door-feature");
                put("readerConfig", "reader-config");
                put("enrollment", "enrollment");
                put("tcpEvent", "tcp-events");
                put("deviceUser", "users");
                put("credential", "credential");
                put("cardReadWrite", "card-read-write");
                put("checkEnrollment", "check-enrollment");
                put("enrollSpCard", "enrollspcard");
                put("enrollUser", "enrolluser");
            }
        };
    }

    public HashMap<Integer, String> apiErrorCodes() {
        return new HashMap<Integer, String>() {
            {
                put(0, "Successful");
                put(1, "Failed - Invalid Login Credentials");
                put(2, "Date and time – manual set failed");
                put(3, "Invalid Date/Time ");
                put(4, "Maximum users are already configured.");
                put(5, "Image – size is too big ");
                put(6, "Image – format not supported");
                put(7, "Card 1 and card 2 are identical");
                put(8, "Card ID exists");
                put(9, "Finger print template/Palm template already exists");
                put(10, "No Record Found");
                put(11, "Template size/format mismatch");
                put(12, "FP Memory full");
                put(13, "User id not found");
                put(14, "Credential limit reached");
                put(15, "Reader mismatch/Reader not configured");
                put(16, "Device Busy");
                put(17, "Internal process error ");
                put(18, "PIN already exists");
                put(19, "Biometric credential not found");
                put(20, "Memory Card Not Found");
                put(21, "Reference User ID exists");
                put(22, "Wrong Selection");
                put(23, "Palm template mode mismatch");
                put(24, "Feature not enabled in the configuration");
                put(25, "Message already exists for same user for same date");
                put(26, "Invalid smart card format/Parameters not applicable as per card type defined.");
                put(27, " Time Out");
                put(28, "Read/Write failed");
                put(29, "Wrong Card Type");
                put(30, "key mismatch");
                put(31, " invalid card");
                put(32, "Scan failed");
                put(33, "Invalid value");
                put(34, "Credential does not match");
            }
        };
    }
}
