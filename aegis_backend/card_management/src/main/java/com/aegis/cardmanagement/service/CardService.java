package com.aegis.cardmanagement.service;

import com.aegis.cardmanagement.exception.SystemException;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.pojos.CardRequest;
import com.aegis.cardmanagement.repository.CardRepository;
import com.aegis.cardmanagement.exception.CardAlreadyExists;
import com.aegis.cardmanagement.exception.CardNotFound;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.utils.DeviceManager;
import com.aegis.cardmanagement.utils.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Setter
@Getter
@Transactional
public class CardService {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    CardActionService cardActionService;




    public Card createCard(CardRequest cardRequest, String userEmail) throws CardAlreadyExists, CardNotFound, SystemException, JsonProcessingException {
        List<Card> cards=cardRepository.findByHardwareId(cardRequest.getHardwareId());
        if(cards.size()>0){
            throw  new CardAlreadyExists("card already exists");
        }
        Card card = Helper.copyCardDetails(cardRequest,new Card());
        card=cardRepository.save(card);
        CardActionRequest cardActionRequest=Helper.getActionRequestDetails(card, CardAction.ActionType.CREATE);
        cardActionService.createCardAction(card.getId(),cardActionRequest,userEmail);
        return card;
    }



    public Card updateCard(CardRequest cardRequest, UUID cardId, String userEmail) throws CardNotFound, SystemException, JsonProcessingException {
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
        Card card=optionalCard.get();
        card.setCardName(cardRequest.getCardName());
        CardActionRequest cardActionRequest=Helper.getActionRequestDetails(card, CardAction.ActionType.UPDATE);
        cardActionService.createCardAction(card.getId(),cardActionRequest,userEmail);
        return cardRepository.save(card);

    }

    public Card updateCardStatus(UUID cardId, Card.Status status) throws CardNotFound{
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
        Card card=optionalCard.get();
        card.setCardStatus(status);
        return cardRepository.save(card);
    }

    public Card updateCardAssignment(UUID cardId,UUID assignmentId) throws CardNotFound{
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
        Card card=optionalCard.get();
        card.setAssignmentId(assignmentId);
        return cardRepository.save(card);
    }

    public void deleteCard(UUID cardId,String userEmail) throws CardNotFound, SystemException, JsonProcessingException {
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
        Card card=optionalCard.get();
        CardActionRequest cardActionRequest=Helper.getActionRequestDetails(card, CardAction.ActionType.DELETE);
        cardActionService.createCardAction(cardId,cardActionRequest,userEmail);
        cardRepository.deleteById(cardId);
    }

    public List<Card> getAllCards() throws SystemException{
        List<Card> cards=null;
        try{
            cards = cardRepository.findAll();
        }
        catch (Exception systemException){
            throw  new SystemException("system exception");
        }
        return cards;
    }



    public Card getCard(UUID cardId) throws  CardNotFound{
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
        return optionalCard.get();
    }



    public void checkIfCardExists(UUID cardId) throws CardNotFound{
        Optional<Card> optionalCard=cardRepository.findById(cardId);
        if(!optionalCard.isPresent()){
            throw  new CardNotFound("card not found");
        }
    }

}


