package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.*;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.TextButton;

public class VictoryPhase extends Phase{
    @Override
    public void activate() {
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
