package com.tann.dice.gameplay.effect.trigger.types;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.die.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TriggerBoostOnRoll extends Trigger {

  public static final TextureRegion image = loadImage("combo");
  int bonus;
  List<Side> sides = new ArrayList<>();

  public TriggerBoostOnRoll(int bonus) {
    this.bonus = bonus;
  }

  @Override
  public void affectSide(Side side) {
    int toAdd = Collections.frequency(sides, side)*bonus;
    for(Eff e:side.getEffects()){
      if(e.getValue()==0) continue;
      e.justValue(e.getValue()+toAdd);
    }
  }

  @Override
  public boolean afterUse(Side side){
    sides.add(side);
    return true;
  }

  @Override
  public boolean showInPanel() {
    return true;
  }

  @Override
  public TextureRegion getImage() {
    return image;
  }

  @Override
  public String describe() {
    return "After you use a side, it gets +"+bonus+" until the end of combat";
  }

  @Override
  public void reset() {
    sides.clear();
  }
}
