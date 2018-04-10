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

    static Comparator<Monster> monCom = new Comparator<Monster>() {
        @Override
        public int compare(Monster o1, Monster o2) {
            if(o1.willSwap() == o2.willSwap()) return 0;
            if(o1.willSwap()) return -1;
            if(o2.willSwap()) return 1;
            return 0;
        }
    };

    public void updateSlids(boolean keepSlids) {
        List<Monster> active = getActiveEntities();
        int numberToSlide = (active.size() - 1) / 3 + 1;

        List<Monster> alreadySlid = new ArrayList<>();
        List<Monster> notAlreadySlid = new ArrayList<>();

        for (Monster m : active) {
            if (m.slidOut) alreadySlid.add(m);
            else notAlreadySlid.add(m);
        }
        Collections.shuffle(notAlreadySlid);
        Collections.shuffle(alreadySlid);
        Collections.sort(notAlreadySlid, monCom);
        Collections.sort(alreadySlid, monCom);

        if (alreadySlid.size() == 1 && active.size() == 1) {
            return;
        }
        if (active.size() == 0) {
            return;
        }
        if (keepSlids) {
            int slideDelta = numberToSlide - alreadySlid.size();
            if (slideDelta > 0) {
                for (int i=0;i<slideDelta;i++) {
                    notAlreadySlid.get(i).slide(true);
                }
            } else if (slideDelta < 0) {
                for (int i=0;i<-slideDelta;i++) {
                    alreadySlid.get(i).slide(false);
                }
            }
        }
        else {
            List<Monster> toSlide = new ArrayList<>();
            List<Monster> toUnSlide = new ArrayList<>(alreadySlid);
            if (notAlreadySlid.size() >= numberToSlide) {
                toSlide = notAlreadySlid.subList(0, numberToSlide);
            } else {
                toSlide.addAll(notAlreadySlid);
                toSlide.addAll(alreadySlid.subList(0, numberToSlide - toSlide.size()));
            }
            toUnSlide.removeAll(toSlide);

            for (DiceEntity de : toSlide) {
                de.slide(true);
            }
            for (DiceEntity de : toUnSlide) {
                de.slide(false);
            }
        }
    }

    public void requestSwap(Monster monster) {
        DiceEntity volunteer = null;
        for(DiceEntity de:Tann.iterandom(activeEntities)){
            Monster m = (Monster) de;
            if(m == monster) continue;
            if(m.slidOut) continue;
            if(!m.aboveHalfHealth()) continue;
            if(m.willSwap())
            volunteer = de;
            break;
        }
        if(volunteer != null){
            volunteer.slide(true);
            monster.slide(false);
            TargetingManager.get().showTargetingHighlights();
        }
    }

    public boolean activateDelayedRolls() {
        boolean found = false;

        List<DiceEntity> entities = new ArrayList<DiceEntity>(getActiveEntities());
        boolean summoned = false;
        for(DiceEntity de:entities){
            Die d = de.getDie();
            Side s = d.getActualSide();
            if(s==null) continue; //(newly summoned probably //TODO better
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
            DungeonScreen.get().enemy.layout(true);
            updateSlids(true);
            BulletStuff.refresh(getAllActive());
        }
        return found;
    }

}
