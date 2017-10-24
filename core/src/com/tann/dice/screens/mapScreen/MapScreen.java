package com.tann.dice.screens.mapScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.util.*;

public class MapScreen extends Screen{

	private static MapScreen self;
	public static MapScreen get(){
		if(self == null){
			self = new MapScreen();
		}
		return self;
	}
	
	public MapScreen() {
        Array<Villager> villagers = new Array<>();
        for(int i=0;i<5;i++){
            villagers.add(new Villager(i));
        }

        BulletStuff.refresh(villagers);
        for(Villager v:villagers){
            v.die.addToScreen();
            v.die.roll(true);
        }
	}
	
	@Override
	public void preDraw(Batch batch) {
		batch.setColor(Colours.blue_dark);
		Draw.fillActor(batch, this);

	}

	@Override
	public void postDraw(Batch batch) {
	}

	@Override
	public void preTick(float delta) {
	}

	@Override
	public void postTick(float delta) {
	}

	@Override
	public void keyPress(int keycode) {
	}

    @Override
    public void layout() {

    }
}
