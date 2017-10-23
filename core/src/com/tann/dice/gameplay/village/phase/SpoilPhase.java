package com.tann.dice.gameplay.village.phase;

import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.review.SpoilPanel;

public class SpoilPhase extends Phase {
    int spoilt;
    public SpoilPhase(int spoilt) {
        this.spoilt=spoilt;
    }

    @Override
    public void activate() {
        Village.getInventory().imposeLimits();
        SpoilPanel panel = new SpoilPanel(spoilt);
        GameScreen.get().addWithProceedButton(panel, true);
    }

    @Override
    public void deactivate() {

    }
}
