package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.LossPanel;
import com.tann.dice.screens.dungeon.PhaseManager;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.Prefs;
import com.tann.dice.util.TextButton;
import com.tann.dice.util.TextWriter;

public class LossPhase extends Phase{
    @Override
    public void activate() {
        LossPanel lossPanel = new LossPanel(LevelManager.get().getLevel());
        DungeonScreen.get().push(lossPanel, true, true, false, false, 0, null);
        Prefs.setInt(Prefs.STREAK, 0);
    }

    @Override
    public void deactivate() {

    }


    @Override
    public String describe() {
        return "You lost";
    }
}
