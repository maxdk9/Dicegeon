package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.villager.die.Side;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.Colours;


public abstract class DiceEntity {


 	Color col;
	protected Die die;
	public TextureRegion lapel;
	public Side[] sides;
	static int i;
	int maxHp = 3+ (int)(Math.random()*3);
	int hp = maxHp;
	int incomingDamage = 0;
	public DiceEntity(Side[] sides) {
	    this.sides=sides;
        this.lapel = Images.lapel0;
        this.col = Colours.classes[(i++)%Colours.classes.length];
    }

    public Die getDie(){
	    if(die==null){
	        die = makeDie();
        }
        return die;
    }

	private Die makeDie() {
		Die d = new Die(this);
		return d;
	}
	

	public Color getColour() {
		return col;
	}

	public abstract boolean isPlayer();

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public int getMaxHp() {
        return maxHp;
    }
    public int getHp() {
        return hp;
    }

    EntityPanel ep;
    public EntityPanel getEntityPanel() {
        if(ep==null) ep = new EntityPanel(this);
        return ep;
    }

  public void hit(Eff e, boolean instant) {
      switch(e.type){
        case Sword:
          if(instant){
            hp -= e.value;
          }
          else{
            addIncomingDamage(e.value);
          }
          break;
        case Shield:
            addIncomingDamage(-e.value);
          break;
      }
    ep.layout();
  }

    public void hit(Side side, boolean instant) {
        for(Eff e:side.effects) {
          hit(e, instant);
        }
    }

    public void addIncomingDamage(int amount){
      incomingDamage+=amount;
    }

    public void resetIncomingDamage(){
      incomingDamage=0;
    }

    public int getIncomingDamage(){
      return incomingDamage;
    }

  public abstract void locked();
}
