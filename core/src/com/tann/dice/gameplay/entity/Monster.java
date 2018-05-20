package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Main;
import com.tann.dice.bullet.BulletStuff;
import com.tann.dice.gameplay.effect.trigger.Trigger;
import com.tann.dice.gameplay.entity.group.Room;
import com.tann.dice.gameplay.entity.type.MonsterType;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.TargetingManager;
import com.tann.dice.screens.dungeon.panels.entityPanel.EntityPanel;
import com.tann.dice.util.*;

public class Monster extends DiceEntity {

    public Monster(MonsterType type) {
        super(type);
        if(startsAtTheBack()){
            slide(false);
        }
        fleePip = type.fleePip;
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
    static final long lockDelay = 400;
    static final long firstLockDelay = 350;

    public static void resetLocks() {
        nextLockTime = 0;
    }

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
                getDie().slideToPanel();
            }
        }, (nextLockTime-now)/1000f);
        nextLockTime += lockDelay;
    }

    @Override
    public boolean canBeTargeted() {
        if(slidOut) return true;
        boolean anySlidOut = false;
        for(Monster m:Room.get().getActiveEntities()){
            if(m.slidOut){
                anySlidOut=true;
                break;
            }
        }
        return !anySlidOut;
    }

    @Override
    public void locked() {
        targets = TargetingManager.get().getRandomTargetForEnemy(die);
        for(DiceEntity de:targets){
            de.hit(die.getActualSide().getEffects(), false);
        }
        EntityPanel ep = getDie().entity.getEntityPanel();
        ep.setArrowIntenity(1, .75f);
    }

    @Override
    protected void die() {
        super.die();
        TextWisp tw = new TextWisp("Enemy attack cancelled", 1.6f);
        tw.setPosition(Main.width/2-tw.getWidth()/2-4, Main.height-tw.getHeight()-1);
        DungeonScreen.get().addActor(tw);
        BulletStuff.disposeDieLater(getDie());
    }

    public Color getColour() {
        return Colours.purple;
    }

    public boolean startsAtTheBack() {
        for(Trigger t:getActiveTriggers()){
            if(t.startsAtTheBack()) return true;
        }
        return false;
    }

}
