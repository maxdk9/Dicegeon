package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.screens.map.MapScreen;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static <T> T getRandom(T[] array){
        return array[(int) (Math.random()*array.length)];
    }

    public static <T> T getRandom(List<T> list){
        return list.get((int)(Math.random()*list.size()));
    }

    public static float dist(float x, float y, float x1, float y1) {
        float xDiff = x1-x;
        float yDiff = y1-y;
        return (float) Math.sqrt(xDiff*xDiff + yDiff*yDiff);
    }

    public static int between (float a, float b){
        return (int)(a + (b-a)/2f);
    }

    public static float randomBetween (float a, float b){
        return (float) (a+Math.random()*(b-a));
    }

    public static void delay(Runnable runnable, float delay) {
        DungeonScreen.get().addAction(Actions.delay(delay, Actions.run(runnable)));
    }

    public static boolean inArray(Object object, Object[] array){
        return Arrays.asList(array).contains(object);
    }

    public static boolean anySharedItems(Object[] array1, Object[] array2){
        List<Object> listOne = Arrays.asList(array1);
        for(Object o:array2){
            if(listOne.contains(o)) return true;
        }
        return false;
    }

    public static List<TextureAtlas.AtlasRegion> getRegionsStartingWith(String prefix){
        List<AtlasRegion> results = new ArrayList<>();
        for(TextureAtlas.AtlasRegion ar:Main.atlas.getRegions()){
            if(ar.name.startsWith(prefix)){
                results.add(ar);
            }
        }
        return results;
    }

    public static <T> List<T> iterandom(List<T> list){
        List copy = new ArrayList(list);
        Collections.shuffle(copy);
        return copy;
    }

    public static void center(Actor child, Group parent) {
        child.setPosition((int)(parent.getWidth()/2-child.getWidth()/2), (int)(parent.getHeight()/2-child.getHeight()/2));
    }
}
