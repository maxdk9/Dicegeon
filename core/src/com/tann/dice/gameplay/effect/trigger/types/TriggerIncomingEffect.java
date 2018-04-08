package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.trigger.Trigger;

public class TriggerIncomingEffect extends Trigger{

  EffType type;
  int bonus;
  public static final TextureRegion image = loadImage("bonusHealing");

  public TriggerIncomingEffect(EffType type, int bonus) {
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
  public TextureRegion getImage() {
    return image;
  }

  @Override
  public boolean showInPanel() {
    return true;
  }

  @Override
  public String describe() {

    switch(type){
      case Shield:
        return "+"+bonus+" to all incoming shields";
      case Healing:
        return "+"+bonus+" to all incoming healing";
      default:
        return noDescription(type.toString());
    }
  }

}
