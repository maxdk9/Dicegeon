package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerBrave extends Trigger {

    //TODO not fully implemented yet

    public static final TextureRegion image = loadImage("brave");

    @Override
    public boolean stayForwards() {
        return true;
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
        return "Stays at the front";
    }

}
