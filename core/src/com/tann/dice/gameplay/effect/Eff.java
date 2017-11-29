package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Die;

public class Eff {

    public void resetBonus() {
        this.bonus=0;
    }

    public void addBonus(int value) {
        this.bonus+=value;
    }

    public enum EffectType{
        Nothing(Images.heart_empty),
        Sword(Images.side_sword),
        Arrow(Images.spell_bow),
        Shield(Images.side_sword),
        Magic(Images.heart),
        Heal(Images.heart);

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
    public Eff(){this( null);};

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

    public Eff nothing() { return type(EffectType.Nothing, -1); }
    public Eff sword(int amount) { return type(EffectType.Sword, amount); }
    public Eff shield(int amount) { return type(EffectType.Shield, amount); }
    public Eff magic(int amount) { return type(EffectType.Magic, amount); }
    public Eff heal(int amount) { return type(EffectType.Heal, amount); }
    public Eff arrow(int amount) { return type(EffectType.Heal, amount); }

    public Eff eachTurn(int numTurns){return setActivation(new EffAct(EffAct.ActivationType.FOR_TURNS, numTurns));}
    public Eff inTurns(int numTurns) {return setActivation(new EffAct(EffAct.ActivationType.IN_TURNS, numTurns));}
    public Eff upkeep(){return setActivation(new EffAct(EffAct.ActivationType.UPKEEP, -1));}
    public Eff now() {return setActivation(new EffAct(EffAct.ActivationType.NOW, 0));}


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
