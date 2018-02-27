package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextureFlasher;

public class Monster extends DiceEntity {

    public Monster(MonsterType type) {
        super(type);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }


    @Override
    public int getPixelSize() {
        return size.pixels;
    }

    @Override
    public void locked() {

        Tann.delay(new Runnable() {
            @Override
            public void run() {
                getDie().removeFromPhysics();

                targets = TargetingManager.get().getRandomTargetForEnemy(die.getActualSide());
                for(DiceEntity de:targets){
                    de.hit(die.getActualSide().effects, false);
                    EntityPanel panel = de.getEntityPanel();
                    TextureFlasher tf = new TextureFlasher(getDie().sides.get(die.getSide()).tr);
                    DungeonScreen.get().addActor(tf);
                    panel.addActor(tf);
                    int x = (int) (panel.getWidth()*.3f);
                    int y = (int) (panel.getHeight()/2-tf.getHeight()/2 - 2);
                    tf.setPosition(x, y);
                    panel.flash();
                }
                EntityPanel ep = getDie().entity.getEntityPanel();
                ep.lockDie();
                locked = true;
            }
        }, .3f);


    }

    public Color getColour() {
        return Colours.purple;
    }

}
