package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hero extends DiceEntity {

    List<Spell> spells;
    public Hero(int hp, String name, Side[] sides, Spell[] spells) {
        super(name, hp, sides, EntitySize.reg);
        this.spells = Arrays.asList(spells);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public List<HeroType> getLevelupOptions() {
        List<HeroType> results = new ArrayList<>();
//        switch(type){
//            case Fighter:
////                results.add(HeroType.Rogue);
//                results.add(HeroType.Ranger);
//                results.add(HeroType.Gladiator);
////                results.add(HeroType.Fencer);
////                results.add(HeroType.Dabbler);
////                break;
////            case Defender:
////                results.add(HeroType.Paladin);
////                results.add(HeroType.Bard);
////                break;
////            case Apprentice:
////                results.add(HeroType.Pyro);
////                results.add(HeroType.Arcanist);
////                break;
////            case Herbalist:
////                results.add(HeroType.Alchemist);
////                results.add(HeroType.Druid);
////                break;
//        }
        return results;
    }

    public enum HeroType{

        Fighter(5, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.shield1, Side.nothing),
        Defender(5, Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing),
        Apprentice(4, new Side[]{Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing}, new Spell[]{Spell.fireWave}),
        Herbalist(4, new Side[]{Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing}, new Spell[]{Spell.healAll}),

        Rogue(6, Side.poison1, Side.poison1, Side.sword2, Side.sword2, Side.vanish, Side.nothing),
        Ranger(6, Side.arrow1, Side.arrow1, Side.arrow2, Side.arrow2, Side.execute3, Side.nothing),
//
        Gladiator(6, Side.swordShield1, Side.swordShield1, Side.swordShield2, Side.swordShield2, Side.shield2, Side.nothing),
//        Dabbler(null, 6, Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.magic2, Side.nothing),
//
//        Paladin(null, 7, Side.shield2, Side.shield2, Side.shield2heal2, Side.sword2, Side.sword2, Side.nothing),
//        Bard(null, 5, Side.wardingchord, Side.wardingchord, Side.reroll, Side.shield2, Side.shield2, Side.nothing),
//
//        Alchemist(null, 4, new Side[]{Side.heal4, Side.magic2, Side.magic1, Side.potionregen, Side.potionHeroism, Side.nothing}, new Spell[]{Spell.stoneSkin}),
//        Druid(null, 5, new Side[]{Side.sword2, Side.magic2, Side.magic2, Side.heal4, Side.heal4, Side.nothing}, new Spell[]{Spell.balance}),
//
//        Pyro(null, 5, new Side[]{Side.magic1, Side.magic1, Side.magic2, Side.magic2, Side.flameWard, Side.nothing}, new Spell[]{Spell.inferno}),
//        Arcanist(null, 5, new Side[]{Side.magic1, Side.magic1, Side.magic2, Side.magic2, Side.magic1, Side.nothing}, new Spell[]{Spell.arcaneMissile}),
        ;

        public Side[] sides;
        public Spell[] spells;
        public TextureRegion lapel;
        int hp;

        HeroType(int hp, Side... sides){
            this(hp, sides, new Spell[0]);
        }

        HeroType(int hp, Side[] sides, Spell[] spells){
            if(sides.length!=6){
                System.err.println("side error making "+this);
            }
            this.hp=hp;
            this.lapel = Images.lapel0;
            this.sides=sides;
            this.spells = spells;
        }
    }

    public void levelUpTo(HeroType type) {
//        this.type = type;
        this.name = type.toString();
        setMaxHp(type.hp);
        setSides(type.sides);
        resetPanels();
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());
        DungeonScreen.get().spellHolder.layout();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getPixelSize() {
        return 16;
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

    public List<Spell> getSpells() {
        return spells;
    }
}
