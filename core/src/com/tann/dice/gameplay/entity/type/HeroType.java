package com.tann.dice.gameplay.entity.type;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.List;

import static com.tann.dice.gameplay.entity.type.EntityType.LevelUpClass.*;

public class HeroType extends EntityType<HeroType> {

    public Color colour;
    public Spell[] spells = new Spell[0];
    public static final List<HeroType> ALL_HEROES = new ArrayList<>();

    private HeroType() {
        ALL_HEROES.add(this);
    }

    private HeroType spells(Spell... spells){
        this.spells= spells;
        return this;
    }

    private HeroType colour(Color colour){
        this.colour = colour;
        return this;
    }

    public static final HeroType fighter = new HeroType().name("Fighter").hp(5).levelsUpInto(fighter1)
            .sides(Side.sword1, Side.sword1, Side.sword2, Side.sword2, Side.shield1, Side.nothing).colour(Colours.yellow);
    public static final HeroType fighterOrange = new HeroType().name("Fighter").hp(5).levelsUpInto(fighter1)
            .sides(Side.sword1, Side.sword1, Side.sword2, Side.sword2, Side.shield1, Side.nothing).colour(Colours.orange);
    public static final HeroType defender = new HeroType().name("Defender").hp(5).levelsUpInto(defender1)
            .sides(Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing).colour(Colours.grey);
    public static final HeroType apprentice = new HeroType().name("Acolyte").hp(4).levelsUpInto(magic1)
            .sides(Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing).colour(Colours.blue)
            .spells(Spell.fireWave);
    public static final HeroType herbalist = new HeroType().name("Herbalist").hp(4).levelsUpInto(healer1)
            .sides(Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing).colour(Colours.red)
            .spells(Spell.healAll);

    public static final HeroType rogue = new HeroType().name("Rogue").hp(6).tag(fighter1)
            .sides(Side.poison1, Side.poison1, Side.sword2, Side.sword2, Side.vanish, Side.nothing);
    public static final HeroType ranger = new HeroType().name("Ranger").hp(6).tag(fighter1)
            .sides(Side.execute3, Side.arrow2, Side.arrow2, Side.arrow1, Side.arrow1,Side.nothing);
    public static final HeroType gladiator = new HeroType().name("Gladiator").hp(7).tag(fighter1)
            .sides(Side.swordShield2, Side.swordShield2, Side.swordShield1, Side.swordShield1, Side.shield2, Side.nothing);
    public static final HeroType dabbler = new HeroType().name("Dabbler").hp(6).tag(fighter1)
            .sides(Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.magic2, Side.nothing);
    public static final HeroType paladin = new HeroType().name("Paladin").hp(7).tag(defender1)
            .sides(Side.shield2, Side.shieldHeart2, Side.shieldHeart2, Side.sword2, Side.sword1, Side.nothing);
    public static final HeroType bard = new HeroType().name("Bard").hp(6).tag(defender1)
            .sides(Side.shield3, Side.wardingchord, Side.wardingchord, Side.reroll, Side.magic2, Side.nothing);
    public static final HeroType alchemist = new HeroType().name("Alchemist").hp(5).tag(healer1)
            .sides(Side.heal4, Side.magic2, Side.magic1, Side.potionregen, Side.potionregen, Side.nothing)
            .spells(Spell.stoneSkin);
    public static final HeroType druid = new HeroType().name("Druid").hp(7).tag(healer1)
            .sides(Side.heal4, Side.heal4, Side.sword2, Side.magic1, Side.magic2, Side.nothing)
            .spells(Spell.balance);
    public static final HeroType pyro = new HeroType().name("Pyro").hp(7).tag(magic1)
            .sides(Side.flameWard, Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing)
            .spells(Spell.inferno);
    public static final HeroType arcanist = new HeroType().name("Arcanist").hp(5).tag(magic1)
            .sides(Side.magic3NextTurn, Side.magic2, Side.magic1, Side.magic1, Side.magic1, Side.nothing)
            .spells(Spell.arcaneMissile);

    public List<HeroType> getLevelupOptions() {
        List<HeroType> results = new ArrayList<>();
        for(HeroType type: ALL_HEROES){
            if(Tann.anySharedItems(type.tags, levelsUpInto)){
                results.add(type);
            }
        }
        return results;
    }

    public Hero buildHero(){
        if(hp==0 || name==null || sides==null || sides.length!=6){
            System.err.println("Uhoh, bad entity type: "+name+". It will probably throw an error soon.");
        }
        Hero h = new Hero(this);
        h.init();
        return h;
    }
}
