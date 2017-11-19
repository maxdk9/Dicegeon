package com.tann.dice.gameplay.phase;


import com.badlogic.gdx.utils.Timer;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class PlayerRollingPhase extends Phase {
    @Override
    public void activate() {
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                DungeonScreen.get().playerRoll(true);
            }
        }, .6f);

    }

    @Override
    public void deactivate() {
        Main.pushPhase(new DamagePhase());
    }
}
