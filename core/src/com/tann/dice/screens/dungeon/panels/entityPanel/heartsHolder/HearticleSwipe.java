package com.tann.dice.screens.dungeon.panels.entityPanel.heartsHolder;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class HearticleSwipe extends Hearticle {

    static final int dx = -4, dy = -4;

    public HearticleSwipe() {
        super(.6f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float lineDist = Interpolation.pow3Out.apply(Math.min(1, (1-ratio)*2));
        float alpha = Interpolation.pow2In.apply(Math.min(1, (ratio*2)));
        batch.setColor(Colours.withAlpha(Colours.light, alpha));
        int heartOffset = 3;
        Draw.drawLine(batch, getX()+getWidth()+heartOffset, getY()+heartOffset, getX()+getWidth()+heartOffset+dx*lineDist, getY()+getHeight()+heartOffset+dy*lineDist, 1);
    }
}
