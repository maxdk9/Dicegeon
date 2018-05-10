package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerOnKill extends Trigger {

    private TextureRegion lifesteal = Main.atlas.findRegion("trigger/lifesteal");

    Eff eff;

    public TriggerOnKill(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void onKill() {
        entity.hit(eff, false);
    }

    @Override
    public String describe() {
        return "When you kill an enemy, "+eff.toString().toLowerCase();
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public TextureRegion getImage() {
        return lifesteal;
    }
}
