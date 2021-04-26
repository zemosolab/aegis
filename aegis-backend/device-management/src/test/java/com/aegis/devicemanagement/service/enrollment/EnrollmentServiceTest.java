package com.aegis.devicemanagement.service.enrollment;

import com.aegis.devicemanagement.exception.device.DeviceNotFound;
import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.helper.device.DeviceStore;
import com.aegis.devicemanagement.helper.enrollment.EnrollmentActionStore;
import com.aegis.devicemanagement.helper.enrollment.EnrollmentStore;
import com.aegis.devicemanagement.model.device.Device;
import com.aegis.devicemanagement.model.enrollment.Enrollment;
import com.aegis.devicemanagement.model.enrollment.EnrollmentAction;
import com.aegis.devicemanagement.pojos.deviceconfig.DeviceBasicConfig;
import com.aegis.devicemanagement.pojos.enrollmentConfig.CardReadWrite;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollUser;
import com.aegis.devicemanagement.pojos.request.DeviceRequest;
import com.aegis.devicemanagement.pojos.request.EnrollmentRequest;
import com.aegis.devicemanagement.repository.device.DeviceRepository;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import com.aegis.devicemanagement.repository.enrollment.EnrollmentRepository;
import com.aegis.devicemanagement.repository.enrollmentaction.EnrollmentActionRepository;
import com.aegis.devicemanagement.service.device.DeviceActionService;
import com.aegis.devicemanagement.service.device.DeviceService;
import com.aegis.devicemanagement.utils.DeviceCommandManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class EnrollmentServiceTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    EnrollmentActionRepository enrollmentActionRepository;

    @Mock
    DeviceCommandManager deviceCommandManager;

    @Mock
    DeviceRepository deviceRepository;
    @Mock
    DeviceActionRepository deviceActionRepository;

    DeviceService deviceService;

    DeviceActionService deviceActionService;

    EnrollmentService enrollmentService;

    EnrollmentActionService enrollmentActionService;

    private DeviceStore deviceStore = new DeviceStore();
    private EnrollmentStore enrollmentStore = new EnrollmentStore();
    private EnrollmentActionStore enrollmentActionStore = new EnrollmentActionStore();

    @Before
    public void setUp() throws DeviceNotFound {
        Mockito.when(deviceRepository.save(any())).thenAnswer(r -> {
            Device device = r.getArgument(0);
            return deviceStore.addToStore(device);
        });
        Mockito.when(deviceRepository.findById(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return Optional.ofNullable(deviceStore.getFromStore(id));
        });
        Mockito.when(deviceRepository.findByIpAddress(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return deviceStore.findByIp(id);
        });

        Mockito.doAnswer(r -> {
            deviceStore.delete(r.getArgument(0));
            return null;
        }).when(deviceRepository).deleteById(any());
        Mockito.when(deviceCommandManager.httpRequestGenerator(any(), any(), any())).thenReturn(new HashMap<>());
        Mockito.when(enrollmentRepository.save(any())).thenAnswer(r -> {
            Enrollment enrollment = r.getArgument(0);
            return enrollmentStore.addToStore(enrollment);
        });
        Mockito.when(enrollmentRepository.findById(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return Optional.ofNullable(enrollmentStore.getFromStore(id));
        });
        Mockito.when(enrollmentRepository.findByDeviceId(any())).thenAnswer(r -> {
            String id = r.getArgument(0);
            return enrollmentStore.findByDeviceId(id);
        });
        Mockito.doAnswer(r -> {
            enrollmentStore.delete(r.getArgument(0));
            return null;
        }).when(enrollmentRepository).deleteById(any());
        Mockito.when(enrollmentActionRepository.save(any())).thenAnswer(r -> {
            EnrollmentAction enrollmentAction = r.getArgument(0);
            return enrollmentActionStore.addToStore(enrollmentAction);
        });
        enrollmentService = new EnrollmentService();
        enrollmentService.setEnrollmentRepository(enrollmentRepository);
        enrollmentActionService = new EnrollmentActionService();
        enrollmentActionService.setEnrollmentActionRepository(enrollmentActionRepository);
        enrollmentActionService.setEnrollmentService(enrollmentService);
        enrollmentService.setEnrollmentActionService(enrollmentActionService);

        deviceService = new DeviceService();
        deviceService.setDeviceRepository(deviceRepository);
        deviceActionService = new DeviceActionService();
        deviceActionService.setDeviceActionRepository(deviceActionRepository);
        deviceActionService.setDeviceService(deviceService);
        deviceService.setDeviceActionService(deviceActionService);
        deviceService.setDeviceCommandManager(deviceCommandManager);
        enrollmentService.setDeviceCommandManager(deviceCommandManager);
        enrollmentService.setDeviceService(deviceService);
    }

    @Test
    public void createEnrollment() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");
            EnrollmentRequest enrollmentRequest = (EnrollmentRequest) Helper.populate(new EnrollmentRequest(),
                    EnrollmentRequest.class);
            EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
            enrollmentRequest.setEnrollUser(enrollUser);
            enrollmentService.createEnrollment(enrollmentRequest, device.getId(), "");
            EnrollmentAction enrollmentAction = enrollmentActionStore.getAllFromStore().get(0);
            Assert.assertEquals(enrollmentAction.getActionType(), EnrollmentAction.ActionType.CREATE);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getAllEnrollmentsFromADevice() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");
            for (int i = 0; i < 5; i++) {
                EnrollmentRequest enrollmentRequest = (EnrollmentRequest) Helper.populate(new EnrollmentRequest(),
                        EnrollmentRequest.class);
                EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
                enrollmentRequest.setEnrollUser(enrollUser);
                enrollmentService.createEnrollment(enrollmentRequest, device.getId(), "");
            }
            List<Enrollment> enrollments = enrollmentService.getAllEnrollmentsFromADevice(device.getId());
            Assert.assertEquals(enrollments.size(), 5);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getEnrollment() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            EnrollmentRequest enrollmentRequest = (EnrollmentRequest) Helper.populate(new EnrollmentRequest(),
                    EnrollmentRequest.class);
            EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
            enrollmentRequest.setEnrollUser(enrollUser);
            Enrollment enrollment = enrollmentService.createEnrollment(enrollmentRequest, device.getId(), "");
            Enrollment enrollment1 = enrollmentService.getEnrollment(enrollment.getId(), device.getId());
            Assert.assertEquals(enrollment, enrollment1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void updateEnrollment() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            EnrollmentRequest enrollmentRequest = (EnrollmentRequest) Helper.populate(new EnrollmentRequest(),
                    EnrollmentRequest.class);
            EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
            enrollmentRequest.setEnrollUser(enrollUser);
            enrollmentService.createEnrollment(enrollmentRequest, device.getId(), "");
            Enrollment enrollment = enrollmentStore.getAllFromStore().get(0);
            CardReadWrite cardReadWrite = (CardReadWrite) Helper.populate(new CardReadWrite(), CardReadWrite.class);
            enrollmentRequest.setCardReadWrite(cardReadWrite);
            Enrollment enrollment2 = enrollmentService.updateEnrollment(enrollmentRequest, device.getId(),
                    enrollment.getId(), "");
            Assert.assertEquals(cardReadWrite.getName(), enrollment2.getCardReadWrite().getName());
            Assert.assertEquals(enrollmentActionStore.getEnrollmentActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void deleteEnrollment() {
        try {
            DeviceRequest deviceRequest = (DeviceRequest) Helper.populate(new DeviceRequest(), DeviceRequest.class);
            DeviceBasicConfig deviceBasicConfig = (DeviceBasicConfig) Helper.populate(new DeviceBasicConfig(),
                    DeviceBasicConfig.class);
            deviceRequest.setDeviceBasicConfig(deviceBasicConfig);
            Device device = deviceService.createDevice(deviceRequest, "");

            EnrollmentRequest enrollmentRequest = (EnrollmentRequest) Helper.populate(new EnrollmentRequest(),
                    EnrollmentRequest.class);
            EnrollUser enrollUser = (EnrollUser) Helper.populate(new EnrollUser(), EnrollUser.class);
            enrollmentRequest.setEnrollUser(enrollUser);
            enrollmentService.createEnrollment(enrollmentRequest, device.getId(), "");

            Enrollment enrollment = enrollmentStore.getAllFromStore().get(0);
            enrollmentService.deleteEnrollment(enrollment.getId(), device.getId(), "");
            Assert.assertEquals(enrollmentStore.getAllFromStore().size(), 0);
            Assert.assertEquals(enrollmentActionStore.getEnrollmentActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }
}