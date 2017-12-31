package com.tann.dice.gameplay.entity.group;

import com.badlogic.gdx.utils.Array;
import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.util.Tann;

public class Room extends EntityGroup {

    private static Room self;
    public static Room get(){
        if(self==null) self = new Room();
        return self;
    }

    public void updateSlids(boolean keepSlids) {
        Array<DiceEntity> active = getActiveEntities();
        if(active.size == 0){
            return;
        }
        int amountShouldSlide = (active.size -1)/3+1;
        if(!keepSlids){
            for(DiceEntity m: Tann.pickNRandomElements(active, Math.max(amountShouldSlide, 0))){
                m.slide(true);
            }
        }
        else{
            int amountSlid = 0;
            for(DiceEntity m: active){
                if(m.slidOut) amountSlid ++;
            }
            int amountToSlide =amountShouldSlide-amountSlid;
            for(int i=0;i<amountToSlide;i++){
                for(DiceEntity m: Tann.pickNRandomElements(active, Math.max(amountShouldSlide, 0))){
                    if(!m.slidOut) {
                        m.slide(true);
                        break;
                    }
                }
            }
        }
    }
}
