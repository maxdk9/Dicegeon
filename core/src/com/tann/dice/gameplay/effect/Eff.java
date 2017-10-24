package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.die.Die;

public class Eff {

    public void resetBonus() {
        this.bonus=0;
    }

    public void addBonus(int value) {
        this.bonus+=value;
    }

    public enum EffectType{
        Food(Images.food),
        Wood(Images.wood),
		Skull(Images.side_skull),
        Morale(Images.morale),
        FoodStorage(Images.food_storage),
        Fate(Images.fate),
        Brain(Images.brain),
        Gem(Images.gem),
        NewVillager(Images.obj_hourglass),
        DEATH(Images.skull_red),
        Lose(Images.skull_red),
        Buff,
        Objective,
        XpToVillager(Images.brain);

        //objectives

        public TextureRegion region;
        EffectType(){
            this.region=Images.dotdotdot;
        }
        EffectType(TextureRegion region){
            this.region=region;
        }

	}
	

	public EffectType type;
	public int value;
    int bonus;
	public Die sourceDie;
    public EffAct effAct = EffAct.now;

	public Eff(EffectType type, int value, Die sourceDie, EffAct effectActivation){
        this.type=type; this.value=value;  this.sourceDie=sourceDie; this.effAct = effectActivation;
    }

    public Eff(EffectType type, int value, Die sourceDie){this(type, value, sourceDie, EffAct.now);}
    public Eff(EffectType type, int value, EffAct effectActivation){
        this(type,value,null, effectActivation);
    }
	public Eff(EffectType type, int value){
	    this(type,value, null, EffAct.now);
    }
    public Eff(EffectType type){this(type, 0);}
    public Eff(){this((EffectType)null);};

    public String getValueString(){
	     return (value>=0?"":"-")+Math.abs(value);
    }

    public String toString(){
	    return getValueString()+" "+typeString()+" "+effAct.toString();
    }


    private String getwriterString(){
        return typeString();
    }

	public String typeString(){
        return "["+type.toString().toLowerCase()+"]";
	}
	
	public Eff copy(){
		Eff result = new Eff(type, value, sourceDie, effAct.copy());
		return result;
	}

	public boolean dead;

    public void turn() {
        if(effAct==null) {
            System.err.println("Trying to tick "+this);
            return;
        }
        switch(effAct.type){
            case NOW:
                break;
            case IN_TURNS:
                effAct.value--;
                break;
            case FOR_TURNS:
                effAct.value--;
                if(effAct.value==0){
                    dead=true;
                }
                else {
                }
                break;
            case UPKEEP:
                break;
            case PASSIVE:
                break;
        }
    }

    public Eff eachTurn(int numTurns){return setActivation(new EffAct(EffAct.ActivationType.FOR_TURNS, numTurns));}
    public Eff inTurns(int numTurns) {return setActivation(new EffAct(EffAct.ActivationType.IN_TURNS, numTurns));}
    public Eff upkeep(){return setActivation(new EffAct(EffAct.ActivationType.UPKEEP, -1));}
    public Eff now() {return setActivation(new EffAct(EffAct.ActivationType.NOW, 0));}

    public Eff newVillager() {return type(EffectType.NewVillager, 1);}
    public Eff villagerXP(int amount) {return type(EffectType.XpToVillager, amount);}
    public Eff food(int amount){return type(EffectType.Food, amount);}
    public Eff wood(int amount){return type(EffectType.Wood, amount);}
    public Eff fate(int amount) {return type(EffectType.Fate, amount);}
    public Eff morale(int amount) {return type(EffectType.Morale, amount);}
    public Eff gem(int amount) {return type(EffectType.Gem, amount);}
    public Eff storage(int amount) {return type(EffectType.FoodStorage, amount);}
    public Eff brain(int amount) {return type(EffectType.Brain, amount);}
    public Eff death(int amount) {return type(EffectType.DEATH, amount);}
    public Eff lose() {return type(EffectType.Lose, 1);}

    public void clearActivation() {
        this.effAct=new EffAct(EffAct.ActivationType.NOW,0);
    }

    private Eff setActivation(EffAct activation){
        if(this.effAct==null){
            System.err.println(this+": trying to overwrite type: "+this.effAct+" to "+activation);
        }
        this.effAct = activation;
        return this;
    }

    private Eff type(EffectType type, int amount){
        if(this.type!=null){
            System.err.println(this+": trying to overwrite type: "+this.type+" to "+type);
        }
        this.type=type;
        this.value=amount;
        return this;
    }


    public int getAdjustedValue() {
        return value+bonus;
    }

    public static Array<Eff> copyArray(Array<Eff> effects) {
        Array<Eff> results = new Array<>();
        for(Eff e:effects){
            results.add(e.copy());
        }
        return results;
    }

    public static Array<Eff> copyArray(Eff[] effects) {
        Array<Eff> results = new Array<>();
        for(Eff e:effects){
            results.add(e.copy());
        }
        return results;
    }

    public Eff invert() {
        this.value = -this.value;
        return this;
    }

}
