package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class SpellPanel extends Actor{

    final Spell spell;
    boolean big;
    public SpellPanel(final Spell spell, boolean big){
        this.big = big;
        int size = big?Images.spellBorderBig.getRegionHeight():Images.spellBorder.getRegionHeight();
        setSize(size, size);
        this.spell = spell;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                DungeonScreen.get().click(spell);
                event.cancel();
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.blue);
        batch.draw(big?Images.spellBorderBig:Images.spellBorder, getX(), getY());
        int imageSize = spell.image.getRegionHeight();
        batch.setColor(Colours.z_white);
        batch.draw(spell.image, getX()+getWidth()/2-imageSize/2, getY()+getHeight()/2-imageSize/2);
        super.draw(batch, parentAlpha);
    }
}
