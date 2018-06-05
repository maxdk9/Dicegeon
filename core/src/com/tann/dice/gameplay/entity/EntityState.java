package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Buff;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.die.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityState {

  private int hp;
  private int maxHp;
  private int shield;
  private boolean dead;
  private List<Buff> buffs;
  private boolean forwards;
  private DiceEntity entity;
  private boolean diedLastRound;
  private int fleePip;
  List<Trigger> describableTriggers;

  public EntityState(DiceEntity entity) {
    this.entity = entity;
    maxHp = entity.getBaseMaxHp();
    hp = maxHp;
    forwards = true;
  }

  public List<Trigger> getDescribableTriggers() {
    if (describableTriggers == null) {
      describableTriggers = new ArrayList<>();
      for (Trigger t : getActiveTriggers()) {
        if (t.showInPanel()) {
          describableTriggers.add(t);
        }
      }
    }
    return describableTriggers;
  }

  public void hit(Eff eff) {
    switch (eff.type) {
      case Damage:
        damage(eff.getValue());
        break;
      case Shield:
        block(eff.getValue());
        break;
      case Healing:
        heal(eff.getValue());
        break;
      case Buff:
        addBuff(eff.getBuff());
        break;
      case Execute:
        if (hp == eff.getValue()) {
          die();
        }
        break;
      case Decurse:
        decurse();
        break;
      case Hook:
        forwards = true;
        break;

      case CopyAbility:
      case RedirectIncoming:
        // hmm complicated...
        break;


      case Empty:
      case Reroll:
      case Summon:
      case DestroyAllSummons:
      case Magic:
        throw new RuntimeException(eff.type+" shold not target an entity");
    }
  }

  private void decurse() {
    for (int i = buffs.size() - 1; i >= 0; i--) {
      Buff b = buffs.get(i);
      if (b.isNegative()) {
        buffs.remove(b);
      }
    }
  }

  private void addBuff(Buff buff) {
    buffs.add(buff);
  }

  private void block(int value) {
    shield += value;
  }

  private void damage(int value) {
    int absorbed = Math.min(shield, value);
    shield -= absorbed;
    hp -= (value - absorbed);
    if (hp <= 0) {
      die();
    }

    boolean aboveFlee = hp > fleePip;
    hp -= value;
    if (aboveFlee && hp <= fleePip) {
      forwards = false;
    }

  }

  private void die() {
    dead = true;
    for (Trigger t : getActiveTriggers()) {
      t.onDeath();
    }
  }

  private void heal(int value) {
    hp = Math.min(hp + value, maxHp);
  }

  public void fullHeal() {
    boolean half = diedLastRound;
    for (Trigger t : getActiveTriggers()) {
      if (t.avoidDeathPenalty()) {
        half = false;
      }
    }
    hp = half ? maxHp/2 : maxHp;
  }

  private ArrayList<Trigger> activeTriggers;

  public List<Trigger> getActiveTriggers() {
    if (activeTriggers == null) {
      activeTriggers = new ArrayList<>();
      for (Trait t : entity.traits) {
        activeTriggers.addAll(t.triggers);
      }
      for (Equipment e : entity.equipment) {
        activeTriggers.addAll(e.getTriggers());
      }
      for (Buff b : buffs) {
        activeTriggers.add(b.trigger);
      }
    }
    //TODO there was some logic here to set the trigger's source and set the trigger's buff here. It sucked and has been removed but will likely need replacing
    return activeTriggers;
  }

  public void startOfFight() {
    diedLastRound = false;
  }

  public void attackedBy(DiceEntity entity) {
    for (Trigger t : getActiveTriggers()) {
      t.attackedBy(entity);
    }
  }

  public boolean hasNegativeBuffs() {
    for (Buff b : buffs) {
      if (b.isNegative()) {
        return true;
      }
    }
    return false;
  }


  public void afterUse(Side actualSide) {
    for (Trigger t : getActiveTriggers()) {
      t.afterUse(actualSide);
    }
  }

  public boolean isDead() {
    return dead;
  }

  public int getMaxHp() {
    return maxHp;
  }

  public int getHp() {
    return hp;
  }

  public DiceEntity getEntity() {
    return entity;
  }

  public List<EntityState> getAdjacents(boolean includeSelf) {
    return null;
  }

  public boolean getForwards() {
    return forwards;
  }

  /*

    public void removeEffectsIfDead() {
    if (!isPlayer() && die.getActualSide() != null && isDead()) {
      TargetingManager.get().cancelEffects(this);
    }
  }
   */

  //calculatedMaxHp = t.affectMaxHp(calculatedMaxHp);

  /*
  bunch of crap here
    public void hit(Eff e, boolean instant) {
    boolean tempDead = dead;
    if(instant || e.source == null || e.source.isPlayer()) e.playSound();
    switch (e.type) {
      case Damage:
        if (e.targetingType == Eff.TargetingType.Self && e.source == this) {
          //ugh hacky I need to redo the system so it works better for this case
//                    e.source.damage(e.getValue());
//                    e.source.somethingChanged();
          // commented out because it breaks multihits with self damage todo be better
          return;
        }
        break;
      case Decurse:
        decurse();
        break;
      case RedirectIncoming:
        List<Eff> incomingEffs = getProfile().effs;
        for (int i = incomingEffs.size() - 1; i >= 0; i--) {
          Eff potential = incomingEffs.get(i);
          if (potential.source != null && !potential.source.isPlayer()) {
            incomingEffs.remove(potential);
            e.source.hit(potential, false);
          }
        }
        somethingChanged();
        e.source.somethingChanged();
        break;
      case CopyAbility:
        e.source.setCurrentSide(getDie().getActualSide().withValue(getDie().getActualSide().getEffects()[0].getValue()));
        return;
      case Hook:
        slide(true);
        return;
      case Shield:
        getEntityPanel().addHearticleShield(e.getValue());
        break;
      case Healing:
        getEntityPanel().addHearticleHeart(e.getValue());
        break;

    }
    getProfile().addEffect(e);
    if (instant || !isPlayer()) {
      getProfile().action();
    }
    if (!isPlayer() && profile.isGoingToDie(false)) {
      getDie().removeFromScreen();
    }
    if (!tempDead && dead) {
      if (e.source != null) {
        e.source.killedEnemy();
      }
    }
    somethingChanged();
  }
   */

  /*
    private void setCurrentSide(Side copy) {
    getDie().setSide(copy);
    for (Eff e : copy.getEffects()) {
      e.source = this;
    }
    copy.useTriggers(getActiveTriggers(), this);
    somethingChanged();
  }
   */


  /*
    public void upkeep() {
    List<Trigger> activeTriggers = getActiveTriggers();
    getProfile().endOfTurn();
    getProfile().action();
    for (int i = buffs.size() - 1; i >= 0; i--) {
      buffs.get(i).turn();
    }
    somethingChanged();
    getDie().clearOverride();
  }

   */

}
