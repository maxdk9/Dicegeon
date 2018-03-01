package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
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


    static long nextLockTime = 0;
    static final long lockDelay = 500;
    static final long firstLockDelay = 250;
    @Override
    public void stopped() {
        long now = System.currentTimeMillis();
        if(now>nextLockTime){
            nextLockTime = now+firstLockDelay;
        }
        Tann.delay(new Runnable() {
            @Override
            public void run() {
                getDie().removeFromPhysics();
                EntityPanel ep = getDie().entity.getEntityPanel();
                ep.lockDie();
                locked = true;
            }
        }, (nextLockTime-now)/1000f);
        nextLockTime += lockDelay;
    }



    @Override
    public void locked() {
        targets = TargetingManager.get().getRandomTargetForEnemy(die.getActualSide());
        for(DiceEntity de:targets){
            de.hit(die.getActualSide().effects, false);
            EntityPanel panel = de.getEntityPanel();
            TextureFlasher tf = new TextureFlasher(getDie().sides.get(die.getSide()).tr);
            DungeonScreen.get().addActor(tf);
            Vector2 holder = panel.getDieHolderLocation();
            tf.setPosition(holder.x, holder.y);
            panel.flash();
        }
        EntityPanel ep = getDie().entity.getEntityPanel();
        ep.setArrowIntenity(1);
    }

    public Color getColour() {
        return Colours.purple;
    }

}
