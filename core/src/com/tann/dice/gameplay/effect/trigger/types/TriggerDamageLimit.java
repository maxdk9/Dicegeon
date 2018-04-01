package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerDamageLimit extends Trigger {

    private static final TextureRegion limitDamage = loadImage("limitDamage");
    int limit;

    public TriggerDamageLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public Integer alterIncomingDamage(Integer incomingDamage) {
        return Math.min(limit, incomingDamage);
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public String describe() {
        return "Can't take more than "+limit+" damage in one turn";
    }

    @Override
    public TextureRegion getImage() {
        return limitDamage;
    }
}
