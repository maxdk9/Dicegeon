package com.tann.dice.screens.gameScreen.panels.bottomBar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Main;
import com.tann.dice.util.*;

public class BottomTab extends Lay {

    public Runnable runnable;
    String name;
    Color bg;
    public BottomTab(String name, Color bg) {
        this.name=name;
        this.bg=bg;

        layout();
    }

    public void setRunnable(final Runnable r){
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                r.run();;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void layout() {
        clearChildren();
        TextBox tb = new TextBox(name, Fonts.fontSmall, -1, Align.center);
        float gap = Main.h(1);
        setSize(tb.getWidth()+gap*2, tb.getHeight()+gap*2);
        addActor(tb);
        tb.setPosition(gap, gap+ BottomBar.border()/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float border = BottomBar.border();
//        batch.setColor(Colours.grey);
//        Draw.fillRectangle(batch, getX()-border, getY(), getWidth()+border*2, getHeight()+border*1);
        batch.setColor(bg);
        Draw.fillActor(batch,this);
        super.draw(batch, parentAlpha);
    }
}
