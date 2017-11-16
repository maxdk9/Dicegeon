package com.tann.dice.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class Lay extends Group {

    public Lay() {
        setTransform(false);
    }

    // on resize, call layChain of the topmost LayGroupThing
    public void layChain(){
        for(Actor a: getChildren()){
            if(a instanceof Lay){
                ((Lay)a).layChain();
            }
            else{
//                System.err.println(a.getClass()+" is not a lay");
            }
        }
        layout();
    }

    // in this method, set up the size of self and placement of children
    public abstract void layout();
}
