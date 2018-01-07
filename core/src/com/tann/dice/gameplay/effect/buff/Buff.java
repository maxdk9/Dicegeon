package com.tann.dice.gameplay.effect.buff;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.DiceEntity;

public abstract class Buff implements Cloneable{

    public enum BuffType{
        dot(Images.poison, "Deal one damage per inturnal"),
        doubleDamage(Images.doubleDamage, "Deal one damage per inturnal"),
        regen(Images.regen, "Restore 1 health each inturnal"),
        stealth(Images.stealth, "Become immune to damage"),
        fireShield(Images.flameWard, "Deal 2 damage to each attacker when attacked");

        public TextureRegion image;
        public String description;
        BuffType(TextureRegion image, String description) {
            this.image = image;
            this.description = description;
        }
    }

    public BuffType type;
    int value;
    int turns;
    DiceEntity target;
    public Buff(int turns){
        this.turns = turns;
    }

    public void inturnal(){
        if(turns == 0) return;
        if(turns != -1) turns--;
        endOfTurn();
    }

    public void endOfTurn(){}

    private void remove() {
        target.removeBuff(this);
    }

    protected int alterOutgoingDamage(int input){
        return input;
    }

    protected int alterIncomingDamage(int amount){
        return amount;
    }

    protected void attackedBy(DiceEntity entity){};

    protected abstract void finaliseCopy(Buff b);

    public Buff copy(){
        Buff clone = null;
        try {
            clone = (Buff) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        finaliseCopy(clone);
        return clone;
    }
}
