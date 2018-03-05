package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.Arrays;
import java.util.List;

public class Hero extends DiceEntity {

    List<Spell> spells;
    public Hero(com.tann.dice.gameplay.entity.type.HeroType type) {
        super(type);
        if(type.colour!=null){
            setColour(type.colour);
        }
        this.spells = Arrays.asList(type.spells);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public void levelUpTo(com.tann.dice.gameplay.entity.type.HeroType type) {
        this.entityType = type;
        this.name = type.name;
        setMaxHp(type.hp);
        setSides(type.sides);
        this.spells = Arrays.asList(type.spells);
        resetPanels();
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());
        DungeonScreen.get().spellHolder.layout();
        getDiePanel().layout();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPixelSize() {
        return 16;
    }

    @Override
    public void stopped() {
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

    public com.tann.dice.gameplay.entity.type.HeroType getHeroType() {
        return (HeroType) entityType;
    }
}
