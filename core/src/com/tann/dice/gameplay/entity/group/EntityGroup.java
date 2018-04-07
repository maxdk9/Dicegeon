package com.tann.dice.gameplay.entity.group;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff.TargetingType;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup {
    List<DiceEntity> entities = new ArrayList<>();
    protected List<DiceEntity> activeEntities = new ArrayList<>();
    private List<Buff> buffs;

    public void die(DiceEntity de){
        activeEntities.remove(de);
    }

    public DiceEntity getRandomActive(boolean targetRestriction){
        if(!targetRestriction) return activeEntities.get((int)(Math.random()*activeEntities.size()));
        int mod = (int) (Math.random()*activeEntities.size());
        for(int i=mod;i<mod+activeEntities.size();i++){
            DiceEntity de = activeEntities.get(i%activeEntities.size());
            return de;
        }
        return null;
    }

    public List<DiceEntity> getEntities() {
        return entities;
    }

    public List<DiceEntity> getActiveEntities() {
        return activeEntities;
    }

    public void setEntities(List<? extends DiceEntity> intialEntities) {
        entities.clear();
        activeEntities.clear();
        entities.addAll(intialEntities);
        activeEntities.addAll(intialEntities);
    }

    public void firstRoll(){
        roll(true);
    }

    protected void clearEntities() {
        entities.clear();
        activeEntities.clear();
    }

    public void roll(boolean firstRoll){
        if(firstRoll){
            for(DiceEntity entity:getActiveEntities()){
                entity.getDie().addToScreen();
                entity.getDie().resetForRoll();
            }
        }
        int amount = getActiveEntities().size();
        BulletStuff.addRollEffects(amount, firstRoll, false);

        for(DiceEntity entity:getActiveEntities()){
            entity.getDie().roll();
        }
    }

    private static List<DiceEntity> tmpALl = new ArrayList<>();
    public static List<DiceEntity> getAllActive(){
        tmpALl.clear();
        tmpALl.addAll(Party.get().getActiveEntities());
        tmpALl.addAll(Room.get().getActiveEntities());
        return tmpALl;
    }

    public static void activateDamage() {
        List<DiceEntity> all = EntityGroup.getAllActive();
        for(int i=all.size()-1;i>=0;i--){
            DiceEntity de = all.get(i);
            de.getProfile().action();
        }
        for(int i=all.size()-1;i>=0;i--){
            DiceEntity de = all.get(i);
            de.upkeep();
        }
    }

    private static List<DiceEntity> targetsTmp = new ArrayList<>();

    public static List<DiceEntity> getValidTargets(Targetable t, boolean player){
        Eff[] effects = t.getEffects();
        TargetingType type = effects[0].targetingType;
        DiceEntity source = null;
        if(t instanceof Die){
            Die d = (Die) t;
            source = d.entity;
        }
        targetsTmp.clear();
        List<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        switch(type){
            case EnemySingle:
            case EnemyOnlyAdjacents:
            case EnemyAndAdjacents:
                for(DiceEntity de:enemies){
                    if(!de.slidOut && player) continue;
                    targetsTmp.add(de);
                }
                break;
            case EnemySingleRanged:
            case EnemyAndAdjacentsRanged:
                for(DiceEntity de:enemies){
                    targetsTmp.add(de);
                }
                break;
            case FriendlySingle:
            case FriendlySingleAndAdjacents:
                targetsTmp.addAll(friends);
                break;
            case FriendlySingleOther:
                targetsTmp.addAll(friends);
                targetsTmp.remove(source);
                break;
            case AllTargeters:
                for(DiceEntity de:friends){
                    if(de.getAllTargeters().size()>0){
                        targetsTmp.add(de);
                    }
                }
                break;
            case EnemyGroup:
            case FriendlyGroup:
            case Self:
            case RandomEnemy:
            case Untargeted:
                break;
        }

        for(int i=targetsTmp.size()-1;i>=0;i--) {
            DiceEntity de = targetsTmp.get(i);
            boolean good = false;
            for(Eff e:effects) {
                switch (e.type) {
                    case Empty:
                    case Magic:
                        break;
                    case Buff:
                    case Damage:
                    case Reroll:
                        good = true;
                        break;
                    case Shield:
                        good = de.getProfile().unblockedRegularIncoming() > 0;
                        break;
                    case RedirectIncoming:
                        good = de.getProfile().getIncomingDamage() > 0;
                        break;
                    case Healing:
                        good = de.getProfile().getTopHealth() < de.getMaxHp();
                        break;
                    case Execute:
                        good = de.getHp() == e.getValue();
                        break;
                    case CopyAbility:
                        good = false;
                        if (de.getEntityPanel().holdsDie){
                            Side theirSide = de.getDie().getActualSide();
                            good= theirSide!= null && theirSide.getEffects()[0].type != Eff.EffType.CopyAbility;
                        }
                        break;
                    case Decurse:
                        good = de.hasNegativeBuffs();
                        break;
                }
                if(good) break;
            }
            if(!good){
                targetsTmp.remove(de);
            }
        }

        return targetsTmp;
    }

    public static List<DiceEntity> getActualTargets(Eff eff, boolean player, DiceEntity target){
        List<DiceEntity> result = new ArrayList<>();
        List<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        Eff.TargetingType type = eff.targetingType;
        switch(type){
            case EnemySingle:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
                result.add(target);
                break;
            case Self:
                result.add(eff.source);
                break;
            case EnemyAndAdjacents:
            case EnemyAndAdjacentsRanged:
            case FriendlySingleAndAdjacents:
                result.addAll(target.getAdjacents(true));
                break;
            case EnemyOnlyAdjacents:
                result.addAll(target.getAdjacents(false));
                break;
            case EnemyGroup:
                result.addAll(enemies);
                break;
            case FriendlyGroup:
                result.addAll(friends);
                break;
            case RandomEnemy:
                result.add(Tann.getRandom(enemies));
                break;
            case AllTargeters:
                result.addAll(target.getAllTargeters());
                break;
            case TopEnemy:
                result.add(enemies.get(enemies.size()-1));
                break;
            case BottomEnemy:
                result.add(enemies.get(0));
                break;
            case TopBottomEnemy:
                result.add(enemies.get(0));
                result.add(enemies.get(enemies.size()-1));
                break;
            case AllFront:
                for(DiceEntity de:enemies){
                    if(de.slidOut) result.add(de);
                }
                break;
            case Untargeted:
                break;
        }
        return result;
    }

    public static void clearTargetedHighlights() {
        for(DiceEntity de : getAllActive()){
            de.getEntityPanel().setTargeted(false);
            de.getEntityPanel().setArrowIntenity(0, 0);
        }
    }

    public void reset() {
    }

    public boolean allDiceStopped() {
        for(DiceEntity de:getAllActive()){
            Die.DieState state = de.getDie().getState();
            switch(state){
                case Rolling:
                case Unlocking:
                    return false;
                case Stopped:
                case Locked:
                case Locking:
                    break;
            }
        }
        return true;
    }

    public void somethingChanged() {
        for(DiceEntity de:entities){
            de.somethingChanged();
        }
    }
}
