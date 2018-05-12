package com.tann.dice.util;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor3d extends Actor {

    public static List<Actor3d> actor3dList = new ArrayList<>();
    public static void resetAllStatics() {
        actor3dList = new ArrayList<>();
    }

    public abstract void draw3D();

    public Actor3d() {
        super();
        actor3dList.add(this);
    }

    @Override
    public boolean remove() {
        actor3dList.remove(this);
        return super.remove();
    }

}
