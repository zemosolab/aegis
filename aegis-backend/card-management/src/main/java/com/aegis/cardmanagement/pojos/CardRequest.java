package com.aegis.cardmanagement.pojos;


import lombok.Data;


@Data
public class CardRequest {
    private String hardwareId;
    private String cardName;
    String reason;

}
