package com.tann.dice.gameplay.entity.type;

import com.tann.dice.gameplay.effect.trigger.types.TriggerCowardly;
import com.tann.dice.gameplay.effect.trigger.types.TriggerDamageAttackers;
import com.tann.dice.gameplay.entity.Monster;
import com.tann.dice.gameplay.entity.die.Side;

import java.util.ArrayList;
import java.util.List;

import static com.tann.dice.gameplay.entity.DiceEntity.EntitySize.*;

public class MonsterType extends EntityType<MonsterType>{

    public static final List<MonsterType> ALL_MONSTERS = new ArrayList<>();

    protected MonsterType() {
        ALL_MONSTERS.add(this);
    }

    public static final MonsterType archer = new MonsterType().name("Archer").hp(3).size(smol)
            .sides(Side.smol_arrow3, Side.smol_arrow3, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2, Side.smol_arrow2)
            .trait(new TriggerCowardly());

    public static final MonsterType goblin = new MonsterType().name("Goblin").hp(5)
        .sides(Side.sword2, Side.sword2, Side.sword2, Side.sword1, Side.sword1, Side.sword1);
    public static final MonsterType snake = new MonsterType().name("Snake").hp(7)
            .sides(Side.snakePoison1, Side.snakePoison1, Side.snakePoison1, Side.sword3, Side.sword3, Side.sword4);

    public static final MonsterType spikeGolem = new MonsterType().name("Spiker").hp(9).size(big)
            .sides(Side.big_punch3, Side.big_punch3, Side.big_punch5, Side.big_punch5, Side.big_heal3, Side.big_heal3)//, Side.big_heal3, Side.big_heal3)
            .trait(new TriggerDamageAttackers(1));
    public static final MonsterType bird = new MonsterType().name("Awk").hp(10).size(big)
        .sides(Side.big_claw2, Side.big_claw2, Side.big_peck3, Side.big_peck3, Side.big_peck5, Side.big_peck5);

    public static final MonsterType dragon = new MonsterType().name("Lizard").hp(30).size(huge)
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
