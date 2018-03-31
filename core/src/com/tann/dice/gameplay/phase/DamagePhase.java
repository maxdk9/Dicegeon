package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.EntityGroup;
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
                if(!DungeonScreen.get().checkEnd()){
                    PhaseManager.get().popPhase();
                }
            }
        }, .3f);

    }

    @Override
    public void deactivate() {
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
    }

    @Override
    public String describe() {
        return "Enemy is targeting";
    }
}
