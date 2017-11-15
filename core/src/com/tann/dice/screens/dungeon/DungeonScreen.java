package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.villager.DiceEntity;
import com.tann.dice.gameplay.village.villager.Hero;
import com.tann.dice.gameplay.village.villager.Monster;
import com.tann.dice.gameplay.village.villager.Monster.MonsterType;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.dungeon.panels.BottomPanel;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

public class DungeonScreen extends Screen {

    private static DungeonScreen self;

    public static DungeonScreen get() {
        if (self == null) {
            self = new DungeonScreen();
        }
        return self;
    }

    Array<Die> dice = new Array<>();
    Array<DiceEntity> all = new Array<>();
    Array<Hero> heroes = new Array<>();
    Array<Monster> monsters = new Array<>();
    int rerolls = 2;

    public DungeonScreen() {

        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Rogue));
            heroes.add(new Hero(Hero.HeroType.Rogue));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            for(int j=0;j<5;j++){
                monsters.add(new Monster(MonsterType.Goblin));
            }
        }
        all.addAll(heroes);
        all.addAll(monsters);
        BulletStuff.refresh(all);
        for (DiceEntity v : all) {
            if(v.dead) continue;
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
        Draw.fillRectangle(batch, 0,0,Main.width, Main.height);
        batch.setColor(Colours.bg);
        Draw.fillActor(batch, this);
        batch.setColor(Colours.brown_dark);
        drawRectThing(batch, BulletStuff.playerArea);
        batch.setColor(Colours.brown_dark);
//        drawRectThing(batch, BulletStuff.enemyArea);


    }

    public void drawRectThing(Batch batch, Rectangle rect) {
        float factor = Main.height / BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x * factor, Main.height - rect.y * factor - rect.height * factor, rect.width * factor, rect.height * factor);
    }

    @Override
    public void postDraw(Batch batch) {

        BulletStuff.render();
        if (BulletStuff.dicePos != null) {
            batch.setColor(Colours.light);
            Draw.drawLine(batch, Gdx.input.getX(), Main.height - Gdx.input.getY(), BulletStuff.dicePos.x, BulletStuff.dicePos.y, 8);
        }


//        Fonts.draw(batch, "Rerolls left: "+rerolls, Fonts.fontSmall, Colours.light, 50, Main.height*.62f, 500, 500, Align.center);
    }

    @Override
    public void preTick(float delta) {
    }

    @Override
    public void postTick(float delta) {
    }

    @Override
    public void keyPress(int keycode) {
        if(rerolls > 0) {
            rerolls--;
            for (Hero h : heroes) {
                h.getDie().roll(true);
            }
        }
    }

    @Override
    public void layout() {

    }

    public void touchUp() {
        if (BulletStuff.dicePos != null) {
            for (DiceEntity de : all) {
                if (de.getEntityPanel().highlight) {
                    de.hit(BulletStuff.selectedDie.getActualSide(), true);
                    BulletStuff.selectedDie.use();
                    break;
                }
            }
        }
        BulletStuff.dicePos = null;
        boolean allUsed = true;
        for (DiceEntity de : heroes) {
            if (!de.getDie().used && !de.dead) {
                allUsed = false;
                break;
            }
        }
        if (allUsed) {
            endOfTurn();
        }
    }

    private void endOfTurn() {
        for(int i=0;i<all.size;i++){
            DiceEntity de = all.get(i);
            de.activatePotentials();
        }
        for (DiceEntity de : heroes) {
            de.getDie().used = false;
        }
        BulletStuff.clearDice();
        for (DiceEntity de : all) {
            if (!de.dead) {
                de.getDie().addToScreen();
                de.getDie().roll(false);
            }
        }
        rerolls = 2;
    }

    public void targetRandom(Eff[] effects) {
        Hero target = heroes.random();
        for (Eff e : effects) {
            target.hit(e, false);
        }
    }

    public void cancelEffects(Eff[] effects) {
        for (DiceEntity de : all) {
            de.removeEffects(effects);
        }

    }
    static float c = 0f;
    public void click(Die d) {
        d.removeFromPhysics();
        EntityPanel ep = d.entity.getEntityPanel();
        d.moveTo(Tann.getLocalCoordinates(ep).add(ep.getWidth()/8, ep.getHeight()/2));

    }
}
