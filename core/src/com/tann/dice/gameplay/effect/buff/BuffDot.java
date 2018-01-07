package com.tann.dice.gameplay.effect.buff;

public class BuffDot extends Buff{
    int damage;
    public BuffDot(int turns, int damage) {
        super(turns);
        this.damage = damage;
    }

    @Override
    public void endOfTurn() {
        target.damage(damage);
    }

    @Override
    protected void finaliseCopy(Buff b) {
        ((BuffDot)b).damage = damage;
    }
}
