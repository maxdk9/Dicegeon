package com.tann.dice.gameplay.fightLog.action;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.TargetingManager;

public class TargatableCommand extends Command {

    Targetable effect;
    DiceEntity target;

    @Override
    public void act(Snapshot snapshot) {
        snapshot.target(target, effect);
    }
}
