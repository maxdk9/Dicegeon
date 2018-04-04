package com.tann.dice.gameplay.entity;

import com.tann.dice.Main;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.Trait;
import com.tann.dice.gameplay.effect.trigger.sources.Equipment;
import com.tann.dice.gameplay.entity.group.Party;
import com.tann.dice.gameplay.entity.type.HeroType;
import com.tann.dice.screens.dungeon.DungeonScreen;

import java.util.Arrays;
import java.util.List;

public class Hero extends DiceEntity {

    List<Spell> spells;
    public int level;
    public Hero(HeroType type) {
        super(type);
        setupLapels(type.level);
        if(type.colour!=null){
            setColour(type.colour);
        }
        this.spells = Arrays.asList(type.spells);
//        addEquipment(Equipment.byName("antivenom"));
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public void levelUpTo(HeroType type) {
        this.entityType = type;
        this.name = type.name;
        this.level = type.level;
        this.traits = (Trait[]) type.traits.toArray(new Trait[0]);
        setupLapels(level);
        setMaxHp(type.hp);
        setSides(type.sides);
        somethingChanged();
        fullHeal();
        resetPanels();

        this.spells = Arrays.asList(type.spells);
        DungeonScreen.get().spellHolder.setup(Party.get().getSpells());
        DungeonScreen.get().spellHolder.layout();
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
        Eff[] effs = die.getActualSide().getEffects();
        if(effs[0].targetingType == Eff.TargetingType.OnRoll){
            for(Eff e:effs){
                Party.get().activateRollEffect(e);
            }
        }
        if(Party.get().getRolls() == 0 && Party.get().allDiceStopped()){
            DungeonScreen.get().confirmDice(true);
        }
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public HeroType getHeroType() {
        return (HeroType) entityType;
    }
}
