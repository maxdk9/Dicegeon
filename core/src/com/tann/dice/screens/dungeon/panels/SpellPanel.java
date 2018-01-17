package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class SpellPanel extends Actor{
    public static final int SIZE = 20;
    public static float IMAGE_MULT = .8f;
    public static float BORDER = 2f;
    final Spell spell;

    public SpellPanel(final Spell spell){
        setSize(SIZE, SIZE);
        this.spell = spell;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DungeonScreen.get().click(spell);
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Draw.fillActor(batch, this, Colours.dark, (spell.selected) ? Colours.light : Colours.magic_dark, BORDER);
        batch.setColor(Colours.z_white);
        Draw.drawSizeCentered(batch, spell.image, getX()+getWidth()/2, getY()+getHeight()/2, getWidth()*IMAGE_MULT, getHeight()*IMAGE_MULT);
        super.draw(batch, parentAlpha);
    }
}
