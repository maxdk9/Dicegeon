package com.tann.dice.gameplay.phase;

import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class DamagePhase extends Phase {
    @Override
    public void activate() {
        EntityGroup.activateDamage();
        Main.popPhase();
    }

    @Override
    public void deactivate() {
        Main.pushPhase(new EnemyRollingPhase());
    }
}
