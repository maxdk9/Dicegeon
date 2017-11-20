package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

public class DieSlot extends Actor{

    private Die contents;

    public DieSlot() {
        setSize(BulletStuff.convertToScreen(1), BulletStuff.convertToScreen(1));
    }

    public void setDie(Die die){
        this.contents = die;
    }

    public Die getContents(){
        return contents;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.grey);
        Draw.fillActor(batch, this);
        super.draw(batch, parentAlpha);
    }
}
