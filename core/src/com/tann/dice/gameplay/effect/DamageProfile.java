package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.entity.DiceEntity;

public class DamageProfile {
    public int incomingDamage;
    public int blockedDamage;
    public int dotDamage;
    public int heals;
    public Array<Buff> incomingBuffs = new Array<>();
    DiceEntity target;
    public Array<Eff> effs = new Array<>();
    boolean goingToDie = false;

    public DamageProfile(DiceEntity entity){
        this.target = entity;
    }

    private void reset() {
        resetValues();
        effs.clear();
    }

    private void resetValues() {
        incomingBuffs.clear();
        incomingDamage = 0;
        blockedDamage = 0;
        dotDamage = 0;
        heals = 0;
        goingToDie = false;
    }

    public void addEffect(Eff e){
        add(e, true);
    }

    private void add(Eff e, boolean log){
        if(log) effs.add(e);
        switch (e.type) {
            case Nothing:
            case Magic:
                break;
            case Sword:
                incomingDamage += e.value;
                break;
            case Shield:
                blockedDamage += e.value;
                break;
            case Heal:
                heals += e.value;
                break;
            case Poison:
                incomingBuffs.add(new Buff(Buff.BuffType.dot, e.value, -1));
                dotDamage += e.value;
                break;
        }
        boolean nowGoingToDie = isGoingToDie();
        if(!goingToDie && nowGoingToDie){
            target.potentialDeath();
        }
        goingToDie = isGoingToDie();
    }

    public void removeEff(Eff remove){
        if(!effs.contains(remove, true)){
            return;
        }
        effs.removeValue(remove, true);
        resetValues();
        for(Eff e:effs){
            add(e,false);
        }
        target.getEntityPanel().layout();
    }

    public void action(){
        target.heal(heals);
        target.damage(Math.max(0, incomingDamage - blockedDamage));
        for(Buff b: incomingBuffs){
            target.addBuff(b);
        }
        reset();
    }

    public boolean isGoingToDie(){
        return getEffectiveHp() <= 0;
    }

    public int getEffectiveHp() {
        return getTopHealth() + blockedDamage - incomingDamage;
    }

    public int getTopHealth() {
        return Math.min(target.getMaxHp(), target.getHp() + heals);
    }

    public int totalIncoming() {
        return Math.max(0, incomingDamage - blockedDamage);
    }
}
