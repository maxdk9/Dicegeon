package com.tann.dice.gameplay.village.villager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.village.villager.die.Die;
import com.tann.dice.gameplay.village.villager.die.Side;

public class Hero extends DiceEntity{
    HeroType type;
    public Hero(HeroType type) {
        super(type.sides);
        this.type=type;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public enum HeroType{

        Apprentice(Side.magic1, Side.magic1, Side.magic2, Side.nothing, Side.nothing, Side.nothing),
        Rogue(Side.sword1, Side.sword1, Side.sword2, Side.sword2, Side.nothing, Side.nothing),
        Fighter(Side.sword1, Side.sword1, Side.sword2, Side.shield2, Side.nothing, Side.nothing),
        Defender(Side.shield1, Side.shield1, Side.shield2, Side.shield2, Side.nothing, Side.nothing),
        Herbalist(Side.magic1, Side.magic1, Side.heal2, Side.magic1heal1, Side.nothing, Side.nothing);

        public Side[] sides;
        public TextureRegion lapel;

        HeroType(Side... sides){
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

    }
}
