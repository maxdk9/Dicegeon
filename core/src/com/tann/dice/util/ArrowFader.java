package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ArrowFader extends Actor {
    float toX, toY;
    public ArrowFader point(float fromX, float fromY, float toX, float toY){
        setPosition(fromX, fromY);
        this.toX = toX; this.toY = toY;
        setColor(Colours.red);
        addAction(Actions.sequence(Actions.fadeOut(1), Actions.removeActor()));
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        Draw.drawArrow(batch, getX(), getY(), toX, toY, 8);
    }
}
