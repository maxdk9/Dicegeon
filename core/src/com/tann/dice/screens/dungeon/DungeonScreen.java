package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.Monster.MonsterType;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.NothingPhase;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.screens.dungeon.panels.BottomBar;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.screens.dungeon.panels.SpellHolder;
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

    Array<DiceEntity> all = new Array<>();
    public Array<Hero> heroes = new Array<>();
    public Array<Monster> monsters = new Array<>();
    private SidePanel friendly;
    private SidePanel enemy;
    int rerolls = 2;

    public BottomBar bottomBar;
    public SpellHolder spellHolder;

    private DungeonScreen() {
    }

    private void init(){
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Defender));
            heroes.add(new Hero(Hero.HeroType.Herbalist));
            heroes.add(new Hero(Hero.HeroType.Apprentice));

            for(int j=0;j<4;j++){
                monsters.add(new Monster(MonsterType.Goblin));
            }
            monsters.add(new Monster(MonsterType.Ogre));
        }
        all.addAll(heroes);
        all.addAll(monsters);
        BulletStuff.refresh(all);

        bottomBar = new BottomBar();
        addActor(bottomBar);
        bottomBar.setPosition(SidePanel.width, 0);

        enemy = new SidePanel(false);
        enemy.addEntities(monsters);
        addActor(enemy);

        addActor(new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.end();
                BulletStuff.render();
                batch.begin();
            }
        });

        spellHolder = new SpellHolder();
        spellHolder.addSpell(Spell.dart);
        spellHolder.addSpell(Spell.resist);
        spellHolder.addSpell(Spell.healAll);
        spellHolder.addSpell(Spell.fireWave);
        addActor(spellHolder);
        spellHolder.setPosition(spellHolder.getX(false), spellHolder.getY(false));

        friendly = new SidePanel(true);
        friendly.addEntities(heroes);
        addActor(friendly);

        Button rollButton = new Button(SidePanel.width, BOTTOM_BUTTON_HEIGHT, .8f, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        playerRoll(false);
                       }
                });
        addActor(rollButton);
        rollButton.setSquare();
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
        confirmButton.setSquare();
        confirmButton.setPosition(Main.width-confirmButton.getWidth(), 0);



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
        v.getDie().addToScreen();
        v.getDie().roll(true);
    }

    @Override
    public void preDraw(Batch batch) {
        Draw.fillRectangle(batch, 0,0,Main.width, Main.height);
        batch.setColor(Colours.bg);
        Draw.fillActor(batch, this);
        batch.setColor(Colours.brown_dark);
        batch.setColor(Colours.brown_dark);
    }

    public void drawRectThing(Batch batch, Rectangle rect) {
        float factor = Main.height / BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x * factor, Main.height - rect.y * factor - rect.height * factor, rect.width * factor, rect.height * factor);
    }

    @Override
    public void postDraw(Batch batch) {
        Fonts.draw(batch, Main.getPhase().toString(), Fonts.fontSmall, Colours.light, 0, Main.height-Fonts.fontSmall.getLineHeight(), Main.width, Fonts.fontSmall.getLineHeight(), Align.center);
    }

    @Override
    public void preTick(float delta) {
    }

    @Override
    public void postTick(float delta) {
        if(enemy!=null) enemy.act(delta);
    }


    @Override
    public void keyPress(int keycode) {
    }

    @Override
    public void layout() {

    }

    public void cancelEffects(Eff[] effects) {
        for (DiceEntity de : all) {
            de.removeEffects(effects);
        }

    }

    Die selectedDie;
    public void click(Die d) {
        if(d.entity instanceof Monster) return;
        if(d.getSide()==-1) return;
        if(Main.getPhase().canRoll()){
            d.toggleLock();
            return;
        }
        if(selectedDie == d){
            selectedDie = null;
            defocus();
            return;
        }
        if(Main.getPhase().canTarget()){
            selectedDie = d;
            focus(d.getActualSide());
        }
    }

    public void target(EntityPanel panel) {
        if(!Main.getPhase().canTarget()) return;
        if(selectedDie == null) return;
        if(selectedDie.getActualSide()==null){
            System.err.println("Failed to drag a die "+selectedDie+":"+selectedDie.getSide()+":"+selectedDie.getState()+":"+selectedDie.entity);
        }
        if(panel != null){
            panel.e.hit(selectedDie.getActualSide(), true);
            selectedDie.use();
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
        defocus();
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

    public void defocus(){
        Explanel.get().remove();
    }

    public void focus(Spell spell){
        Explanel.get().setup(spell);
        positionExplanel();
    }

    public void focus(Side side){
        defocus();
        Explanel.get().setup(side);
        positionExplanel();
    }

    private void positionExplanel() {
        Explanel.get().setPosition(Explanel.get().getNiceX(), BOTTOM_BUTTON_HEIGHT + (Main.height-BOTTOM_BUTTON_HEIGHT)/2-Explanel.get().getHeight()/2);
        addActor(Explanel.get());
    }

}
