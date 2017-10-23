package com.tann.dice.gameplay.village.phase;

import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.inventory.MoralePoint;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.MoralePointPanel;

public class MoralePointPhase extends Phase {
    MoralePoint point;
    public MoralePointPhase(MoralePoint point) {
        this.point = point;
    }

    @Override
    public void activate() {
        Village.get().activate(point.effs, true);
        MoralePointPanel panel = new MoralePointPanel(point);
        GameScreen.get().addWithProceedButton(panel, true);
        GameScreen.get().checkEnd();
    }

    @Override
    public void deactivate() {

    }
}
