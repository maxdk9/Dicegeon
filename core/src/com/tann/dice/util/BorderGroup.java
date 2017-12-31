package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BorderGroup extends Group {
    static final int gap = 2;
    public BorderGroup(Group a) {
        setSize(a.getWidth() + gap*2, a.getHeight() + gap*2);
        a.addActor(this);
        setPosition(-gap, -gap);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.drawRectangle(batch, getX(), getY(), getWidth(), getHeight(), gap);
        super.draw(batch, parentAlpha);
    }
}
