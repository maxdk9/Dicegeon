package com.tann.dice.gameplay.phase;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.HeroType;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.util.Tann;

public class LevelUpPhase extends Phase {

    @Override
    public void activate() {
        Hero h = null;
        for(int i=0;i<1000;i++){
            h = (Hero) Tann.getRandom(Party.get().getActiveEntities());
            if(h.getHeroType().getLevelupOptions().size()>0) break;
        }
        DungeonScreen.get().showLevelupPanel(h, Tann.pickNRandomElements(h.getHeroType().getLevelupOptions(), 2));
    }

    @Override
    public void deactivate() {
        DungeonScreen.get().friendly.setEntities(Party.get().getEntities());
    }
}
