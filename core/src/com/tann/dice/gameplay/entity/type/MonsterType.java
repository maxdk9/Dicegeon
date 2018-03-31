package com.tann.dice.gameplay.entity.type;

import com.tann.dice.gameplay.entity.DiceEntity;
import com.tann.dice.gameplay.entity.DiceEntity.EntitySize;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Side;

import java.util.ArrayList;
import java.util.List;

public class MonsterType extends EntityType<MonsterType>{

    public static final List<MonsterType> ALL_MONSTERS = new ArrayList<>();

    protected MonsterType() {
        ALL_MONSTERS.add(this);
    }

    public static final MonsterType goblin = new MonsterType().name("Goblin").hp(1)
        .sides(Side.sword2, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1);
    public static final MonsterType archer = new MonsterType().name("Archer").hp(3).size(DiceEntity.EntitySize.smol)
            .sides(Side.smol_arrow3, Side.smol_arrow3, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2);
    public static final MonsterType snake = new MonsterType().name("Snake").hp(7)
            .sides(Side.snakePoison1, Side.snakePoison1, Side.snakePoison1, Side.sword3, Side.sword3, Side.sword4);
    public static final MonsterType spikeBat = new MonsterType().name("SpikeBat").hp(6)
            .sides(Side.sword4, Side.sword4, Side.sword2, Side.sword2, Side.sword3, Side.sword3);

    public static final MonsterType bird = new MonsterType().name("Awk").hp(10).size(DiceEntity.EntitySize.big)
        .sides(Side.big_claw2, Side.big_claw2, Side.big_peck3, Side.big_peck3, Side.big_peck5, Side.big_peck5);
    public static final MonsterType dragon = new MonsterType().name("Lizard").hp(30).size(EntitySize.huge)
        .sides(Side.huge_flame2, Side.huge_flame2, Side.huge_flame3, Side.huge_posionChomp2, Side.huge_posionChomp2, Side.huge_posionChomp3);

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

    public static List<Monster> monsterList(List<MonsterType> monsters){
        List<Monster> results = new ArrayList<>();
        for(MonsterType type:monsters){
            results.add(type.buildMonster());
        }
        return results;
    }
}
