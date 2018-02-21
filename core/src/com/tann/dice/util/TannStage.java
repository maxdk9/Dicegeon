package com.tann.dice.util;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tann.dice.Main;

public class TannStage extends Stage {

    public TannStage(Viewport viewport) {
        super(viewport);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        try {
            return super.touchDown(screenX, screenY, pointer, button);
        }
        catch (RuntimeException e){
            Main.logException(e);
            return false;
        }
    }
}
