package com.tann.dice.screens.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.effect.buff.DamageMultiplier;
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
import com.tann.dice.screens.particles.SwordParticle;
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

    public static final float BOTTOM_BUTTON_HEIGHT = 25;
    public static final float BOTTOM_BUTTON_WIDTH = 78;
    public static final float BUTT_GAP = 2;
    public SidePanel friendly;
    private SidePanel enemy;
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

        Actor bulletActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.end();
//                BulletStuff.render();
                batch.begin();
            }
        };
        addActor(bulletActor);
        rollButton = new Button(BOTTOM_BUTTON_WIDTH, BOTTOM_BUTTON_HEIGHT, 1, Images.roll, Colours.dark,
                new Runnable() {
                    @Override
                    public void run() {
                        if(Party.get().getRolls()>0){
                            Party.get().roll();
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

    private void bottomClick() {
        deselectTargetable();
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

    public int level=0;

    public void nextLevel() {
        spellButt.removeAllHovers();
        Explanel.get().remove();
        List<Monster> monsters =  new ArrayList<>();
        level ++;
        switch(level){
            case 1:
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                monsters.add(new Monster(Monster.MonsterType.Goblin));
                break;
//            case 2:
//                monsters.add(new Monster(Monster.MonsterType.Archer));
//                monsters.add(new Monster(Monster.MonsterType.Archer));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                break;
//            case 3:
//                monsters.add(new Monster(Monster.MonsterType.Serpent));
//                monsters.add(new Monster(Monster.MonsterType.Serpent));
//                monsters.add(new Monster(Monster.MonsterType.Serpent));
//                break;
//            case 4:
//                monsters.add(new Monster(Monster.MonsterType.Ogre));
//                monsters.add(new Monster(Monster.MonsterType.Ogre));
//                monsters.add(new Monster(Monster.MonsterType.Ogre));
//                break;
//            case 5:
//                monsters.add(new Monster(Monster.MonsterType.Dragon));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                monsters.add(new Monster(Monster.MonsterType.Goblin));
//                monsters.add(new Monster(Monster.MonsterType.Archer));
//                break;
            case 6:
                Main.clearPhases();
                Main.pushPhase(new NothingPhase());
                Main.pushPhase(new VictoryPhase());
                Main.popPhase();
                return;
        }
        Party.get().rejig();
        setup(monsters);
        spellHolder.setup(Party.get().getSpells());
        spellButt.setSpellHolder(spellHolder);
//        spellHolder.setPosition(spellHolder.getX(false), spellHolder.getY(false));

        Main.clearPhases();
        Main.pushPhase(new NothingPhase());

        if(level>1){
            Main.pushPhase(new LevelUpPhase());
        }

        for(DiceEntity de:Party.get().getActiveEntities()){
            de.reset();
        }

        Main.pushPhase(new EnemyRollingPhase());
        Main.popPhase();
//        for(DiceEntity de:Room.get().getActiveEntities()) {
//            for (int i = 0; i < 2; i++) {
//                Buff b = new BuffDot(-1, 1);
//                b.target = de;
//                de.addBuff(b);
//            }
//        }
    }

    public void restart() {
        level = 0;
        resetHeroes();
        nextLevel();
    }

    public void setup(List<Monster> monsters){
        Room.get().setEntities(monsters);
        spellButt.hide();
        Party.get().resetMagic();
        BulletStuff.reset();
        BulletStuff.refresh(EntityGroup.getAllActive());
        enemy.setEntities(monsters);
    }

    public void resetHeroes(){
        List<Hero> heroes = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            heroes.add(new Hero(Hero.HeroType.Fighter));
            heroes.add(new Hero(Hero.HeroType.Fighter2));
            heroes.add(new Hero(Hero.HeroType.Defender));
            heroes.add(new Hero(Hero.HeroType.Herbalist));
            heroes.add(new Hero(Hero.HeroType.Apprentice));
        }
        friendly.setEntities(heroes);
        Party.get().setEntities(heroes);
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

    }

    public void drawRectThing(Batch batch, Rectangle rect) {
        float factor = Main.height / BulletStuff.heightFactor;
        Draw.fillRectangle(batch, rect.x * factor, Main.height - rect.y * factor - rect.height * factor, rect.width * factor, rect.height * factor);
    }

    @Override
    public void postDraw(Batch batch) {
        batch.setColor(Colours.light);
        TannFont.font.drawString(batch, "Level "+level+"/5",Main.width/2-10, Main.height- TannFont.font.getHeight()-1);
        batch.setColor(Colours.grey);
        Draw.fillRectangle(batch, 78, 78, 4, 4);
        batch.setColor(Colours.light);
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
        if(keycode == Input.Keys.B) {
            spellButt.addSpellHover(7);
        }
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
                hitMultiple(Room.get().getActiveEntities(), d.getEffects(), false);
                d.use();
                break;
            case FriendlyGroup:
                hitMultiple(Party.get().getActiveEntities(), d.getEffects(), false);
                d.use();
                break;
            case Self:
                d.entity.hit(d.getEffects(), false);
                d.use();
            case RandomEnemy:
                Tann.getRandom(Room.get().getActiveEntities()).hit(d.getEffects(), false);
                d.use();
                break;
            default:
                targetableClick(d);
                break;
        }

        checkEnd();
    }

    private void hitMultiple(List<DiceEntity> entities, Eff[] effects, boolean instant){
        for(int i=entities.size()-1;i>=0;i--){
            entities.get(i).hit(effects, instant);
        }
    }

    public void activateAutoEffects(){
        for(DiceEntity de:Party.get().getActiveEntities()){
            Eff[] effs = de.getDie().getEffects();
                if(effs[0].targetingType == Eff.TargetingType.Untargeted){
                    for(Eff e:effs){
                        switch(e.type){
                            case Magic:
                                Party.get().addMagic(e.getValue());
                                spellButt.addSpellHover(e.getValue());
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
            positionExplanel();
            push(Explanel.get(), false, true, true, false, false);
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
        if(!checkEnd()) {
            confirmDice(false);
        }

        if(Party.get().getAvaliableMagic() == 0){
            DungeonScreen.get().spellButt.hide();
        }

        Particle p = new SwordParticle((int) entity.getEntityPanel().getX(), (int)(entity.getEntityPanel().getY()));
        addParticle(p);
        System.out.println("adding particle");

        return true;
    }

    private boolean checkAllDiceUsed(){
        for (DiceEntity de : Party.get().getActiveEntities()) {
            Die d = de.getDie();
            Eff.TargetingType tt = d.getActualSide().effects[0].targetingType;
            if (!d.getUsed() && tt != Eff.TargetingType.Untargeted && tt != Eff.TargetingType.OnRoll) {
                return false;
            }
        }
        return true;
    }

    private void hitEntities(List<DiceEntity> entities, Eff e){
        for(int i=0;i<entities.size();i++){
            entities.get(i).hit(e, false);
        }
    }

    public boolean checkEnd() {
        if(checkDead(Room.get().getActiveEntities(), true)){
            nextLevel();
            return true;
        }
        else if(checkDead(Party.get().getActiveEntities(), false)){
            Main.clearPhases();
            Main.pushPhase(new NothingPhase());
            Main.pushPhase(new LossPhase());
            Main.popPhase();
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

    public void showLevelupPanel(Hero hero, List<HeroType>options) {
        LevelUpPanel lup = new LevelUpPanel(hero, options);
        lup.setPosition(getWidth()/2, getHeight()*2/3f, Align.center);
        addActor(lup);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
