package com.spotit.gamev2.Commands;

import com.spotit.gamev2.Deck;
import com.spotit.gamev2.GameStateMachine;

public class ResetGameCommand extends Command {

    private GameStateMachine gsm;
    private final Deck deck;

    public ResetGameCommand(GameStateMachine gsm, Deck deck) {
        this.gsm = gsm;
        this.deck = deck;
    }

    @Override
    public void execute() {
        deck.resetDeck();
        gsm.changeState(GameStateMachine.GameStates.BEGIN);
    }

}
