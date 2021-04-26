package com.aegis.cardmanagement.service;


import com.aegis.cardmanagement.exception.ActionNotFound;
import com.aegis.cardmanagement.exception.CardNotFound;
import com.aegis.cardmanagement.exception.CardToActionMismatch;
import com.aegis.cardmanagement.exception.SystemException;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.repository.CardActionRepository;
import com.aegis.cardmanagement.utils.DeviceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@Setter
@Getter
public class CardActionService {

    @Autowired
    private CardActionRepository cardActionRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    DeviceManager deviceManager;

    public CardAction createCardAction(UUID cardId, CardActionRequest cardActionRequest, String userEmail) throws CardNotFound, JsonProcessingException {
        Card.Status cardStatus = null;
        CardAction.ActionCreationType actionCreationType = CardAction.ActionCreationType.USERGENERATED;
        boolean isAssignUpdate = false;
        switch (cardActionRequest.getActionType()) {
            case DELETE:
            case UPDATE:
            case CREATE:
                cardStatus = null;
                break;
            case BLOCK:
                cardStatus = Card.Status.BLOCKED;
                break;
            case UNASSIGN:
                cardStatus = Card.Status.UNASSIGNED;
                break;
            case UNBLOCK:
                cardStatus = Card.Status.UNBLOCKED;
                break;
            case ASSIGN:
                cardStatus = Card.Status.ASSIGNED;
                isAssignUpdate = true;
                break;
            case EXTEND:
                cardStatus = Card.Status.ASSIGNED;
                isAssignUpdate = true;
                actionCreationType = CardAction.ActionCreationType.SYSTEMGENERATED;
                break;
        }
        CardAction cardAction = new CardAction();
        copyDetails(cardAction, cardActionRequest, cardId);
        generateSystemDetails(cardAction, userEmail, actionCreationType);
        Card card = cardService.getCard(cardId);
        cardAction = cardActionRepository.save(cardAction);
        if (cardStatus != null) {
            cardService.updateCardStatus(cardId, cardStatus);
        }
        if (isAssignUpdate)
            cardService.updateCardAssignment(cardId, cardAction.getId());

       // if (cardActionRequest.getActionType() != CardAction.ActionType.CREATE) {
         //   Optional<CardAction> latestAssignAction = cardActionRepository.findById(card.getAssignmentId());
           // if (latestAssignAction.isPresent())
           //     deviceManager.cardActionOnDevice(latestAssignAction.get(), card.getHardwareId(), cardActionRequest.getActionType());
        //}


        return cardAction;
    }


    public List<CardAction> getAllActions(UUID cardId) throws SystemException, CardNotFound {
        cardService.checkIfCardExists(cardId);
        List<CardAction> cardActions = null;
        try {
            cardActions = cardActionRepository.findAllByCardId(cardId);
        } catch (Exception systemException) {
            throw new SystemException("system exception");
        }
        return cardActions;
    }

    public CardAction getActionById(UUID cardId, UUID actionId) throws ActionNotFound, CardToActionMismatch, CardNotFound {
        Optional<CardAction> optionalCardAction = cardActionRepository.findById(actionId);
        cardService.checkIfCardExists(cardId);
        if (!optionalCardAction.isPresent()) {
            throw new ActionNotFound("action not found");
        }
        CardAction cardAction = optionalCardAction.get();
        if (!cardId.equals(cardAction.getCard().getId())) {
            throw new CardToActionMismatch("this action does not belong to this card");
        }
        return cardAction;
    }


    public void copyDetails(CardAction cardAction, CardActionRequest cardActionRequest, UUID cardId) throws CardNotFound {
        cardAction.setActionType(cardActionRequest.getActionType());
        cardAction.setDetails(cardActionRequest.getDetails());
        cardAction.setReason(cardActionRequest.getReason());
        cardAction.setCard(cardService.getCard(cardId));
    }

    public void generateSystemDetails(CardAction cardAction, String userEmail, CardAction.ActionCreationType actionCreationType) {
        cardAction.setCreatedAt(new Date());
        cardAction.setActionCreationType(actionCreationType);
        cardAction.setCreatedBy(userEmail);
    }


}



