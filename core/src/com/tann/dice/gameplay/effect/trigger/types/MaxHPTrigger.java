package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.trigger.Trigger;

public class MaxHPTrigger extends Trigger {

  int maxHpModifier;

  public MaxHPTrigger(int maxHpModifier) {
    this.maxHpModifier = maxHpModifier;
  }

  @Override
  public int affectMaxHp(int hp) {
    return hp + maxHpModifier;
  }

  @Override
  public String describe() {
    return "+"+maxHpModifier+" maximum health";
  }


}
