package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.util.Sounds;
import com.tann.dice.util.Tann;

public class DamagePhase extends Phase {
    @Override
    public void activate() {
        Tann.delay(new Runnable(){
            @Override
            public void run() {
                Sounds.playSound(Sounds.fwips, 4, 1);
                EntityGroup.activateDamage();
                boolean activated = Room.get().activateDelayedRolls();

                Tann.delay(new Runnable() {
                    @Override
                    public void run() {
                        if(!DungeonScreen.get().checkEnd()){
                            PhaseManager.get().popPhase();
                        }
                    }
                }, activated?.5f:0);


            }
        }, .3f);

    }

    @Override
    public void deactivate() {
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
    }

    @Override
    public boolean canTarget() {
        return true;
    }

    @Override
    public String describe() {
        return "Enemy is targeting";
    }
}
