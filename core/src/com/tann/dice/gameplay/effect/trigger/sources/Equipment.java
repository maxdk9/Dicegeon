package com.tann.dice.gameplay.effect.trigger.sources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.*;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Draw;

import java.util.*;

public class Equipment {

  private static final List<Equipment> all = new ArrayList<>();
  private static int defaultLevel = 0;
  {
    level = defaultLevel;
  }
  static {
    defaultLevel = 0;
    add(new Equipment().name("Leather Vest").image("leatherVest")
            .fluff("A [sin]lovely[sin] leather vest").trigger(new TriggerMaxHP(1)));
    add(new Equipment().name("Ruby Pendant").image("heartPendant")
            .fluff("It feels warm").trigger(new TriggerIncomingEffect(EffType.Healing, 1)));
    add(new Equipment().name("Hidden Dagger").image("concealedDagger")
            .fluff("A slim dagger, tucked away").trigger(new TriggerSideChange(EffType.Empty, Side.sword2)));
    add(new Equipment().name("Casta Root").image("herb")
            .fluff("A natural cure-all").trigger(new TriggerEffTypeBonus(EffType.Healing, 1)));
    add(new Equipment().name("Antivenom").image("antivenom")
            .fluff("Weird trick discovered by a mum, snakes hate her!").trigger(new TriggerDamageImmunity(true, false)));
    add(new Equipment().name("Blood Vial").image("bloodVial")
            .fluff("Something description").trigger(new TriggerOnKill(new Eff().heal(1).self())));
    add(new Equipment().name("Bone Idol").image("bone")
            .fluff("Something something lazybones").trigger(new TriggerNoDeathPenalty()));

    defaultLevel = 1;
    add(new Equipment().name("Reinforced Shield").image("shieldReinforce")
            .fluff("Extra plating is always good").trigger(new TriggerEffTypeBonus(EffType.Shield, 1)));
    add(new Equipment().name("Gauntlet").image("gauntlet")
            .fluff("A pair of [sin]chunky[sin] gauntlets").trigger(new TriggerEffTypeBonus(EffType.Damage, 1)));
    add(new Equipment().name("Chainmail").image("chainmail") // please forward to all your friends
            .fluff("Please forward to all your friends").trigger(new TriggerMaxHP(3)));
    add(new Equipment().name("Crystal Heart").image("crystalHeart")
            .fluff("You feel warmth inside").trigger(new TriggerEffTypeBonus(EffType.Magic, 1)));
    add(new Equipment().name("Thorns").image("thorns")
            .fluff("Something description").trigger(new TriggerDamageAttackers(1)));
    add(new Equipment().name("Relic").image("relic")
            .fluff("Something description").trigger(new TriggerOneHealthBonusOutgoing(2)));


    defaultLevel = 2;
    add(new Equipment().name("Glow Stone").image("glowStone")
            .fluff("A glowing purple stone").trigger(new TriggerAllSidesBonus(1, false)));
    add(new Equipment().name("Iron Helmet").image("ironHelmet")
            .fluff("A visored metal helmet").trigger(new TriggerMaxHP(6)));
//    add(new Equipment().name("Loaded Die").image("dice")
//            .fluff("It doesn't feel quite right...").trigger(new TriggerSideChange(EffType.Empty, Side.reroll)));

    defaultLevel = 3;
//    add(new Equipment().name("Savings Book").image("book")
//            .fluff("A [sin][yellow]high interest[grey][sin] monthly ISA!").trigger(new TriggerEndOfTurnSelf(6)));
  }

  private static void add(Equipment add){
    if(byName(add.name)!=null){
      System.err.println("Wuhoh, duplicate equipment with name "+add.name);
      return;
    }
    all.add(add);
  }

  private static List<Equipment> randomBag = new ArrayList<>();
  public static Equipment random() {
    if(randomBag.isEmpty()){
      randomBag.addAll(all);
      Collections.shuffle(randomBag);
    }
    return randomBag.remove(0).copy();
  }

  private static Map<Integer, List<Equipment>> levelBag = new HashMap<>();
  public static Equipment random(int level) {
    List<Equipment> list = levelBag.get(level);
    if(list==null) {
      list = new ArrayList<>();
    }
    if(list.isEmpty()){
      for(Equipment e:all) {
        if (e.level == level) {
          list.add(e);
        }
      }
      Collections.shuffle(list);
      levelBag.put(level, list);
    }
    return list.remove(0).copy();
  }

  public static Equipment byName(String name){
    name = name.toLowerCase();
    for(Equipment e:all){
      if(e.name.toLowerCase().equals(name)){
        return e.copy();
      }
    }
    return null;
  }


  public String name;
  private String description;
  private int level;
  public String fluff;
  public TextureRegion image;
  private List<Trigger> triggers = new ArrayList<>();

  public Equipment name(String name){
    this.name = name;
    return this;
  }

  public Equipment fluff(String fluff){
    this.fluff = fluff;
    return this;
  }

  public Equipment level(int level){
    this.level = level;
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
    copy.triggers = new ArrayList<>();
    for(Trigger t:triggers){
      copy.triggers.add(t.copy());
    }
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



}
