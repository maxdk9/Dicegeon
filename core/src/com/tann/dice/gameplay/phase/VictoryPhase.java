package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.TextButton;

public class VictoryPhase extends Phase{
    @Override
    public void activate() {
        TextButton tb = new TextButton(500, 100, "Wow you won!");
        DungeonScreen.get().push(tb, true, false, true, false);
    }

    @Override
    public void deactivate() {

    }
}
