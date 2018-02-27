package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.HeroType;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.MonsterType;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.LevelUpPhase;
import com.tann.dice.gameplay.phase.LossPhase;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.gameplay.phase.TargetingPhase;
import com.tann.dice.gameplay.phase.VictoryPhase;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.LevelUpPanel;
import com.tann.dice.screens.dungeon.panels.SidePanel;
import com.tann.dice.screens.dungeon.panels.SpellButt;
import com.tann.dice.screens.dungeon.panels.SpellHolder;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.InputBlocker;
import com.tann.dice.util.OnPop;
import com.tann.dice.util.Screen;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TannFont;
import com.tann.dice.util.TextButton;
import com.tann.dice.util.TextWriter;
import java.util.ArrayList;
import java.util.List;

public class DungeonScreen extends Screen {

    private static DungeonScreen self;
    public static DungeonScreen get() {
        if (self == null) {
            self = new DungeonScreen();
            self.init();
        }
        return self;
    }

    public static final float BOTTOM_BUTTON_HEIGHT = 25;
    public static final float BOTTOM_BUTTON_WIDTH = 78;
    public static final float BUTT_GAP = 2;
    public SidePanel friendly;
    public SidePanel enemy;
    public SpellHolder spellHolder;

    private DungeonScreen() {
    }

    Button rollButton;
    Button confirmButton;
    public SpellButt spellButt;
    private void init(){

        spellHolder = new SpellHolder();
        enemy = new SidePanel(false);
        addActor(enemy);
        friendly = new SidePanel(true);
        addActor(friendly);
        rollButton = new Button(BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT, 1, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        if(Party.get().getRolls()>0){
                            Party.get().roll(false);
                            spellButt.hide();
                        }
                    }
                }){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this, Colours.dark, Colours.grey, 1);
                batch.setColor(Colours.light);
                TannFont.font.drawString(batch, Party.get().getRolls()+"/"+Party.get().getMaxRolls(), (int)(this.getX()+this.getWidth()/3), (int)(this.getY()+this.getHeight()/2), Align.center);
                batch.setColor(Colours.z_white);
                batch.draw(Images.roll, (int)(this.getX()+this.getWidth()/3*2-Images.roll.getRegionWidth()/2), (int)(this.getY() + this.getHeight()/2 - Images.roll.getRegionHeight()/2));
            }
        };
        addActor(rollButton);
        rollButton.setPosition(-500, 0);
        slideRollButton(false);
        confirmButton = new Button(BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT, 1, Images.tick, Colours.dark){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this, Colours.dark, Colours.grey, 1);
                batch.setColor(Colours.light);
                batch.draw(Images.tick, (int)(this.getX()+this.getWidth()/2-Images.tick.getRegionWidth()/2), (int)(this.getY() + this.getHeight()/2 - Images.tick.getRegionHeight()/2));
            }
        };
        confirmButton.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        confirmDice(true);
                    }
                });
        confirmButton.setColor(Colours.yellow);
        addActor(confirmButton);
        confirmButton.setPosition(getWidth(), 0);
        slideConfirmButton(false);

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!event.isHandled()) bottomClick();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        spellButt = new SpellButt();
        addActor(spellButt);
        float gap = 12;
        spellButt.setPosition(SidePanel.width + friendly.getX() + gap,Main.height-spellButt.getHeight()-gap);
    }

    public int level=0;
    String levelString = "";

    public void setup(List<Monster> monsters){
        Room.get().reset();
        Room.get().setEntities(monsters);
        spellButt.hide();
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getAllActive());
        enemy.setEntities(monsters);
    }

    public void resetHeroes(){
        List<Hero> heroes = new ArrayList<>();

        Hero m = HeroType.pyro.buildHero();
        m.setColour(Colours.blue);
        heroes.add(m);
        Hero h = HeroType.herbalist.buildHero();
        h.setColour(Colours.red);
        heroes.add(h);
        Hero d = HeroType.defender.buildHero();
        d.setColour(Colours.grey);
        heroes.add(d);
        Hero f1 = HeroType.fighter.buildHero();
        f1.setColour(Colours.orange);
        heroes.add(f1);
        Hero f2 = HeroType.rogue.buildHero();
        f2.setColour(Colours.yellow);
        heroes.add(f2);

        friendly.setEntities(heroes);
        Party.get().setEntities(heroes);
    }

    public void nextLevel() {
        spellButt.removeAllHovers();
        Explanel.get().remove();
        level ++;
        setupLevelString();
        Party.get().rejig();
        switch(level){
            case 1:
                setup(MonsterType.monsterList(MonsterType.goblin, MonsterType.goblin, MonsterType.goblin, MonsterType.goblin));
                break;
            case 2:
                setup(MonsterType.monsterList(MonsterType.goblin, MonsterType.archer, MonsterType.goblin, MonsterType.goblin, MonsterType.archer, MonsterType.goblin));
                break;
            case 3:
                setup(MonsterType.monsterList(MonsterType.goblin, MonsterType.bird, MonsterType.archer, MonsterType.goblin));
                break;
            case 4:
                setup(MonsterType.monsterList(MonsterType.bird, MonsterType.bird, MonsterType.bird));
                break;
            case 5:
                setup(MonsterType.monsterList(MonsterType.goblin, MonsterType.dragon, MonsterType.goblin));
                break;
            case 6:
                Main.clearPhases();
                Main.pushPhase(new VictoryPhase());
                Main.kickstartPhase(VictoryPhase.class);
                return;
        }
        spellHolder.setup(Party.get().getSpells());
        spellButt.setSpellHolder(spellHolder);
        Party.get().reset();
        Main.clearPhases();
        if(level>1){
            Main.pushPhase(new LevelUpPhase());
        }
        Main.pushPhase(new EnemyRollingPhase());
        Main.kickstartPhase();
    }

    private void setupLevelString() {
        levelString = "Level "+level+"/5";
    }

    public void restart() {
        level = 0;
        setupLevelString();
        resetHeroes();
        nextLevel();
    }

    public void drawBackground(Batch batch){
        batch.setColor(Colours.dark);
        Draw.fillRectangle(batch, 0, 0, getWidth(), getHeight());
        batch.setColor(Colours.z_white);
        batch.draw(Images.background, 0,0);
        for(DiceEntity de: EntityGroup.getAllActive()){
            de.getEntityPanel().drawBackground(batch);
        }
    }

    private void confirmDice(boolean force) {
        if(Main.getPhase() instanceof PlayerRollingPhase) {
            boolean allGood = true;
            for (DiceEntity h : Party.get().getActiveEntities()) {
                Die d = h.getDie();
                if (d.getSide() == -1) {
                    allGood = false;
                } else if (d.getState() != DieState.Locked && d.getState() != DieState.Locking) {
                    d.slideToPanel();
                }
            }
            if (allGood) {
                Main.popPhase(PlayerRollingPhase.class);
            }
        }
        else if (Main.getPhase() instanceof TargetingPhase){
            if(Party.get().getAvaliableMagic() > 0){
                if(force) {
                    showDialog("Spend all your magic first!");
                }
                return;
            }
            if(!checkAllDiceUsed()){
                if(force) {
                    showDialog("Use all your dice first!");
                }
                return;
            }
            Main.popPhase(TargetingPhase.class);
        }
    }

    private void showDialog(String s) {
        TextWriter tw = new TextWriter(s, Integer.MAX_VALUE, Colours.purple, 2);
        push(tw, true, true, true, false, false);
    }

    public void enemyCombat(){
        Room.get().updateSlids(false);
        List<DiceEntity> monsters = Room.get().getActiveEntities();
        float timer = 0;
        float timerAdd = .1f;
        for (final DiceEntity de : monsters) {
            final Monster m = (Monster) de;
            if(m.isDead()) continue;
            m.locked=false;
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
        v.getDie().resetForRoll();
        v.getDie().roll();
    }

    @Override
    public void preDraw(Batch batch) {

    }

    public void drawRectThing(Batch batch, Rectangle rect) {
        float factor = Main.height / BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x * factor, Main.height - rect.y * factor - rect.height * factor, rect.width * factor, rect.height * factor);
    }

    @Override
    public void postDraw(Batch batch) {
        batch.setColor(Colours.light);
        TannFont.font.drawString(batch, levelString,Main.width/2-10, Main.height- TannFont.font.getHeight()-1);
        if(false){
            Draw.fillEllipse(batch, (float) (getWidth()/2+Math.sin(Main.ticks*5)*getWidth()/2), 50, 20, 20);
        }
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
        Room.get().updateSlids(false);
    }

    @Override
    public void layout() {

    }



    private boolean checkAllDiceUsed(){
        for (DiceEntity de : Party.get().getActiveEntities()) {
            Die d = de.getDie();
            Eff.TargetingType tt = d.getActualSide().effects[0].targetingType;
            if (!d.getUsed() && tt != Eff.TargetingType.OnRoll && tt != Eff.TargetingType.DoesNothing) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEnd() {
        if(checkDead(Room.get().getActiveEntities(), true)){
            nextLevel();
            return true;
        }
        else if(checkDead(Party.get().getActiveEntities(), false)){
            Main.clearPhases();
            Main.pushPhase(new LossPhase());
            Main.kickstartPhase(LossPhase.class);
            return true;
        }
        return false;
    }

    private boolean checkDead(List<DiceEntity> entities, boolean testGoingToDie) {
        for(DiceEntity de:entities){
            if(testGoingToDie){
                if(!de.getProfile().isGoingToDie()){
                    return false;
                }
            }
            else if(!de.isDead()){
                return false;
            }
        }
        return true;
    }

    public List<DiceEntity> getRandomTargetForEnemy(Side side) {
        Eff e = side.effects[0];
        DiceEntity target = null;
        List<DiceEntity> validTargets = EntityGroup.getValidTargets(e.targetingType, side.effects, false);
        if(validTargets.size()> 0){
            target = Tann.getRandom(validTargets);
        }
        return EntityGroup.getActualTargets(e, false, target);
    }

    public void positionExplanel() {
        Explanel.get().setPosition(Explanel.get().getNiceX(true), Explanel.get().getNiceY());
        addActor(Explanel.get());
    }

    public void removeLeftoverDice() {
        for(DiceEntity h:Party.get().getActiveEntities()){
            if(!h.getDie().getUsed()){
                h.getDie().use();
            }
        }
    }

    public void showDiePanel(DiceEntity entity){
        DiePanel pan = entity.getDiePanel();
        push(pan);
        pan.setPosition(pan.getNiceX(false), pan.getNiceY());
        if(entity.getTarget() != null) {
            for (DiceEntity de : entity.getTarget()) {
                de.getEntityPanel().setTargeted(true);
            }
        }
    }

    public void push(final Actor a, boolean center, boolean listener, boolean blockerListen, final boolean remove, final boolean endPhase){
        addActor(InputBlocker.get());
        InputBlocker.get().toFront();
        InputBlocker.get().setActiveClicker(blockerListen);
        modalStack.add(a);
        addActor(a);
        if(center){
            a.setPosition((int)(getWidth()/2-a.getWidth()/2), (int)(getHeight()/2-a.getHeight()/2));
        }
        if(listener){
            a.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(remove) {
                        a.remove();
                        pop();
                    }
                    if(endPhase) {
                        Main.popPhase();
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }

    public void push(Actor a){
        push(a, false, false, true,  false, false);
    }

    public void pop(){
        TargetingManager.get().deselectTargetable();
        if(modalStack.size()==0) return;
        Actor a =modalStack.remove(modalStack.size()-1);
        a.remove();
        if(a instanceof OnPop){
            ((OnPop) a).onPop();
        }
        InputBlocker.get().remove();
        if(modalStack.size()>0){
            addActor(InputBlocker.get());
            modalStack.get(modalStack.size()-1).toFront();
        }
    }

    List<Actor> modalStack = new ArrayList<>();

    public void showLevelupPanel(Hero hero, List<HeroType> options) {
        LevelUpPanel lup = new LevelUpPanel(hero, options);
        lup.setPosition(getWidth()/2, getHeight()/2f, Align.center);
        addActor(lup);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void layoutSidePanels() {
        enemy.layout(true);
        friendly.layout(true);
    }

    public void showExceptionPopup(final String ex) {
        Group a = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this, Colours.dark, Colours.light, 1);
                super.draw(batch, parentAlpha);
            }
        };
        TextWriter tw = new TextWriter("Crashed last time![n]Copy log to clipboard?[n](for emailing to tann@tann.space)");
        TextButton yes = new TextButton("Yes", 5);
        yes.setRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.getClipboard().setContents(ex);
                pop();
            }
        });
        TextButton no = new TextButton("No", 5);
        no.setRunnable(new Runnable() {
            @Override
            public void run() {
                pop();
            }
        });
        a.setSize(130, 38);
        tw.setPosition((int)(a.getWidth()/2-tw.getWidth()/2), (int)(a.getHeight()-tw.getHeight()-2));
        a.addActor(tw);
        a.addActor(yes);
        yes.setPosition((int)(a.getWidth()/3-yes.getWidth()/2), 2);
        a.addActor(no);
        no.setPosition((int)(a.getWidth()/3*2-yes.getWidth()/2), 2);
        a.setPosition((int)(getWidth()/2-a.getWidth()/2), (int)(getHeight()/2-a.getHeight()/2));
        push(a);
    }

    private void bottomClick() {
        TargetingManager.get().deselectTargetable();
    }

    public void slideRollButton(boolean in){
        rollButton.addAction(Actions.moveTo(in?BUTT_GAP:-rollButton.getWidth(), BUTT_GAP, .3f, Interpolation.pow2Out));
    }

    public void slideConfirmButton(boolean in){
        confirmButton.addAction(Actions.moveTo(in?getWidth()-confirmButton.getWidth()-BUTT_GAP: getWidth(), BUTT_GAP, .3f, Interpolation.pow2Out));
    }

    public void setConfirmText(String s) {
//        confirmButton.setText(s);
    }

    public void checkDoneTargeting() {
        if(!checkEnd()){
            confirmDice(false);
        }
    }
}
