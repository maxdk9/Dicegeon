package com.tann.dice.screens.dungeon.panels;

import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.entity.die.Die;
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
        BulletStuff.drawSpinnyDie3(d, (result.x+getWidth()/2)* Main.scale, (result.y+getHeight()/2)* Main.scale, getWidth()*Main.scale);
    }
}