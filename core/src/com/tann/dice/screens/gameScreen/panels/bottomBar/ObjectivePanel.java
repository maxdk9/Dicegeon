package com.tann.dice.screens.gameScreen.panels.bottomBar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.island.objective.*;
import com.tann.dice.util.*;

public class ObjectivePanel extends BottomBarPanel{

	Array<Objective> objectives = new Array<>();
	public ObjectivePanel() {
        setColor(Colours.brown_dark);
        layout();
	}

    @Override
    public void layout() {
        setSize(BottomBar.width(), BottomBar.height());
        refresh();
    }

    public void addObject(Objective obj){
        obj.init();
	    objectives.add(obj);
	    refresh();
	    somethingAdded();
    }

    public void refresh(){
	    clearChildren();
	    Layoo parentLay = new Layoo(this);
	    parentLay.gap(1);
	    for(int i=0;i<objectives.size;i++){
            Objective o = objectives.get(i);
            BasicLay bl = new BasicLay(){
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    Draw.fillActor(batch, this, Colours.dark, Colours.brown_light, Main.h(.6f));
                    super.draw(batch, parentAlpha);
                }
            };
            bl.setSize(BottomBar.width()/3, BottomBar.height()*.8f);
            Layoo l = new Layoo(bl);
            TextBox objText = new TextBox(o.getTitleString(), Fonts.fontSmall, getWidth(), Align.center);
            TextBox progress = new TextBox(o.getProgressString(), Fonts.fontSmall, getWidth(), Align.center);
            l.row(1);
            l.actor(objText);
            l.row(1);
            l.actor(progress);
            l.row(1);
            l.layoo();
            parentLay.actor(bl);
            parentLay.gap(1);

        }
		parentLay.layoo();
        toFront();
	}

    @Override
    public void reset() {
        objectives.clear();
    }

    @Override
    public String getName() {
        return "objective";
    }

    public void objectiveProgress(Objective.ObjectiveEffect type, int i) {
        for(Objective o:objectives){
            o.objectiveProgress(type, i);
        }
    }
    public enum ObjectiveOutcome{Success, Fail, Nothing}
    public ObjectiveOutcome objectivesCompletes() {
        if(objectives.size==0){
            return ObjectiveOutcome.Nothing;
        }
        boolean complete = true;
        for(Objective o:objectives){
            if(o.isComplete() && o.isDeath()){
                return ObjectiveOutcome.Fail;
            }
            if(!o.isComplete() && !o.isDeath()){
                complete = false;
            }
        }
        return complete? ObjectiveOutcome.Success: ObjectiveOutcome.Nothing;
    }


}
