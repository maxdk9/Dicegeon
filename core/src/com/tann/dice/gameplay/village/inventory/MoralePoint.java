package com.tann.dice.gameplay.village.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.phase.MoralePointPhase;

/**
 * Created by Oliver.Garland on 02/10/2017.
 */
public class MoralePoint {
    public int morale;
    public Eff[]effs;
    public TextureRegion tr;
    public MoralePoint(int morale, TextureRegion tr, Eff[] effs) {
        this.morale = morale;
        this.effs = effs;
        this.tr=tr;
    }

    public void trigger() {
        if(effs != null) Village.get().pushPhase(new MoralePointPhase(this));
    }
}
