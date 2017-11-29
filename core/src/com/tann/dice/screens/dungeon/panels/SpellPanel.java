package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class SpellPanel extends Actor{
    public static final int SIZE = 62;
    public static float IMAGE_MULT = .8f;
    public static float BORDER = 2f;
    final Spell spell;
    public SpellPanel(Spell spell){
        setSize(SIZE, SIZE);
        this.spell = spell;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, Colours.light, BORDER);
        batch.setColor(Colours.z_white);
        Draw.drawSizeCentered(batch, spell.image, getX()+getWidth()/2, getY()+getHeight()/2, getWidth()*IMAGE_MULT, getHeight()*IMAGE_MULT);
        super.draw(batch, parentAlpha);
    }
}
