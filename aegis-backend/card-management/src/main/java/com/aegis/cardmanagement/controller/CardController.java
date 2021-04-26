package com.aegis.cardmanagement.controller;

import com.aegis.cardmanagement.exception.CardAlreadyExists;
import com.aegis.cardmanagement.exception.CardNotFound;
import com.aegis.cardmanagement.exception.NoCardAvailable;
import com.aegis.cardmanagement.modal.Card;
import com.aegis.cardmanagement.pojos.CardRequest;
import com.aegis.cardmanagement.service.CardService;
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
public class CardController {

    @Autowired
    CardService cardService;



    @CrossOrigin
    @PostMapping("/cards")
    public ResponseEntity<Card> createCard(@RequestBody CardRequest cardRequest, HttpServletRequest httpServletRequest) {
        Card cardResp = null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            cardResp = cardService.createCard(cardRequest,userEmail);
        } catch (CardAlreadyExists cardException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "card already exists", cardException);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);

        }
        return new ResponseEntity<Card>(cardResp, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<Card> getCard(@PathVariable UUID cardId) {
        Card card = null;
        try {
            card = cardService.getCard(cardId);
        } catch (CardNotFound cardNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }
        return new ResponseEntity<Card>(card, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards(){
        List<Card> card;
        try{
            card = cardService.getAllCards();
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return ResponseEntity.ok(card);
    }

    @CrossOrigin
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<Card> updateCard(@RequestBody CardRequest cardRequest, @PathVariable UUID cardId, HttpServletRequest httpServletRequest) {
        Card card = null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            card = cardService.updateCard(cardRequest, cardId,userEmail);
        } catch (CardNotFound cardNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }
        catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return new ResponseEntity<Card>(card, HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity deleteCard(@PathVariable UUID cardId,HttpServletRequest httpServletRequest) {
        Card card = null;
        String userEmail=(String)httpServletRequest.getAttribute("userEmail");
        try {
            cardService.deleteCard(cardId,userEmail);
        } catch (CardNotFound cardNotFound) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "card does not exist", cardNotFound);
        }      catch (Exception systemException){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "system exception", systemException);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/health/liveness")
    public ResponseEntity<String> health(){
        return new ResponseEntity<String>("alive",HttpStatus.OK);
    }


}
