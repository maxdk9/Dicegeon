package com.tann.dice.gameplay.effect.trigger.types;

import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerMaxHP extends Trigger {

  int maxHpModifier;

  public TriggerMaxHP(int maxHpModifier) {
    this.maxHpModifier = maxHpModifier;
  }

  @Override
  public void setValue(int value) {
    this.maxHpModifier= value;
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
