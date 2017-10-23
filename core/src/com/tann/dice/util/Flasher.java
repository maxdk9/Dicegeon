package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Flasher extends Group {
    public Flasher(Actor a, Color col) {
        setSize(a.getWidth(), a.getHeight());
        setColor(col);
        addAction(Actions.fadeOut(.8f));
        addAction(Actions.after(Actions.removeActor()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        Draw.fillActor(batch, this);
        super.draw(batch, parentAlpha);
    }
}
