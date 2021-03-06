package com.tann.dice.gameplay.entity.group;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Buff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.ArrayList;
import java.util.List;

public class EntityGroup <T extends DiceEntity>{
    List<T> entities = new ArrayList<>();
    protected List<T> activeEntities = new ArrayList<>();
    private List<Buff> buffs;

    public void die(T de){
        activeEntities.remove(de);
    }

    public T getRandomActive(boolean targetRestriction){
        if(!targetRestriction) return activeEntities.get((int)(Math.random()*activeEntities.size()));
        int mod = (int) (Math.random()*activeEntities.size());
        for(int i=mod;i<mod+activeEntities.size();i++){
            T de = activeEntities.get(i%activeEntities.size());
            return de;
        }
        return null;
    }

    public List<T> getEntities() {
        return entities;
    }

    public List<T> getActiveEntities() {
        return activeEntities;
    }

    public void setEntities(List<? extends T> intialEntities) {
        entities.clear();
        activeEntities.clear();
        entities.addAll(intialEntities);
        activeEntities.addAll(intialEntities);
    }

    public void addEntity(T entity){
        entities.add(0, entity);
        activeEntities.add(0, entity);
        if(entity instanceof Monster) {
            DungeonScreen.get().layoutSidePanels();
        }
    }

    public void firstRoll(){
        for(DiceEntity de:getActiveEntities()){
            if(de.getDie().getState()!= Die.DieState.Stopped) return;
        }
        roll(true);
    }

    protected void clearEntities() {
        entities.clear();
        activeEntities.clear();
    }

    public void roll(boolean firstRoll){
        int amount = 0;
        for(DiceEntity de:getActiveEntities()){
            if(de.getDie().getState()== Die.DieState.Stopped){
                amount++;
            }
        }
        BulletStuff.addRollEffects(amount, firstRoll, false);

        for(T entity:getActiveEntities()){
            entity.getDie().roll(firstRoll);
        }
    }

    private static List<DiceEntity> tmpALl = new ArrayList<>();
    public static List<DiceEntity> getEveryEntity(){
        tmpALl.clear();
        tmpALl.addAll(Party.get().getActiveEntities());
        tmpALl.addAll(Room.get().getActiveEntities());
        return tmpALl;
    }

    public static void clearTargetedHighlights() {
        for(DiceEntity de : getEveryEntity()){
            de.getEntityPanel().setTargeted(false);
            de.getEntityPanel().setArrowIntenity(0, 0);
        }
    }

    public void reset() {
    }

    public boolean allDiceStopped() {
        for(DiceEntity de: getEveryEntity()){
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

    public void resetForRoll() {
        for(final DiceEntity de:activeEntities){
            de.getDie().reset();
            de.getDie().used=false;
            de.getEntityPanel().holdsDie = false;
            de.getDie().returnToPlay(new Runnable() {
                @Override
                public void run() {
                    firstRoll();
                }
            }, Die.INTERP_SPEED_SLOW);
        }
    }

    public void dispose() {
        for(DiceEntity de:entities){
            de.getDie().dispose();
        }
    }
}
