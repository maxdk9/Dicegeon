package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerTotalDamageReduction extends Trigger {

    private static TextureRegion shield = loadImage("shield");

    private final int reduceBy;
    public TriggerTotalDamageReduction(int reduceBy) {
        this.reduceBy = reduceBy;
    }

    @Override
    public Integer alterIncomingDamage(Integer incomingDamage) {
        return Math.max(0, incomingDamage-reduceBy);
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public TextureRegion getImage() {
        return shield;
    }

    @Override
    public String describe() {
        return "Absorb "+reduceBy+" damage each turn";
    }
}
