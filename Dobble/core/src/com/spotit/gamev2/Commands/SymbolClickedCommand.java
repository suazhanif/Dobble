package com.spotit.gamev2.Commands;

import com.spotit.gamev2.CardPair;
import com.spotit.gamev2.Symbol;

public class SymbolClickedCommand extends Command {

    private Symbol symbol;
    private CardPair cardPair;

    public SymbolClickedCommand(Symbol symbol, CardPair cardPair) {
        super();
        this.symbol = symbol;
        this.cardPair = cardPair;
    }

    @Override
    public void execute() {
        cardPair.solved = cardPair.isMatchingSymbol(symbol);
    }
}
