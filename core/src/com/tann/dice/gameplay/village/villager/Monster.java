package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.gameplay.village.villager.die.Side;
import com.tann.dice.screens.dungeon.DungeonScreen;

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

        Goblin(Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1, Side.nothing);

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
        DungeonScreen.get().targetRandom(die.getActualSide().effects);
    }
}
