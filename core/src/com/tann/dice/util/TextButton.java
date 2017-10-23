package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;

public class TextButton extends Group{
    String text;
    public TextButton(float width, float height, String text) {
        this.text=text;
        setSize(width, height);
    }

    public void setRunnable(final Runnable runnable){
        addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    runnable.run();
                    return true;
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    background = Colours.fate_light;
                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    background = Colours.dark;
                    super.exit(event, x, y, pointer, toActor);
                }
            }


        );
    }

    Color background = Colours.dark;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int BORDER = (int)(Main.h(.4f));
        super.draw(batch, parentAlpha);
        batch.setColor(Colours.brown_dark);
        Draw.fillRectangle(batch, getX()-BORDER, getY()-BORDER, getWidth()+BORDER*2, getHeight()+BORDER*2);
        batch.setColor(background);
        Draw.fillActor(batch, this);
        Fonts.draw(batch, text, Fonts.fontSmall, Colours.light, getX(), getY(), getWidth(), getHeight());
    }
}
