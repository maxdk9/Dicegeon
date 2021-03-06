package com.tann.dice.gameplay.effect.trigger;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffType;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Side;

import java.util.List;

public abstract class Trigger implements Cloneable{

  private static final TextureRegion unknown = loadImage("unknown");
  public Buff buff;

  protected static TextureRegion loadImage(String name){
    return Main.atlas.findRegion("trigger/"+name);
  }

  protected DiceEntity entity;

  public int affectMaxHp(int hp){return hp;}

  public void endOfTurn(){}

  public void reset(){}

  public Integer alterIncomingDamage(Integer incomingDamage) { return incomingDamage; }
  public Integer alterIncomingPoisonDamage(Integer incomingDamage) { return incomingDamage; }
  public Integer getIncomingPoisonDamage() { return 0; }
  public void attackedBy(DiceEntity entity) { }
  public int alterOutgoingEffect(EffType type, int value) { return value; }
  public int alterIncomingEffect(EffType type, int value) { return value; }
  public boolean avoidDeath(){ return false; }
  public boolean avoidDeathPenalty(){ return false; }
  public boolean startsAtTheBack(){ return false; }
  public void onKill(){}
  public void onDeath(){}
  public void onHitWithEff(Eff e){}
  public Integer getRegen(){return 0;};
  public void affectSide(Side side, DiceEntity owner) { }
  public boolean activateOnDamage(int old, int now){ return false; };

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

  public boolean showInPanel() { return false; }

  public boolean highlightForNewPlayers(){ return false; }

  public TextureRegion getImage(){
    return unknown;
  }

  public String describeForBuffText(){ return describe(); }

  public boolean afterUse(Side side){ return false; };

  public abstract String describe();

  public String describeForPanelText(){return describeForBuffText();};

  public void setEntity(DiceEntity entity){
    this.entity = entity;
  }

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

  public static Trigger[] copy(Trigger[] triggers){
    Trigger[] result = new Trigger[triggers.length];
    for(int i=0;i<triggers.length;i++){
      result[i]=triggers[i].copy();
    }
    return result;
  }

  public boolean isNegative() {
    return false;
  }

}
