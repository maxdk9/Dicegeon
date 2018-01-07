package com.tann.dice.gameplay.effect.buff;

import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.DiceEntity;

public class ReturnDamage extends Buff{

    private int damage;
    public ReturnDamage(int turns, int damage) {
        super(turns, Images.flameWard);
        this.damage = damage;
    }

    @Override
    public void attackedBy(DiceEntity entity){
        if(entity.isPlayer() != target.isPlayer()) {
            entity.damage(damage);
        }
    }

    @Override
    public String toNiceString() {
        return "Target reflects "+damage+" damage for "+turns+" turns";
    }

}
