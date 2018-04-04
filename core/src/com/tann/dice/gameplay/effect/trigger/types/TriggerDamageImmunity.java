package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerDamageImmunity extends Trigger{

    private static final TextureRegion stealth = loadImage("stealth");

    boolean poison, regular;

    public TriggerDamageImmunity(boolean poison, boolean regular) {
        this.poison = poison;
        this.regular = regular;
    }

    @Override
    public Integer alterIncomingDamage(Integer incomingDamage) {
        return regular?0:incomingDamage;
    }

    @Override
    public Integer alterIncomingPoisonDamage(Integer incomingDamage) {
        return poison?0:incomingDamage;
    }

    @Override
    public TextureRegion getImage() {
        return stealth;
    }

    @Override
    public boolean showInPanel() {
        return regular;
    }

    @Override
    public String describe() {
        if(poison&&regular) return "Immune to damage";
        if(poison&&!regular) return "Immune to poison damage";
        return "Damage immunity gone wrong!";
    }
}
