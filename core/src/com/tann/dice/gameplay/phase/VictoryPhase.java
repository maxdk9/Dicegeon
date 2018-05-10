package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.VictoryPanel;
import com.tann.dice.util.Prefs;

public class VictoryPhase extends Phase{
    @Override
    public void activate() {
        Prefs.setBoolean(Prefs.EASY, true);
        if(!LevelManager.get().easy) {
            Prefs.setBoolean(Prefs.HARD, true);
            Prefs.setInt(Prefs.STREAK, Prefs.getInt(Prefs.STREAK, 0)+1);
        }
        DungeonScreen.get().push(new VictoryPanel(), true, true, false, false, 0, null);
    }

    @Override
    public void deactivate() {
    }

    @Override
    public String describe() {
        return "You won!!";
    }
}
