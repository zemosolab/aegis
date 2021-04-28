package com.aegis.cardmanagement.service;

import com.aegis.cardmanagement.helpers.CardActionStore;
import com.aegis.cardmanagement.helpers.CardStore;
import com.aegis.cardmanagement.helpers.Helper;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.pojos.CardRequest;
import com.aegis.cardmanagement.repository.CardActionRepository;
import com.aegis.cardmanagement.repository.CardRepository;
import com.aegis.cardmanagement.utils.DeviceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class CardActionServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CardActionRepository cardActionRepository;

    private  CardService cardService;

    private  CardActionService cardActionService;

    @Mock
    private DeviceManager deviceManager;


    private CardStore cardStore= new CardStore();
    private CardActionStore cardActionStore= new CardActionStore();



    @Before
    public void init() throws JsonProcessingException {

        Mockito.when(deviceManager.retrieveUserInfo(any())).thenReturn(null);
        Mockito.doAnswer(r->{
            return null;
        }).when(deviceManager).cardActionOnDevice(any(),any(),any());

        Mockito.when(cardRepository.save( any())).thenAnswer(r->{
            Card card=r.getArgument(0);
            return cardStore.addToStore(card);
        });

        Mockito.when(cardRepository.findByHardwareId(any())).thenAnswer(r->{
            String hardwareId=r.getArgument(0);
            return cardStore.findByHardwareId(hardwareId);
        });



        Mockito.when(cardRepository.findById(any())).thenAnswer(r->{
            UUID uuid=r.getArgument(0);
            return Optional.ofNullable(cardStore.getFromStore(uuid));
        });




        Mockito.when(cardActionRepository.save(any())).thenAnswer(r->{
            CardAction cardAction=r.getArgument(0);
            return cardActionStore.addToStore(cardAction);

        });
        cardService= new CardService();
        cardService.setCardRepository(cardRepository);
        cardActionService= new CardActionService();
        cardActionService.setCardActionRepository(cardActionRepository);
        cardActionService.setCardService(cardService);
        cardActionService.setDeviceManager(deviceManager);
        cardService.setCardActionService(cardActionService);
    }


    @Test
    public void completeActionTest(){
        try{
            CardRequest cardRequest =(CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user=Helper.generateRandomString();

            Card card=cardService.createCard(cardRequest,user);
            CardActionRequest cardActionRequest=(CardActionRequest) Helper.populate(new CardActionRequest(),CardActionRequest.class);
            cardActionRequest.setActionType(CardAction.ActionType.UPDATE);
            HashMap<String,String> details=new HashMap();
            cardActionRequest.setDetails(details);
            cardActionService.createCardAction(card.getId(),cardActionRequest,Helper.generateRandomString());
            CardAction cardAction=cardActionStore.getAllFromStore()
                                    .stream()
                                    .filter(cardAction2 -> !cardAction2.getActionType().equals(CardAction.ActionType.CREATE))
                                    .findFirst()
                                    .get();
            Assert.assertEquals(cardAction.getActionType(),cardActionRequest.getActionType());
            Assert.assertEquals(cardActionRequest.getReason(),cardAction.getReason());

        }
        catch (Exception test){
            test.printStackTrace();
        }

    }

}
