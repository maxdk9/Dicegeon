package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class TriggerDamageAttackers extends Trigger {

    int amount;
    private static final TextureRegion image = loadImage("thorns");

    public TriggerDamageAttackers(int amount) {
        this.amount = amount;
    }

    @Override
    public void attackedBy(DiceEntity entity) {
        entity.damage(amount);
    }

    @Override
    public TextureRegion getImage() {
        return image;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public String describe() {
        return "Return "+amount+" damage whenever an enemy hits you";
    }

    @Override
    public boolean highlightForNewPlayers() {
        return true;
    }
}
