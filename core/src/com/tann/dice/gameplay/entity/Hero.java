package com.tann.dice.gameplay.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.Images;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.ArrayList;
import java.util.List;

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

    public List<HeroType> getLevelupOptions() {
        List<HeroType> results = new ArrayList<>();
        switch(type){
            case Fighter:
                results.add(HeroType.Rogue);
                results.add(HeroType.Ranger);
                results.add(HeroType.Fencer);
                results.add(HeroType.Dabbler);
                break;
            case Defender:
                results.add(HeroType.Paladin);
                results.add(HeroType.Bard);
                break;
            case Apprentice:
                results.add(HeroType.Pyro);
                results.add(HeroType.Arcanist);
                break;
            case Herbalist:
                results.add(HeroType.Alchemist);
                results.add(HeroType.Druid);
                break;
        }
        return results;
    }

    public enum HeroType{

        Fighter(4, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.shield1, Side.nothing),
        Defender(5, Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing),
        Apprentice(3, new Side[]{Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing}, new Spell[]{Spell.fireWave}),
        Herbalist(3, new Side[]{Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing}, new Spell[]{Spell.healAll}),

        Rogue(5, Side.poison1, Side.poison1, Side.sword2, Side.sword2, Side.stealth, Side.nothing),
        Ranger(5, Side.arrow1, Side.arrow1, Side.arrow2, Side.arrow2, Side.snipe, Side.nothing),

        Fencer(6, Side.sword1shield2, Side.sword2shield1, Side.sword2shield1, Side.trident, Side.trident, Side.nothing),
        Dabbler(6, Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.sword1, Side.sword1),

        Paladin(7, Side.shield2, Side.shield2, Side.shield2heal2, Side.sword2, Side.sword2, Side.nothing),
        Bard(5, Side.wardingchord, Side.wardingchord, Side.reroll, Side.shield2, Side.shield2, Side.nothing),

        Alchemist(4, new Side[]{Side.heal4, Side.magic2, Side.magic1, Side.potionregen, Side.potionHeroism, Side.nothing}, new Spell[]{Spell.stoneSkin}),
        Druid(5, new Side[]{Side.sword2, Side.magic2, Side.magic2, Side.heal4, Side.heal4, Side.nothing}, new Spell[]{Spell.balance}),

        Pyro(5, new Side[]{Side.magic1, Side.magic1, Side.magic2, Side.magic2, Side.flameWard, Side.nothing}, new Spell[]{Spell.inferno}),
        Arcanist(5, new Side[]{Side.magic1, Side.magic1, Side.magic2, Side.magic2, Side.nothing, Side.nothing}, new Spell[]{Spell.arcaneMissile}),
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
        this.type = type;
        this.name = type.toString();
        setMaxHp(type.hp);
        setSides(type.sides);
        resetPanels();
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());
        DungeonScreen.get().spellHolder.layout();
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

    public List<Spell> getSpells() {
        List<Spell> spells = new ArrayList<>();
        for(Spell s:type.spells){
            spells.add(s);
        }
        return spells;
    }
}
