package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tann.dice.Images;
import com.tann.dice.screens.EscMenu;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.ImageActor;
import com.tann.dice.util.Pixl;

public class LossPanel extends Group {

    public LossPanel(int level) {
        new Pixl(this, 5, 150)
                .text("[red]YOU LOSE!")
                .row()
                .actor(new ImageActor(Images.hugeSkull))
                .row()
                .text("You got to level " + (level+1))
                .row()
                .actor(EscMenu.makeQuit())
                .actor(EscMenu.makeRestart())
                .pix();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int border = 2;
        Draw.fillActor(batch, this, Colours.dark, Colours.red, border);
        super.draw(batch, parentAlpha);
    }
}
