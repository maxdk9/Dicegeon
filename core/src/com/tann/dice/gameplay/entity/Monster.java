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

public class Monster extends DiceEntity{
    MonsterType type;
    public Monster(MonsterType type) {
        super(type.sides);
        this.type=type;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public enum MonsterType{

        Goblin(Side.sword1, Side.sword1, Side.sword1, Side.sword1, Side.sword1, Side.sword1);

        public Side[] sides;
        public TextureRegion lapel;

        MonsterType(Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.lapel = Images.lapel0;
            this.sides=sides;
        }
    }
    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public void locked() {
        DiceEntity target =DungeonScreen.get().getRandomTarget();
        target.hit(die.getActualSide().effects, false);
        EntityPanel panel = target.getEntityPanel();
        Vector2 panelCoords = Tann.getLocalCoordinates(panel);

        TextureFlasher tf = new TextureFlasher(getDie().sides.get(0).effects[0].type.region);
        DungeonScreen.get().addActor(tf);
        tf.setPosition(panel.getX()+140, panel.getY()+20);
        panel.flash();
//        ArrowFader af = new ArrowFader().point(die.getScreenPosition().x, die.getScreenPosition().y, panelCoords.x+panel.getWidth(), panelCoords.y+panel.getHeight()/2);
//        DungeonScreen.get().addActor(af);
        getDie().removeFromPhysics();
        EntityPanel ep = getDie().entity.getEntityPanel();
        getDie().moveTo(Tann.getLocalCoordinates(ep).add(EntityPanel.gap, EntityPanel.gap));
    }

    public Color getColour() {
        return Colours.brown_dark;
    }
}
