package com.tann.dice.screens.mapScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.gameplay.village.villager.Hero;
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
        Array<DiceEntity> heroes = new Array<>();
        heroes.add(new Hero(Hero.HeroType.Apprentice));
        heroes.add(new Hero(Hero.HeroType.Rogue));
        heroes.add(new Hero(Hero.HeroType.Fighter));
        heroes.add(new Hero(Hero.HeroType.Defender));
        heroes.add(new Hero(Hero.HeroType.Herbalist));


        BulletStuff.refresh(heroes);
        for(DiceEntity v:heroes){
            dice.add(v.getDie());
            v.getDie().addToScreen();
            v.getDie().roll(true);
        }
	}
	
	@Override
	public void preDraw(Batch batch) {
		batch.setColor(Colours.bg);
		Draw.fillActor(batch, this);
		batch.setColor(Colours.blue_dark);
		Draw.fillRectangle(batch, 50, 350, 50, 50);

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
