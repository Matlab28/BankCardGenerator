package com.example.bankcardgenerator.dto;

import com.example.bankcardgenerator.constant.CardType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CardResponse {
    private String card;
    private CardType cardType;
    private int length;

    public CardResponse(String card, CardType cardType, int length) {
        this.card = card;
        this.cardType = cardType;
        this.length = length;
    }
}