package com.tann.dice.gameplay.phase;


import com.badlogic.gdx.utils.Timer;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class PlayerRollingPhase extends Phase {
    @Override
    public void activate() {
        DungeonScreen.get().playerRoll(true);
    }

    @Override
    public void deactivate() {
        Main.pushPhase(new TargetingPhase());
    }

    @Override
    protected boolean doneCheck() {
        for(Hero h:DungeonScreen.get().heroes){
            if(h.getDie().getState() != Die.DieState.Locked && h.getDie().getState() != Die.DieState.Locking){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canRoll() {
        return true;
    }
}
