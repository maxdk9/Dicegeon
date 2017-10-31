package com.tann.dice.screens.mapScreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.gameplay.village.villager.Hero;
import com.tann.dice.gameplay.village.villager.Monster;
import com.tann.dice.gameplay.village.villager.Monster.MonsterType;
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
		Array<DiceEntity> entities = new Array<>();

        for(int i=0;i<1;i++) {
					entities.add(new Hero(Hero.HeroType.Apprentice));
					entities.add(new Hero(Hero.HeroType.Rogue));
					entities.add(new Hero(Hero.HeroType.Fighter));
					entities.add(new Hero(Hero.HeroType.Defender));
					entities.add(new Hero(Hero.HeroType.Herbalist));
					entities.add(new Monster(MonsterType.Goblin));
					entities.add(new Monster(MonsterType.Goblin));
					entities.add(new Monster(MonsterType.Goblin));
					entities.add(new Monster(MonsterType.Goblin));
        }

        BulletStuff.refresh(entities);
        for(DiceEntity v:entities){
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
        drawRectThing(batch, BulletStuff.playerArea);
		batch.setColor(Colours.red);
        drawRectThing(batch, BulletStuff.enemyArea);

	}

	public void drawRectThing(Batch batch, Rectangle rect){
	    float factor = Main.height/BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x*factor, Main.height-rect.y*factor-rect.height*factor, rect.width*factor, rect.height*factor);
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
