package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.EntityType;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;

import com.tann.dice.util.TextWriter;
import java.util.ArrayList;
import java.util.List;


public abstract class DiceEntity {

    protected Die die;
    // gameplay vars
    protected Side[] sides;
    protected int baseMaxHp;
    protected boolean dead;
    public boolean diedLastRound;
    protected int hp;
    protected List<DiceEntity> targets;
    List<Buff> buffs = new ArrayList<>();
    public DiceEntity targeted;
    // rendering vars
    protected Color col = Colours.purple;
    protected TextureRegion lapel;
    protected TextureRegion lapel2d;
    private EntityPanel ep;
    // temp junky variables
    public String name;
    EntitySize size;
    public EntityType entityType;
    public AtlasRegion portrait;
    public int portraitOffset;
    public ArrayList<Equipment> equipment = new ArrayList<>();
    public int equipmentMaxSize = 1;
    public final Trait[] traits;

    public DiceEntity(EntityType type) {
        this.entityType = type;
        this.name = type.name;
        this.traits = (Trait[]) type.traits.toArray(new Trait[0]);
        this.size = type.size;
        setupLapels(0);
        List<AtlasRegion> portraits = Tann.getRegionsStartingWith("portrait/"+name);
        if(portraits.size()>0){
            portrait = Tann.getRandom(portraits);
            portraitOffset = Integer.valueOf(portrait.name.split("-")[1])+0;
        }
        setMaxHp(type.hp);
        fullHeal();
        setSides(entityType.sides);
    }

    public void startOfFight(){
        diedLastRound = false;
    }

    public void init(){
        for(int i=0;i<10;i++){
//         addBuff(new Buff(5, Images.regen, new TriggerEndOfTurnSelf(new Eff().damage(1))));
        }
    }

    protected void setSides(Side[] sides) {
        this.sides = Side.copy(sides);
        for(Side s:this.sides){
            for(Eff e:s.getEffects()){
                e.source=this;
            }
        }
        getDie().refresh();
    }

    // gameplay junk
    public void setMaxHp(int maxHp) {
        this.baseMaxHp = maxHp;
    }

    public void fullHeal() {
        calculatedMaxHp = null;
        hp = diedLastRound?getMaxHp()/2:getMaxHp();
    }

    protected void resetPanels() {
        getEntityPanel().resetDieHolding();
        getEntityPanel().layout();
        getDiePanel().layout();
    }

    private Integer calculatedMaxHp;
    private ArrayList<Trigger> activeTriggers;

    public void somethingChanged(){
        calculatedMaxHp = null;
        activeTriggers = null;
        for(Side s:sides){
            s.useTriggers(getActiveTriggers());
        }
        getProfile().somethingChanged();
        getDiePanel().somethingChanged();
        getDie().refresh();
    }

    public int getMaxHp() {
        if(calculatedMaxHp == null){
            calculatedMaxHp = baseMaxHp;
            for(Trigger t:getActiveTriggers()){
                calculatedMaxHp = t.affectMaxHp(calculatedMaxHp);
            }
        }
        return calculatedMaxHp;
    }

    public List<Trigger> getActiveTriggers(){
        if(activeTriggers == null) {
            activeTriggers = new ArrayList<>();
            for (Equipment e : equipment) {
                activeTriggers.addAll(e.getTriggers());
            }
            for (Buff b:getBuffs()){
                activeTriggers.add(b.trigger);
            }
            for (Trait t:traits){
                activeTriggers.addAll(t.triggers);
            }
        }
        return activeTriggers;
    }

    public int getHp() {
        return hp;
    }

    public void heal(int amount) {
        this.hp += amount;
    }

    public void reduceToHalfHP(){}

    public void reset(){
        dead = false;
        fullHeal();
        targeted = null;
        getDie().flatDraw = false;
        buffs.clear();
        if(targets != null){
            targets.clear();
        }
        profile.reset();
        resetPanels();
        somethingChanged();
    }

    public abstract void stopped();

    public void hit(Eff[] effects, boolean instant) {
        for (Eff e : effects) {
            hit(e, instant);
        }
    }

    public void hit(Eff e, boolean instant) {
        switch(e.type){
            case RedirectIncoming:
                List<Eff> incomingEffs = getProfile().effs;
                for(int i=incomingEffs.size()-1;i>=0;i--){
                    Eff potential = incomingEffs.get(i);
                    if(!potential.source.isPlayer()){
                        incomingEffs.remove(potential);
                        e.source.hit(potential, false);
                    }
                }
                somethingChanged();
                e.source.somethingChanged();
                break;
        }
        getProfile().addEffect(e);
        if (instant || !isPlayer()) getProfile().action();
        if(!isPlayer() && profile.isGoingToDie(false)){
            getDie().removeFromScreen();
        }
        somethingChanged();
    }

    private DamageProfile profile;
    public DamageProfile getProfile(){
        if(profile == null) this.profile = new DamageProfile(this);
        return profile;
    }

    public void damage(int value) {
        if(value > 0){
            getEntityPanel().addDamageFlib(value);
        }
        boolean above = aboveHalfHealth();
        hp -= value;
        if(above && !aboveHalfHealth()){
            reduceToHalfHP();
        }
        if (hp <= 0) {
            die();
        }
    }

    public boolean aboveHalfHealth(){
        return getHp()>getMaxHp()/2;
    }

    public void kill() {
        die();
    }

    protected void die() {
        hp = 0;
        die.removeFromScreen();
        getEntityPanel().setPossibleTarget(false);
        slide(false);
//        getEntityPanel().remove();
        if (targets != null) {
            for (DiceEntity de : targets) {
                de.untarget(this);
            }
        }
        BulletStuff.dice.remove(getDie());
        dead = true;
        diedLastRound = true;
        if (this instanceof Monster) {
            Room.get().getActiveEntities().remove(this);
        } else {
            Party.get().getActiveEntities().remove(this);
        }
        if(!isPlayer()){
            Room.get().updateSlids(true);
        }
        getDie().flatDraw = false;
        DungeonScreen.get().layoutSidePanels();
        removeEffectsIfDead();
        TargetingManager.get().showTargetingHighlights();
    }

    public void removeEffectsIfDead(){
        if(!isPlayer() && die.getActualSide() != null && isDead()) {
            TargetingManager.get().cancelEffects(this);
        }
    }

    public void untarget(DiceEntity diceEntity) {
        if (targeted == diceEntity) targeted = null;
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
        somethingChanged();
    }

    public void hit(Side side, boolean instant) {
        for (Eff e : side.getEffects()) {
            hit(e, instant);
        }
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff);
    }

    List<Buff> tempBuffs = new ArrayList<>();
    public List<Buff> getBuffs(){
        tempBuffs.clear();
        tempBuffs.addAll(buffs);
        tempBuffs.addAll(getProfile().getIncomingBuffs());
        return tempBuffs;
    }

    public List<DiceEntity> getTarget() {
        return targets;
    }

    public Side[] getSides() {
        return sides;
    }

    public boolean isDead() {
        return dead;

    }

    public abstract boolean isPlayer();

    // boring junk

    public Die getDie() {
        if (die == null) {
            die = new Die(this);
        }
        return die;
    }

    public Color getColour() {
        return col;
    }

    public void setColour(Color col) {
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public EntityPanel getEntityPanel() {
        if (ep == null) {
            ep = new EntityPanel(this);
        }
        return ep;
    }

    public TextureRegion getLapel() {
        return lapel;
    }

    public TextureRegion get2DLapel() {
        return lapel2d;
    }


    public void targetedBy(DiceEntity e) {
        targeted = e;
    }

    public DieShader.DieShaderState shaderState = DieShader.DieShaderState.Nothing;

    public void clicked() {
        shaderState = DieShader.DieShaderState.Selected;
    }

    public void setShaderState(DieShader.DieShaderState shaderState) {
        this.shaderState = shaderState;
    }

    public boolean slidOut;

    public void slide(boolean slid) {
        slidOut = slid;
        DungeonScreen.get().layoutSidePanels();
    }

    private DiePanel panel;

    public DiePanel getDiePanel() {
        if (panel == null) panel = new DiePanel(this);
        return panel;
    }

    List<DiceEntity> tmp = new ArrayList<>();

    public List<DiceEntity> getAdjacents(boolean includeSelf) {
        tmp.clear();
        List<DiceEntity> mine = isPlayer() ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        int index = mine.indexOf(this);
        for (int i = Math.max(index - 1, 0); i <= index + 1 && i < mine.size(); i++) {
            if (i == index && !includeSelf) continue;
            tmp.add(mine.get(i));
        }
        return tmp;
    }

    public void removeEffects(DiceEntity entity) {
       getProfile().removeEffsFromSource(entity);
    }

    public int getEffectiveHp() {
        return getProfile().getEffectiveHp();
    }

    public void upkeep() {
        List<Trigger> activeTriggers = getActiveTriggers();
        for(Trigger t:activeTriggers){
            t.endOfTurn(this);
        }
        getProfile().action();
        for(int i=buffs.size()-1; i>=0; i--){
            buffs.get(i).turn();
        }
        somethingChanged();
    }

    public void attackedBy(DiceEntity entity) {
        for(Trigger t:getActiveTriggers()){
            t.attackedBy(entity);
        }
    }

    public abstract int getPixelSize();

    public List<DiceEntity> getAllTargeters() {
        List<DiceEntity> results = new ArrayList<>();
        for(Eff e: getProfile().effs){
            DiceEntity source = e.source;
            if(source!=null && source.isPlayer()!=isPlayer()){
                results.add(source);
            }
        }
        return results;
    }

    public void locked() {
    }

    public void addEquipment(Equipment e) {
        DiceEntity previousOwner = Party.get().getEquippee(e);
        if(previousOwner!=null) {
            previousOwner.removeEquipment(e);
        }
        if(equipment.size()>= equipmentMaxSize){
            Equipment replaced = equipment.get(0);
            removeEquipment(replaced);
            if(previousOwner!=null){
                previousOwner.addEquipment(replaced);
            }
            else {
                Party.get().addEquipment(replaced);
            }

        }
        equipment.add(e);
        somethingChanged();
        fullHeal();
        somethingChanged();
    }

    public void removeEquipment(Equipment e) {
        equipment.remove(e);
        somethingChanged();
        fullHeal();
        somethingChanged();
    }

    public void resetEquipment(){
        equipment.clear();
        somethingChanged();
        fullHeal();
        somethingChanged();
    }

    private static final float BASE_SIZE = 5.69f/Gdx.graphics.getHeight()*Main.scale;

  public String getColourTag() {
      return TextWriter.getColourTagForColour(getColour());
  }

    public void imposeMaximumHealth() {
      hp = Math.min(hp, getMaxHp());
    }

    public enum EntitySize {

        smol(12), reg(16), big(24), huge(32);

        public final float dieSize;
        public final int pixels;
        EntitySize(int pixels) {
            this.pixels = pixels;
            this.dieSize = BASE_SIZE * pixels;
        }
    }

    public EntitySize getSize() {
        return size;
    }

    public void setupLapels(int level){
        this.lapel = Main.atlas_3d.findRegion(size+"/lapel/"+level);
        this.lapel2d = Main.atlas.findRegion("lapel2d/"+level);
    }
}
