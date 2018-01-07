package com.tann.dice.gameplay.effect.buff;

import com.tann.dice.Images;

public class BuffDot extends Buff{
    int damage;
    public BuffDot(int turns, int damage) {
        super(turns, Images.poison);
        this.damage = damage;
    }

    @Override
    public void endOfTurn() {
        target.damage(damage);
    }

    @Override
    public String toNiceString() {
        if(damage>0){
            return damage + " damage per turn to an enemy target";
        }
        else{
            return -damage + " healing per turn to a friendly target";
        }
    }

}
