package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.Hero.HeroType;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Die.DieState;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.EntityGroup;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.phase.*;
import com.tann.dice.screens.dungeon.panels.*;
import com.tann.dice.screens.dungeon.panels.Explanel.*;
import com.tann.dice.util.*;

import java.util.ArrayList;
import java.util.List;

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

    public SidePanel friendly;
    private SidePanel enemy;
    public SpellHolder spellHolder;

    private DungeonScreen() {
    }

    Button rollButton;
    TextButton confirmButton;
    private void init(){

        spellHolder = new SpellHolder();
        spellHolder.addSpell(Spell.dart);
        spellHolder.addSpell(Spell.resist);
        spellHolder.addSpell(Spell.healAll);
        spellHolder.addSpell(Spell.fireWave);
        addActor(spellHolder);
        spellHolder.setPosition(spellHolder.getX(false), spellHolder.getY(false));

        enemy = new SidePanel(false);
        addActor(enemy);

        friendly = new SidePanel(true);
        addActor(friendly);

        Actor bulletActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.end();
                BulletStuff.render();
                batch.begin();
            }
        };
        addActor(bulletActor);

        rollButton = new Button(SidePanel.width, BOTTOM_BUTTON_HEIGHT, .6f, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        if(Party.get().getRolls()>0){
                            Party.get().roll();
                        }
                    }
                }){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                Fonts.draw(batch, Party.get().getRolls()+"/"+Party.get().getMaxRolls(),
                        Fonts.fontSmall, Colours.light, this.getX(), this.getY(), this.getWidth(), this.getHeight()/5, Align.center);
            }
        };
        addActor(rollButton);
        rollButton.setSquare();
        rollButton.setPosition(-rollButton.getWidth(), 0);
        confirmButton = new TextButton(SidePanel.width, BOTTOM_BUTTON_HEIGHT, "Confirm Dice");
        confirmButton.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        confirmDice();
                    }
                });
        confirmButton.setFont(Fonts.font);
        confirmButton.setColor(Colours.green_light);
        addActor(confirmButton);
        confirmButton.setPosition(getWidth(), 0);

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!event.isHandled()) bottomClick();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void bottomClick() {
        deselectTargetable();
    }

    public void slideRollButton(boolean in){
        rollButton.addAction(Actions.moveTo(in?0:-rollButton.getWidth(), 0, .3f, Interpolation.pow2Out));
    }

    public void slideConfirmButton(boolean in){
        confirmButton.addAction(Actions.moveTo(in?getWidth()-confirmButton.getWidth(): getWidth(), 0, .3f, Interpolation.pow2Out));
    }

    public void setConfirmText(String s) {
        confirmButton.setText(s);
    }

    int level=0;

    public void nextLevel() {
        List<Monster> monsters =  new ArrayList<>();
        level ++;
        switch(level){
            case 1:
                monsters.add(new Monster(Monster.MonsterType.Ogre));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                break;
            case 2:
                monsters.add(new Monster(Monster.MonsterType.Archer));
                monsters.add(new Monster(Monster.MonsterType.Archer));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                break;
            case 3:
                monsters.add(new Monster(Monster.MonsterType.Serpent));
                monsters.add(new Monster(Monster.MonsterType.Serpent));
                break;
            case 4:
                monsters.add(new Monster(Monster.MonsterType.Dragon));
                monsters.add(new Monster(Monster.MonsterType.Archer));
                monsters.add(new Monster(Monster.MonsterType.Archer));
                break;
        }
        setup(monsters);
    }

    public void setup(List<Monster> monsters){
        Room.get().setEntities(monsters);
        spellHolder.hide();
        Party.get().resetMagic();
        List<Hero> heroes = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Defender));
            heroes.add(new Hero(Hero.HeroType.Herbalist));
            heroes.add(new Hero(Hero.HeroType.Apprentice));
        }
        Party.get().setEntities(heroes);
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getAllActive());
        friendly.setEntities(heroes);
        enemy.setEntities(monsters);

        Main.clearPhases();

        Main.pushPhase(new NothingPhase());
        Main.pushPhase(new LevelUpPhase());
        Main.pushPhase(new EnemyRollingPhase());
        Main.popPhase();
    }

    private void confirmDice() {
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
                Main.popPhase();
            }
        }
        else if (Main.getPhase() instanceof TargetingPhase){
            if(Party.get().getAvaliableMagic() > 0){
                showDialog("Spend all your magic first!");
                return;
            }
            if(!checkAllDiceUsed()){
                showDialog("Use all your dice first!");
                return;
            }
            Main.popPhase();
        }
    }

    private void showDialog(String s) {
        TextButton tb = new TextButton(550, 100, s);
        tb.setFont(Fonts.font);
        push(tb, true, true);
    }

    public void enemyCombat(){
//        enemy.layout(false);
        for(DiceEntity m:Room.get().getActiveEntities()){
            m.slide(false);
        }
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
        for (DiceEntity de : EntityGroup.getAllActive()) {
            de.removeEffects(effects);
        }

    }

    public void click(Die d, boolean fromPhysics) {
        if(d.entity instanceof Monster) return;
        if(d.getSide()==-1) return;
        if(Main.getPhase().canRoll()){
            d.toggleLock();
            return;
        }
        if(fromPhysics){
            return;
        }
        Eff first = d.getEffects()[0];
        switch(first.targetingType){
            case EnemyGroup:
                for(DiceEntity de:Room.get().getActiveEntities()){
                    de.hit(d.getEffects(), false);
                }
                d.use();
                break;
            case FriendlyGroup:
                for(DiceEntity de:Party.get().getActiveEntities()){
                    de.hit(d.getEffects(), false);
                }
                d.use();
                break;
            case Self:
                d.entity.hit(d.getEffects(), false);
                d.use();
                break;
        }

        targetableClick(d);
    }

    public void activateAutoEffects(){
        for(DiceEntity de:Party.get().getActiveEntities()){
            Eff[] effs = de.getDie().getEffects();
                if(effs[0].targetingType == Eff.TargetingType.Untargeted){
                    for(Eff e:effs){
                        switch(e.type){
                            case Magic:
                                System.out.println("adding magic");
                                Party.get().addMagic(e.value);
                                break;
                            case Nothing:
                                break;
                            default:
                                System.err.println("oh shit you need to implement new untargeted effect");
                                break;
                        }
                    }
            }
        }
    }

    public void click(Spell spell){
        targetableClick(spell);
    }

    private void targetableClick(Targetable t){
        if(!Main.getPhase().canTarget()){
            Explanel.get().setup(t, false);
            push(Explanel.get(), true, true);
            return;
        }
        for(DiceEntity de:Party.get().getActiveEntities()){
            de.setShaderState(DieShader.DieShaderState.Nothing);
        }
        if(Party.get().getSelectedTargetable() == t){
            deselectTargetable();
            return;
        }
        deselectTargetable();
        Party.get().setSelectedTargetable(t);
        t.select();
        showTargetingHighlights();
        Explanel.get().setup(t, true);
        positionExplanel();
    }

    private void deselectTargetable(){
        clearTargetingHighlights();
        if(Party.get().getSelectedTargetable() != null) {
            Party.get().getSelectedTargetable().deselect();
            Explanel.get().remove();
            Party.get().setSelectedTargetable(null);
        }
    }

    static List<DiceEntity> tmp = new ArrayList<>();

    public boolean target(DiceEntity entity) {
        Targetable t = Party.get().getSelectedTargetable();
        if(!Main.getPhase().canTarget()) return false;
        if(t == null) return false;
        if(t.getEffects() == null) return false;
        if(t.getEffects().length==0) return false;


        Eff.TargetingType type = t.getEffects()[0].targetingType;
        List<DiceEntity> valids = EntityGroup.getValidTargets(type, true);
        boolean contains = valids.contains(entity);
        if(!contains && !(entity==null && valids.isEmpty())){
            return false;
        }

        if(t.use()){
            for(Eff e:t.getEffects()){
                hitEntities(EntityGroup.getActualTargets(e, true, entity), e);
            }
        }
        deselectTargetable();
        return true;
    }

    private boolean checkAllDiceUsed(){
        boolean allUsed = true;
        for (DiceEntity de : Party.get().getActiveEntities()) {
            if (!de.getDie().getUsed() && de.getDie().getActualSide().effects[0].type != Eff.EffectType.Nothing) {
                return false;
            }
        }
        return true;
        /*
        if(checkEnd()){
            nextLevel();
        }
         */
    }

    private void hitEntities(List<DiceEntity> entities, Eff e){
        for(int i=0;i<entities.size();i++){
            entities.get(i).hit(e, false);
        }
    }

    private boolean checkEnd() {
        return Room.get().getActiveEntities().size() == 0;
    }

    public List<DiceEntity> getRandomTargetForEnemy(Side side) {
        Eff e = side.effects[0];
        DiceEntity target = null;
        List<DiceEntity> validTargets = EntityGroup.getValidTargets(e.targetingType, false);
        if(validTargets.size()> 0){
            target = Tann.getRandom(validTargets);
        }
        return EntityGroup.getActualTargets(e, false, target);
    }

    private void positionExplanel() {
        Explanel.get().setPosition(Explanel.get().getNiceX(true), Explanel.get().getNiceY());
        addActor(Explanel.get());
    }

    public void closeSpellHolder() {
        if(Party.get().getSelectedTargetable() instanceof Spell){
            deselectTargetable();
        }
    }

    public void clearTargetingHighlights(){
        for(DiceEntity de: EntityGroup.getAllActive()){
            de.getEntityPanel().setPossibleTarget(false);
        }
    }



    public void showTargetingHighlights(){
        Targetable t = Party.get().getSelectedTargetable();
        if(t == null || t.getEffects().length == 0) return;
        Eff.TargetingType tType = t.getEffects()[0].targetingType;
        for(DiceEntity de: EntityGroup.getValidTargets(tType, true)){
            de.getEntityPanel().setPossibleTarget(true);
        }
    }

    public void removeLeftoverDice() {
        for(DiceEntity h:Party.get().getActiveEntities()){
            if(!h.getDie().getUsed()){
                h.getDie().use();
            }
        }
    }

    public void clicked(DiceEntity entity, boolean dieSide) {
        if (Party.get().getActiveEntities() != null) {
            if (target(entity)) return;
        }

        if (entity.isPlayer()) {
            if (dieSide) {
                DungeonScreen.get().click(entity.getDie(), false);
            } else {
                showDiePanel(entity);
            }
        } else {
            showDiePanel(entity);
        }
    }

    private void showDiePanel(DiceEntity entity){
        DiePanel pan = entity.getDiePanel();
        BorderGroup bg = new BorderGroup(pan);
        push(pan);
        pan.setPosition(pan.getNiceX(false), pan.getNiceY());
        if(entity.getTarget() != null) {
            for (DiceEntity de : entity.getTarget()) {
                de.getEntityPanel().setTargeted(true);
            }
        }
    }

    private void push(Actor a, boolean center, boolean listener){
        addActor(InputBlocker.get());
        InputBlocker.get().toFront();
        modalStack.add(a);
        addActor(a);

        if(center){
            a.setPosition(getWidth()/2-a.getWidth()/2, getHeight()/2-a.getHeight()/2);
        }
        if(listener){
            a.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Main.popPhase();
                    return super.touchDown(event, x, y, pointer, button);
                }
            });
        }
    }

    public void push(Actor a){
        push(a, false, false);
    }

    public void pop(){
        deselectTargetable();
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

    public void showLevelupPanel(Hero hero, HeroType[] options) {
        LevelUpPanel lup = new LevelUpPanel(hero, options);
        lup.setPosition(getWidth()/2, getHeight()/2, Align.center);
        addActor(lup);
    }

}
