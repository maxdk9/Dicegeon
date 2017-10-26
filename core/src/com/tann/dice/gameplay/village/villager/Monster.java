package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.die.Side;

public class Monster extends DiceEntity{
    public Monster(MonsterType type) {
        super(type.sides);
    }

    public enum MonsterType{

        Goblin(Side.magic1, Side.magic1, Side.magic2, Side.nothing, Side.nothing, Side.nothing);

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
}
