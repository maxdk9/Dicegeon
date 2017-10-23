package com.tann.dice.gameplay.village;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Eff.EffectType;

public class Buff {
    public boolean dead;


    public enum BuffType{
        BonusFoodFromDice,
        BonusWoodFromDice,
        Rerolls
    }
    public BuffType type;
    int value=0;
    int turns=1;
    int turnsLeft=1;

    public Buff copy(){
        Buff result = new Buff();
        result.type=type;
        result.value=value;
        result.turns=turns;
        result.resetTurns();
        return result;
    }

    public Buff(){}

    public Buff bonusFood(int amount){
        return type(BuffType.BonusFoodFromDice, amount);
    }

    public Buff bonusWood(int amount){
        return type(BuffType.BonusWoodFromDice, amount);
    }

    public Buff rerolls(int amount){
        return type(BuffType.Rerolls, amount);
    }

    public Buff forTurns(int amount){
        this.turns=amount;
        resetTurns();
        return this;
    }

    public void resetTurns(){
        this.turnsLeft=this.turns;
    }

    private Buff type(BuffType type, int value){
        this.type=type;
        this.value=value;
        return this;
    }

    public void process(Eff e){
        switch(type){
            case BonusFoodFromDice:
                if(e.sourceDie!=null && e.type== EffectType.Food){
                    e.addBonus(value);
                }
                break;
            case BonusWoodFromDice:
                if(e.sourceDie!=null && e.type== EffectType.Wood){
                    e.addBonus(value);
                }
                break;
            case Rerolls:
                break;
        }
    }

    public void turn(){
        this.turnsLeft--;
        this.dead=turnsLeft==0;
    }

    public String getValueString(){
        return value>0?"+"+value:""+value;
    }


    public String toWriterString(){
        switch(type){
            case BonusFoodFromDice:
                return getValueString()+"[h][dice][food]";
            case BonusWoodFromDice:
                return getValueString()+"[dice][wood]";
            case Rerolls:
                return getValueString()+"[dice]";
        }
        return "";
    }
}
