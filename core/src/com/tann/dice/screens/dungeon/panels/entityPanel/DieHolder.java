package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class DieHolder extends Actor {
    static final int extraGap = 0;

    public DieHolder(float size, Color col) {
        setSize(size, size);
        setColor(col);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
