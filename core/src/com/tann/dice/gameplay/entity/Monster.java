package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.screens.dungeon.DungeonScreen;
import com.tann.dice.screens.dungeon.panels.EntityPanel;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;
import com.tann.dice.util.TextureFlasher;

public class Monster extends DiceEntity {
    MonsterType type;

    public Monster(MonsterType type) {
        super(type.sides);
        this.type=type;
        setMaxHp(type.minHp + (int)(Math.random()*(type.maxHp-type.minHp+1)));
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public enum MonsterType{

        Goblin(4, 5, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1, Side.sword1),
        Ogre(8, 9, Side.sword2, Side.sword3, Side.sword3, Side.sword4, Side.sword4, Side.sword5);

        public Side[] sides;
        public TextureRegion lapel;
        public int minHp, maxHp;
        MonsterType(int minHp, int maxHp, Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.minHp = minHp;
            this.maxHp = maxHp;
            this.lapel = Images.lapel0;
            this.sides=sides;
        }
    }
    @Override
    public String getName() {
        return type.toString();
    }


    public boolean locked;
    @Override
    public void locked() {
        target =DungeonScreen.get().getRandomTarget();

        target.hit(die.getActualSide().effects, false);
        EntityPanel panel = target.getEntityPanel();
        Vector2 panelCoords = Tann.getLocalCoordinates(panel);

        TextureFlasher tf = new TextureFlasher(getDie().sides.get(0).effects[0].type.region);
        DungeonScreen.get().addActor(tf);
        panel.addActor(tf);
        tf.setPosition(panel.getWidth()*.7f-tf.getWidth()/2, panel.getHeight()/2-tf.getHeight()/2);
        panel.flash();
        getDie().removeFromPhysics();
        EntityPanel ep = getDie().entity.getEntityPanel();
        getDie().moveTo(Tann.getLocalCoordinates(ep).add(EntityPanel.gap, EntityPanel.gap));
        locked = true;


    }

    public Color getColour() {
        return Colours.brown_dark;
    }
}
