package com.tann.dice.screens.dungeon.panels.entityPanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class SwipeyActor extends Actor {
    static final float maxLife = .6f, dx = -4, dy = -4;
    float life = maxLife;
    float ratio;
    public SwipeyActor() {
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        life -= delta;
        ratio = life/maxLife;
        if(life <= 0) remove();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float lineDist = Interpolation.pow3Out.apply(Math.min(1, (1-ratio)*2));
        float alpha = Interpolation.pow2In.apply(Math.min(1, (ratio*2)));
        batch.setColor(Colours.withAlpha(Colours.light, alpha));
        Draw.drawLine(batch, getX(), getY(), getX()+dx*lineDist, getY()+dy*lineDist, 1);
    }
}
