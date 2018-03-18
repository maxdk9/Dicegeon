package com.tann.dice.gameplay.effect.trigger.sources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.AllSidesBonusTrigger;
import com.tann.dice.gameplay.effect.trigger.types.IncomingEffectTrigger;
import com.tann.dice.gameplay.effect.trigger.types.MaxHPTrigger;
import com.tann.dice.gameplay.effect.trigger.types.SideChangeTrigger;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;
import com.tann.dice.util.Tann;
import java.util.ArrayList;
import java.util.List;

public class Equipment {

  private static final List<Equipment> all = new ArrayList<>();

  public static final Equipment leatherVest = new Equipment().name("Leather Vest").image("leatherVest")
          .fluff("A [sin]lovely[sin] leather vest").trigger(new MaxHPTrigger(1));

  public static final Equipment heartPendant = new Equipment().name("Heart Pendant").image("heartPendant")
          .fluff("A ruby carved into a heart").trigger(new IncomingEffectTrigger(EffType.Healing, 1));

  public static final Equipment hiddenDagger = new Equipment().name("Hidden Dagger").image("concealedDagger")
          .fluff("A concealed dagger").trigger(new SideChangeTrigger(EffType.Empty, Side.sword2));

  public static final Equipment glowStone = new Equipment().name("Glow Stone").image("glowStone")
          .fluff("A glowing purple stone").trigger(new AllSidesBonusTrigger(1));

  public static final Equipment gauntlet = new Equipment().name("Gauntlet").image("gauntlet")
          .fluff("A pair of [sin]chunky[sin] gauntlets").trigger(new EffTypeBonus(EffType.Damage, 1));

  public static final Equipment castaRoot = new Equipment().name("Casta Root").image("herb")
          .fluff("A natural cure-all").trigger(new EffTypeBonus(EffType.Healing, 1));

  public String name;
  private String description;
  public String fluff;
  public TextureRegion image;
  private List<Trigger> triggers = new ArrayList<>();

  public Equipment(){
    all.add(this);
  }

  public Equipment name(String name){
    this.name = name;
    return this;
  }

  public Equipment fluff(String fluff){
    this.fluff = fluff;
    return this;
  }

  public Equipment image(String imageName){
    this.image = Main.atlas.findRegion("equipment/"+imageName);
    if(image == null){
      System.err.println("Unable to find image "+imageName+" for equipment "+name);
    }
    return this;
  }

  public Equipment trigger(Trigger t){
    triggers.add(t);
    return this;
  }

  public List<Trigger> getTriggers() {
    return triggers;
  }

  public void draw(Batch batch, float x, float y, int scale){
    batch.setColor(Colours.grey);
    Draw.drawScaled(batch, Images.spellBorder, (int)x, (int)y, scale, scale);
    batch.setColor(Colours.z_white);
    Draw.drawScaled(batch, image, (int)x, (int)y, scale, scale);
  }

  public Equipment copy(){
    Equipment copy = new Equipment().name(name).fluff(fluff);
    copy.image = image;
    copy.triggers = triggers;
    return copy;
  }

  public String getDescription(){
    if(description == null){
      description = Trigger.describe(triggers);
    }
    return description;
  }

  public String toString(){
    return name;
  }

  public static Equipment random() {
    return Tann.getRandom(all).copy();
  }

  public static Equipment recent() {
    System.out.println(all.get(all.size()-1));
    return all.get(all.size()-1).copy();
  }
}
