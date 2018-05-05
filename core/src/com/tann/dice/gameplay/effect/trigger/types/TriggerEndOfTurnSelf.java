package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;

public class TriggerEndOfTurnSelf extends Trigger {

    private static final TextureRegion poison = Main.atlas.findRegion("trigger/poison");
    private static final TextureRegion regen = Main.atlas.findRegion("trigger/regen");

    Eff eff;
    public TriggerEndOfTurnSelf(Eff eff) {
        this.eff = eff;
    }

    @Override
    public void endOfTurn() {
        entity.hit(eff, false);
    }

    @Override
    public Integer getIncomingPoisonDamage() {
        if(eff.type== Eff.EffType.Damage){
            return eff.getValue();
        }
        return 0;
    }

    @Override
    public Integer getRegen() {
        if(eff.type== Eff.EffType.Healing){
            return eff.getValue();
        }
        return 0;
    }

    @Override
    public boolean showInPanel() {
        return true;
    }

    @Override
    public TextureRegion getImage() {
        switch(eff.type){
            case Damage:
                return poison;
            case Healing:
                return regen;
        }
        return super.getImage();
    }

    @Override
    public String describeForBuffText() {
        switch (eff.type){
            case Damage:
                return eff.getValue() + " poison damage ([green][heart][green]) each turn";
            case Healing:
                return "Regenerate "+eff.getValue() + " health each turn";
            default: return "missing buff text: "+eff.type;
        }
    }

    @Override
    public String describe() {
        return describeForBuffText();
    }

    @Override
    public boolean isNegative() {
        return eff.type== Eff.EffType.Damage;
    }

    @Override
    public void setValue(int value) {
        eff.justValue(value);
    }
}
