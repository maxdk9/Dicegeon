package com.tann.dice.screens.gameScreen.panels.villagerStuff;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.tann.dice.Main;
import com.tann.dice.gameplay.village.Village;
import com.tann.dice.gameplay.village.villager.Villager;
import com.tann.dice.screens.gameScreen.GameScreen;
import com.tann.dice.util.Lay;
import com.tann.dice.util.Layoo;

public class VillagerBarPanel extends Lay{

    @Override
    public void layout() {
        setSize(VillagerIcon.width(), Main.height-GameScreen.getConstructionCircleSize());
        clearChildren();
        Layoo l = new Layoo(this);
        for(Villager v: Village.get().villagers){
            l.gap(100000000);
            l.actor(v.getIcon());
            l.row(1);
        }
        l.layoo();
        setTouchable(Touchable.childrenOnly);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
