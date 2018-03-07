package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class EndOfTurnSelfTrigger extends Trigger {
    Eff eff;
    public EndOfTurnSelfTrigger(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void endOfTurn(DiceEntity target) {
        target.hit(eff, true);
    }
}
