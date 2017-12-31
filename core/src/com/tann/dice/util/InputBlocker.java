package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class InputBlocker extends Actor{
    private static InputBlocker self;
    public static InputBlocker get(){
        if(self == null){
            self = new InputBlocker();
        }
        return self;
    }

    Runnable r;
    public void setAction(Runnable r){
        this.r=r;
    }

    private InputBlocker(){
        setAction(
            new Runnable() {
                @Override
                public void run() {
                    DungeonScreen.get().pop();
                }
            }
        );
        setSize(Main.width, Main.height);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(r!=null) r.run();
                event.handle();
                event.stop();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.setColor(0,0,0,.5f);
//        Draw.fillActor(batch, this);
    }
}
