package com.tann.dice.gameplay.effect.buff;

public class Stealth extends Buff{

    public Stealth(int turns) {
        super(turns);
    }

    @Override
    protected int alterIncomingDamage(int amount) {
        return 0;
    }

    @Override
    protected void finaliseCopy(Buff b) {
    }
}
