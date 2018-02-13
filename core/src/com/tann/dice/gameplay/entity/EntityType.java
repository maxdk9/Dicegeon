package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.effect.Spell;
import com.tann.dice.gameplay.entity.die.Side;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.List;
import static com.tann.dice.gameplay.entity.EntityType.LevelUpClass.*;

public class EntityType {

    public enum LevelUpClass {
        fighter1, defender1, magic1, healer1,
    }

    public static final List<EntityType> ALL_HEROES = new ArrayList<>();
    public static final List<EntityType> ALL_MONSTERS = new ArrayList<>();

    // basics!
    public static final EntityType fighter = hero().name("Fighter").hp(5).levelsUpInto(fighter1)
            .sides(Side.sword1, Side.sword1, Side.sword2, Side.sword2, Side.shield1, Side.nothing);
    public static final EntityType defender = hero().name("Defender").hp(5).levelsUpInto(defender1)
            .sides(Side.shield2, Side.shield2, Side.shield1, Side.sword1, Side.sword1, Side.nothing);
    public static final EntityType apprentice = hero().name("Apprentice").hp(4).levelsUpInto(magic1)
            .sides(Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing, Side.nothing)
            .spells(Spell.fireWave);
    public static final EntityType herbalist = hero().name("Herbalist").hp(4).levelsUpInto(healer1)
            .sides(Side.heal3, Side.heal2, Side.magic1, Side.magic1, Side.magic1, Side.nothing)
            .spells(Spell.healAll);

    // fighter levelups!
    public static final EntityType rogue = hero().name("Rogue").hp(6).tag(fighter1)
            .sides(Side.poison1, Side.poison1, Side.sword2, Side.sword2, Side.vanish, Side.nothing);
    public static final EntityType ranger = hero().name("Ranger").hp(6).tag(fighter1)
            .sides(Side.execute3, Side.arrow2, Side.arrow2, Side.arrow1, Side.arrow1,Side.nothing);
    public static final EntityType gladiator = hero().name("Gladiator").hp(7).tag(fighter1)
            .sides(Side.swordShield2, Side.swordShield2, Side.swordShield1, Side.swordShield1, Side.shield2, Side.nothing);
    public static final EntityType dabbler = hero().name("Dabbler").hp(6).tag(fighter1)
            .sides(Side.sword2, Side.arrow2, Side.heal2, Side.shield2, Side.magic2, Side.nothing);

    public static final EntityType paladin = hero().name("Paladin").hp(7).tag(defender1)
            .sides(Side.shield2, Side.shieldHeart2, Side.shieldHeart2, Side.sword2, Side.sword1, Side.nothing);
    public static final EntityType bard = hero().name("Bard").hp(6).tag(defender1)
            .sides(Side.shield3, Side.wardingchord, Side.wardingchord, Side.reroll, Side.magic2, Side.nothing);

    public static final EntityType alchemist = hero().name("Alchemist").hp(5).tag(healer1)
            .sides(Side.heal4, Side.magic2, Side.magic1, Side.potionregen, Side.potionregen, Side.nothing)
            .spells(Spell.stoneSkin);
    public static final EntityType druid = hero().name("Druid").hp(7).tag(healer1)
        .sides(Side.heal4, Side.heal4, Side.sword2, Side.magic1, Side.magic2, Side.nothing)
        .spells(Spell.balance);

    public static final EntityType pyro = hero().name("Pyro").hp(7).tag(magic1)
        .sides(Side.flameWard, Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.nothing)
        .spells(Spell.inferno);

    public static final EntityType arcanist = hero().name("Arcanist").hp(5).tag(magic1)
        .sides(Side.magic2, Side.magic2, Side.magic1, Side.magic1, Side.magic1, Side.nothing)
        .spells(Spell.arcaneMissile);

    // monsters!
    public static final EntityType goblin = monster().name("Goblin").hp(5)
            .sides(Side.sword2, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1);
    public static final EntityType archer = monster().name("Archer").hp(3).size(DiceEntity.EntitySize.smol)
            .sides(Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2);
    public static final EntityType serpent = monster().name("Serpent").hp(7).size(DiceEntity.EntitySize.big)
            .sides(Side.axe, Side.axe, Side.axe, Side.axe, Side.axe, Side.axe);

    boolean hero;
    String name;
    int hp;
    Side[] sides;
    Spell[] spells = new Spell[0];
    LevelUpClass[] tags = new LevelUpClass[0];
    LevelUpClass[] levelsUpInto = new LevelUpClass[0];
    DiceEntity.EntitySize size = DiceEntity.EntitySize.reg;

    private static EntityType hero(){
        EntityType type = new EntityType(true);
        ALL_HEROES.add(type);
        return type;
    }

    private static EntityType monster(){
        EntityType type = new EntityType(false);
        ALL_MONSTERS.add(type);
        return type;
    }

    private EntityType(boolean hero){
        this.hero = hero;
    }

    public EntityType hp(int amount){
        this.hp = amount;
        return this;
    }

    public EntityType size(DiceEntity.EntitySize size){
        this.size = size;
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

    public EntityType tag(LevelUpClass... tags){
        this.tags = tags;
        return this;
    }

    public EntityType levelsUpInto(LevelUpClass... tags){
        this.levelsUpInto = tags;
        return this;
    }

    public void validate(){

    }

    public Hero buildHero(){
        if(hp==0 || name==null || sides==null || sides.length!=6){
            System.err.println("Uhoh, bad entity type: "+name+". It will probably throw an error soon.");
        }
        Hero h = new Hero(this);
        h.init();
        return h;
    }

    public Monster buildMonster(){
        if(spells.length>0){
            System.err.println("Uhoh, bad entity type: "+name+". Monsters can't have spells.");
        }
        Monster m = new Monster(this);
        m.init();
        return m;
    }

    public static List<Monster> monsterList(EntityType... monsters){
        List<Monster> results = new ArrayList<>();
        for(EntityType type:monsters){
            results.add(type.buildMonster());
        }
        return results;
    }

    public List<EntityType> getLevelupOptions() {
        List<EntityType> results = new ArrayList<>();
        for(EntityType type: ALL_HEROES){
            if(Tann.anySharedItems(type.tags, levelsUpInto)){
                results.add(type);
            }
        }
        return results;
    }

}
