package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public abstract class InfoPanel extends Group {
    public int getNiceY() {
        return (int) (DungeonScreen.BOTTOM_BUTTON_HEIGHT + (Main.height-DungeonScreen.BOTTOM_BUTTON_HEIGHT)/2-getHeight()/2);
    }
    public int getNiceX(boolean care){
        return (int) (Main.width/2-getWidth()/2);
    }
}
