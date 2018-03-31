package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.debugScreen.DebugScreen;
import com.tann.dice.util.Actor3d;

public class DieSpinner extends Actor3d {
    Die d;
    public DieSpinner(Die d, float size) {
        this.d=d;
        setSize(size,size);
    }

    @Override
    public void draw3D() {


        Vector2 result = localToStageCoordinates(new Vector2());
        if(Main.getCurrentScreen() instanceof DebugScreen){
            BulletStuff.drawSpinnyDie3(d,
                    -Main.stage.getCamera().position.x*Main.scale+(result.x+getWidth()/2)* Main.scale+ Gdx.graphics.getWidth()/2,
                    -Main.stage.getCamera().position.y*Main.scale+(result.y+getHeight()/2)* Main.scale+ Gdx.graphics.getHeight()/2,
                    getWidth()*Main.scale);
        }
        else{
            BulletStuff.drawSpinnyDie3(d,
                    -Main.stage.getCamera().position.x*Main.scale+(result.x+getWidth()/2)* Main.scale,
                    -Main.stage.getCamera().position.y*Main.scale+(result.y+getHeight()/2)* Main.scale,
                    getWidth()*Main.scale);
        }

    }
}