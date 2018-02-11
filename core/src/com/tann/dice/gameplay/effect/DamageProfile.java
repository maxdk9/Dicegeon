package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;

import java.util.ArrayList;
import java.util.List;

public class DamageProfile {
    private int incomingDamage;
    private int blockedDamage;
    private int heals;
    boolean execute;
    public List<Buff> incomingBuffs = new ArrayList<>();
    DiceEntity target;
    public List<Eff> effs = new ArrayList<>();

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
            case Damage:
                incomingDamage += e.getValue();
                break;
            case Shield:
                blockedDamage += e.getValue();
                break;
            case Heal:
                heals += e.getValue();
                break;
            case Buff:
                Buff b = e.buff.copy();
                b.target = target;
                incomingBuffs.add(b);
                break;
            case Execute:
                if(target.getHp() == e.getValue()){
                    execute = true;
                }
                break;
        }
    }

    public void removeEff(Eff remove){
        if(!effs.contains(remove)){
            return;
        }
        effs.remove(remove);
        resetValues();
        for(Eff e:effs){
            add(e,false);
        }
        target.getEntityPanel().layout();
    }

    public void action(){
        target.heal(heals);
        target.damage(Math.max(0, getIncomingDamage() - blockedDamage));
        for(Eff e:effs){
            target.attackedBy(e.source);
        }
        for(Buff b: incomingBuffs){
            target.addBuff(b);
        }
        if(execute){
            target.kill();
        }
        reset();
        target.getEntityPanel().layout();
    }

    private List<Buff> allBuffs = new ArrayList<>();
    public int getIncomingDamage(){
        allBuffs.clear();
        allBuffs.addAll(incomingBuffs);
        allBuffs.addAll(target.getBuffs());
        int damage = incomingDamage;
        for(Buff b:allBuffs){
            damage = b.alterIncomingDamage(damage);
        }
        return damage;
    }

    public boolean isGoingToDie(){
        return getEffectiveHp() <= 0 || execute;
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
