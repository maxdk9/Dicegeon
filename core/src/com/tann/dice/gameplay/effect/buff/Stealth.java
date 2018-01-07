package com.tann.dice.gameplay.effect.buff;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;

public class Stealth extends Buff{

    public Stealth(int turns) {
        super(turns, Images.stealth);
    }

    @Override
    public int alterIncomingDamage(int amount) {
        return 0;
    }

    @Override
    public String toNiceString() {
        return "target is immune to damage for "+turns+" turns";
    }
}
