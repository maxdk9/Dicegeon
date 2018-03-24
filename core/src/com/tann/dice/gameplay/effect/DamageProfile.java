package com.tann.dice.gameplay.effect;

import com.tann.dice.gameplay.effect.buff.Buff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.DiceEntity;
import static com.tann.dice.gameplay.effect.Eff.EffType.*;
import java.util.ArrayList;
import java.util.List;

public class DamageProfile {
    DiceEntity target;
    public List<Eff> effs = new ArrayList<>();

    public DamageProfile(DiceEntity entity){
        this.target = entity;
    }

    public void reset() {
        effs.clear();
        somethingChanged();
    }

    public void somethingChanged(){
        incomingPoison = null;
        incomingDamage = null;
        buffs = null;
        execute = null;
        blockedDamage = null;
        heals = null;
    }


    public void addEffect(Eff e){
        effs.add(e);
        somethingChanged();
    }

    private Integer incomingDamage = null;
    public int getIncomingDamage(){
        if(incomingDamage == null){
            incomingDamage = 0;
            for(Eff e:effs){
                if(e.type == Damage){
                    incomingDamage+= e.getValue(target);
                }
            }
            for(Trigger t:target.getActiveTriggers()){
                incomingDamage = t.alterIncomingDamage(incomingDamage);
            }
        }
        return incomingDamage;
    }

    private List<Buff> buffs = null;
    public List<Buff> getIncomingBuffs(){
        if(buffs == null){
            buffs = new ArrayList<>();
            for(Eff e:effs){
                if(e.type==Buff){
                    Buff buff = e.buff.copy();
                    buff.target = target;
                    buffs.add(buff);
                }
            }
        }
        return buffs;
    }

    private Integer incomingPoison;
    public int getIncomingPoisonDamage(){
        if(incomingPoison == null){
            incomingPoison = 0;
            for(Trigger t:target.getActiveTriggers()){
                incomingPoison += t.getIncomingPoisonDamage();;
            }
        }
        return incomingPoison;
    }


    private Integer blockedDamage;
    public int getBlockedDamage(){
        if(blockedDamage == null){
            blockedDamage = 0;
            for (Eff e:effs){
                if(e.type == Shield){
                    blockedDamage += e.getValue(target);
                }
            }
        }
        return blockedDamage;
    }

    private Integer heals;
    public int getHeals(){
        if(heals == null){
            heals = 0;
            for (Eff e:effs){
                if(e.type == Healing){
                    heals += e.getValue(target);
                }
            }
        }
        return heals;
    }

    Boolean execute;
    public boolean getExecute(){
        if(execute == null){
            execute = false;
            for (Eff e:effs){
                if(e.type == Execute && target.getHp() == e.getValue(target)){
                    execute = true;
                    break;
                }
            }
        }
        return execute;
    }

    public void removeEffsFromSource(DiceEntity entity) {
        boolean removed = false;
        for(int i=effs.size()-1;i>=0;i--){
            Eff e = effs.get(i);
            if(e.source == entity){
                effs.remove(e);
                removed = true;
            }
        }
        if(removed) somethingChanged();
    }

    public void action(){
        target.heal(getHeals());
        target.damage(Math.max(0, getIncomingDamage() - getBlockedDamage()));
        for(Eff e:effs){
            target.attackedBy(e.source);
        }
        for(Buff b: getIncomingBuffs()){
            target.addBuff(b);
        }
        if(getExecute()){
            target.kill();
        }
        reset();
        target.somethingChanged();
    }

    public boolean isGoingToDie(boolean includePoison){
        return getEffectiveHp()-(includePoison?getIncomingPoisonDamage():0) <= 0 || getExecute();
    }

    public int getEffectiveHp() {
        return getTopHealth() + getBlockedDamage() - getIncomingDamage();
    }

    public int getTopHealth() {
        return Math.min(target.getMaxHp(), target.getHp() + getHeals());
    }

    public int unblockedRegularIncoming() {
        return Math.max(0, getIncomingDamage() - getBlockedDamage());
    }

    public int unblockedTotalIncoming() {
        return unblockedRegularIncoming()+getIncomingPoisonDamage();
    }

    public int getOverkill(boolean poison) {
        int regularOverkill = unblockedRegularIncoming()  - Math.min(target.getMaxHp(), target.getHp() + getHeals());
        if(!poison) return regularOverkill;
        else return getIncomingPoisonDamage() + Math.min(0, regularOverkill);
    }

}
