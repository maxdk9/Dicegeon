package com.tann.dice.gameplay.fightLog;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Targetable;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.fightLog.action.Command;
import com.tann.dice.gameplay.entity.EntityState;

import com.tann.dice.util.Tann;
import java.util.ArrayList;
import java.util.List;

public class Snapshot {


    List<EntityState> entityStateList = new ArrayList<>();
    List<EntityState> aliveHeroes = new ArrayList<>();
    List<EntityState> aliveMonsters = new ArrayList<>();
    Command recentestCommand;

    public Snapshot(Party party, Room room) {
    }


    public void action(Command command){
        recentestCommand = command;

    }

    public void target(DiceEntity target, Targetable targetable) {
        for(Eff eff:targetable.getEffects()){
            for(EntityState es:getActualTargets(target, eff)){
                es.hit(eff);
            }
        }
    }

    private List<EntityState> getActualTargets(DiceEntity targetEntity, Eff eff){
        boolean player = false;
        if(eff.source == null || eff.source.isPlayer()){
            player = true;
        }
        EntityState target = getState(targetEntity);
        EntityState source = getState(eff.source); // could be null if a spell
        Eff.TargetingType type = eff.targetingType;
        List<EntityState> result = new ArrayList<>();

        List<EntityState> friends = player ? aliveHeroes : aliveMonsters;
        List<EntityState> enemies = player ? aliveMonsters : aliveHeroes;

        switch(type){
            case EnemySingle:
            case enemyHalfHealthOrLess:
            case EnemySingleRanged:
            case FriendlySingle:
            case FriendlySingleOther:
                result.add(target);
                break;
            case Self:
                result.add(source);
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
            case Allies:
                result.addAll(friends);
                result.remove(eff.source);
                break;
            case FriendlyGroup:
                result.addAll(friends);
                break;
            case RandomEnemy:
                result.add(Tann.getRandom(enemies));
                break;
            case AllTargeters:
                //TODO this
//                result.addAll(getState(target.getAllTargeters()));
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
                for(EntityState enemy:enemies){
                    if(!enemy.getForwards()) result.add(enemy);
                }
                break;
            case FriendlyMostDamaged:
                int mostDamage = -1;
                EntityState record = null;
                for(EntityState friend:friends){
                    int damage = friend.getMaxHp()-friend.getHp();
                    if(damage>mostDamage){
                        mostDamage = damage;
                        record = friend;
                    }
                }
                result.add(record);
                break;
            case Untargeted:
                break;
        }
        return result;
    }

    private EntityState getState(DiceEntity entity){
        for(EntityState es:entityStateList){
            if(es.getEntity() == entity){
                return es;
            }
        }
        return null;
    }

    private List<EntityState> getStates(List<DiceEntity> entities){
        List<EntityState> results = new ArrayList<>();
        for(EntityState es:entityStateList){
            for(DiceEntity de:entities){
                if(de==es.getEntity()){
                    results.add(es);
                }
            }
        }
        return results;
    }
}
