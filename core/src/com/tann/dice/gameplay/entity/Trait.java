package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Eff.EffectType;

public class Trait {

  int bonusHP;


  public int affectMaxHp(int hp){
    return hp + bonusHP;
  }

  public int getSideBonus(EffectType type){
    return 0;
  }

  public void endOfTurn(DiceEntity me){

  }



}
