package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;

public class EntityGroup {
    private Array<DiceEntity> entities;
    private Array<DiceEntity> activeEntities;
    private Array<Buff> buffs;

    public void die(DiceEntity de){
        activeEntities.removeValue(de, true);
    }

    public DiceEntity getRandomActive(){
        return activeEntities.random();
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
}
