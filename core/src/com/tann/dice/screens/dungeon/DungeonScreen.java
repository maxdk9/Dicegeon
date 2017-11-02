package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
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
import com.tann.dice.screens.dungeon.panels.BottomPanel;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class DungeonScreen extends Screen{

	private static DungeonScreen self;
	public static DungeonScreen get(){
		if(self == null){
			self = new DungeonScreen();
		}
		return self;
	}

	Array<Die> dice = new Array<>();

	public DungeonScreen() {
        Array<Hero> heroes = new Array<>();
        Array<Monster> monsters = new Array<>();
        Array<DiceEntity> all = new Array<>();
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Apprentice));
            heroes.add(new Hero(Hero.HeroType.Rogue));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Defender));
            heroes.add(new Hero(Hero.HeroType.Herbalist));
            monsters.add(new Monster(MonsterType.Goblin));
            monsters.add(new Monster(MonsterType.Goblin));
            monsters.add(new Monster(MonsterType.Goblin));
            monsters.add(new Monster(MonsterType.Goblin));
        }
        all.addAll(heroes);
        all.addAll(monsters);
        BulletStuff.refresh(all);
        for (DiceEntity v : all) {
            dice.add(v.getDie());
            v.getDie().addToScreen();
            v.getDie().roll(true);
        }
        BottomPanel friendly = new BottomPanel(true);
        friendly.addEntities(heroes);
        addActor(friendly);
        BottomPanel enemy = new BottomPanel(false);
        enemy.addEntities(monsters);
        addActor(enemy);
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
	  if(BulletStuff.dicePos!=null){
      batch.setColor(Colours.light);
      Draw.drawLine(batch, Gdx.input.getX(), Main.height-Gdx.input.getY(), BulletStuff.dicePos.x, BulletStuff.dicePos.y, 8);
    }
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

  public void touchUp() {
	  BulletStuff.dicePos = null;
  }
}
