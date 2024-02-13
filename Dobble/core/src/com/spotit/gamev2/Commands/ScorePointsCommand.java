package com.spotit.gamev2.Commands;

import com.spotit.gamev2.Player;

public class ScorePointsCommand extends Command {

    private Player player;
    private int points;

    public ScorePointsCommand(Player player, int points) {
        this.player = player;
        this.points = points;
    }

    @Override
    public void execute() {
        player.addPoints(points);
    }

}
