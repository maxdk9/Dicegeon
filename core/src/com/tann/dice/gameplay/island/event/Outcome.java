package com.tann.dice.gameplay.island.event;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.effect.Cost;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.screens.gameScreen.panels.eventStuff.OutcomePanel;
import com.tann.dice.util.Prefs;

public class Outcome {
    private Array<Eff> effects = new Array<>();
    public String description;
    private Cost cost;
    public boolean pickedBeforeEver;
    public boolean fateful;
    private boolean triple;
    public boolean chosen;
    public Outcome(String description, Array<Eff> events, Cost cost) {
        this.cost=cost;
        this.effects = events;
        this.description = description;
        if(cost!=null && cost.has(Eff.EffectType.Fate)){
            fateful=true;
        }
    }

    public Array<Eff> combineEffects(){
        Array<Eff> result = new Array<>();
        if(cost!=null){
            for(Eff e:cost.effects){
                result.add(e.copy().invert());
            }
        }
        result.addAll(Eff.copyArray(effects));
        return result;
    }

    public Array<Eff> getEffects(){
        return effects;
    }

    public Cost getCost(){
        return cost;
    }

//    public void activate(){
//        if(cost!=null) Village.get().activate(cost.effects, true, true);
//        Array<Eff> copied = Eff.copyArray(effects);
//        Event.process(copied);
//        Village.get().activate(copied, true);
//    }

    OutcomePanel ocp;
    public OutcomePanel makePanel(){
        this.pickedBeforeEver = Prefs.getBoolean(getPrefKey(), false);
        return new OutcomePanel(this, false);
    }

    public void reset(){
        ocp=null;
        this.chosen=false;
    }

    public boolean isValid() {
        if(cost!=null && !Village.getInventory().checkCost(cost)) return false;
        for(Eff e:effects){
            if(!Village.getInventory().isEffectValidAllowOvershoot(e)){
                return false;
            }
        }
        return true;
    }

    public String getPrefKey(){
        return "outcome: "+description;
    }

    public void pick() {
        pickedBeforeEver = true;
        Prefs.setBoolean(getPrefKey(), true);
    }

    public void setTriple(){
        this.triple=true;
    }
}
