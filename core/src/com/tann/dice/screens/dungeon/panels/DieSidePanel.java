package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class DieSidePanel extends Actor {
    DiceEntity e;
    Side s;

    public DieSidePanel(DiceEntity e, Side s) {
        this.e = e;
        this.s = s;
        setSize(e.getPixelSize(), e.getPixelSize());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, e.getColour(), 1);
        batch.setColor(Colours.z_white);
        batch.draw(s.tr, getX(), getY());
        batch.draw(Side.sizeToPips.get(e.getSize())[s.effects[0].getValue()], getX(), getY());
        super.draw(batch, parentAlpha);
    }
}
