package com.tann.dice.gameplay.village.phase;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.screens.gameScreen.panels.review.LossPanel;

public class LossPhase extends Phase{

    Eff lossCause;

    public LossPhase(Eff e) {
        lossCause = e;
    }

    @Override
    public void activate() {
        LossPanel lp = new LossPanel(LossPanel.LossReason.Morale, 2);
        GameScreen.get().addWithProceedButton(lp, false);
    }

    @Override
    public void deactivate() {

    }
}
