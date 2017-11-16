package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TextureFlasher extends Actor {
    TextureRegion tr;
    public TextureFlasher(TextureRegion tr) {
        this.tr=tr;
        addAction(Actions.sequence(Actions.fadeOut(.7f), Actions.removeActor()));
        setSize(100,100);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        Draw.drawSize(batch, tr, getX(), getY(), getWidth(), getHeight());
    }
}
