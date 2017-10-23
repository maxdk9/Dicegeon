package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class CircleButton extends Lay{
    public CircleButton(float x, float y, final float radius, Color col) {
        setSize(radius*2, radius*2);
        setCirclePosition(x,y);
        setColor(col);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                float centerX = x-getWidth()/2, centerY = y-getHeight()/2;
                double dist = Math.sqrt(centerX*centerX + centerY*centerY);
                if(dist>getWidth()/2) return false;
                if(clickAction!=null) clickAction.run();
                return true;
            }
        });
    }

    public void setCirclePosition(float x, float y){
        setPosition(x-getWidth()/2, y-getHeight()/2);
    }

    Runnable clickAction;
    public void setClickAction(Runnable clickAction){
        this.clickAction = clickAction;
    }

    TextureRegion tr;
    float textureX, textureY, textureWidth, textureHeight;
    public void setTexture(TextureRegion tr, float x, float y, float width, float height){
        this.tr=tr;
        this.textureX=x; this.textureY=y;
        this.textureWidth=width; this.textureHeight = height;
    }

    public void setActor(Actor a, float x, float y){
        addActor(a);
        a.setPosition(x,y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor());
        Draw.fillEllipse(batch, getX()+getWidth()/2, getY()+getHeight()/2, getWidth(), getHeight());
        if(tr!=null){
            batch.setColor(Colours.z_white);
            Draw.drawSizeCentered(batch, tr, getX()+textureX*getWidth(), getY()+textureY*getHeight(), textureWidth, textureHeight);
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void layout() {
    }
}
