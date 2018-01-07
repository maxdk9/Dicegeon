package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.buff.DamageMultiplier;
import com.tann.dice.gameplay.entity.die.Die;

public class Eff {

    public enum TargetingType{
        EnemySingle,
        EnemySingleRanged,
        EnemyGroup,
        FriendlySingle,
        FriendlyGroup,
        EnemyAndAdjacents,
        EnemyOnlyAdjacents,
        Self,
        OnRoll,
        Untargeted,
        RandomEnemy;
    }

    public TargetingType targetingType = TargetingType.EnemySingle;

    public enum EffectType{
        Nothing,
        Damage,
        Shield,
        Magic,
        Heal,
        Buff,
        Execute,
        Reroll


        ;

        //objectives

        EffectType(){
        }

	}
	

	public EffectType type;
    public com.tann.dice.gameplay.effect.buff.Buff.BuffType buffType;
    public int buffDuration;
	public int value;
	public Die sourceDie;

    public Eff(){};

    public String getValueString(){
	     return (value>=0?"":"-")+Math.abs(value);
    }

    public String toString(){
        String result = "";
        switch(type){
            case Nothing:
                return "Nothing!";
            case Damage:
                switch(targetingType){
                    case EnemySingle:
                        return value +" damage to a forward enemy";
                    case EnemySingleRanged:
                        return value +" damage to any enemy";
                    case EnemyGroup:
                        return value +" damage to all enemies";
                    case EnemyAndAdjacents:
                        return value +" damage a forward enemy and both adjacent enemies";
                }
                return "ahh help damage";
            case Shield:
                switch(targetingType){
                    case FriendlySingle:
                        return "Block "+value+" incoming damage from anyone";
                    case FriendlyGroup:
                        return "Block "+value+" incoming damage from everyone";
                    case Self:
                        return "Block "+value+" incoming damage from yourself";
                }
                return "ahh help shield";
            case Magic:
                return "Gain "+value+" magic to spend on spells this inturnal.";
            case Heal:
                return "Restore "+value+" health to a damaged character";
            case Execute:
                return "Kills target if they are on exactly "+value+" hp";
            case Reroll:
                return "When you roll this, gain +1 reroll this inturnal";
            case Buff:
                return buffType.description+(buffDuration==-1?"":" for "+
                        buffDuration+(buffDuration==1?" inturnal":"turns"));
        }
        return "yeowch?? "+type;
    }

    public Eff nothing() { return type(EffectType.Nothing, -1); }
    public Eff damage(int amount) { return type(EffectType.Damage, amount); }
    public Eff shield(int amount) { return type(EffectType.Shield, amount); }
    public Eff magic(int amount) { return type(EffectType.Magic, amount); }
    public Eff heal(int amount) { return type(EffectType.Heal, amount); }
    public Eff execute(int amount) { return type(EffectType.Execute, amount); }
    public Eff reroll(int amount) { return type(EffectType.Reroll, amount); }

    public Eff poison(int amount) {return buff(amount, com.tann.dice.gameplay.effect.buff.Buff.BuffType.dot, -1); }
    public Eff stealth() {return buff(0, com.tann.dice.gameplay.effect.buff.Buff.BuffType.stealth, 1);}
    public Eff buff(int amount, com.tann.dice.gameplay.effect.buff.Buff.BuffType type, int duration){return type(EffectType.Buff, amount).buffType(type, duration); }

    public Eff friendlySingle() { return targetType(TargetingType.FriendlySingle);}
    public Eff friendlyGroup() { return targetType(TargetingType.FriendlyGroup);}
    public Eff enemySingle() { return targetType(TargetingType.EnemySingle);}
    public Eff enemyGroup() { return targetType(TargetingType.EnemyGroup);}
    public Eff untargeted() { return targetType(TargetingType.Untargeted);}
    public Eff enemyAndAdjacents() { return targetType(TargetingType.EnemyAndAdjacents);}
    public Eff ranged() { return targetType(TargetingType.EnemySingleRanged);}
    public Eff self() { return targetType(TargetingType.Self);}
    public Eff onRoll() { return targetType(TargetingType.OnRoll);}
    public Eff randomEnemy() { return targetType(TargetingType.RandomEnemy);}



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

    private Eff buffType(com.tann.dice.gameplay.effect.buff.Buff.BuffType type, int duration){
        this.buffType = type;
        this.buffDuration = duration;
        return this;
    }

    public Eff copy(){
        Eff e = new Eff();
        e.targetingType = targetingType;
        e.type = type;
        e.value = value;
        e.sourceDie = sourceDie;
        e.buffType = buffType;
        e.buffDuration = buffDuration;
        return e;
    }

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
