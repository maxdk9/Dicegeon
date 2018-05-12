package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerNoDeathPenalty extends Trigger {

    private static final TextureRegion image = loadImage("bone");

    @Override
    public boolean avoidDeathPenalty() {
        return true;
    }

    @Override
    public String describe() {
        return "No HP penalty in the next fight for dying";
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public TextureRegion getImage() {
        return image;
    }
}
