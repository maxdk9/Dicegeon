package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.effect.trigger.types.TriggerAllSidesBonus;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.util.Sounds;

import java.util.List;

public class Eff {

    public enum TargetingType{
        EnemySingle, EnemySingleRanged, EnemyAndAdjacents, EnemyGroup, EnemyOnlyAdjacents, RandomEnemy, EnemyAndAdjacentsRanged, enemyHalfHealthOrLess,
        FriendlySingle, FriendlySingleOther, FriendlyGroup, FriendlySingleAndAdjacents, FriendlyMostDamaged,
        Self, OnRoll, Untargeted, AllTargeters,
        TopEnemy, BottomEnemy, TopBottomEnemy, AllFront,
        DoesNothing, Allies
    }

    public TargetingType targetingType = TargetingType.EnemySingle;

    public enum EffType {
        Damage, Shield, Magic, Healing,
        Empty, Buff, Execute, Reroll, RedirectIncoming,
        CopyAbility, Decurse, Hook, Summon, DestroyAllSummons
	}


    public EffType type;
    private Buff buff;
    public int buffDuration;
    private int value;
    public DiceEntity source;
    boolean nextTurn;
    public String summonType;

    public Eff(){}

    public String getBaseString(){
        switch(type){
            case Empty:
                return "Does nothing, better hope you have a reroll left";
            case Damage:
                return getValue() +" damage";
            case Shield:
                return "Block "+getValue()+" incoming damage ([yellow][heart][yellow])";
            case Magic:
                return "Gain "+getValue()+" magic to spend on spells";
            case Healing:
                return "Restore "+getValue()+" missing health ([purple][heartempty][purple]) to";
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
            case Summon:
                return "Summon "+getValue()+" "+summonType+(getValue()==1?"":"s");
            case DestroyAllSummons:
                return "Destroy all summoned monsters";
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
                    case enemyHalfHealthOrLess: result += " to an enemy on half hp or less"; break;
                    case EnemySingleRanged: result +=" to ANY enemy"; break;
                    case EnemyGroup: result += " to ALL enemies"; break;
                    case EnemyAndAdjacents: result += " to an enemy and both adjacent enemies"; break;
                    case EnemyAndAdjacentsRanged: result += " to ANY enemy and both adjacent enemies"; break;
                    case TopEnemy: result += " to the top enemy"; break;
                    case BottomEnemy: result += " to the bottom enemy"; break;
                    case TopBottomEnemy: result += " to the top and bottom enemy"; break;
                    case AllFront: result += " to all forward enemies"; break;
                    case AllTargeters: result += " to all enemies who have targeted the hero of your choice"; break;
                    case RandomEnemy: result += " to a random enemy"; break;
                    case Self: result = "Take "+getValue()+" damage"; break;
                    default: result = "ahh help damage"; break;
                }
                break;
            case Shield:
                result = getBaseString();
                switch(targetingType){
                    case FriendlySingleOther: result += " from one other hero"; break;
                    case FriendlySingleAndAdjacents: result += " from one hero and both adjacents"; break;
                    case FriendlySingle: result += " from any hero"; break;
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
                    case FriendlySingle: result += " any damaged character"; break;
                    case FriendlyGroup: result += " ALL damaged characters"; break;
                    case FriendlyMostDamaged: result += " the most-damaged friendly character"; break;
                    case Self: result += " yourself"; break;
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
    public Eff summon(String monsterType, int value) {this.summonType=monsterType; return type(EffType.Summon, value); }
    public Eff destroyAllSummons() {return type(EffType.DestroyAllSummons); }

    public Eff enemySingle() { return targetType(TargetingType.EnemySingle);} //implicit
    public Eff allTargeters() { return targetType(TargetingType.AllTargeters); }
    public Eff friendlySingle() { return targetType(TargetingType.FriendlySingle);}
    public Eff friendlyAndAdjacents() { return targetType(TargetingType.FriendlySingleAndAdjacents);}
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
    public Eff friendlyMostDamaged() { return targetType(TargetingType.FriendlyMostDamaged);}
    public Eff allies() { return targetType(TargetingType.Allies);}
    public Eff enemyHalfOrLess() {return targetType(TargetingType.enemyHalfHealthOrLess);}

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
        e.summonType = summonType;
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
        TargetingType previousTarget;
        for(int i=0;i<effects.length-1;i++){
            Eff e = effects[i];
            if (i < effects.length-1 && effects[i+1].targetingType!=e.targetingType){
                result += e.toString();
            }
            else if(i!=0) result += e.getBaseString();
            else result += e.getBaseString().toLowerCase();
            result += " and ";
        }
        String lastEff = effects[effects.length-1].toString();
        if(effects.length>1) lastEff = lastEff.toLowerCase();
        result += lastEff;
        return  result;
    }

    public int getValue() {
        int actualValue = value; // + bonusFromSide;
        if(source != null) {
            List<Trigger> triggers = source.getActiveTriggers();
            for(int i=0;i<triggers.size();i++){
                Trigger t = triggers.get(i);
                actualValue = t.alterOutgoingEffect(type, actualValue);
            }
        }
        return actualValue;
    }

    public int getValue(DiceEntity target) {
        int actualValue = getValue();
        List<Trigger> triggers = target.getActiveTriggers();
        for(int i=0;i<triggers.size();i++){
            Trigger t = triggers.get(i);
            actualValue = t.alterIncomingEffect(type, actualValue);
        }
        return actualValue;
    }

    public void untargetedUse(boolean delayed) {
        switch(type) {
            case Magic:
                playSound();
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
        switch(targetingType){
            case enemyHalfHealthOrLess:
                return "Can only target enemies on half health or less";
        }
        return "Invalid no target string";
    }

    public boolean isTargeted() {
        switch(targetingType){
            case EnemySingle:
            case enemyHalfHealthOrLess:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
            case FriendlySingleAndAdjacents:
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
            case AllTargeters:
                return true;
            case EnemyGroup:
            case EnemyOnlyAdjacents:
            case RandomEnemy:
            case FriendlyGroup:
            case Allies:
            case Self:
            case OnRoll:
            case Untargeted:
            case DoesNothing:
            case TopEnemy:
            case BottomEnemy:
            case TopBottomEnemy:
            case AllFront:
            case FriendlyMostDamaged:
                return false;
            default:
        }
        return false;
    }

    public boolean needsUsing() {
        switch(targetingType){
            case EnemySingle:
            case enemyHalfHealthOrLess:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
            case FriendlySingleAndAdjacents:
            case FriendlyMostDamaged:
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
            case Allies:
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

    public void playSound(){
        switch (type) {
            case Shield:
                Sounds.playSound(Sounds.blocks, 1, 1);
                break;
            case Healing:
                Sounds.playSound(Sounds.heals, 1, 1);
                break;
            case Damage:
                if(source==null || source.isPlayer()) {
                    Sounds.playSound(Sounds.fwips, 1, 1);
                }
                else{
                    Sounds.playSound(Sounds.hits, 1, 1);
                }
                break;
            case Magic:
                Sounds.playSound(Sounds.magic, 1, 1);
                break;
            case Buff:
                if(buff.trigger instanceof TriggerAllSidesBonus){
                    Sounds.playSound(Sounds.boost);
                }
                break;
            case CopyAbility:
                Sounds.playSound(Sounds.copy);
                break;
            case RedirectIncoming:
                Sounds.playSound(Sounds.taunt);
                break;
        }
    }

}

