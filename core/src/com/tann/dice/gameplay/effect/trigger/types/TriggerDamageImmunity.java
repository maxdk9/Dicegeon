package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerDamageImmunity extends Trigger{

    private static final TextureRegion stealth = Main.atlas.findRegion("stealth");

    @Override
    public Integer alterIncomingDamage(Integer incomingDamage) {
        return 0;
    }

    @Override
    public Integer alterIncomingPoisonDamage(Integer incomingDamage) {
        return 0;
    }

    @Override
    public TextureRegion getImage() {
        return stealth;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public String describe() {
        return "Immune to damage";
    }
}
