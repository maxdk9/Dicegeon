package com.tann.dice.gameplay.entity.group;

import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Room extends EntityGroup<Monster> {



    private static Room self;
    public static Room get(){
        if(self==null) self = new Room();
        return self;
    }

    public boolean activateDelayedRolls() {
        boolean found = false;

        List<DiceEntity> entities = new ArrayList<DiceEntity>(getActiveEntities());
        boolean summoned = false;
        for(DiceEntity de:entities){
            Die d = de.getDie();
            Side s = d.getActualSide();
            if(s==null) continue; //(newly summoned probably //TODO make better
            Eff e = s.getEffects()[0];
            switch(e.type){
                case Healing:
                    found = true;
                    TargetingManager.get().target(null, d, false);
                    break;
                case Summon:
                    found = true;
                    summoned = true;
                    for(int i=0;i<e.getValue();i++) {
                        Room.get().addEntity(MonsterType.byName(e.summonType).buildMonster());
                    }
                    break;
            }
        }
        if(summoned){
            DungeonScreen.get().enemy.setEntities(getActiveEntities());
            DungeonScreen.get().layoutSidePanels();
            BulletStuff.refresh(getAllActive());
        }
        return found;
    }

    @Override
    public void reset() {
        super.reset();
    }
}
