package com.tann.dice.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.entity.DiceEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tann {

    static Vector2 tmp = new Vector2();
    public static Vector2 getLocalCoordinates(Actor a){
        tmp.set(0,0);
        return a.localToStageCoordinates(tmp);
    }

    public static <E> List<E> pickNRandomElements(List<E> list, int n){
        if(n>list.size()) throw new ArrayIndexOutOfBoundsException();
        List<E> result = new ArrayList<>(list);
        Collections.shuffle(result);
        while(result.size()>n){
            result.remove(0);
        }
        return result;
    }

    public static <T> T getRandom(List<T> list){
        return list.get((int)(Math.random()*list.size()));
    }
}
