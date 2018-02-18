package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.TextButton;

public class VictoryPhase extends Phase{
    @Override
    public void activate() {
        TextButton tb = new TextButton("Wow you won!", 5);
        DungeonScreen.get().push(tb, true, false, true, false, true);
    }

    @Override
    public void deactivate() {

    }
}
