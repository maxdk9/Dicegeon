package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class DieSidePanel extends Actor {
    Side side;
    Color colour;
    int scale;

    public DieSidePanel(Side side, Color colour, int scale) {
        this.side = side;
        this.colour = colour;
        this.scale = scale;
        setSize(side.tr.getRegionWidth()*scale, side.tr.getRegionHeight()*scale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        side.draw(batch, getX(), getY(), scale, colour);
        super.draw(batch, parentAlpha);
    }
}
