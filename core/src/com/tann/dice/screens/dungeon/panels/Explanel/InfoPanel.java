package com.tann.dice.screens.dungeon.panels.Explanel;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public abstract class InfoPanel extends Group {
    public float getNiceY() {
        return DungeonScreen.BOTTOM_BUTTON_HEIGHT + (Main.height-DungeonScreen.BOTTOM_BUTTON_HEIGHT)/2-getHeight()/2;
    }
    public float getNiceX(boolean care){
        return Main.width/2-getWidth()/2;
    }
}
