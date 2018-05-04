package com.tann.dice.util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tann.dice.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tann {

    private static final Vector2 tmp = new Vector2();
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
        Main.getCurrentScreen().addAction(Actions.delay(delay, Actions.run(runnable)));
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

    public static void center(Actor child) {
        Actor parent = child.getParent();
        child.setPosition(
            (int)(parent.getWidth()/2-child.getWidth()/2),
            (int)(parent.getHeight()/2-child.getHeight()/2)
        );
    }

    public static void swap(Object[] array, int i1, int i2){
        Object o = array[i1];
        array[i1]=array[i2];
        array[i2]=o;
    }

    public enum TannPosition{
        Left, Right, Top, Bot
    }

    private static Vector2 getPos(Actor a, TannPosition tannPosition){
        switch (tannPosition){
            case Left:
                tmp.set(-a.getWidth(), a.getY());
                break;
            case Right:
                tmp.set(Main.width, a.getY());
                break;
            case Top:
                tmp.set(a.getX(), Main.height);
                break;
            case Bot:
                tmp.set(a.getX(), -a.getHeight());
                break;
        }
        return tmp;
    }

    public static void slideAway(Actor a, TannPosition tannPosition) {
        a.clearActions();
        Vector2 pos = getPos(a, tannPosition);
        a.addAction(Actions.sequence(
            Actions.moveTo(pos.x,pos.y,Chrono.d, Chrono.i),
            Actions.removeActor()
        ));
    }

    public static void slideIn(Actor a, Group parent, TannPosition tannPosition, int distance) {
        parent.addActor(a);
        a.clearActions();
        Vector2 pos = getPos(a, tannPosition);
        a.setPosition(pos.x, pos.y);
        switch(tannPosition){
            case Left:
                pos.x += distance+a.getWidth();
                break;
            case Right:
                pos.x -= distance+a.getWidth();
                break;
            case Top:
                pos.y -= distance+a.getHeight();
                break;
            case Bot:
                pos.y += distance+a.getHeight();
                break;

        }
        a.addAction(Actions.moveTo(pos.x, pos.y, Chrono.d, Chrono.i));
    }

}
