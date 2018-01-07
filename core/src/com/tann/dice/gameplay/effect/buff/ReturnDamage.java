package com.tann.dice.gameplay.effect.buff;

import com.tann.dice.gameplay.entity.DiceEntity;

public class ReturnDamage extends Buff{

    private int damage;
    public ReturnDamage(int turns, int damage) {
        super(turns);
        this.damage = damage;
    }

    @Override
    protected void attackedBy(DiceEntity entity){
        entity.damage(damage);
    }

    @Override
    protected void finaliseCopy(Buff b) {
        ((ReturnDamage)b).damage = damage;
    }
}
