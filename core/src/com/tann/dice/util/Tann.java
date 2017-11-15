package com.tann.dice.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Tann {

    static Vector2 tmp = new Vector2();
    public static Vector2 getLocalCoordinates(Actor a){
        tmp.set(0,0);
        return a.localToStageCoordinates(tmp);
    }


}
