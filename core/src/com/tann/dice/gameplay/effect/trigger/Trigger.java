package com.tann.dice.gameplay.effect.trigger;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffectType;
import com.tann.dice.gameplay.entity.DiceEntity;

public class Trigger {

  DiceEntity source, target;

  public int affectMaxHp(int hp){return hp;}

  public int getSideBonus(EffectType type){
    return 0;
  }

  public void endOfTurn(DiceEntity target){}

  public Integer alterIncomingDamage(Integer incomingDamage) { return incomingDamage; }

  public Integer getIncomingPoisonDamage() { return 0; }

  public void attackedBy(DiceEntity entity) { }

  public int alterOutgoingEffect(EffectType type, int value) { return value; }

  public int alterIncomingEffect(EffectType type, int value) { return value; }
}
