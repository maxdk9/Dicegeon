package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tann.dice.Images;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.bullet.DieShader;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.DamageProfile;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.Explanel.DiePanel;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.Colours;

import java.util.ArrayList;
import java.util.List;


public abstract class DiceEntity {

    protected Die die;
    // gameplay vars
    protected Side[] sides;
    protected int maxHp;
    protected int hp;
    protected boolean dead;
    protected List<Eff> potentialEffects = new ArrayList<>();
    protected List<DiceEntity> targets;
    List<Buff> buffs = new ArrayList<>();
    public DiceEntity targeted;
    // rendering vars
    protected Color col;
    protected TextureRegion lapel;
    private EntityPanel ep;
    // temp junky variables
    private static int ixix;
    public String name;
    EntitySize size;
    public boolean locked; // only used for monster


    public DiceEntity(Side[] sides, String name, EntitySize size) {
        this.sides = sides;
        this.name = name;
        this.lapel = Images.lapel0;
        this.col = Colours.classes[(ixix++) % Colours.classes.length];
        this.size = size;
    }

    protected void setSides(Side[] sides) {
        this.sides = sides;
        getDie().setup();
    }

    // gameplay junk
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    protected void resetPanels() {
        panel.layout();
        ep.layout();
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public void heal(int amount) {
        this.hp = Math.min(maxHp, getHp() + amount);
    }

    public abstract void locked();

    public void hit(List<Eff> effects, boolean instant) {
        for (Eff e : effects) {
            hit(e, instant);
        }
    }

    public void hit(Eff[] effects, boolean instant) {
        for (Eff e : effects) {
            hit(e, instant);
        }
    }

    public void hit(Eff e, boolean instant) {
        getProfile().addEffect(e);
        if (instant || !isPlayer()) getProfile().action();
        getEntityPanel().layout();
        if(!isPlayer() && profile.isGoingToDie()){
            getDie().removeFromScreen();
        }
    }

    private DamageProfile profile;
    public DamageProfile getProfile(){
        if(profile == null) this.profile = new DamageProfile(this);
        return profile;
    }

    public void damage(int value) {
        hp -= value;
        if (hp <= 0) {
            die();
        }
    }

    private void die() {
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
        if (this instanceof Monster) {
            Room.get().getActiveEntities().remove(this);
        } else {
            Party.get().getActiveEntities().remove(this);
        }
        if(!isPlayer()){
            if (die.getActualSide() != null) {
                DungeonScreen.get().cancelEffects(die.getActualSide().effects);
            }
            Room.get().updateSlids(true);
        }
    }

    public void untarget(DiceEntity diceEntity) {
        if (targeted == diceEntity) targeted = null;
    }

    public void hit(Side side, boolean instant) {
        for (Eff e : side.effects) {
            hit(e, instant);
        }
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff);
    }

    List<Buff> tempBuffs = new ArrayList<>();
    public List<Buff> getBuffs(){
        tempBuffs.clear();
        tempBuffs.addAll(buffs);
        tempBuffs.addAll(getProfile().incomingBuffs);
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

    public String getName() {
        return this.getClass().getSimpleName();
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
        getEntityPanel().slide(slid);
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

    public void removeEffects(Eff[] effects) {
        for(Eff e:effects) {
            getProfile().removeEff(e);
        }
    }

    public int getEffectiveHp() {
        return getProfile().getEffectiveHp();
    }

    public void upkeep() {
        for(int i=buffs.size()-1; i>=0; i--){
            buffs.get(i).turn();
        }
    }

    public boolean canBeTargeted() {
        for(Buff b:getBuffs()){
            if(b.type == Buff.BuffType.stealth) return false;
        }
        return true;
    }

    public enum EntitySize {
        Small(.4f), Regular(.5f), Big(.63f), Huge(.95f);
        public final float dieSize;

        EntitySize(float dieSize) {
            this.dieSize = dieSize;
        }
    }

    public EntitySize getSize() {
        return size;
    }

}
