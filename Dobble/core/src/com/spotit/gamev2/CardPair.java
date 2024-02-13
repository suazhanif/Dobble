package com.spotit.gamev2;

public class CardPair {

    public Card cardL, cardR;
    public boolean solved;

    public CardPair(Card cardL, Card cardR) {
        this.cardL = cardL;
        this.cardR = cardR;
        solved = false;
    }

    public boolean isMatchingSymbol(Symbol symbol) {
        return cardL.contains(symbol) && cardR.contains(symbol);
    }

    public Symbol[] getSymbols() {
        Symbol[] symbols = new Symbol[cardL.getSymbols().length + cardR.getSymbols().length];
        for (int i = 0; i < cardL.getSymbols().length; i++) {
            symbols[i] = cardL.getSymbols()[i];
        }
        for (int i = cardL.getSymbols().length; i < cardL.getSymbols().length + cardR.getSymbols().length; i++) {
            symbols[i] = cardR.getSymbols()[i - cardL.getSymbols().length];
        }
        return symbols;
    }

    @Override
    public String toString() {
        return "CardPair{" +
                "cardL=" + cardL +
                ", cardR=" + cardR +
                ", solved=" + solved +
                '}';
    }

    public String toFormattedString() {
        return "CardPair{" +
                "\n\tcardL=" + cardL +
                ",\n\tcardR=" + cardR +
                ",\n\tsolved=" + solved +
                "\n}";
    }
}
