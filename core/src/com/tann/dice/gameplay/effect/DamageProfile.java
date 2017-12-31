package com.tann.dice.gameplay.effect;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.entity.DiceEntity;

public class DamageProfile {
    private int incomingDamage;
    private int blockedDamage;
    private int heals;
    public Array<Buff> incomingBuffs = new Array<>();
    DiceEntity target;
    public Array<Eff> effs = new Array<>();

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
        heals = 0;
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
            case Buff:
                incomingBuffs.add(new Buff(target, e.buffType, e.value, -1));
                break;
        }
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
        target.damage(Math.max(0, getIncomingDamage() - blockedDamage));
        for(Buff b: incomingBuffs){
            target.addBuff(b);
        }
        reset();
        target.getEntityPanel().layout();
    }

    Array<Buff> allBuffs = new Array<>();
    public int getIncomingDamage(){
        allBuffs.clear();
        allBuffs.addAll(incomingBuffs);
        allBuffs.addAll(target.getBuffs());
        for(Buff b:allBuffs){
            if(b.type == Buff.BuffType.stealth){
                return 0;
            }
        }
        return incomingDamage;
    }

    public boolean isGoingToDie(){
        return getEffectiveHp() <= 0;
    }

    public int getEffectiveHp() {
        return getTopHealth() + blockedDamage - getIncomingDamage();
    }

    public int getTopHealth() {
        return Math.min(target.getMaxHp(), target.getHp() + heals);
    }

    public int totalIncoming() {
        return Math.max(0, getIncomingDamage() - blockedDamage);
    }

    public int getOverkill() {
        return getIncomingDamage()  - Math.min(target.getMaxHp(), target.getHp() + heals) - blockedDamage;
    }
}
