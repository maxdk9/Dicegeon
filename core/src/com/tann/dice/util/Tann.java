package com.tann.dice.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.List;

public class Tann {

    static Vector2 tmp = new Vector2();
    public static Vector2 getLocalCoordinates(Actor a){
        tmp.set(0,0);
        return a.localToStageCoordinates(tmp);
    }

    public static <E> Array<E> pickNRandomElements(Array<E> list, int n){
        if(n>list.size) throw new ArrayIndexOutOfBoundsException();
        Array<E> result = new Array<>(list);
        result.shuffle();
        while(result.size>n){
            result.removeIndex(0);
        }
        return result;
    }

}
