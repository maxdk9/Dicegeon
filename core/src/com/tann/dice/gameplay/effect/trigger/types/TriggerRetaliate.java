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
        return "Retaliates by dealing 1 damage to all heroes after you cover a [orange][heartArrowLeft][orange] symbol with damage";
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
        //TODO gameplay stuff
//        for(int threshold:hps){
//            if(old>threshold && now <= threshold){
//                for(DiceEntity de: new ArrayList<>(Party.get().getActiveEntities())){
//                    de.damage(1);
//                    activated = true;
//                }
//            }
//        }
        return activated;
    }
}
