package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class IncomingEffectTrigger extends Trigger{

  EffType type;
  int bonus;

  public IncomingEffectTrigger(EffType type, int bonus) {
    this.type = type;
    this.bonus = bonus;
  }

  @Override
  public int alterIncomingEffect(EffType type, int value) {
    if(this.type==type){
      return value + bonus;
    }
    return value;
  }

  @Override
  public String describe() {

    switch(type){
      case Shield:
        return "+"+bonus+" shields from all sources";
      case Heal:
        return "+"+bonus+" healing from all sources";
      default:
        return noDescription(type.toString());
    }
  }

}
