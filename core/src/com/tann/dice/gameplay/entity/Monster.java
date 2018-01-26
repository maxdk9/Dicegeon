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
        super(type.sides, type.toString(), type.size, Colours.purple);
        this.type=type;
        setMaxHp(type.hp);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public enum MonsterType{

        Goblin(5, EntitySize.Regular, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1, Side.sword1),
//        Ogre(9, EntitySize.Big, Side.cleave1, Side.cleave1, Side.sword3, Side.sword4, Side.sword4, Side.sword5),
//        Archer(3, EntitySize.Small, Side.arrow2, Side.arrow2, Side.arrow2, Side.arrow2, Side.arrow3, Side.arrow3),
//        Serpent(7, EntitySize.Big, Side.trident2, Side.trident2, Side.sword3, Side.sword3, Side.cleave1, Side.cleave1),
//        Dragon(40, EntitySize.Huge, Side.cleave2, Side.cleave3, Side.cleave3, Side.sword6, Side.sword6, Side.poison2);



        ;

        public Side[] sides;
        public TextureRegion lapel;
        int hp;
        public EntitySize size;
        MonsterType(int hp, EntitySize size, Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.hp = hp;
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
        return Colours.purple;
    }

}
