package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room extends EntityGroup {

    private static Room self;
    public static Room get(){
        if(self==null) self = new Room();
        return self;
    }

    public void updateSlids(boolean keepSlids) {
        List<DiceEntity> active = getActiveEntities();
        int numberToSlide = (active.size() - 1) / 3 + 1;

        List<DiceEntity> alreadySlid = new ArrayList<>();
        List<DiceEntity> notAlreadySlid = new ArrayList<>();

        for (DiceEntity m : active) {
            if (m.slidOut) alreadySlid.add(m);
            else notAlreadySlid.add(m);
        }

        if (alreadySlid.size() == 1 && active.size() == 1) {
            return;
        }
        if (active.size() == 0) {
            return;
        }
        if (keepSlids) {
            int slideDelta = numberToSlide - alreadySlid.size();
            if (slideDelta > 0) {
                for (DiceEntity de : Tann.pickNRandomElements(notAlreadySlid, slideDelta)) {
                    de.slide(true);
                }
            } else if (slideDelta < 0) {
                for (DiceEntity de : Tann.pickNRandomElements(alreadySlid, -slideDelta)) {
                    de.slide(false);
                }
            }
        }
        else {
            List<DiceEntity> toSlide = new ArrayList<>();
            List<DiceEntity> toUnSlide = new ArrayList<>(alreadySlid);
            if (notAlreadySlid.size() >= numberToSlide) {
                toSlide = Tann.pickNRandomElements(notAlreadySlid, Math.max(numberToSlide, 0));
            } else {
                toSlide.addAll(notAlreadySlid);
                toSlide.addAll(Tann.pickNRandomElements(alreadySlid, numberToSlide - toSlide.size()));
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
            if(de == monster) continue;
            if(de.slidOut) continue;
            if(!de.aboveHalfHealth()) continue;
            volunteer = de;
            break;
        }
        if(volunteer != null){
            volunteer.slide(true);
            monster.slide(false);
        }
    }
}
