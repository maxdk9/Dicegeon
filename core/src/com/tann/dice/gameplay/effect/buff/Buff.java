package com.tann.dice.gameplay.effect.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;

public abstract class Buff implements Cloneable{

    int turns;
    public DiceEntity target;
    public TextureRegion image;
    public Buff(int turns, TextureRegion image){
        this.image = image;
        this.turns = turns;
    }

    public void inturnal(){
        if(turns != -1) turns--;
        if(turns == 0){
            remove();
            return;
        }
        endOfTurn();
    }

    public void endOfTurn(){}

    private void remove() {
        target.removeBuff(this);
    }

    public int alterOutgoingDamage(Eff.EffectType type, int input){return input;}

    public int alterIncomingDamage(int amount){return amount;}

    public void attackedBy(DiceEntity entity){};

    public Buff copy(){
        Buff clone = null;
        try {
            clone = (Buff) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public abstract String toNiceString();
}
