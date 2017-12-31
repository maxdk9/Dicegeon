package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup {
    private List<DiceEntity> entities;
    private List<DiceEntity> activeEntities;
    private List<Buff> buffs;

    public void die(DiceEntity de){
        activeEntities.remove(de);
    }

    public DiceEntity getRandomActive(boolean targetRestriction){
        if(!targetRestriction) return activeEntities.get((int)(Math.random()*activeEntities.size()));
        int mod = (int) (Math.random()*activeEntities.size());
        for(int i=mod;i<mod+activeEntities.size();i++){
            DiceEntity de = activeEntities.get(i%activeEntities.size());
            if(de.canBeTargeted()) return de;
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
        if(entities == null){
            entities = new ArrayList<>();
            activeEntities = new ArrayList<>();
        }
        entities.clear();
        activeEntities.clear();
        entities.addAll(intialEntities);
        activeEntities.addAll(intialEntities);
    }

    public void firstRoll(){
        for(DiceEntity entity:getActiveEntities()){
            entity.getDie().addToScreen();
            entity.getDie().resetForRoll();
        }
        roll();
    }

    public void roll(){
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
        for(int i=0;i<all.size();i++){
            DiceEntity de = all.get(i);
            de.getProfile().action();
            de.upkeep();
            de.getEntityPanel().layout();
        }
    }

    private static List<DiceEntity> targetsTmp = new ArrayList<>();
    public static List<DiceEntity> getValidTargets(Eff.TargetingType type, boolean player){
        targetsTmp.clear();
        List<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        switch(type){
            case EnemySingle:
            case EnemyOnlyAdjacents:
            case EnemyAndAdjacents:
                for(DiceEntity de:enemies){
                    if(!de.slidOut && player || !de.canBeTargeted()) continue;
                    targetsTmp.add(de);
                }
                break;
            case EnemySingleRanged:
                for(DiceEntity de:enemies){
                    if(!de.canBeTargeted()) continue;
                    targetsTmp.add(de);
                }
                break;
            case FriendlySingle:
                targetsTmp.addAll(friends);
                break;
            case EnemyGroup:
            case FriendlyGroup:
            case Self:
            case Untargeted:
                break;
        }
        return targetsTmp;
    }

    public static List<DiceEntity> getActualTargets(Eff.TargetingType type, boolean player, DiceEntity target){
        targetsTmp.clear();
        List<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        List<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
        switch(type){
            case EnemySingle:
            case EnemySingleRanged:
            case Self:
            case FriendlySingle:
                targetsTmp.add(target);
                break;
            case EnemyAndAdjacents:
                targetsTmp.addAll(target.getAdjacents(true));
                break;
            case EnemyOnlyAdjacents:
                targetsTmp.addAll(target.getAdjacents(false));
                break;
            case EnemyGroup:
                targetsTmp.addAll(enemies);
                break;
            case FriendlyGroup:
                targetsTmp.addAll(friends);
                break;
            case Untargeted:
                break;
        }
        return targetsTmp;
    }
}
