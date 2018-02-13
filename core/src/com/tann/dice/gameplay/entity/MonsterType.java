package com.tann.dice.gameplay.entity;

import com.tann.dice.gameplay.entity.die.Side;

import java.util.ArrayList;
import java.util.List;

public class MonsterType extends EntityType<MonsterType>{

    public static final List<MonsterType> ALL_MONSTERS = new ArrayList<>();

    protected MonsterType() {
        ALL_MONSTERS.add(this);
    }

    public static final MonsterType goblin = new MonsterType().name("Goblin").hp(5)
            .sides(Side.sword2, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1);
    public static final MonsterType archer = new MonsterType().name("Archer").hp(3).size(DiceEntity.EntitySize.smol)
            .sides(Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2);
    public static final MonsterType serpent = new MonsterType().name("Serpent").hp(7).size(DiceEntity.EntitySize.big)
            .sides(Side.axe, Side.axe, Side.axe, Side.axe, Side.axe, Side.axe);

    public Monster buildMonster(){
        Monster m = new Monster(this);
        m.init();
        return m;
    }

    public static List<Monster> monsterList(MonsterType... monsters){
        List<Monster> results = new ArrayList<>();
        for(MonsterType type:monsters){
            results.add(type.buildMonster());
        }
        return results;
    }
}
