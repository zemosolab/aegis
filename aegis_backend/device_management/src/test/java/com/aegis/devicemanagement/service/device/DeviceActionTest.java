package com.aegis.devicemanagement.service.device;


import com.aegis.devicemanagement.helper.Helper;
import com.aegis.devicemanagement.helper.device.DeviceActionStore;
import com.aegis.devicemanagement.model.device.DeviceAction;
import com.aegis.devicemanagement.repository.deviceaction.DeviceActionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class DeviceActionTest {

    @Mock
    DeviceActionRepository deviceActionRepository;

    DeviceActionService deviceActionService= new DeviceActionService();

    DeviceActionStore deviceActionStore= new DeviceActionStore();

    @Before
    public void init(){
        Mockito.when(deviceActionRepository.save(any())).thenAnswer(r->{
            DeviceAction deviceAction=r.getArgument(0);
            return deviceActionStore.addToStore(deviceAction);
        });
        deviceActionService.setDeviceActionRepository(deviceActionRepository);
    }


    @Test
    public void createActionTest(){
        try{
            String deviceId=Helper.generateRandomString();
            deviceActionService.createAction(deviceId, DeviceAction.ActionType.CREATE,null,"");
            Assert.assertEquals(deviceActionStore.getAllFromStore().get(0).getDeviceId(),deviceId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
