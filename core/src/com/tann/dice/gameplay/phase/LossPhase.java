package com.tann.dice.gameplay.phase;

import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.TextButton;

public class LossPhase extends Phase{
    @Override
    public void activate() {
        TextButton tb = new TextButton(500, 100, "You lose, you got to level "+ DungeonScreen.get().level);
        DungeonScreen.get().push(tb, true, true, false, true);
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().restart();
    }
}
