package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerExtraLife extends Trigger{

    private static TextureRegion extraLife = loadImage("extraLife");

    @Override
    public boolean avoidDeath() {
        //TODO implement this
        return true;
    }

    @Override
    public String describe() {
        return "Survive lethal damage on 1 hp";
    }

    @Override
    public TextureRegion getImage() {
        return extraLife;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }
}
