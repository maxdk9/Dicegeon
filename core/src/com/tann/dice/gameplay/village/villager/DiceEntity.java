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
	private Die die;
	public TextureRegion lapel;
	public Side[] sides;
	static int i;
	int maxHp = 3;
	int hp = maxHp + (int)(Math.random()*3);
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

    public void hit(Side side) {
        for(Eff e:side.effects){
            switch(e.type){
                case Sword:
                    hp -= e.value;
                    break;
            }
        }
        ep.layout();
    }
}
