package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
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
import com.tann.dice.gameplay.phase.EnemyRollingPhase;
import com.tann.dice.gameplay.phase.LevelUpPhase;
import com.tann.dice.gameplay.phase.NothingPhase;
import com.tann.dice.gameplay.phase.PlayerRollingPhase;
import com.tann.dice.screens.dungeon.panels.*;
import com.tann.dice.screens.dungeon.panels.Explanel.*;
import com.tann.dice.util.*;

public class DungeonScreen extends Screen {

    public static DungeonScreen self;
    public Targetable selectedTargetable;

    public static DungeonScreen get() {
        if (self == null) {
            self = new DungeonScreen();
            self.init();
        }
        return self;
    }

    public static final float BOTTOM_BUTTON_HEIGHT = Main.height*.2f;

    private static Array<DiceEntity> tmpALl = new Array<>();
    public Array<DiceEntity> getAll(){
        tmpALl.clear();
        tmpALl.addAll(heroes);
        tmpALl.addAll(monsters);
        return tmpALl;
    }

    public Array<DiceEntity> heroes = new Array<>();
    public Array<DiceEntity> monsters = new Array<>();
    private SidePanel friendly;
    private SidePanel enemy;
    int rolls = BASE_ROLLS;

    public static final int BASE_ROLLS = 3;

    public SpellHolder spellHolder;

    private DungeonScreen() {
    }

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

        Button rollButton = new Button(SidePanel.width, BOTTOM_BUTTON_HEIGHT, .6f, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        if(rolls>0){
                            playerRoll(false);
                        }
                    }
                }){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                Fonts.draw(batch, rolls+"/"+BASE_ROLLS, Fonts.fontSmall, Colours.light, this.getX(), this.getY(), this.getWidth(), this.getHeight()/5, Align.center);
            }
        };
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
    }

    int level;

    public void nextLevel() {
        level ++;
        Array<Monster> monsters =  new Array<>();
        for(int i=0;i<level+2;i++){
            monsters.add(new Monster(Monster.MonsterType.Goblin));
        }
        monsters.add(new Monster(Monster.MonsterType.Ogre));
        setup(monsters);
    }

    public void setup(Array<Monster> pMonsters){
        heroes.clear();
        monsters.clear();
        spellHolder.hide();
        resetMagic();
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Defender));
            heroes.add(new Hero(Hero.HeroType.Herbalist));
            heroes.add(new Hero(Hero.HeroType.Apprentice));
        }
        monsters.addAll(pMonsters);
        BulletStuff.reset();
        BulletStuff.refresh(getAll());

        friendly.setEntities(heroes);
        enemy.setEntities(monsters);

        Main.clearPhases();

        Main.pushPhase(new NothingPhase());
        Main.pushPhase(new LevelUpPhase());
        Main.pushPhase(new EnemyRollingPhase());
        Main.popPhase();
    }

    private void confirmDice() {
        if(!(Main.getPhase() instanceof PlayerRollingPhase)) return;
        boolean allGood = true;
        for(DiceEntity h:heroes){
            Die d = h.getDie();
            if(d.getSide()==-1){
                allGood=false;
            }
            else if(d.getState()!= DieState.Locked && d.getState() != DieState.Locking){
                d.slideToPanel();
            }
        }
        if(allGood){
            Main.popPhase();
        }
    }

    public void enemyCombat(){
        enemy.layout(false);
        for(DiceEntity m:monsters){
            m.slidOut = false;
        }
        for(DiceEntity m: Tann.pickNRandomElements(monsters, Math.min(monsters.size, 2))){
            m.getEntityPanel().slideOut();
        }
        float timer = 0;
        float timerAdd = .1f;
        for (final DiceEntity de : monsters) {
            final Monster m = (Monster) de;
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
        for (DiceEntity de : getAll()) {
            de.removeEffects(effects);
        }

    }

    public void click(Die d) {
        if(d.entity instanceof Monster) return;
        if(d.getSide()==-1) return;
        if(Main.getPhase().canRoll()){
            d.toggleLock();
            return;
        }

        targetableClick(d);
    }

    public void click(Spell spell){
        targetableClick(spell);
    }

    private void targetableClick(Targetable t){
        for(DiceEntity de:heroes){
            de.setShaderState(DieShader.DieShaderState.Nothing);
        }
        if(selectedTargetable == t){
            deselectTargetable();
            return;
        }
        deselectTargetable();
        selectedTargetable = t;
        t.select();
        Explanel.get().setup(t);
        positionExplanel();
        showTargetingHighlights();
    }

    private void deselectTargetable(){
        clearTargetingHighlights();
        if(selectedTargetable != null) {
            selectedTargetable.deselect();
            Explanel.get().remove();
            selectedTargetable = null;
        }
    }

    static Array<DiceEntity> tmp = new Array<>();

    public boolean target(DiceEntity entity) {
        if(!Main.getPhase().canTarget()) return false;
        if(selectedTargetable == null) return false;
        if(selectedTargetable.getEffects() == null) return false;
        if(selectedTargetable.getEffects().length==0) return false;

        // validate the targeting
        switch (selectedTargetable.getEffects()[0].targetingType){
            case EnemySingle:
                if(!entity.slidOut || entity.isPlayer()) return false;
                break;
            case EnemySingleRanged:
                if(entity.isPlayer()) return false;
                break;
            case EnemyGroup:
            case FriendlyGroup:
                if(entity!=null) return false;
            case FriendlySingle:
                if(!entity.isPlayer()) return false;
                break;
            case Untargeted:
                return false;
        }


        if(selectedTargetable.use()){
            for(Eff e:selectedTargetable.getEffects()){
                tmp.clear();
                switch(e.targetingType){
                    case EnemySingle:
                    case EnemySingleRanged:
                    case FriendlySingle:
                        entity.hit(e, true);
                        break;
                    case EnemyGroup:
                        tmp.addAll(monsters);
                        hitEntities(tmp, e);
                        break;
                    case FriendlyGroup:
                        tmp.addAll(heroes);
                        hitEntities(tmp, e);
                        break;
                    case EnemyAndAdjacents:
                        hitEntities(entity.getAdjacents(true), e);
                        break;
                    case EnemyOnlyAdjacents:
                        hitEntities(entity.getAdjacents(false), e);
                        break;
                    case Untargeted:
                        break;
                }
            }
        }
        boolean allUsed = true;
        for (DiceEntity de : heroes) {
            if (!de.getDie().getUsed() && !de.isDead() && de.getDie().getActualSide().effects[0].targetingType != Eff.TargetingType.Untargeted) {
                allUsed = false;
                break;
            }
        }
        if (getAvaliableMagic()>0){
            allUsed = false;
        }
        if (allUsed) {
            Main.popPhase();
        }
        deselectTargetable();

        if(checkEnd()){
            nextLevel();
        }
        return true;
    }

    private void hitEntities(Array<DiceEntity> entities, Eff e){
        for(DiceEntity de:entities){
            de.hit(e, true);
        }
    }

    private boolean checkEnd() {
        for(DiceEntity m:monsters){
            if(!m.isDead()) return false;
        }
        return true;
    }

    public Array<DiceEntity> getRandomTargetForEnemy(Side side) {
        Eff e = side.effects[0];
        Array<DiceEntity> targets = new Array<>();
        switch (e.targetingType){
            case EnemySingle:
            case EnemyAndAdjacents:
            case EnemyOnlyAdjacents:
                targets.add(heroes.random());
                break;
            case EnemySingleRanged:
                targets.add(heroes.random());
                break;
            case EnemyGroup:
                targets.addAll(heroes);
                break;
            case FriendlySingle:
                targets.add(monsters.random());
                break;
            case FriendlyGroup:
                targets.addAll(monsters);
                break;
            case Untargeted:
                break;
        }
        while(targets.contains(null, true)){
            System.out.println("ah feck");
            targets.removeValue(null, true);
        }
        return targets;
    }


    public void playerRoll(boolean firstRoll) {
        if(!Main.getPhase().canRoll()) return;
        if(firstRoll){
            rolls = BASE_ROLLS;
        }
        rolls --;
        for(DiceEntity hero:heroes){
            if(firstRoll) hero.getDie().addToScreen();
            hero.getDie().roll(firstRoll);
        }
    }

    public void activateDamage() {
        Array<DiceEntity> all = getAll();
        for(int i=0;i<all.size;i++){
            DiceEntity de = all.get(i);
            de.activatePotentials();
        }
    }

    private void positionExplanel() {
        Explanel.get().setPosition(Explanel.get().getNiceX(true), Explanel.get().getNiceY());
        addActor(Explanel.get());
    }


    private int magic = 0;

    public void addMagic(int add){
        this.magic += add;
    }

    public void resetMagic(){
        this.magic = 0;
    }

    public int getAvaliableMagic() {
        return magic;
    }

    public void spendMagic(int cost) {
        magic -= cost;
    }

    public void closeSpellHolder() {
        if(selectedTargetable instanceof Spell){
            deselectTargetable();
        }
    }

    public void activateAutoEffects() {
        for(DiceEntity h:heroes){
            for(Eff e:h.getDie().getActualSide().effects){
                switch(e.type){
                    case Magic:
                        addMagic(e.value);
                        break;
                }
            }
        }
    }

    public void clearTargetingHighlights(){
        for(DiceEntity de:getAll()){
            de.getEntityPanel().setTargetingHighlight(false);
        }
        enemy.setTargetingHighlight(false);
        friendly.setTargetingHighlight(false);
    }

    public void showTargetingHighlights(){
        if(selectedTargetable == null || selectedTargetable.getEffects().length == 0) return;
        Eff.TargetingType tType = selectedTargetable.getEffects()[0].targetingType;
        switch (tType){
            case EnemySingle:
            case EnemyAndAdjacents:
            case EnemyOnlyAdjacents:
                for(DiceEntity m : monsters){
                    if(m.slidOut){
                        m.getEntityPanel().setTargetingHighlight(true);
                    }
                }
                break;
            case EnemySingleRanged:
                for(DiceEntity m : monsters){
                        m.getEntityPanel().setTargetingHighlight(true);
                }
                break;
            case EnemyGroup:
                enemy.setTargetingHighlight(true);
                break;
            case FriendlySingle:
                for(DiceEntity h:heroes){
                    h.getEntityPanel().setTargetingHighlight(true);
                }
                break;
            case FriendlyGroup:
                friendly.setTargetingHighlight(true);
                break;
            case Untargeted:
                break;
        }
    }

    public void removeLeftoverDice() {
        for(DiceEntity h:heroes){
            if(!h.getDie().getUsed()){
                h.getDie().use();
            }
        }
    }

    public void clicked(DiceEntity entity, boolean dieSide) {
        if(selectedTargetable != null){
            if(target(entity)) return;
        }

        if(entity.isPlayer()){
            if(dieSide){
                DungeonScreen.get().click(entity.getDie());
            }
           else{
                showEntityPanel(entity);
            }
        }
        else{
            showEntityPanel(entity);
        }
    }

    private void showEntityPanel(DiceEntity entity){
        DiePanel pan = entity.getDiePanel();
        push(pan);
        pan.setPosition(pan.getNiceX(false), pan.getNiceY());
    }


    public void push(Actor a){
        addActor(InputBlocker.get());
        InputBlocker.get().toFront();
        modalStack.add(a);
        addActor(a);
    }

    public void pop(){
        if(modalStack.size==0) return;
        modalStack.removeIndex(modalStack.size-1).remove();
        InputBlocker.get().remove();
        if(modalStack.size>0){
            addActor(InputBlocker.get());
            modalStack.get(modalStack.size-1).toFront();
        }
    }

    Array<Actor> modalStack = new Array<>();

    public DiceEntity getRandomHero() {
        return heroes.random();
    }

    public void showLevelupPanel(Hero hero) {
        LevelUpPanel lup = new LevelUpPanel(hero, new HeroType[]{HeroType.Protector, HeroType.Rogue, HeroType.Wizard});
        lup.setPosition(getWidth()/2, getHeight()/2, Align.center);
        addActor(lup);
    }
}
