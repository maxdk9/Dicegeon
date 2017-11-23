package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.Monster.MonsterType;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.NothingPhase;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.screens.dungeon.panels.BottomBar;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.util.*;

public class DungeonScreen extends Screen {

    public static DungeonScreen self;

    public static DungeonScreen get() {
        if (self == null) {
            self = new DungeonScreen();
            self.init();
        }
        return self;
    }

    public static final float BOTTOM_BUTTON_HEIGHT = Main.height*.2f;

    Array<Die> dice = new Array<>();
    Array<DiceEntity> all = new Array<>();
    public Array<Hero> heroes = new Array<>();
    public Array<Monster> monsters = new Array<>();
    SidePanel friendly;
    SidePanel enemy;
    int rerolls = 2;

    public BottomBar bottomBar;

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

        Button rollButton = new Button(SidePanel.width, BOTTOM_BUTTON_HEIGHT, .8f, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        playerRoll(false);
                       }
                });
        addActor(rollButton);
        rollButton.setPosition(0, 0);

        Button confirmButton = new Button(SidePanel.width, BOTTOM_BUTTON_HEIGHT, .8f, Images.tick, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        confirmDice();
                    }
                });
        confirmButton.setColor(Colours.green_light);
        addActor(confirmButton);
        confirmButton.setPosition(Main.width-confirmButton.getWidth(), 0);

        bottomBar = new BottomBar();
        addActor(bottomBar);
        bottomBar.setPosition(SidePanel.width, 0);

        Main.pushPhase(new NothingPhase());
        Main.pushPhase(new EnemyRollingPhase());
        Main.popPhase();
    }

    private void confirmDice() {
        if(!(Main.getPhase() instanceof PlayerRollingPhase)) return;
        boolean allGood = true;
        for(Hero h:heroes){
            Die d = h.getDie();
            if(d.getSide()==-1){
                allGood=false;
            }
            else if(d.getState()!= DieState.Locked && d.getState() != DieState.Locking){
                d.slideToBottomBar();
            }
        }
        if(allGood){
            Main.popPhase();
        }
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
        for (final Monster m : monsters) {
            if(m.isDead()) continue;
            m.locked=false;
            m.getDie().resetForRoll();
            addAction(Actions.delay(timer, Actions.run(
                    new Runnable() {
                        @Override
                        public void run() {
                            addDie(m);
                        }
                    })));
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
        if (selectedDie != null && Main.getPhase().canTarget()) {
            batch.setColor(Colours.light);
            Draw.drawLine(batch, Gdx.input.getX(), Main.height - Gdx.input.getY(), selectedDiePosition.x, selectedDiePosition.y, 8);
        }

        Fonts.draw(batch, Main.getPhase().toString(), Fonts.fontSmall, Colours.light, 0, Main.height*.57f, Main.width, 500, Align.center);
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

    public void touchUp() {
        if(!Main.getPhase().canTarget()) return;
        if (selectedDie != null) {
            if(selectedDie.getActualSide()==null){
                System.err.println("Failed to drag a die "+selectedDie+":"+selectedDie.getSide()+":"+selectedDie.getState()+":"+selectedDie.entity);
            }
            for (DiceEntity de : all) {
                if (de.getEntityPanel().mouseOver && (de.isTargetable())) {
                    de.hit(selectedDie.getActualSide(), true);
                    selectedDie.use();
                    break;
                }
            }
        }
        boolean allUsed = true;
        for (DiceEntity de : heroes) {
            if (!de.getDie().getUsed() && !de.isDead()) {
                allUsed = false;
                break;
            }
        }
        if (allUsed) {
            Main.popPhase();
        }
        selectedDie = null;
    }

    public void cancelEffects(Eff[] effects) {
        for (DiceEntity de : all) {
            de.removeEffects(effects);
        }

    }

    Die selectedDie;
    Vector2 selectedDiePosition;
    public void click(Die d) {
        if(d.entity instanceof Monster) return;
        if(Main.getPhase().canRoll()){
            d.toggleLock();
        }
        if(Main.getPhase().canTarget()){
            selectedDie = d;
            selectedDiePosition = d.getScreenPosition();
        }
    }

    public DiceEntity getRandomTarget() {
        for(int i=0;i<100;i++){
            //super lazy
            Hero h = heroes.random();
            if(!h.isDead())return h;
        }
        return null;
    }


    public void playerRoll(boolean firstRoll) {
        if(!Main.getPhase().canRoll()) return;
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
