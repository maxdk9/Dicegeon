package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.LevelManager;
import com.tann.dice.screens.dungeon.VictoryPanel;
import com.tann.dice.util.Prefs;

public class VictoryPhase extends Phase{
    @Override
    public void activate() {
        if(LevelManager.get().easy) {
            Prefs.add(Prefs.EASY, 1);
        }
        else{
            Prefs.add(Prefs.HARD, 1);
            Prefs.add(Prefs.STREAK, 1);
            int streak = Prefs.getInt(Prefs.STREAK, 0);
            if(streak > Prefs.getInt(Prefs.MAX_STREAK, 0)){
                Prefs.setInt(Prefs.MAX_STREAK, streak);
            }
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
