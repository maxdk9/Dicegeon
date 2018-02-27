package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;

import java.util.List;

public class Eff {

    public enum TargetingType{
        EnemySingle, EnemySingleRanged, EnemyGroup, EnemyOnlyAdjacents, RandomEnemy,
        FriendlySingle, FriendlyGroup, EnemyAndAdjacents,
        Self, OnRoll, Untargeted, AllTargeters, EnemyAndAdjacentsRanged, DoesNothing
    }

    public TargetingType targetingType = TargetingType.EnemySingle;

    public enum EffectType{
        Nothing, Damage, Shield, Magic, Heal, Buff, Execute, Reroll
	}
	

	public EffectType type;
    public Buff buff;
    public int buffDuration;
	private int value;
	public DiceEntity source;
	boolean nextTurn;

    public Eff(){}

    public String getValueString(){
	     return (value>=0?"":"-")+Math.abs(value);
    }

    public String toString(){
        String result;
        switch(type){
            case Nothing:
                result = "Nothing!"; break;
            case Damage:
                switch(targetingType){
                    case EnemySingle: result = value +" damage to an enemy"; break;
                    case EnemySingleRanged: result = value +" damage to ANY enemy"; break;
                    case EnemyGroup: result = value +" damage to ALL enemies"; break;
                    case EnemyAndAdjacents: result = value +" damage an enemy and both adjacent enemies"; break;
                    case EnemyAndAdjacentsRanged: result = value +" damage ANY enemy and both adjacent enemies"; break;
                    case AllTargeters: result = value+" damage to all enemies who have targeted the hero of your choice"; break;
                    default: result = "ahh help damage"; break;
                }
                break;
            case Shield:
                result = "Block "+value+" incoming damage ([yellow][heart][light]) to ";
                switch(targetingType){
                    case FriendlySingle: result += "one hero"; break;
                    case FriendlyGroup: result += "everyone"; break;
                    case Self: result += "yourself"; break;
                    default: result += "????"; break;
                }
                break;
            case Magic: result = "Gain "+value+" magic to spend on spells this turn"; break;
            case Heal: result = "Restore "+value+" missing health ([purple][heartEmpty][light]) to a damaged character"; break;
            case Execute: result = "Kills target if they are on exactly "+value+" hp"; break;
            case Reroll: result = "When you roll this, gain +1 reroll this turn"; break;
            case Buff: result = buff.toNiceString(); break;
            default: result = "uhoh unknown "+type;
        }
        if(nextTurn){
            result += " next turn";
        }
        return result;
    }

    public Eff nothing() { return type(EffectType.Nothing, 0).targetType(Eff.TargetingType.DoesNothing); }
    public Eff damage(int amount) { return type(EffectType.Damage, amount); }
    public Eff shield(int amount) { return type(EffectType.Shield, amount); }
    public Eff magic(int amount) { return type(EffectType.Magic, amount); }
    public Eff heal(int amount) { return type(EffectType.Heal, amount); }
    public Eff execute(int amount) { return type(EffectType.Execute, amount); }
    public Eff reroll(int amount) { return type(EffectType.Reroll, amount); }


    public Eff buff(Buff buff){this.buff = buff; return type(EffectType.Buff); }

    public Eff allTargeters() { return targetType(TargetingType.AllTargeters); }
    public Eff friendlySingle() { return targetType(TargetingType.FriendlySingle);}
    public Eff friendlyGroup() { return targetType(TargetingType.FriendlyGroup);}
    public Eff enemySingle() { return targetType(TargetingType.EnemySingle);}
    public Eff enemyGroup() { return targetType(TargetingType.EnemyGroup);}
    public Eff untargeted() { return targetType(TargetingType.Untargeted);}
    public Eff enemyAndAdjacents() { return targetType(TargetingType.EnemyAndAdjacents);}
    public Eff enemyAndAdjacentsRanged() { return targetType(TargetingType.EnemyAndAdjacentsRanged);}
    public Eff ranged() { return targetType(TargetingType.EnemySingleRanged);}
    public Eff self() { return targetType(TargetingType.Self);}
    public Eff onRoll() { return targetType(TargetingType.OnRoll);}
    public Eff randomEnemy() { return targetType(TargetingType.RandomEnemy);}
    public Eff justValue(int amount) {this.value = amount; return this;}

    public Eff nextTurn() {
        this.nextTurn = true;
        return this;
    }

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

    private Eff type(EffectType type){
        if(this.type!=null){
            System.err.println(this+": trying to overwrite type: "+this.type+" to "+type);
        }
        this.type=type;
        return this;
    }

    public Eff copy(){
        Eff e = new Eff();
        e.targetingType = targetingType;
        e.type = type;
        e.value = value;
        e.source = source;
        if(buff!=null){
            e.buff = buff.copy();
        }
        e.buffDuration = buffDuration;
        e.nextTurn = nextTurn;
        return e;
    }

    public void setSource(DiceEntity entity){
        this.source = entity;
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

    public int getValue() {
        int actualValue = value;
        if(source != null) {
            List<Buff> buffs = source.getBuffs();
            for(int i=0;i<buffs.size();i++){
                Buff b = buffs.get(i);
                actualValue = b.alterOutgoingDamage(type, actualValue);
            }
        }
        return actualValue;
    }

    public void untargetedUse(boolean delayed) {
        switch(type) {
            case Magic:
                if(nextTurn && !delayed){
                    Party.get().addNextTurnEffect(this);
                }
                else{
                    Party.get().addMagic(value);
                }
                break;
        }
    }

    public String getNoTargetsString() {
        switch(type){
            case Shield:
                return "No incoming damage to block";
            case Heal:
                return "No damaged heroes to heal";
        }
        return "I dunno";
    }

    public boolean isTargeted() {
        switch(targetingType){
            case EnemySingle:
            case EnemySingleRanged:
            case FriendlySingle:
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
                return true;
            case EnemyGroup:
            case EnemyOnlyAdjacents:
            case RandomEnemy:
            case FriendlyGroup:
            case Self:
            case OnRoll:
            case Untargeted:
            case AllTargeters:
            case DoesNothing:
                return false;
            default:
        }
        return false;
    }

    public boolean needsUsing() {
        switch(targetingType){
            case EnemySingle:
            case EnemySingleRanged:
            case FriendlySingle:
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
            case EnemyGroup:
            case EnemyOnlyAdjacents:
            case RandomEnemy:
            case FriendlyGroup:
            case Self:
            case Untargeted:
            case AllTargeters:
                return true;
            case OnRoll:
            case DoesNothing:
                return false;
        }
        return false;
    }

}
