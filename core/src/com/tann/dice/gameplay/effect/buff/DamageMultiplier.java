package com.tann.dice.gameplay.effect.buff;

public class DamageMultiplier extends Buff{
    private float multiplier;

    public DamageMultiplier(float multiplier, int turns) {
        super(turns);
        this.multiplier = multiplier;
    }

    @Override
    public int alterOutgoingDamage(int amount) {
        return (int)(amount * multiplier);
    }

    @Override
    protected void finaliseCopy(Buff copy) {
        ((DamageMultiplier)copy).multiplier = multiplier;
    }
}
