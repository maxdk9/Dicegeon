package com.tann.dice.gameplay.effect;


import static com.tann.dice.gameplay.effect.EffAct.ActivationType.*;

public class EffAct {

    public enum ActivationType{NOW, IN_TURNS, FOR_TURNS, UPKEEP, PASSIVE}

    public static EffAct now = new EffAct(NOW, 0);
    public ActivationType type;
    public int value;
    public EffAct(ActivationType type, int value){
        this.type=type;
        this.value=value;
    }

    public EffAct(ActivationType type){
        this(type, 0);
    }

    public String toString(){
        switch(type){
            case NOW:
                return "now";
            case IN_TURNS:
                return "in "+value+" turns";
            case FOR_TURNS:
                return "for "+value+" turns";
            case UPKEEP:
                break;
            case PASSIVE:
                break;
        }
        return "";
    }

    public String toWriterString(){
        switch(type){
            case IN_TURNS:
                return "in "+value+"[turn]";
            case FOR_TURNS:
                return "/[turn]("+value+")";
        }
        return "";
    }

    public boolean equiv(EffAct other){
        return this.type==other.type && this.value==other.value;
    }

    public EffAct copy() {
        return new EffAct(type, value);
    }
}
