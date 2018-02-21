package com.tann.dice.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;

public class TextButton extends Group{
    String text;
    TannFont font = TannFont.font;
    boolean highlight;
    public TextButton(float width, float height, String text) {
        setText(text);
        setSize(width, height);
    }

    public TextButton(String text, int gap) {
        setText(text);
        setSize(TannFont.font.getWidth(text)+gap*2, TannFont.font.getHeight()+gap*2);
    }

    public void setText(String text){
        this.text = text;
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
                    super.enter(event, x, y, pointer, fromActor);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                }
            }


        );
    }

    Color background = Colours.dark;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int BORDER = 2;
        Draw.fillActor(batch, this, background, Colours.purple, BORDER);
        batch.setColor(Colours.light);
        font.drawString(batch, text, (int)(getX()+getWidth()/2f-font.getWidth(text)/2f), (int) (getY()+getHeight()/2-font.getHeight()/2));
    }

}
