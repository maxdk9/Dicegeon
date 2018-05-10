package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;

import java.util.ArrayList;

public class TriggerRetaliate extends Trigger{

    public static final TextureRegion image = loadImage("retaliate");

    public int[] hps;
    Eff eff;

    public TriggerRetaliate(int[] hps, Eff eff) {
        this.hps = hps;
        this.eff = eff;
    }

    @Override
    public String describe() {
        return "Retaliates by dealing one damage to all heroes after taking enough damage to cover a [orange][heartArrowLeft][orange]symbol";
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public TextureRegion getImage() {
        return image;
    }

    @Override
    public boolean highlightForNewPlayers() {
        return true;
    }

    @Override
    public boolean activateOnDamage(int old, int now){
        boolean activated = false;
        for(int threshold:hps){
            if(old>threshold && now <= threshold){
                for(DiceEntity de: new ArrayList<>(Party.get().getActiveEntities())){
                    de.damage(1);
                    activated = true;
                }
            }
        }
        return activated;
    }
}
