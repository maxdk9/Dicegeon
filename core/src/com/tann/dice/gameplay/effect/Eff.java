package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Die;

public class Eff {

    public enum TargetingType{
        EnemySingle, EnemySingleRanged, EnemyGroup, FriendlySingle, FriendlyGroup, Untargeted;
    }

    public TargetingType targetingType = TargetingType.EnemySingle;

    public enum EffectType{
        Nothing(Images.heart_empty),
        Sword(Images.side_sword),
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
	public Die sourceDie;

    public Eff(){};

    public String getValueString(){
	     return (value>=0?"":"-")+Math.abs(value);
    }

    public String toString(){
        switch(type){
            case Nothing:
                return "Nothing!";
            case Sword:
                switch(targetingType){
                    case EnemySingle:
                        return value +" damage to a forward enemy";
                    case EnemySingleRanged:
                        return value +" damage to any enemy";
                    case EnemyGroup:
                        return value +" damage to all enemies";
                }
                return "ahh help sword";
            case Shield:
                switch(targetingType){
                    case FriendlySingle:
                        return "Block "+value+" incoming damage from anyone";
                    case FriendlyGroup:
                        return "Block "+value+" incoming damage from everyone";
                }
                return "ahh help shield";
            case Magic:
                return "Gain "+value+" magic to spend on spells this turn.";
            case Heal:
                return "Restore "+value+" health to a damaged character";
        }
        return "yeowch?? "+type;
    }

    public Eff nothing() { return type(EffectType.Nothing, -1); }
    public Eff sword(int amount) { return type(EffectType.Sword, amount); }
    public Eff shield(int amount) { return type(EffectType.Shield, amount); }
    public Eff magic(int amount) { return type(EffectType.Magic, amount); }
    public Eff heal(int amount) { return type(EffectType.Heal, amount); }

    public Eff friendlySingle() { return targetType(TargetingType.FriendlySingle);}
    public Eff friendlyGroup() { return targetType(TargetingType.FriendlyGroup);}
    public Eff enemySingle() { return targetType(TargetingType.EnemySingle);}
    public Eff enemyGroup() { return targetType(TargetingType.EnemyGroup);}
    public Eff untargeted() { return targetType(TargetingType.Untargeted);}
    public Eff ranged() { return targetType(TargetingType.EnemySingleRanged);}


    public Eff targetType(TargetingType type){
        this.targetingType = type;
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


    public Eff copy(){
        Eff e = new Eff();
        e.targetingType = targetingType;
        e.type = type;
        e.value = value;
        e.sourceDie = sourceDie;
        return e;
    }

//    public static Array<Eff> copyArray(Array<Eff> effects) {
//        Array<Eff> results = new Array<>();
//        for(Eff e:effects){
//            results.add(e.copy());
//        }
//        return results;
//    }
//
//    public static Array<Eff> copyArray(Eff[] effects) {
//        Array<Eff> results = new Array<>();
//        for(Eff e:effects){
//            results.add(e.copy());
//        }
//        return results;
//    }

    public Eff invert() {
        this.value = -this.value;
        return this;
    }

    public static String describe(Eff[] effects) {
        String result = "";
        for(int i=0;i<effects.length;i++){
            Eff e = effects[i];
            result += e.toString();
            if(i<effects.length-1) result += ", ";
        }
        return  result;
    }

}
