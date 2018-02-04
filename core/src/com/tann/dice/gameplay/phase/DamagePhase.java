package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.screens.dungeon.DungeonScreen;
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
                    Main.popPhase();
                }
            }
        }, .3f);

    }

    @Override
    public void deactivate() {
        Main.pushPhase(new EnemyRollingPhase());
    }
}
