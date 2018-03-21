package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.tann.dice.Main;
import com.tann.dice.screens.dungeon.DungeonScreen;

public class InputBlocker extends Actor{

    public static final float DARK = .7f;

    Runnable r;
    public void setAction(Runnable r){
        this.r=r;
    }

    public InputBlocker(){
        setAction(
            new Runnable() {
                @Override
                public void run() {
                    if(blockerListen) {
                        Main.getCurrentScrren().pop();
                    }

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

    private float alpha = 0;
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Colours.withAlpha(Colours.z_black, alpha));
        Draw.fillActor(batch, this);
    }

    boolean blockerListen;
    public void setActiveClicker(boolean blockerListen) {
        this.blockerListen = blockerListen;
    }


}
