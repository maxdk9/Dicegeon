package com.tann.dice.gameplay.entity.type;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.effect.trigger.types.TriggerBoostOnRoll;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageLimit;
import com.tann.dice.gameplay.entity.Hero;
import static com.tann.dice.gameplay.entity.die.Side.*;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tann.dice.gameplay.entity.type.EntityType.LevelUpClass.*;

public class HeroType extends EntityType<HeroType> implements Cloneable {

    public Color colour;
    public Spell[] spells = new Spell[0];
    public static final Map<String, HeroType> ALL_HEROES = new HashMap<>();
    public final int level;

    private static int lv;
    static{
        lv = 0;
        add(new HeroType().name("Fighter").hp(5).levelsUpInto(fighter1).colour(Colours.yellow)
                .sides( sword2, sword2, sword1, sword1,shield1, nothing));
        add(new HeroType().name("Defender").hp(5).levelsUpInto(defender1).colour(Colours.grey)
                .sides(shield2, shield2, shield1, sword1, sword1, nothing));
        add(new HeroType().name("Acolyte").hp(4).levelsUpInto(wizard1).colour(Colours.blue)
                .sides(magic2, magic2, magic1, magic1, nothing, nothing)
                .spells(Spell.fireWave));
        add(new HeroType().name("Herbalist").hp(4).levelsUpInto(healer1)
                .sides(heal3, heal2, magic1, magic1, magic1, nothing).colour(Colours.red)
                .spells(Spell.healAll));

        lv = 1;
        add(new HeroType().name("Rogue").hp(5).tag(fighter1)
                .sides(poison1, poison1, sword2, arrow1, vanish, nothing));
        add(new HeroType().name("Ranger").hp(6).tag(fighter1)
                .sides(execute4, arrow2, arrow2, arrow1, arrow1,nothing));
        add(new HeroType().name("Gladiator").hp(5).tag(fighter1)
                .sides(swordShield2, swordShield2, trident1, trident1, shield3, nothing));
        add(new HeroType().name("Dabbler").hp(6).tag(fighter1)
                .sides(sword3, heal3, shield2, arrow2, magic2, nothing));
//        add(new HeroType().name("Berserker").hp(7).tag(fighter1)
//                .sides(sword1AllSelfDamage1, sword1AllSelfDamage1, sword2, sword2, shield2, nothing)
//                .trait(new TriggerHalfHealthEffTypeBonus(Eff.EffType.Damage, 1)));
        add(new HeroType().name("Whirl").hp(6).tag(fighter1)
                .sides(whirlwind1, trident1, trident1, sword2, shieldPlusAdjacent1, nothing));
        add(new HeroType().name("Crusher").hp(6).tag(fighter1)
                .sides(topbot2, topbot1, topbot1, sword2, shield2, nothing));
        add(new HeroType().name("Bruiser").hp(6).tag(fighter1)
            .sides(sword3, hook1, hook1, sword2, shield2, nothing));
        add(new HeroType().name("Combo").hp(6).tag(fighter1)
            .sides(trident1, sword2, sword2, shield2, nothing, nothing)
        .trait(new TriggerBoostOnRoll(1)));


        add(new HeroType().name("Paladin").hp(7).tag(defender1)
                .sides(shieldHeart2, shieldHeart2, shield2,  sword2, sword2, nothing));
        add(new HeroType().name("Bard").hp(6).tag(defender1)
                .sides(shield3, wardingchord, wardingchord, reroll, magic2, nothing));
        add(new HeroType().name("Bouncer").hp(9).tag(defender1)
                .sides(taunt, taunt, shield2, shield2, sword2, nothing)
                .trait(new TriggerDamageLimit(4)));
        add(new HeroType().name("Guardian").hp(7).tag(defender1)
                .sides(shieldPlusAdjacent2, shieldPlusAdjacent2, shield3, shield3, trident1, nothing));


        add(new HeroType().name("Alchemist").hp(5).tag(healer1)
                .sides(heal4, magic2, magic1, potionregen, potionregen, nothing)
                .spells(Spell.stoneSkin));
        add(new HeroType().name("Druid").hp(7).tag(healer1)
                .sides(heal4, heal4, sword2, magic1, magic2, nothing)
                .spells(Spell.balance));
        add(new HeroType().name("Witch").hp(6).tag(healer1)
                .sides(healBuff1, healBuff1, poison1, heal5, heal3, magic2)
                .spells(Spell.lifeSpark));
        add(new HeroType().name("Medic").hp(7).tag(healer1)
            .sides(healAll2, cure3, cure3, magic2, magic2, nothing)
            .spells(Spell.rejuvenate));
        add(new HeroType().name("Vampire").hp(6).tag(healer1)
            .sides(bloodPact1, swordHeal2, heal3, heal3, magic2, nothing)
            .spells(Spell.healingMist));



        add(new HeroType().name("Pyro").hp(6).tag(wizard1)
                .sides(flameWard2, flameWard1, magic2, magic2, magic1, nothing)
                .spells(Spell.bloodBoil));
        add(new HeroType().name("Arcanist").hp(7).tag(wizard1)
                .sides(magic3NextTurn, magic2NextTurn, magic2, magic2, nothing, nothing)
                .spells(Spell.deathSpike));
        add(new HeroType().name("Channeler").hp(6).tag(wizard1)
                .sides(buff2, buff1, magic2, magic2, magic1, nothing)
                .spells(Spell.arcaneMissile));
        add(new HeroType().name("Trickster").hp(7).tag(wizard1)
                .sides(copy, copy, magic2, magic1, magic1, nothing)
                .spells(Spell.lightningStrike));//TODO new spell here (enemy reroll!??)



        // novelty huge die

//        add(new HeroType().size(DiceEntity.EntitySize.huge).name("RogueHuge").hp(5).tag(fighter1)
//                .sides(huge_chomp1, huge_chomp1, huge_chomp1, huge_chomp1, huge_chomp1, huge_chomp1));
    }

    public static HeroType fighter = byName("fighter");
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

    public HeroType withColour(Color col){
        try {
            HeroType copy = (HeroType) clone();
            copy.colour = col;
            return copy;
        } catch (CloneNotSupportedException e) {
            System.err.println("bloody java");
        }
        return null;
    }

    public Hero buildHero(){
        if(hp==0 || name==null || sides==null || sides.length!=6){
            System.err.println("Uhoh, bad entity type: "+name+". It will probably throw an error soon.");
        }
        Hero h = new Hero(this);
        h.init();
        return h;
    }

    public static HeroType getLevelsUpInto(LevelUpClass luc){
        for(HeroType ht:ALL_HEROES.values()){
            if(ht.levelsUpInto.length>0 && ht.levelsUpInto[0]==luc){
                return ht;
            }
        }
        return null;
    }
}
