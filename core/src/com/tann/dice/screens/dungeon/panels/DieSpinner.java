package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.util.Draw;

public class DieSpinner extends Actor {
    Die d;
    public DieSpinner(Die d, float size) {
        this.d=d;
        setSize(size,size);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this);
        batch.end();
        Vector2 result = localToStageCoordinates(new Vector2());
        BulletStuff.drawSpinnyDie3(d, result.x+getWidth()/2, result.y+getHeight()/2, getWidth());
        batch.begin();
        super.draw(batch, parentAlpha);
    }
}