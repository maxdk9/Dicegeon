package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Buff;

import com.tann.dice.gameplay.effect.Eff;
import java.util.List;

public class EntityState {

  int hp;
  int maxHp;
  int shield;
  boolean dead;
  List<Buff> buffs;
  boolean forwards;
  DiceEntity entity;

  public EntityState(DiceEntity entity) {
    this.entity = entity;
    maxHp = entity.getMaxHp();
    hp = maxHp;
    forwards = true;
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
  }

  private void die() {
    dead = true;
  }

  private void heal(int value) {
    hp = Math.min(hp + value, maxHp);
  }

}
