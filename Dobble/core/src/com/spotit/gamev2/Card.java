package com.spotit.gamev2;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

public class Card {


    /* Variables */

    private final Symbol[] symbols;
    private final Circle circle;


    /* Constructor */

    public Card(Symbol[] symbols) {
        this.symbols = symbols;
        circle = new Circle();
    }


    /* Methods */

    public boolean contains(Symbol s) {
        for (Symbol symbol : symbols) {
            if (symbol.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCardPosition(Vector2 pos) {
        circle.setPosition(pos);
    }

    public void setCardRadius(float r) {
        circle.setRadius(r);
    }

    public Symbol[] getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        return "Card->{" +
                "symbols=" + Arrays.toString(symbols) +
                "}";
    }

    public String toFormattedString() {
        String s = "Card->{\n\t\tsymbols=\n";
        for (Symbol symbol : symbols) {
            s = s.concat(String.format("\t\t\t%s\n", symbol.toString()));
        }
        return s.concat("\t\t}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Card card = (Card) o;
        if (!Arrays.equals(this.symbols, card.symbols)) {
            for (Symbol s : this.symbols) {
                if (!card.contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(symbols);
    }


}
