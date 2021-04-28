package com.aegis.cardmanagement.controller;

import com.aegis.cardmanagement.exception.ActionNotFound;
import com.aegis.cardmanagement.exception.CardNotFound;
import com.aegis.cardmanagement.exception.CardToActionMismatch;
import com.aegis.cardmanagement.modal.CardAction;
import com.aegis.cardmanagement.pojos.CardActionRequest;
import com.aegis.cardmanagement.service.CardActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CardActionController {

    @Autowired
    CardActionService cardActionService;

    @PostMapping("cards/{cardId}/actions")
    public ResponseEntity<CardAction> doAction(
            @RequestBody CardActionRequest cardActionRequest,
            @PathVariable UUID cardId , HttpServletRequest httpServletRequest){

        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        CardAction completedAction=null;
        try{
            completedAction=cardActionService.createCardAction(cardId,cardActionRequest,userEmail);
        }catch (CardNotFound cardNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return  new ResponseEntity<CardAction>(completedAction, HttpStatus.OK);
    }

    @GetMapping("cards/{cardId}/actions")
    public ResponseEntity<List<CardAction>> getActions(@PathVariable UUID cardId){
        List<CardAction> cardActionList=null;
        try{
           cardActionList= cardActionService.getAllActions(cardId);
        }
        catch (CardNotFound cardNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(cardActionList);
    }

    @GetMapping("cards/{cardId}/actions/{actionId}")
    public ResponseEntity<CardAction> getAction(@PathVariable UUID cardId,@PathVariable UUID actionId){
        CardAction cardAction=null;
        try{
            cardAction= cardActionService.getActionById(cardId,actionId);
        }
        catch (ActionNotFound actionNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "action not found", actionNotFound);
        }
        catch (CardNotFound cardNotFound){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }
        catch (CardToActionMismatch cardToActionMismatch){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "card action mismatch", cardToActionMismatch);
        }
        return ResponseEntity.ok(cardAction);
    }


}
