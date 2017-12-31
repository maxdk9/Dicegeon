package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Die;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextureFlasher;

public class Monster extends DiceEntity {
    MonsterType type;

    public Monster(MonsterType type) {
        super(type.sides, type.toString(), type.size);
        this.type=type;
        setMaxHp(type.minHp + (int)(Math.random()*(type.maxHp-type.minHp+1)));
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public enum MonsterType{

        Goblin(4, 5, EntitySize.Regular, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1, Side.sword1),
        Ogre(8, 9, EntitySize.Big, Side.cleave1, Side.cleave1, Side.sword3, Side.sword4, Side.sword4, Side.sword5);

        public Side[] sides;
        public TextureRegion lapel;
        public int minHp, maxHp;
        public EntitySize size;
        MonsterType(int minHp, int maxHp, EntitySize size, Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.minHp = minHp;
            this.maxHp = maxHp;
            this.lapel = Images.lapel0;
            this.sides=sides;
            this.size = size;
        }
    }
    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public void locked() {
        getDie().removeFromPhysics();

        targets = DungeonScreen.get().getRandomTargetForEnemy(die.getActualSide());
        for(DiceEntity de:targets){
            de.hit(die.getActualSide().effects, false);
            EntityPanel panel = de.getEntityPanel();
            TextureFlasher tf = new TextureFlasher(getDie().sides.get(0).tr[0]);
            DungeonScreen.get().addActor(tf);
            panel.addActor(tf);
            tf.setPosition(panel.getWidth()*.35f-tf.getWidth()/2, panel.getHeight()/2-tf.getHeight()/2);
            panel.flash();
        }
        EntityPanel ep = getDie().entity.getEntityPanel();
        ep.lockDie();
        locked = true;
    }

    public Color getColour() {
        return Colours.brown_dark;
    }

}
