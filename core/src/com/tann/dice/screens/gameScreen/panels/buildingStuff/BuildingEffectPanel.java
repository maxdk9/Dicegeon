package com.tann.dice.screens.gameScreen.panels.buildingStuff;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.screens.gameScreen.panels.eventStuff.EffectPanel;
import com.tann.dice.util.*;

public class BuildingEffectPanel extends Lay{

	public static final float WIDTH = (EffectPanel.staticWidth());
	public static final float HEIGHT = 90;

	Array<Eff> effects;
	Layoo l;
	public BuildingEffectPanel(Array<Eff> effects) {
		
		
		this.effects=effects;

		layout();
		
	}

	@Override
	public void layout() {
	    setSize(WIDTH, HEIGHT);
		l = new Layoo(this);
        for(int i=0;i<effects.size;i++){
            Eff e =effects.get(i);
            EffectPanel item = new EffectPanel(e, false);
            l.actor(item);
            if(i<effects.size-1){
                l.row(1);
            }
        }
        l.layoo();
		
	}
	

	@Override
	public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
	}
}
