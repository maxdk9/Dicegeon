package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;

import java.util.List;

public class Eff {

    public enum TargetingType{
        EnemySingle, EnemySingleRanged, EnemyAndAdjacents, EnemyGroup, EnemyOnlyAdjacents, RandomEnemy, EnemyAndAdjacentsRanged,
        FriendlySingle, FriendlySingleOther, FriendlyGroup,
        Self, OnRoll, Untargeted, AllTargeters,
        TopEnemy, BottomEnemy, TopBottomEnemy, AllFront,
        DoesNothing
    }

    public TargetingType targetingType = TargetingType.EnemySingle;

    public enum EffType {
        Damage, Shield, Magic, Healing,
        Empty, Buff, Execute, Reroll, RedirectIncoming, CopyAbility, Decurse, Hook
	}


    public EffType type;
    private Buff buff;
    public int buffDuration;
    private int value;
    public DiceEntity source;
    boolean nextTurn;

    public Eff(){}

    public String getBaseString(){
        switch(type){
            case Empty:
                return "Nothing!";
            case Damage:
                return getValue() +" damage";
            case Shield:
                return "Block "+getValue()+" incoming damage ([yellow][heart][light])";
            case Magic:
                return "Gain "+getValue()+" magic to spend on spells";
            case Healing:
                return "Restore "+getValue()+" missing health ([purple][heartempty][light])";
            case Buff:
                return getBuff().toNiceString();
            case Execute:
                return "Kills target if they are on exactly "+getValue()+" hp";
            case Reroll:
                return "gain +1 reroll this turn";
            case RedirectIncoming:
                return "Redirect all attacks from a hero to you";
            case CopyAbility:
                return "Copy a side from another hero";
            case Decurse:
                return "Remove all negative effects";
            case Hook:
                return "Pull an enemy forwards";

        }
        return "no base for "+type;
    }

    public String toString(){
        String result = getBaseString();
        switch(type){
            case Empty:
                break;
            case Damage:
                switch(targetingType){
                    case EnemySingle: result += " to an enemy"; break;
                    case EnemySingleRanged: result +=" to ANY enemy"; break;
                    case EnemyGroup: result += " to ALL enemies"; break;
                    case EnemyAndAdjacents: result += " to an enemy and both adjacent enemies"; break;
                    case EnemyAndAdjacentsRanged: result += " to ANY enemy and both adjacent enemies"; break;
                    case TopEnemy: result += " to the top enemy"; break;
                    case BottomEnemy: result += " to the bottom enemy"; break;
                    case TopBottomEnemy: result += " to the top and bottom enemy"; break;
                    case AllFront: result += " to all forward enemies"; break;
                    case AllTargeters: result += " to all enemies who have targeted the hero of your choice"; break;
                    case Self: result = "Take "+value+" damage"; break;
                    default: result = "ahh help damage"; break;
                }
                break;
            case Shield:
                result = getBaseString();
                switch(targetingType){
                    case FriendlySingleOther: result += " from one other hero"; break;
                    case FriendlySingle: result += " from one hero"; break;
                    case FriendlyGroup: result += " from everyone"; break;
                    case Self: result += " from yourself"; break;
                    default: result += " from ????"+targetingType; break;
                }
                break;
            case Magic: result = getBaseString(); break;
            case Healing:
                result = getBaseString();
                switch(targetingType){
                    case FriendlySingleOther: result += " another damaged character"; break;
                    case FriendlySingle: result += " a damaged character"; break;
                    case FriendlyGroup: result += " ALL damaged characters"; break;
                    default: result += " Need description: " +targetingType;
                }
                break;
            case Execute: result = getBaseString(); break;
            case Reroll: result = "When you roll this, "+getBaseString(); break;
            case Buff: result = getBaseString(); break;
        }
        if(nextTurn){
            result += " next turn";
        }
        return result;
    }

    public Eff nothing() { return type(EffType.Empty, 0).targetType(Eff.TargetingType.DoesNothing); }
    public Eff damage(int amount) { return type(EffType.Damage, amount); }
    public Eff shield(int amount) { return type(EffType.Shield, amount); }
    public Eff magic(int amount) { return type(EffType.Magic, amount); }
    public Eff heal(int amount) { return type(EffType.Healing, amount); }
    public Eff execute(int amount) { return type(EffType.Execute, amount); }
    public Eff reroll(int amount) { return type(EffType.Reroll, amount); }
    public Eff buff(Buff buff){this.buff = buff; return type(EffType.Buff); }
    public Eff redirectIncoming(){return type(EffType.RedirectIncoming); }
    public Eff copyAbility() { return type(EffType.CopyAbility); }
    public Eff decurse() {return type(EffType.Decurse); }
    public Eff hook() {return type(EffType.Hook); }

    public Eff enemySingle() { return targetType(TargetingType.EnemySingle);} //implicit
    public Eff allTargeters() { return targetType(TargetingType.AllTargeters); }
    public Eff friendlySingle() { return targetType(TargetingType.FriendlySingle);}
    public Eff friendlySingleOther() { return targetType(TargetingType.FriendlySingleOther);}
    public Eff friendlyGroup() { return targetType(TargetingType.FriendlyGroup);}
    public Eff enemyGroup() { return targetType(TargetingType.EnemyGroup);}
    public Eff untargeted() { return targetType(TargetingType.Untargeted);}
    public Eff enemyAndAdjacents() { return targetType(TargetingType.EnemyAndAdjacents);}
    public Eff enemyAndAdjacentsRanged() { return targetType(TargetingType.EnemyAndAdjacentsRanged);}
    public Eff ranged() { return targetType(TargetingType.EnemySingleRanged);}
    public Eff self() { return targetType(TargetingType.Self);}
    public Eff onRoll() { return targetType(TargetingType.OnRoll);}
    public Eff randomEnemy() { return targetType(TargetingType.RandomEnemy);}
    public Eff topEnemy() { return targetType(TargetingType.TopEnemy);}
    public Eff botEnemy() { return targetType(TargetingType.BottomEnemy);}
    public Eff topBotEnemy() { return targetType(TargetingType.TopBottomEnemy);}
    public Eff allFront() { return targetType(TargetingType.AllFront);}

    public Eff justValue(int amount) {this.value = amount; return this;}

    public Eff nextTurn() {
        this.nextTurn = true;
        return this;
    }

    public Eff targetType(TargetingType type){
        this.targetingType = type;
        return this;
    }

    private Eff type(EffType type, int amount){
        if(this.type!=null){
            System.err.println(this+": trying to overwrite type: "+this.type+" to "+type);
        }
        this.type=type;
        this.value=amount;
        return this;
    }

    private Eff type(EffType type){
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
        for(int i=0;i<effects.length-1;i++){
            Eff e = effects[i];
            result += e.getBaseString() + " and ";
        }
        String lastEff = effects[effects.length-1].toString();
        if(effects.length==1) lastEff = lastEff.toLowerCase();
        result += lastEff;
        return  result;
    }

    public int getValue() {
        int actualValue = value; // + bonusFromSide;
//        if(source != null) {
//            List<Trigger> triggers = source.getActiveTriggers();
//            for(int i=0;i<triggers.size();i++){
//                Trigger t = triggers.get(i);
//                actualValue = t.alterOutgoingEffect(type, actualValue, source);
//            }
//        }
        return actualValue;
    }

    public int getValue(DiceEntity target) {
        int actualValue = getValue();
        List<Trigger> triggers = target.getActiveTriggers();
        for(int i=0;i<triggers.size();i++){
            Trigger t = triggers.get(i);
            actualValue = t.alterIncomingEffect(type, actualValue);
        }
        if(source!=null) {
            triggers = source.getActiveTriggers();
            for (int i = 0; i < triggers.size(); i++) {
                Trigger t = triggers.get(i);
                actualValue = t.alterOutgoingEffect(type, actualValue, source);
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
                    Party.get().addMagic(getValue());
                }
                break;
        }
    }

    public String getNoTargetsString() {
        switch(type){
            case Shield:
                return "No incoming damage to block";
            case RedirectIncoming:
                return "No incoming damage to redirect";
            case Healing:
                return "No damaged heroes to heal";
            case Execute:
                return "Can only target monsters on exactly "+getValue()+" hp";
            case CopyAbility:
                return "No sides left to copy";
        }
        return "Invalid no target string";
    }

    public boolean isTargeted() {
        switch(targetingType){
            case EnemySingle:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
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
            case TopEnemy:
            case BottomEnemy:
            case TopBottomEnemy:
            case AllFront:
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
            case FriendlySingleOther:
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
            case TopEnemy:
            case BottomEnemy:
            case TopBottomEnemy:
            case AllFront:
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

    public Buff getBuff(){
        Buff b = buff.copy();
        b.setValue(getValue());
        return b;
    }

}

