package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
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
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.LevelEndPhase;
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
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.Button;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Screen;
import com.tann.dice.util.TannFont;
import com.tann.dice.util.TextWriter;

import java.util.*;

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
        nextLevel();
        PartyManagementPanel.get();
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

    static final List<List<MonsterType>> levels = new ArrayList<>();
    private static void addLevel(MonsterType... monsterTypes){
        levels.add(Arrays.asList(monsterTypes));
    }
    static{
        addLevel(MonsterType.goblin, MonsterType.goblin, MonsterType.goblin, MonsterType.goblin);
        addLevel(MonsterType.goblin, MonsterType.archer, MonsterType.goblin, MonsterType.archer, MonsterType.goblin);
        addLevel(MonsterType.snake, MonsterType.snake, MonsterType.goblin, MonsterType.goblin);
        addLevel(MonsterType.spikeBat, MonsterType.snake, MonsterType.spikeBat);
        addLevel(MonsterType.archer, MonsterType.bird, MonsterType.bird, MonsterType.archer);
        addLevel(MonsterType.bird, MonsterType.bird, MonsterType.bird);
        addLevel(MonsterType.archer, MonsterType.archer, MonsterType.goblin, MonsterType.goblin, MonsterType.goblin, MonsterType.archer, MonsterType.archer);
        addLevel(MonsterType.snake, MonsterType.snake, MonsterType.snake, MonsterType.snake, MonsterType.snake);
        addLevel(MonsterType.bird, MonsterType.spikeBat, MonsterType.goblin, MonsterType.spikeBat, MonsterType.bird);
        addLevel(MonsterType.goblin, MonsterType.dragon, MonsterType.goblin);
    }

    public void nextLevel() {
        spellButt.removeAllHovers();
        Explanel.get().remove();
        setupLevelString();
        Party.get().rejig();
        spellButt.setSpellHolder(spellHolder);
        Party.get().reset();
        spellHolder.setup(Party.get().getSpells());
        PhaseManager.get().clearPhases();

        if(level<levels.size()) {
            setup(MonsterType.monsterList(levels.get(level)));
            level ++;
        }
        else {
            PhaseManager.get().pushPhase(new VictoryPhase());
            PhaseManager.get().kickstartPhase(VictoryPhase.class);
        }

        if(level>1){
            PhaseManager.get().pushPhase(new LevelEndPhase());
        }
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
        PhaseManager.get().kickstartPhase();
    }

    private void setupLevelString() {
        levelString = "Level "+level+"/5";
    }

    public void restart() {
        level = 0;
        setupLevelString();
        Party.get().fullyReset();
        nextLevel();
    }

    @Override
    public void drawBackground(Batch batch){
        batch.setColor(Colours.dark);
        Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Colours.z_white);
        batch.draw(Images.background, getX(),getY());
        for(DiceEntity de: EntityGroup.getAllActive()){
            de.getEntityPanel().drawBackground(batch);
        }
    }

    private void confirmDice(boolean force) {
        if(PhaseManager.get().getPhase() instanceof PlayerRollingPhase) {
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
                PhaseManager.get().popPhase(PlayerRollingPhase.class);
            }
        }
        else if (PhaseManager.get().getPhase() instanceof TargetingPhase){
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
            PhaseManager.get().popPhase(TargetingPhase.class);
        }
    }

    public void showDialog(String s) {
        showDialog(s, false);
    }

    public void showDialog(String s, boolean popPhase) {
        TextWriter tw = new TextWriter(s, Integer.MAX_VALUE, Colours.purple, 2);
        DungeonScreen.get().push(tw, true, true, true, popPhase? PhaseManager.popPhaseRunnable:null);
    }

    public void enemyCombat(){
        Room.get().updateSlids(false);
        List<DiceEntity> monsters = Room.get().getActiveEntities();
        float timer = 0;
        float timerAdd = .00f;
        for (final DiceEntity de : monsters) {
            final Monster m = (Monster) de;
            if(m.isDead()) continue;
            m.locked=false;
            timer += timerAdd;
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

    }

    @Override
    public void layout() {

    }

    private boolean checkAllDiceUsed(){
        for (DiceEntity de : Party.get().getActiveEntities()) {
            Die d = de.getDie();
            Eff first = d.getActualSide().getEffects()[0];
            if (d.getUsed()) continue;
            if (!first.needsUsing()) continue;
            if (!first.isTargeted()) return false;
            if (EntityGroup.getValidTargets(d).size()>0) return false;
        }
        return true;
    }

    public boolean checkEnd() {
        if(checkDead(Room.get().getActiveEntities(), true)){
            nextLevel();
            return true;
        }
        else if(checkDead(Party.get().getActiveEntities(), false)){
            PhaseManager.get().clearPhases();
            PhaseManager.get().pushPhase(new LossPhase());
            PhaseManager.get().kickstartPhase(LossPhase.class);
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
        entity.getEntityPanel().setArrowIntenity(1, 0);
    }

    public void showLevelupPanel(Hero hero, List<HeroType> options) {
        LevelUpPanel lup = new LevelUpPanel(hero);
        lup.setPosition(getWidth()/2, getHeight()/2f, Align.center);
        addActor(lup);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        PhaseManager.get().getPhase().checkIfDone();
    }

    public void layoutSidePanels() {
        enemy.layout(true);
        friendly.layout(true);
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
