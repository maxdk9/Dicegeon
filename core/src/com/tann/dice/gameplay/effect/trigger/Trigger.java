package com.tann.dice.gameplay.effect.trigger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;

import java.util.List;

public abstract class Trigger implements Cloneable{

  private static final TextureRegion unknown = loadImage("unknown");

  protected static TextureRegion loadImage(String name){
    return Main.atlas.findRegion("trigger/"+name);
  }

  DiceEntity source, target;

  public int affectMaxHp(int hp){return hp;}

  public void endOfTurn(DiceEntity target){}

  public Integer alterIncomingDamage(Integer incomingDamage) { return incomingDamage; }

  public Integer alterIncomingPoisonDamage(Integer incomingDamage) { return incomingDamage; }

  public Integer getIncomingPoisonDamage() { return 0; }

  public void attackedBy(DiceEntity entity) { }

  public int alterOutgoingEffect(EffType type, int value, DiceEntity source) { return value; }

  public int alterIncomingEffect(EffType type, int value) { return value; }

  public boolean avoidDeath(){ return false; }

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

  public Integer getRegen(){return 0;};

  public void affectSide(Side side) { }

  public boolean showInPanel() { return false; }

  public TextureRegion getImage(){
    return unknown;
  }

  public String describeForBuffText(){ return describe(); }

  public abstract String describe();

  public void setValue(int value) {
  }

  public Trigger copy(){
    try{
      return (Trigger) this.clone();
    } catch (CloneNotSupportedException e) {
      System.out.println("fuck you java");
    }
    return null;
  }

}
