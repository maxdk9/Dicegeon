package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;

public class EntityGroup {
    private Array<DiceEntity> entities;
    private Array<DiceEntity> activeEntities;
    private Array<Buff> buffs;

    public void die(DiceEntity de){
        activeEntities.removeValue(de, true);
    }

    public DiceEntity getRandomActive(boolean targetRestriction){
        if(!targetRestriction) return activeEntities.random();
        int mod = (int) (Math.random()*activeEntities.size);
        for(int i=mod;i<mod+activeEntities.size;i++){
            DiceEntity de = activeEntities.get(i%activeEntities.size);
            if(de.canBeTargeted()) return de;
        }
        return null;
    }

    public Array<DiceEntity> getEntities() {
        return entities;
    }

    public Array<DiceEntity> getActiveEntities() {
        return activeEntities;
    }

    public void setEntities(Array<? extends DiceEntity> intialEntities) {
        if(entities == null){
            entities = new Array<>();
            activeEntities = new Array<>();
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

    private static Array<DiceEntity> tmpALl = new Array<>();
    public static Array<DiceEntity> getAllActive(){
        tmpALl.clear();
        tmpALl.addAll(Party.get().getActiveEntities());
        tmpALl.addAll(Room.get().getActiveEntities());
        return tmpALl;
    }

    public static void activateDamage() {
        Array<DiceEntity> all = EntityGroup.getAllActive();
        for(int i=0;i<all.size;i++){
            DiceEntity de = all.get(i);
            de.getProfile().action();
            de.upkeep();
            de.getEntityPanel().layout();
        }
    }

    private static Array<DiceEntity> targetsTmp = new Array<>();
    public static Array<DiceEntity> getValidTargets(Eff.TargetingType type, boolean player){
        targetsTmp.clear();
        Array<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        Array<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
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

    public static Array<DiceEntity> getActualTargets(Eff.TargetingType type, boolean player, DiceEntity target){
        targetsTmp.clear();
        Array<DiceEntity> friends = player ? Party.get().getActiveEntities() : Room.get().getActiveEntities();
        Array<DiceEntity> enemies = player ? Room.get().getActiveEntities() : Party.get().getActiveEntities();
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
