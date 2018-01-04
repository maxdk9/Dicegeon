package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

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

        Rogue(5, Side.poison1, Side.poison1, Side.sword2, Side.sword2, Side.stealth, Side.nothing),
        Ranger(5, Side.arrow1, Side.arrow1, Side.arrow2, Side.arrow2, Side.snipe, Side.nothing),

        Fencer(5, Side.sword1shield2, Side.sword2shield1, Side.sword2shield1, Side.trident, Side.trident, Side.nothing),
        Dabbler(5, Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.magic2, Side.nothing),

        Paladin(5, Side.shield2, Side.shield2, Side.shield2heal2, Side.sword2, Side.sword2, Side.nothing),
        Bard(5, Side.wardingchord, Side.wardingchord, Side.reroll, Side.shield2, Side.shield2, Side.nothing),

        Wizard(4, Side.stealth, Side.stealth, Side.stealth, Side.magic2, Side.magic2, Side.magic2),
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
        this.name = type.toString();
        setSides(type.sides);
        resetPanels();
    }

    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public void locked() {
        Eff[] effs = die.getActualSide().effects;
        if(effs[0].targetingType == Eff.TargetingType.OnRoll){
            for(Eff e:effs){
                Party.get().activateRollEffect(e);
            }
        }
    }
}
