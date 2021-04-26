package com.aegis.cardmanagement.service;

import com.aegis.cardmanagement.helpers.CardActionStore;
import com.aegis.cardmanagement.helpers.CardStore;
import com.aegis.cardmanagement.helpers.Helper;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardRequest;
import com.aegis.cardmanagement.repository.CardActionRepository;
import com.aegis.cardmanagement.repository.CardRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CardActionRepository cardActionRepository;

    CardService cardService;

    CardActionService cardActionService;

    private CardStore cardStore = new CardStore();
    private CardActionStore cardActionStore = new CardActionStore();

    @Before
    public void init() {

        Mockito.when(cardRepository.save(any())).thenAnswer(r -> {
            Card card = r.getArgument(0);
            return cardStore.addToStore(card);
        });

        Mockito.when(cardRepository.findAll()).thenAnswer(r -> cardStore.getAllFromStore());

        Mockito.when(cardRepository.findById(any())).thenAnswer(r -> {
            UUID uuid = r.getArgument(0);
            return Optional.ofNullable(cardStore.getFromStore(uuid));
        });
        Mockito.doAnswer(r -> {
            cardStore.delete(r.getArgument(0));
            return null;
        }).when(cardRepository).deleteById(any());


        Mockito.when(cardActionRepository.save(any())).thenAnswer(r -> {
            CardAction cardAction = r.getArgument(0);
            return cardActionStore.addToStore(cardAction);

        });
        cardService = new CardService();
        cardService.setCardRepository(cardRepository);
        cardActionService = new CardActionService();
        cardActionService.setCardActionRepository(cardActionRepository);
        cardActionService.setCardService(cardService);
        cardService.setCardActionService(cardActionService);
    }

    @Test
    public void createCardTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            cardService.createCard(cardRequest, user);
            Card card = cardStore.getAllFromStore().get(0);
            CardAction cardAction = cardActionStore.getAllFromStore().get(0);
            Assert.assertEquals(card.getCardName(), cardRequest.getCardName());
            Assert.assertEquals(cardAction.getActionType(), CardAction.ActionType.CREATE);
            Assert.assertEquals(cardAction.getCreatedBy(), user);
        } catch (Exception test) {
            test.printStackTrace();
        }

    }

    @Test
    public void updateCardTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            cardService.createCard(cardRequest, user);
            Card card = cardStore.getAllFromStore().get(0);
            String updatedCardName = Helper.generateRandomString();
            cardRequest.setCardName(updatedCardName);
            Card card1 = cardService.updateCard(cardRequest, card.getId(), Helper.generateRandomString());
            Assert.assertEquals(card1.getCardName(), updatedCardName);
            Assert.assertEquals(cardActionStore.getCardActionStore().size(), 2);
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void updateCardStatusTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            cardService.createCard(cardRequest, user);
            Card card = cardStore.getAllFromStore().get(0);
            Card.Status status = Card.Status.BLOCKED;
            Card card1 = cardService.updateCardStatus(card.getId(), status);
            Assert.assertEquals(card1.getCardStatus(), Card.Status.BLOCKED);
            Assert.assertEquals(card1.getHardwareId(), card.getHardwareId());
            Assert.assertEquals(card1.getCardName(), card.getCardName());
            Assert.assertEquals(card1.getAssignmentId(), card.getAssignmentId());
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void updateCardAssignmentTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            cardService.createCard(cardRequest, user);
            Card card = cardStore.getAllFromStore().get(0);
            UUID assignmentId = UUID.randomUUID();
            Card card1 = cardService.updateCardAssignment(card.getId(), assignmentId);
            Assert.assertEquals(assignmentId, card1.getAssignmentId());
            Assert.assertEquals(card1.getHardwareId(), card.getHardwareId());
            Assert.assertEquals(card1.getCardName(), card.getCardName());
        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void deleteCardTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            cardService.createCard(cardRequest, user);
            Card card = cardStore.getAllFromStore().get(0);
            String userEmail = Helper.generateRandomString();
            cardService.deleteCard(card.getId(), userEmail);
            Assert.assertEquals(cardStore.getAllFromStore().size(), 0);
            Assert.assertEquals(cardActionStore.getCardActionStore().size(), 2);

        } catch (Exception test) {
            test.printStackTrace();
        }
    }

    @Test
    public void getAllCardsTest() {
        try {
            for (int i = 0; i < 20; i++) {
                CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
                String user = Helper.generateRandomString();
                cardService.createCard(cardRequest, user);
            }
            List<Card> cards = cardService.getAllCards();
            Assert.assertEquals(cards.size(), 20);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void getCardByIdTest() {
        try {
            CardRequest cardRequest = (CardRequest) Helper.populate(new CardRequest(), CardRequest.class);
            String user = Helper.generateRandomString();
            Card card = cardService.createCard(cardRequest, user);

            Card card1 = cardService.getCard(card.getId());
            Assert.assertEquals(card1, card);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}