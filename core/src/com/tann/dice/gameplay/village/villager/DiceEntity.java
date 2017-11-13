package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.village.villager.die.Side;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;
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
	public boolean dead;
	Array<Eff> potentialEffects = new Array<>();
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
        if(instant) {
            switch (e.type) {
                case Sword:
                    damage(e.value);
                    break;
                case Shield:
                    potentialEffects.add(e);
                    break;
            }
        }
        else{
            potentialEffects.add(e);
        }
        getEntityPanel().layout();
  }

    private void damage(int value) {
        hp -= value;
        if(hp<=0){
            die();
        }
    }

    private void die() {
        if(die.getActualSide() != null) {
            DungeonScreen.get().cancelEffects(die.getActualSide().effects);
        }
        die.removeFromScreen();
        getEntityPanel().remove();
        getEntityPanel().highlight=false;
        dead=true;
    }


    public void hit(Side side, boolean instant) {
        for(Eff e:side.effects) {
          hit(e, instant);
        }
    }

    public int getIncomingDamage() {
        int total = 0;
        for (Eff e : potentialEffects) {
            switch (e.type) {
                case Sword:
                    total += e.value;
                    break;
                case Shield:
                    total -= e.value;
                    break;
            }
        }
        return total;
    }

  public abstract void locked();

    public void removeEffects(Eff[] effects) {
        boolean recalculate = potentialEffects.removeAll(new Array<>(effects), true);
        if(recalculate){
            getEntityPanel().layout();
        }
    }

    public void activatePotentials() {
        int incoming = getIncomingDamage();
        if(incoming > 0){
            damage(incoming);
        }
        potentialEffects.clear();
        getEntityPanel().layout();
    }
}
