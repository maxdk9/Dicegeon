package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
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

    float alpha = 0;
    private static final float ALPHILLRATE = .15f;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(contents!=null && contents.entity.shaderState == DieShader.DieShaderState.Selected){
            alpha += ALPHILLRATE;
        }
        else{
            alpha -= ALPHILLRATE;
        }
        if(alpha != 0){
            batch.setColor(Colours.withAlpha(Colours.light, alpha));
            float highlightGap = 3;
            Draw.fillRectangle(batch, getX()-highlightGap, getY()-highlightGap, getWidth()+highlightGap*2, getHeight()+highlightGap*2);
        }
        alpha = Math.max(0, Math.min(alpha, 1));
        batch.setColor(Colours.grey);
        Draw.fillActor(batch, this);
        super.draw(batch, parentAlpha);
    }
}
