package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerCowardly extends Trigger {

    public static final TextureRegion image = loadImage("cowardly");

    @Override
    public boolean cancelVolunteerForwards() {
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
        return "Stays at the back if possible";
    }
}
