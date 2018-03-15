package com.tann.dice.gameplay.effect.trigger;

import com.tann.dice.gameplay.effect.Eff.EffectType;
import com.tann.dice.gameplay.entity.DiceEntity;

import java.util.List;

public abstract class Trigger {

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

  protected String noDescription(String extra){
    return "No description for "+this.getClass().getSimpleName()+" ("+extra+")";
  }

  public static String describe(List<Trigger> triggers) {
    String result = "";
    for(Trigger t:triggers){
      result += t.describe();
    }
    return result;
  }

  public abstract String describe();
}
