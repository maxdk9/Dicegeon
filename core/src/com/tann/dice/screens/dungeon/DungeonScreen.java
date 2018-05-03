package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.gameplay.phase.*;
import com.tann.dice.screens.EscMenu;
import com.tann.dice.screens.dungeon.panels.EntityContainer;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.Explanel.Explanel;
import com.tann.dice.screens.dungeon.panels.ExplanelReposition;
import com.tann.dice.screens.dungeon.panels.SpellButt;
import com.tann.dice.screens.dungeon.panels.SpellHolder;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.screens.generalPanels.PartyManagementPanel;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tann.dice.gameplay.entity.type.MonsterType.*;

public class DungeonScreen extends Screen implements ExplanelReposition{

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
    public EntityContainer friendly;
    public EntityContainer enemy;
    public SpellHolder spellHolder;

    private DungeonScreen() {
    }

    Button rollButton;
    Button confirmButton;
    public SpellButt spellButt;
    private TextWriter turnPhaseWriter;

    private void init() {
        turnPhaseWriter = new TextWriter("", 990, Colours.purple, 2);
        addActor(turnPhaseWriter);
        spellHolder = new SpellHolder();
        enemy = new EntityContainer(false);
        addActor(enemy);
        friendly = new EntityContainer(true);
        addActor(friendly);
        rollButton = new Button(BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT, 1, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        if (Party.get().getRolls() > 0) {
                            Party.get().roll(false);
                            spellButt.hide();
                        }
                    }
                }) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Draw.fillActor(batch, this, Colours.dark, Colours.grey, 1);
                batch.setColor(Colours.light);
                int rolls = Party.get().getRolls();
                int maxRolls = Party.get().getMaxRolls();
                String rollText;
                if (rolls == 0) {
                    batch.setColor(Colours.red);
                    rollText = "Final Roll";
                } else {
                    batch.setColor(Colours.light);
                    rollText = rolls + "/" + maxRolls;
                }
                TannFont.font.drawString(batch, rollText, (int) (this.getX() + this.getWidth() / 3), (int) (this.getY() + this.getHeight() / 2), Align.center);
                batch.setColor(Colours.z_white);
                batch.draw(Images.roll, (int) (this.getX() + this.getWidth() / 3 * 2 - Images.roll.getRegionWidth() / 2), (int) (this.getY() + this.getHeight() / 2 - Images.roll.getRegionHeight() / 2));
            }
        };
        addActor(rollButton);
        rollButton.setPosition(-500, 0);
        slideRollButton(false);
        confirmButton = new Button(BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT, 1, Images.tick, Colours.dark) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color border = Colours.grey;
                if(PhaseManager.get().getPhase() instanceof PlayerRollingPhase && Party.get().allDiceLockedOrLocking()){
                    border = Colours.light;
                }
                Draw.fillActor(batch, this, Colours.dark, border, 1);
                batch.setColor(Colours.light);
                batch.draw(Images.tick, (int) (this.getX() + this.getWidth() / 2 - Images.tick.getRegionWidth() / 2), (int) (this.getY() + this.getHeight() / 2 - Images.tick.getRegionHeight() / 2));
            }
        };
        confirmButton.setRunnable(new Runnable() {
            @Override
            public void run() {
                popAllLight();
                confirmDice(true);
            }
        });
        confirmButton.setColor(Colours.yellow);
        addActor(confirmButton);
        confirmButton.setPosition(getWidth(), 0);
        slideConfirmButton(false);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) bottomClick();
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        int gap = 6;
        int topAvailableWidth = (int) (Main.width- EntityContainer.width*2 - friendly.getX()*2 - EntityPanel.slideAmount - gap*2);
        int topStartX = (int) (EntityContainer.width + friendly.getX()+gap);

        spellButt = new SpellButt();
        addActor(spellButt);
        spellButt.setPosition(topStartX, (int)(Main.height - spellButt.getHeight() - gap));

        ImageActor target = new ImageActor(Images.target);
        addActor(target);
        target.setPosition((int)(topStartX + topAvailableWidth - target.getWidth()), Main.height-gap-target.getHeight());
        target.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                TargetingManager.get().showAllTargetingArrows();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Party.clearTargetedHighlights();
                super.touchUp(event, x, y, pointer, button);
            }
        });


        ImageActor cog = new ImageActor(Images.cog);
        addActor(cog);
        cog.setPosition((int)(topStartX), gap);
        cog.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toggleMenu();
                return false;
            }
        });

    }

    public int level = 0;

    public void setup(List<Monster> monsters) {
        Room.get().reset();
        Room.get().setEntities(monsters);
        spellButt.hide();
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getAllActive());
        enemy.setEntities(monsters);
    }

    public static final List<List<MonsterType>> levels = new ArrayList<>();

    private static void addLevel(MonsterType... monsterTypes) {
        levels.add(Arrays.asList(monsterTypes));
    }

    static {
//        addLevel(rat, goblin, bird, dragon); // all sizes
//        addLevel(rat); // ezpz
        addLevel(goblin, goblin, goblin, goblin);
        addLevel(goblin, archer, goblin, archer, goblin);
        addLevel(goblin, goblin, bird, goblin);
        addLevel(spikeGolem, bird);
        addLevel(archer, slimoBig, goblin);
        addLevel(skeleton, summoner, zombie);
        addLevel(spikeGolem, spikeGolem, snake, snake);
        addLevel(slimoHuge);
        addLevel(bird, spikeGolem, spikeGolem, bird);
        addLevel(archer, dragon, bird);
    }

    public void nextLevel() {

//        spellButt.removeAllHovers();
        Explanel.get().remove();
        Party.get().rejig();
        spellButt.setSpellHolder(spellHolder);
        spellHolder.setup(Party.get().getSpells());
        PhaseManager.get().clearPhases();

        if (level < levels.size()) {
            setup(MonsterType.monsterList(levels.get(level)));
            level++;
        } else {
            PhaseManager.get().pushPhase(new VictoryPhase());
            PhaseManager.get().kickstartPhase(VictoryPhase.class);
            return;
        }

        if (level > 1) {
            PhaseManager.get().pushPhase(new LevelEndPhase());
            Party.get().reset();
        }
        PhaseManager.get().pushPhase(new EnemyRollingPhase());
//        PhaseManager.get().pushPhase(new LevelEndPhase());
        PhaseManager.get().kickstartPhase();

        PartyManagementPanel.get();
        Party.get().somethingChanged();
    }

    public void restart() {
        level = 0;
        Party.get().fullyReset();
        nextLevel();
    }

    @Override
    public void drawBackground(Batch batch) {
        batch.setColor(Colours.dark);
        Draw.fillRectangle(batch, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Colours.z_white);
        batch.draw(Images.background, getX(), getY());
        for (DiceEntity de : EntityGroup.getAllActive()) {
            de.getEntityPanel().drawBackground(batch);
        }
    }



    public void confirmDice(boolean force) {
        if (PhaseManager.get().getPhase() instanceof PlayerRollingPhase) {
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
                if(force) {
                    PhaseManager.get().popPhase(PlayerRollingPhase.class);
                }
            }
        } else if (PhaseManager.get().getPhase() instanceof TargetingPhase) {
            if (Party.get().getAvaliableMagic() > 0) {
                if (force) {
                    showDialog("Spend all your magic first!");
                    DungeonScreen.get().spellButt.show();
                }
                return;
            }
            if (!checkAllDiceUsed()) {
                if (force) {
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
        tw.setPosition((int) (getWidth() / 2 - tw.getWidth() / 2), (int) (getHeight() / 3 - tw.getHeight() / 2));
        DungeonScreen.get().push(tw, false, popPhase, true, true, 0, popPhase ? PhaseManager.popPhaseRunnable : null);
    }

    public void enemyCombat() {
        DungeonScreen.get().layoutSidePanels();
        Room.get().resetForRoll();
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

    }

    @Override
    public void preTick(float delta) {
    }

    @Override
    public void postTick(float delta) {
        if (enemy != null) enemy.act(delta);
    }


    @Override
    public void keyPress(int keycode) {
        switch(keycode){
            case Keys.ESCAPE:
                toggleMenu();
                break;
            case Keys.W:
                if(Main.debug) nextLevel();
                break;
        }
    }

    private void toggleMenu() {
        if(pop(EscMenu.get())){
            Sounds.playSound(Sounds.pop);
            return;
        }
        push(EscMenu.get(), true, true, true, false, .8f, null);
        Sounds.playSound(Sounds.pip);
    }

    @Override
    public void layout() {

    }

    private boolean checkAllDiceUsed() {
        for (DiceEntity de : Party.get().getActiveEntities()) {
            Die d = de.getDie();
            Eff first = d.getActualSide().getEffects()[0];
            if (d.getUsed()) continue;
            if (!first.needsUsing()) continue;
            if (!first.isTargeted()) return false;
            if (EntityGroup.getValidTargets(d, true).size() > 0) return false;
        }
        return true;
    }

    public boolean checkEnd() {
        if (checkDead(Room.get().getActiveEntities(), true)) {
            nextLevel();
            return true;
        } else if (checkDead(Party.get().getActiveEntities(), false)) {
            PhaseManager.get().clearPhases();
            PhaseManager.get().pushPhase(new LossPhase());
            PhaseManager.get().kickstartPhase(LossPhase.class);
            return true;
        }
        return false;
    }

    private boolean checkDead(List<? extends DiceEntity> entities, boolean testGoingToDie) {
        for (DiceEntity de : entities) {
            if (testGoingToDie) {
                if (!de.getProfile().isGoingToDie(false)) {
                    return false;
                }
            } else if (!de.isDead()) {
                return false;
            }
        }
        return true;
    }

    public void positionExplanel() {
        push(Explanel.get(), false, false, true, true, 0, null);
        repositionExplanel(Explanel.get());
    }

    public void removeLeftoverDice() {
        for (DiceEntity h : Party.get().getActiveEntities()) {
            if (!h.getDie().getUsed()) {
                h.getDie().use();
            }
        }
    }

    public void showDiePanel(DiceEntity entity) {
        Sounds.playSound(Sounds.pip);
        DiePanel pan = entity.getDiePanel();
        push(pan, true, false, true, true, 0, null);
        pan.setScale(0);
        EntityPanel ePan = entity.getEntityPanel();
        Vector2 coord = Tann.getLocalCoordinates(ePan);
        pan.setPosition(coord.x+ePan.getWidth()*(entity.isPlayer()?.8f:.2f), coord.y+ePan.getHeight()/2);
        float dur = .4f;
        Interpolation terp = Chrono.i;
        pan.clearActions();
        pan.addAction(Actions.parallel(
                Actions.scaleTo(1, 1, dur*.8f, terp),
                Actions.moveTo(pan.getNiceX(false), pan.getNiceY(), dur, terp)
        ));
//        pan.setPosition(pan.getNiceX(false), pan.getNiceY());
        if (entity.getTarget() != null) {
            for (DiceEntity de : entity.getTarget()) {
                de.getEntityPanel().setTargeted(true);
            }
        }
        entity.getEntityPanel().setArrowIntenity(1, 0);
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
        if((stackContains(Explanel.class) || stackContains(DiePanel.class))){
            Sounds.playSound(Sounds.pop);
        }
        popAllLight();
        spellButt.hide();
        TargetingManager.get().deselectTargetable();
    }

    public void slideRollButton(boolean in) {
        rollButton.addAction(Actions.moveTo(in ? BUTT_GAP : -rollButton.getWidth(), BUTT_GAP, Chrono.d, Chrono.i));
    }

    public void slideConfirmButton(boolean in) {
        confirmButton.addAction(Actions.moveTo(in ? getWidth() - confirmButton.getWidth() - BUTT_GAP : getWidth(), BUTT_GAP, Chrono.d, Chrono.i));
    }

    public void setConfirmText(String s) {
//        confirmButton.setText(s);
    }

    public void checkDoneTargeting() {
        if (!checkEnd()) {
            confirmDice(false);
        }
    }

    @Override
    public void activatePhase(Phase phase) {
        turnPhaseWriter.setText(phase.describe());
        turnPhaseWriter.setPosition((int) (getWidth() / 2 - turnPhaseWriter.getWidth() / 2), 7);
    }

    public void layoutEntityGroups(){
        enemy.layout(true);
        friendly.layout(true);
    }

    @Override
    public void repositionExplanel(Group g) {
        if(spellButt.shown){
            Vector2 bounds = Tann.getLocalCoordinates(spellHolder);
            g.setPosition((int)(Main.width/2-g.getWidth()/2), (int)(bounds.y-g.getHeight()-2));
        }
        else{
            g.setPosition((int)(Main.width/2-g.getWidth()/2), (int)(Main.height/2-g.getHeight()/2));
        }
    }
}