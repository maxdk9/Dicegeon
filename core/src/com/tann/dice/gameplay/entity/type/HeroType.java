package com.tann.dice.gameplay.entity.type;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.Eff;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageLimit;
import com.tann.dice.gameplay.effect.trigger.types.TriggerHalfHealthEffTypeBonus;
import com.tann.dice.gameplay.effect.trigger.types.TriggerTotalDamageReduction;
import com.tann.dice.gameplay.entity.Hero;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tann.dice.gameplay.entity.type.EntityType.LevelUpClass.*;

public class HeroType extends EntityType<HeroType> {

    public Color colour;
    public Spell[] spells = new Spell[0];
    public static final Map<String, HeroType> ALL_HEROES = new HashMap<>();
    public final int level;

    private static int lv;
    static{
        lv = 0;
        add(new HeroType().name("Fighter").hp(5).levelsUpInto(fighter1).colour(Colours.yellow)
                .sides( Side.sword2, Side.sword2, Side.sword1, Side.sword1,Side.shield1, Side.nothing));
        add(new HeroType().name("Fighter2").hp(5).levelsUpInto(fighter1).colour(Colours.orange)
                .sides( Side.sword2, Side.sword2, Side.sword1, Side.sword1,Side.shield1, Side.nothing));
        add(new HeroType().name("Defender").hp(5).levelsUpInto(defender1).colour(Colours.grey)
                .sides(Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing));
        add(new HeroType().name("Acolyte").hp(4).levelsUpInto(magic1).colour(Colours.blue)
                .sides(Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing)
                .spells(Spell.fireWave));
        add(new HeroType().name("Herbalist").hp(4).levelsUpInto(healer1)
                .sides(Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing).colour(Colours.red)
                .spells(Spell.healAll));

        lv = 1;
        add(new HeroType().name("Rogue").hp(5).tag(fighter1)
                .sides(Side.poison1, Side.poison1, Side.sword2, Side.arrow1, Side.vanish, Side.nothing));
        add(new HeroType().name("Ranger").hp(6).tag(fighter1)
                .sides(Side.execute3, Side.arrow2, Side.arrow2, Side.arrow1, Side.arrow1,Side.nothing));
        add(new HeroType().name("Gladiator").hp(7).tag(fighter1)
                .sides(Side.swordShield2, Side.swordShield2, Side.swordShield1, Side.swordShield1, Side.shield2, Side.nothing));
        add(new HeroType().name("Jack").hp(6).tag(fighter1)
                .sides(Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.magic2, Side.nothing));
        add(new HeroType().name("Berserker").hp(9).tag(fighter1)
                .sides(Side.sword3SelfDamage3, Side.sword3SelfDamage3, Side.sword2SelfDamage2, Side.sword2, Side.sword2, Side.nothing)
        .trait(new TriggerHalfHealthEffTypeBonus(Eff.EffType.Damage, 1)));

        add(new HeroType().name("Paladin").hp(7).tag(defender1)
                .sides(Side.shieldHeart2, Side.shieldHeart2, Side.shield2,  Side.sword2, Side.sword1, Side.nothing));
        add(new HeroType().name("Bard").hp(6).tag(defender1)
                .sides(Side.shield3, Side.wardingchord, Side.wardingchord, Side.reroll, Side.magic2, Side.nothing));
        add(new HeroType().name("Bouncer").hp(9).tag(defender1)
                .sides(Side.taunt, Side.taunt, Side.shield2, Side.shield2, Side.sword2, Side.nothing).
                        trait(new TriggerDamageLimit(4)));

        add(new HeroType().name("Alchemist").hp(5).tag(healer1)
                .sides(Side.heal4, Side.magic2, Side.magic1, Side.potionregen, Side.potionregen, Side.nothing)
                .spells(Spell.stoneSkin));
        add(new HeroType().name("Druid").hp(7).tag(healer1)
                .sides(Side.heal4, Side.heal4, Side.sword2, Side.magic1, Side.magic2, Side.nothing)
                .spells(Spell.balance));
        add(new HeroType().name("Medic").hp(6).tag(healer1)
                .sides(Side.heal4, Side.heal4, Side.sword2, Side.magic1, Side.magic2, Side.nothing)
                .spells(Spell.balance));


        add(new HeroType().name("Pyro").hp(6).tag(magic1)
                .sides(Side.flameWard, Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing)
                .spells(Spell.inferno));
        add(new HeroType().name("Arcanist").hp(5).tag(magic1)
                .sides(Side.magic3NextTurn, Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing)
                .spells(Spell.arcaneMissile));
    }

    public static HeroType fighter = byName("fighter");
    public static HeroType fighter2 = byName("fighter2");
    public static HeroType defender = byName("defender");
    public static HeroType herbalist = byName("herbalist");
    public static HeroType acolyte = byName("acolyte");

    private HeroType() {
        this.level = lv;
    }

    private HeroType tag(LevelUpClass... tags){
        this.tags = tags;
        return this;
    }
    private HeroType levelsUpInto(LevelUpClass... tags){
        this.levelsUpInto = tags;
        return this;
    }

    private HeroType spells(Spell... spells){
        this.spells= spells;
        return this;
    }

    private HeroType colour(Color colour){
        this.colour = colour;
        return this;
    }

    private static void add(HeroType h){
        ALL_HEROES.put(h.name.toLowerCase(), h);
    }

    public static HeroType byName(String name){
        return ALL_HEROES.get(name.toLowerCase());
    }
    
    public List<HeroType> getLevelupOptions() {
        List<HeroType> results = new ArrayList<>();
        for(HeroType type: ALL_HEROES.values()){
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
