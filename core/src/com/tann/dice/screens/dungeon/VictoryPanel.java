package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.screens.EscMenu;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.ImageActor;
import com.tann.dice.util.Pixl;

public class VictoryPanel extends Group {

    public VictoryPanel() {
        new Pixl(this, 10)
                .actor(new ImageActor(LevelManager.get().easy?Images.ending1:Images.ending2))
                .row()
                .actor(EscMenu.makeQuit(false))
                .pix();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int border = 2;
        Draw.fillActor(batch, this, Colours.dark, Colours.blue, border);
        super.draw(batch, parentAlpha);
    }
}
