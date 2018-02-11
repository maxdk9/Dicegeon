package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.die.Side;

public class EntityType {

    public enum levelUpClass{
        basic, fighter1,
    }

    public static final EntityType fighter = hero().name("fighter").hp(5)
            .sides(Side.sword1, Side.sword1, Side.sword2, Side.sword2, Side.shield1, Side.nothing);
    public static final EntityType defender = hero().name("defender").hp(5)
            .sides(Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing);
    public static final EntityType apprentice = hero().name("apprentice").hp(4)
            .sides(Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing)
            .spells(Spell.fireWave);
    public static final EntityType herbalist = hero().name("herbalist").hp(4)
            .sides(Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing)
            .spells(Spell.healAll);

    boolean hero;
    String name;
    int hp;
    Side[] sides;
    Spell[] spells = new Spell[0];

    private static EntityType hero(){
        return new EntityType(true);
    }

    private static EntityType monster(){
        return new EntityType(false);
    }

    private EntityType(boolean hero){
        this.hero = hero;
    }

    public EntityType hp(int amount){
        this.hp = amount;
        return this;
    }

    public EntityType sides(Side... sides){
        this.sides = sides;
        return this;
    }

    public EntityType spells(Spell... spells){
        this.spells= spells;
        return this;
    }

    public EntityType name(String name){
        this.name = name;
        return this;
    }


    public Hero buildHero(){
        if(hp==0 || name==null || sides==null || sides.length!=6){
            System.err.println("Uhoh, bad entity type: "+name+". It will probably throw an error soon.");
        }

        return new Hero(hp, name, sides, spells);
    }

    public Monster buildMonster(){
        if(spells.length>0){
            System.err.println("Uhoh, bad entity type: "+name+". Monsters can't have spells.");
        }
        return null;
    }

}
