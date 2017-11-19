package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.Monster.MonsterType;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.NothingPhase;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.util.*;

public class DungeonScreen extends Screen {

    private static DungeonScreen self;

    public static DungeonScreen get() {
        if (self == null) {
            self = new DungeonScreen();
            self.init();
        }
        return self;
    }

    Array<Die> dice = new Array<>();
    Array<DiceEntity> all = new Array<>();
    public Array<Hero> heroes = new Array<>();
    public Array<Monster> monsters = new Array<>();
    SidePanel friendly;
    SidePanel enemy;
    int rerolls = 2;

    private DungeonScreen() {
    }

    private void init(){
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Rogue));
            heroes.add(new Hero(Hero.HeroType.Rogue));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            for(int j=0;j<4;j++){
                monsters.add(new Monster(MonsterType.Goblin));
            }
            monsters.add(new Monster(MonsterType.Ogre));
        }
        all.addAll(heroes);
        all.addAll(monsters);
        BulletStuff.refresh(all);
        friendly = new SidePanel(true);
        friendly.addEntities(heroes);
        addActor(friendly);
        enemy = new SidePanel(false);
        enemy.addEntities(monsters);
        addActor(enemy);
//        enemyCombat();

        Main.pushPhase(new NothingPhase());
        Main.pushPhase(new EnemyRollingPhase());
        Main.popPhase();
    }

    public void enemyCombat(){
        enemy.layout(false);
        for(Monster m:monsters){
            m.getEntityPanel().slidOut = false;
        }
        for(Monster m: Tann.pickNRandomElements(monsters, Math.min(monsters.size, 2))){
            m.getEntityPanel().slideOut();
        }
        float timer = 0;
        float timerAdd = .1f;
        for (Monster m : monsters) {
            if(m.dead) continue;
            m.locked=false;
            m.getDie().resetForRoll();
            addAction(Actions.delay(timer, Actions.run(()-> addDie(m))));
        }
    }

    private void addDie(DiceEntity v){
        dice.add(v.getDie());
        v.getDie().addToScreen();
        v.getDie().roll(true);
    }

    @Override
    public void preDraw(Batch batch) {
        Draw.fillRectangle(batch, 0,0,Main.width, Main.height);
        batch.setColor(Colours.bg);
        Draw.fillActor(batch, this);
        batch.setColor(Colours.brown_dark);
        drawRectThing(batch, BulletStuff.playerArea);
        batch.setColor(Colours.brown_dark);
    }

    public void drawRectThing(Batch batch, Rectangle rect) {
        float factor = Main.height / BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x * factor, Main.height - rect.y * factor - rect.height * factor, rect.width * factor, rect.height * factor);
    }

    @Override
    public void postDraw(Batch batch) {

        BulletStuff.render();
        batch.flush();
        batch.end();
        batch.begin();
        if (BulletStuff.dicePos != null) {
            batch.setColor(Colours.light);
            Draw.drawLine(batch, Gdx.input.getX(), Main.height - Gdx.input.getY(), BulletStuff.dicePos.x, BulletStuff.dicePos.y, 8);
        }

        Fonts.draw(batch, Main.getPhase().toString(), Fonts.fontSmall, Colours.light, 50, Main.height*.62f, 500, 500, Align.center);
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
        if(Main.getPhase() instanceof PlayerRollingPhase){
            playerRoll(false);
        }

    }

    @Override
    public void layout() {

    }

    public void touchUp() {
        if (BulletStuff.dicePos != null) {
            for (DiceEntity de : all) {
                if (de.getEntityPanel().mouseOver && (de.isTargetable())) {
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
            Main.popPhase();
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

    public void cancelEffects(Eff[] effects) {
        for (DiceEntity de : all) {
            de.removeEffects(effects);
        }

    }
    public void click(Die d) {
//        d.removeFromPhysics();
//        EntityPanel ep = d.entity.getEntityPanel();
//        d.moveTo(Tann.getLocalCoordinates(ep).add(EntityPanel.gap, EntityPanel.gap));

    }

    public DiceEntity getRandomTarget() {
        return heroes.random();
    }


    public void playerRoll(boolean firstRoll) {
        if(firstRoll){
            for(Hero h:heroes){
                h.getDie().used=false;
            }
        }
        for(Hero hero:heroes){
            if(firstRoll) hero.getDie().addToScreen();
            hero.getDie().roll(firstRoll);
        }
    }

    public void activateDamage() {
        for(int i=0;i<all.size;i++){
            DiceEntity de = all.get(i);
            de.activatePotentials();
        }
    }
}
