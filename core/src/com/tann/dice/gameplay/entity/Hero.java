package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.entity.die.Side;

public class Hero extends DiceEntity {
    HeroType type;
    public Hero(HeroType type) {
        super(type.sides, type.toString(), EntitySize.Regular);
        this.type=type;
        setMaxHp(type.hp);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public enum HeroType{

        Apprentice(3, Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing),
        Fighter(4, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.shield1, Side.nothing),
        Defender(5, Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing),
        Herbalist(3, Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing),

        Wizard(4, Side.stealth, Side.stealth, Side.stealth, Side.magic2, Side.magic2, Side.magic2),
        Rogue(5, Side.poison1, Side.poison1, Side.sword3, Side.sword3, Side.stealth, Side.nothing),
        Healer(5, Side.heal3, Side.heal3, Side.heal3, Side.heal3, Side.heal3, Side.heal3),
        Protector(6, Side.shield2, Side.shield2, Side.shield2, Side.shield2, Side.shield2, Side.shield2),

        ;

        public Side[] sides;
        public TextureRegion lapel;
        int hp;
        HeroType(int hp, Side... sides){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.hp=hp;
            this.lapel = Images.lapel0;
            this.sides=sides;
        }
    }

    public void levelUpTo(HeroType type) {
        this.type = type;
        setSides(type.sides);
        resetPanels();
    }

    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public void locked() {

    }
}
