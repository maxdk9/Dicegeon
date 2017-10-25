package com.tann.dice.screens.mapScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.util.*;

public class MapScreen extends Screen{

	private static MapScreen self;
	public static MapScreen get(){
		if(self == null){
			self = new MapScreen();
		}
		return self;
	}

	Array<Die> dice = new Array<>();

	public MapScreen() {
        Array<Villager> villagers = new Array<>();
        villagers.add(new Villager(0, Villager.VillagerType.Apprentice));
        villagers.add(new Villager(1, Villager.VillagerType.Rogue));
        villagers.add(new Villager(2, Villager.VillagerType.Fighter));
        villagers.add(new Villager(3, Villager.VillagerType.Defender));
        villagers.add(new Villager(4, Villager.VillagerType.Herbalist));


        BulletStuff.refresh(villagers);
        for(Villager v:villagers){
            dice.add(v.die);
            v.die.addToScreen();
            v.die.roll(true);
        }
	}
	
	@Override
	public void preDraw(Batch batch) {
		batch.setColor(Colours.bg);
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
	    for(Die d: dice){
	        d.roll(true);
        }
	}

    @Override
    public void layout() {

    }
}
