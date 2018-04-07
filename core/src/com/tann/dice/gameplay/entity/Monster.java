package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Main;
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
            }
        }, (nextLockTime-now)/1000f);
        nextLockTime += lockDelay;
    }

    @Override
    public void reduceToHalfHP() {
        if(slidOut){
            Room.get().requestSwap(this);
        }
    }

    @Override
    public void locked() {
        targets = TargetingManager.get().getRandomTargetForEnemy(die);
        for(DiceEntity de:targets){
            de.hit(die.getActualSide().getEffects(), false);
            EntityPanel panel = de.getEntityPanel();
            TextureRegion tr = getSides()[die.getSide()].getTexture();
            TextureFlasher tf = new TextureFlasher(tr);
            DungeonScreen.get().addActor(tf);
            Vector2 holder = panel.getDieHolderLocation();
            int regSize =EntitySize.reg.pixels;
            tf.setPosition(holder.x+regSize/2-tr.getRegionWidth()/2, holder.y+regSize/2-tr.getRegionHeight()/2);
            panel.flash();
        }
        EntityPanel ep = getDie().entity.getEntityPanel();
        ep.setArrowIntenity(1, .75f);
    }

    @Override
    protected void die() {
        EntityPanel ep = getEntityPanel();
        Vector2 pos = Tann.getLocalCoordinates(ep);
        TextWisp tw = new TextWisp("Enemy attack cancelled");
        tw.setPosition(pos.x+ep.getWidth()/2-tw.getWidth()/2, pos.y+ep.getHeight()/2-tw.getHeight()/2);
        DungeonScreen.get().addActor(tw);
        Tann.center(tw);
        tw.setY(Main.height-tw.getHeight()-13);
        super.die();
    }

    public Color getColour() {
        return Colours.purple;
    }

    public boolean willSwap() {
        for(Trigger t:getActiveTriggers()){
            if(t.cancelVolunteerForwards()) return false;
        }
        return true;
    }
}
